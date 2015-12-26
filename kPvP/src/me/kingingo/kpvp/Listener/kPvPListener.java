package me.kingingo.kpvp.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.AntiLogout.Events.AntiLogoutAddPlayerEvent;
import me.kingingo.kcore.AntiLogout.Events.AntiLogoutDelPlayerEvent;
import me.kingingo.kcore.Command.Admin.CommandLocations;
import me.kingingo.kcore.ELO.ELO;
import me.kingingo.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.PLAYER_VOTE;
import me.kingingo.kcore.Packet.Packets.TWITTER_PLAYER_FOLLOW;
import me.kingingo.kcore.Packet.Packets.WORLD_CHANGE_DATA;
import me.kingingo.kcore.Permission.Event.PlayerLoadPermissionEvent;
import me.kingingo.kcore.Scoreboard.Events.PlayerSetScoreboardEvent;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.Event.PlayerStatsCreateEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.TabTitle;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilScoreboard;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kpvp.Command.CommandStats;
import me.kingingo.kpvp.Manager.kPvPManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scoreboard.DisplaySlot;

public class kPvPListener extends kListener{

	@Getter
	private kPvPManager manager;
	private HashMap<Player,NameTagMessage> holo = new HashMap<>();
	private HashMap<Player,String> pet_respawn = new HashMap<>();
	public static NameTagMessage ranking_day;
	public static NameTagMessage ranking_week;
	public static NameTagMessage ranking_month;
	public static NameTagMessage ranking_total;
	private ArrayList<UUID> vote_list = new ArrayList<>();
	
	public kPvPListener(kPvPManager manager){
		super(manager.getPvP(),"[kPvPListener]");
		this.manager=manager;
		this.ranking_day=manager.getPvP().getHologram().sendText(CommandLocations.getLocation("Ranking_Today"), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_day(),"Tag"));
		this.ranking_week=manager.getPvP().getHologram().sendText(CommandLocations.getLocation("Ranking_Week"), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_week(),"Woche"));
		this.ranking_month=manager.getPvP().getHologram().sendText(CommandLocations.getLocation("Ranking_Month"), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_month(),"Monat"));
		this.ranking_total=manager.getPvP().getHologram().sendText(CommandLocations.getLocation("Ranking_Total"), manager.getStatsManager().getRankingMessage(CommandStats.getRanking()));
	}
	
	Player player;
	@EventHandler
	public void PacketReceive(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof WORLD_CHANGE_DATA){
			WORLD_CHANGE_DATA packet = (WORLD_CHANGE_DATA)ev.getPacket();
			UtilPlayer.setWorldChangeUUID(Bukkit.getWorld(packet.getWorldName()), packet.getOld_uuid(), packet.getNew_uuid());
		}else if(ev.getPacket() instanceof PLAYER_VOTE){
			PLAYER_VOTE vote = (PLAYER_VOTE)ev.getPacket();
			
			if(UtilPlayer.isOnline(vote.getPlayer())){
				player=Bukkit.getPlayer(vote.getPlayer());
				if(UtilServer.getDeliveryPet()!=null){
					UtilServer.getDeliveryPet().deliveryUSE(player, "§aVote for EpicPvP", true);
				}
				manager.getStatsManager().setDouble(player, manager.getStatsManager().getDouble(Stats.MONEY, player)+200, Stats.MONEY);
				UtilInv.repairInventory(player, true);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "VOTE_THX"));
			}else{
				vote_list.add(vote.getUuid());
			}
		}else if(ev.getPacket() instanceof TWITTER_PLAYER_FOLLOW){
			TWITTER_PLAYER_FOLLOW tw = (TWITTER_PLAYER_FOLLOW)ev.getPacket();
			
			if(UtilPlayer.isOnline(tw.getPlayer())){
				Player p = Bukkit.getPlayer(tw.getPlayer());
				if(!tw.isFollow()){
					getManager().getPvP().getMysql().Update("DELETE FROM BG_TWITTER WHERE uuid='" + UtilPlayer.getRealUUID(p) + "'");
					p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_FOLLOW_N"));
					p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_REMOVE"));
				}else{
					UtilServer.getDeliveryPet().deliveryBlock(p, "§cTwitter Reward");
					getManager().getStatsManager().addDouble(p, 300, Stats.MONEY);
					p.setLevel(p.getLevel()+15);
					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "MONEY_RECEIVE_FROM", new String[]{"§bThe Delivery Jockey!","300"}));
				}
			}
		}
	}
	
	@EventHandler
	public void Enderpearl(PlayerTeleportEvent ev){
		if(ev.getCause()==TeleportCause.ENDER_PEARL&& (!ev.getPlayer().isPermissionSet("kpvp.enderpearl")) ){
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerTeleport(PlayerTeleportEvent ev){
		if(!getManager().getAntiManager().is(ev.getPlayer())){
			ev.getPlayer().sendMessage(Language.getText(player, "PREFIX")+Language.getText(ev.getPlayer(), "ANIT_LOGOUT_FIGHT"));
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void GildeHome(GildenPlayerTeleportEvent ev){
		if(!getManager().getAntiManager().is(ev.getPlayer())){
			ev.getPlayer().sendMessage(Language.getText(player, "PREFIX")+Language.getText(ev.getPlayer(), "ANIT_LOGOUT_FIGHT_CMD","/gilde home"));
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void AntiLogoutdelPlayer(AntiLogoutDelPlayerEvent ev){
		if(pet_respawn.containsKey(ev.getPlayer())){
			manager.getPetHandler().loadPetSettings(ev.getPlayer(), pet_respawn.get(ev.getPlayer()));
			pet_respawn.remove(ev.getPlayer());
		}
	}

	@EventHandler
	public void AntiLogoutAddPlayer(AntiLogoutAddPlayerEvent ev){
		if(manager.getPetManager().hasPlayer(ev.getPlayer())){
			pet_respawn.put(ev.getPlayer(), getManager().getPetHandler().toString( manager.getPetManager().GetPet(ev.getPlayer()) ));
			manager.getPetManager().RemovePet(ev.getPlayer(), true);
		}
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		ev.setDeathMessage(null);
		if(ev.getEntity() instanceof Player){
			Player v = (Player)ev.getEntity();
			v.sendMessage(Language.getText(v, "PREFIX")+Language.getText(v, "PVP_DEATH"));
			
			getManager().getStatsManager().setInt(v, getManager().getStatsManager().getInt(Stats.DEATHS, v)+1, Stats.DEATHS);
			if(ev.getEntity().getKiller()!=null&&ev.getEntity().getKiller() instanceof Player){
				double elo = manager.getStatsManager().getDouble(Stats.ELO, ev.getEntity().getKiller());
				ELO.eloCHANGEProzent(ev.getEntity().getKiller(), v,10, manager.getStatsManager());
				elo=manager.getStatsManager().getDouble(Stats.ELO, ev.getEntity().getKiller())-elo;
				getManager().getStatsManager().setInt(ev.getEntity().getKiller(), getManager().getStatsManager().getInt(Stats.KILLS, ev.getEntity().getKiller())+1, Stats.KILLS);
				
				ev.getEntity().getKiller().sendMessage(Language.getText(ev.getEntity().getKiller(), "PREFIX")+Language.getText(ev.getEntity().getKiller(), "PVP_KILL",new Object[]{v.getName(),elo} ));
				updateFame(ev.getEntity().getKiller());
			}else{
				ELO.eloCHANGE(v, manager.getStatsManager());
			}
			updateFame( v );
		}
	}
	
	@EventHandler
	public void NEW(PlayerStatsCreateEvent ev){
		getManager().getNeulingManager().add(ev.getPlayer());
		getManager().getStatsManager().setDouble(ev.getPlayer(), ELO.START_WERT, Stats.ELO);
		getManager().getStatsManager().setDouble(ev.getPlayer(), 0, Stats.TIME_ELO);
		getManager().getStatsManager().setString(ev.getPlayer(), ""+System.currentTimeMillis(), Stats.TIME);
		ev.getPlayer().teleport(ev.getPlayer().getWorld().getSpawnLocation());
	}
	
	@EventHandler
	public void SendHologram(PlayerQuitEvent ev){
		if(holo.containsKey(ev.getPlayer())){
			holo.get(ev.getPlayer()).clear(ev.getPlayer());
			holo.get(ev.getPlayer()).remove();
			holo.remove(ev.getPlayer());
		}
	}
	
	@EventHandler
	public void saveStats(PlayerQuitEvent ev){
		ev.setQuitMessage(null);
		getManager().getStatsManager().SaveAllPlayerData(ev.getPlayer());
	}
	
	public void updateFame(Player player){
		for(Player p : UtilServer.getPlayers()){
			if(player!=null){
				if(player.getScoreboard()!=null){
					UtilScoreboard.setScore(player.getScoreboard(), p.getName(), DisplaySlot.BELOW_NAME, UtilNumber.toInt(getManager().getStatsManager().getDouble(Stats.ELO, p)));
				}
			}
		}
	}
	
	@EventHandler
	public void AddBoard(PlayerSetScoreboardEvent ev){
		UtilPlayer.setScoreboard(ev.getPlayer(), 
				UtilServer.getGemsShop().getGems());
	}
	
	@EventHandler
	public void loadPerm(PlayerLoadPermissionEvent ev){
		if(ev.getPlayer().getScoreboard()==null)ev.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		if(ev.getPlayer().getScoreboard().getObjective(DisplaySlot.BELOW_NAME)==null)UtilScoreboard.addBoard(ev.getPlayer().getScoreboard(), DisplaySlot.BELOW_NAME, "§6FAME");	
		for(Player player : UtilServer.getPlayers()){
			if(player.getName().equalsIgnoreCase(ev.getPlayer().getName()))continue;
			if(player.getScoreboard()==null)player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			if(player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME)==null)UtilScoreboard.addBoard(player.getScoreboard(), DisplaySlot.BELOW_NAME, "§6FAME");		
			UtilScoreboard.setScore(player.getScoreboard(), ev.getPlayer().getName(), DisplaySlot.BELOW_NAME, UtilNumber.toInt(getManager().getStatsManager().getDouble(Stats.ELO, ev.getPlayer())));
		}
		updateFame(ev.getPlayer());
	}
	
	@EventHandler
	public void SendHolo(PlayerJoinEvent ev){
		setHologramm(ev.getPlayer());
		TabTitle.setHeaderAndFooter(ev.getPlayer(), "§eEpicPvP§8.§eeu §8| §aPvP Server", "§aTeamSpeak: §7ts.EpicPvP.eu §8| §eWebsite: §7EpicPvP.eu");
	}

	@EventHandler
	public void SendRanking(PlayerJoinEvent ev){
		this.ranking_day.sendToPlayer(ev.getPlayer());
		this.ranking_week.sendToPlayer(ev.getPlayer());
		this.ranking_total.sendToPlayer(ev.getPlayer());
		this.ranking_month.sendToPlayer(ev.getPlayer());
	}
	
	@EventHandler
	public void pet(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Creature){
			if(getManager().getPetManager().isPet( ((Creature)ev.getEntity()) )){
				ev.setCancelled(true);
			}
		}else if(ev.getDamager() instanceof Creature){
			if(getManager().getPetManager().isPet( ((Creature)ev.getDamager()) )){
				ev.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		ev.setJoinMessage(null);
		 ev.getPlayer().sendMessage(Language.getText(ev.getPlayer(), "PREFIX")+Language.getText(ev.getPlayer(), "WHEREIS_TEXT","PvP"));
		 
		 if(vote_list.contains( UtilPlayer.getRealUUID(ev.getPlayer()) )){
			 if(UtilServer.getDeliveryPet()!=null){
				 UtilServer.getDeliveryPet().deliveryUSE(ev.getPlayer(), "§aVote for EpicPvP", true);
			 }
			 vote_list.remove(UtilPlayer.getRealUUID(ev.getPlayer()));
			 manager.getStatsManager().setDouble(ev.getPlayer(), manager.getStatsManager().getDouble(Stats.MONEY, ev.getPlayer())+200, Stats.MONEY);
			 UtilInv.repairInventory(ev.getPlayer(), true);
			 ev.getPlayer().sendMessage(Language.getText(ev.getPlayer(), "PREFIX")+Language.getText(ev.getPlayer(), "VOTE_THX"));
		 }
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_08){
			for(Player p : UtilServer.getPlayers())setHologramm(p);
			
			this.ranking_day.remove();
			this.ranking_week.remove();
			this.ranking_month.remove();
			this.ranking_total.remove();

			this.ranking_day=manager.getPvP().getHologram().sendText(CommandLocations.getLocation("Ranking_Today"), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_day(),"Tag"));
			this.ranking_week=manager.getPvP().getHologram().sendText(CommandLocations.getLocation("Ranking_Week"), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_week(),"Woche"));
			this.ranking_month=manager.getPvP().getHologram().sendText(CommandLocations.getLocation("Ranking_Month"), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_month(),"Monat"));
			this.ranking_total=manager.getPvP().getHologram().sendText(CommandLocations.getLocation("Ranking_Total"), manager.getStatsManager().getRankingMessage(CommandStats.getRanking()));
		}
	}
	
	public void setHologramm(Player p){
		getManager().getStatsManager().ExistPlayer(p);
		if(holo.containsKey(p)){
			holo.get(p).remove();
			holo.remove(p);
		}
		
		holo.put(p, getManager().getPvP().getHologram().sendText(p, CommandLocations.getLocation("Player_Stats"), new String[]{
			"§6Name§a "+p.getName(),
			"§6Gilde§b "+getManager().getGildenManager().getPlayerGilde(p),
			"§6Ranking§b "+getManager().getStatsManager().getRank(Stats.KILLS, p),
			"§6Kills§b "+getManager().getStatsManager().getInt(Stats.KILLS, p),
			"§6Fame§b "+getManager().getStatsManager().getDouble(Stats.ELO, p),
			"§6Tode§b "+getManager().getStatsManager().getInt(Stats.DEATHS, p),
			"§6Money§b "+getManager().getStatsManager().getDouble(Stats.MONEY, p),
			"§6KDR§b "+getManager().getStatsManager().getKDR(getManager().getStatsManager().getInt(Stats.KILLS, p), getManager().getStatsManager().getInt(Stats.DEATHS, p)),
		}));
	}
	
}
