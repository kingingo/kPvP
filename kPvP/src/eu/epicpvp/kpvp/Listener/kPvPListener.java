package eu.epicpvp.kpvp.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.AntiLogout.Events.AntiLogoutAddPlayerEvent;
import eu.epicpvp.kcore.AntiLogout.Events.AntiLogoutDelPlayerEvent;
import eu.epicpvp.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Scoreboard.Events.PlayerSetScoreboardEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsCreateEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.TabTitle;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kpvp.kPvPManager;
import lombok.Getter;

public class kPvPListener extends kListener{

	@Getter
	private kPvPManager manager;
	private HashMap<Player,String> pet_respawn = new HashMap<>();
	private ArrayList<UUID> vote_list = new ArrayList<>();
	
	public kPvPListener(kPvPManager manager){
		super(manager.getPvP(),"[kPvPListener]");
		this.manager=manager;
	}
	
//	Player player;
//	@EventHandler
//	public void PacketReceive(PacketReceiveEvent ev){
//		if(ev.getPacket() instanceof WORLD_CHANGE_DATA){
//			WORLD_CHANGE_DATA packet = (WORLD_CHANGE_DATA)ev.getPacket();
//			UtilPlayer.setWorldChangeUUID(Bukkit.getWorld(packet.getWorldName()), packet.getOld_uuid(), packet.getNew_uuid());
//		}else if(ev.getPacket() instanceof PLAYER_VOTE){
//			PLAYER_VOTE vote = (PLAYER_VOTE)ev.getPacket();
//			
//			if(UtilPlayer.isOnline(vote.getPlayer())){
//				player=Bukkit.getPlayer(vote.getPlayer());
//				if(UtilServer.getDeliveryPet()!=null){
//					UtilServer.getDeliveryPet().deliveryUSE(player, "§aVote for EpicPvP", true);
//				}
//				manager.getStatsManager().setDouble(player, manager.getStatsManager().getDouble(Stats.MONEY, player)+200, Stats.MONEY);
//				UtilInv.repairInventory(player, true);
//				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "VOTE_THX"));
//			}else{
//				vote_list.add(vote.getUuid());
//			}
//		}else if(ev.getPacket() instanceof TWITTER_PLAYER_FOLLOW){
//			TWITTER_PLAYER_FOLLOW tw = (TWITTER_PLAYER_FOLLOW)ev.getPacket();
//			
//			if(UtilPlayer.isOnline(tw.getPlayer())){
//				Player p = Bukkit.getPlayer(tw.getPlayer());
//				if(!tw.isFollow()){
//					getManager().getPvP().getMysql().Update("DELETE FROM BG_TWITTER WHERE uuid='" + UtilPlayer.getRealUUID(p) + "'");
//					p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_FOLLOW_N"));
//					p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_REMOVE"));
//				}else{
//					UtilServer.getDeliveryPet().deliveryBlock(p, "§cTwitter Reward");
//					getManager().getStatsManager().addDouble(p, 300, Stats.MONEY);
//					p.setLevel(p.getLevel()+15);
//					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "MONEY_RECEIVE_FROM", new String[]{"§bThe Delivery Jockey!","300"}));
//				}
//			}
//		}
//	}
	
	@EventHandler
	public void Enderpearl(PlayerTeleportEvent ev){
		if(ev.getCause()==TeleportCause.ENDER_PEARL&& (!ev.getPlayer().isPermissionSet("kpvp.enderpearl")) ){
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerTeleport(PlayerTeleportEvent ev){
		if(!getManager().getAntiManager().is(ev.getPlayer())){
			ev.getPlayer().sendMessage(TranslationManager.getText(ev.getPlayer(), "PREFIX")+TranslationManager.getText(ev.getPlayer(), "ANIT_LOGOUT_FIGHT"));
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void GildeHome(GildenPlayerTeleportEvent ev){
		if(!getManager().getAntiManager().is(ev.getPlayer())){
			ev.getPlayer().sendMessage(TranslationManager.getText(ev.getPlayer(), "PREFIX")+TranslationManager.getText(ev.getPlayer(), "ANIT_LOGOUT_FIGHT_CMD","/gilde home"));
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
			v.sendMessage(TranslationManager.getText(v, "PREFIX")+TranslationManager.getText(v, "PVP_DEATH"));
			
			getManager().getStatsManager().addInt(v, 1, StatsKey.DEATHS);
			if(ev.getEntity().getKiller()!=null&&ev.getEntity().getKiller() instanceof Player){
				getManager().getStatsManager().addInt(ev.getEntity().getKiller(), 1, StatsKey.KILLS);
				
				ev.getEntity().getKiller().sendMessage(TranslationManager.getText(ev.getEntity().getKiller(), "PREFIX")+TranslationManager.getText(ev.getEntity().getKiller(), "PVP_KILL",new Object[]{v.getName()} ));
			}
		}
	}
	
	@EventHandler
	public void NEW(PlayerStatsCreateEvent ev){
		if(ev.getManager().getType() != GameType.Money){
			if(UtilPlayer.isOnline(ev.getPlayername())){
				getManager().getNeulingManager().add(Bukkit.getPlayer(ev.getPlayername()));
				Bukkit.getPlayer(ev.getPlayername()).teleport(Bukkit.getPlayer(ev.getPlayername()).getWorld().getSpawnLocation());
			}
		}
	}
	
	@EventHandler
	public void saveStats(PlayerQuitEvent ev){
		ev.setQuitMessage(null);
		getManager().getStatsManager().SaveAllPlayerData(ev.getPlayer());
	}
	
	@EventHandler
	public void AddBoard(PlayerSetScoreboardEvent ev){
		UtilPlayer.setScoreboardGems(ev.getPlayer(), UtilServer.getGemsShop().getGems());
	}
	
	@EventHandler
	public void SendHolo(PlayerJoinEvent ev){
		TabTitle.setHeaderAndFooter(ev.getPlayer(), "§eEpicPvP§8.§eeu §8| §aPvP Server", "§aTeamSpeak: §7ts.EpicPvP.eu §8| §eWebsite: §7EpicPvP.eu");
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
	public void loadedStats(PlayerStatsLoadedEvent ev){
		if(ev.getManager().getType() != GameType.Money){
			if(UtilPlayer.isOnline(ev.getPlayername())){
				if(vote_list.contains( UtilPlayer.getRealUUID(Bukkit.getPlayer(ev.getPlayername())) )){
					 if(UtilServer.getDeliveryPet()!=null){
						 UtilServer.getDeliveryPet().deliveryUSE(Bukkit.getPlayer(ev.getPlayername()), "§aVote for EpicPvP", true);
					 }
					 vote_list.remove(UtilPlayer.getRealUUID(Bukkit.getPlayer(ev.getPlayername())));
					 manager.getStatsManager().addDouble(Bukkit.getPlayer(ev.getPlayername()), 200, StatsKey.MONEY);
					 UtilInv.repairInventory(Bukkit.getPlayer(ev.getPlayername()), true);
					 Bukkit.getPlayer(ev.getPlayername()).sendMessage(TranslationManager.getText(Bukkit.getPlayer(ev.getPlayername()), "PREFIX")+TranslationManager.getText(Bukkit.getPlayer(ev.getPlayername()), "VOTE_THX"));
				 }
			}
		}
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		ev.setJoinMessage(null);
		getManager().getStatsManager().loadPlayer(ev.getPlayer());
		getManager().getMoney().loadPlayer(ev.getPlayer());
		getManager().getGildenManager().loadPlayer(ev.getPlayer());
		ev.getPlayer().sendMessage(TranslationManager.getText(ev.getPlayer(), "PREFIX")+TranslationManager.getText(ev.getPlayer(), "WHEREIS_TEXT","PvP"));
	}
}
