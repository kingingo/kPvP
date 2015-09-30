package me.kingingo.kpvp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.ELO.ELO;
import me.kingingo.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.PLAYER_VOTE;
import me.kingingo.kcore.Packet.Packets.TWITTER_PLAYER_FOLLOW;
import me.kingingo.kcore.Packet.Packets.WORLD_CHANGE_DATA;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Permission.Event.PlayerLoadPermissionEvent;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.Event.PlayerStatsCreateEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.RestartScheduler;
import me.kingingo.kcore.Util.TabTitle;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilScoreboard;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilWorldGuard;
import me.kingingo.kpvp.Command.CommandHologram;
import me.kingingo.kpvp.Command.CommandStats;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;

public class kPvPListener extends kListener{

	@Getter
	private kPvP manager;
	private HashMap<Player,NameTagMessage> holo = new HashMap<>();
	public static NameTagMessage ranking_day;
	public static NameTagMessage ranking_week;
	public static NameTagMessage ranking_month;
	public static NameTagMessage ranking_total;
	private ArrayList<UUID> vote_list = new ArrayList<>();
	
	public kPvPListener(kPvP manager){
		super(manager.getInstance(),"[kPvPListener]");
		this.manager=manager;
		this.ranking_day=manager.getHologram().createNameTagMessage(CommandHologram.getToday(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_day(),"Tag"));
		this.ranking_week=manager.getHologram().createNameTagMessage(CommandHologram.getWeek(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_week(),"Woche"));
		this.ranking_month=manager.getHologram().createNameTagMessage(CommandHologram.getMonth(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_month(),"Monat"));
		this.ranking_total=manager.getHologram().createNameTagMessage(CommandHologram.getTotal(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking()));
	}
	
	@EventHandler
	public void Sign(SignChangeEvent ev){
		if(ev.getPlayer().hasPermission(kPermission.CHAT_FARBIG.getPermissionToString())){
			ev.setLine(0, ev.getLine(0).replaceAll("&", "§"));
			ev.setLine(1, ev.getLine(1).replaceAll("&", "§"));
			ev.setLine(2, ev.getLine(2).replaceAll("&", "§"));
			ev.setLine(3, ev.getLine(3).replaceAll("&", "§"));
		}
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
					UtilServer.getDeliveryPet().deliveryUSE(player, Material.PAPER, true);
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
					getManager().getMysql().Update("DELETE FROM BG_TWITTER WHERE uuid='" + UtilPlayer.getRealUUID(p) + "'");
					p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_FOLLOW_N"));
					p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_REMOVE"));
				}else{
					UtilServer.getDeliveryPet().deliveryBlock(p, Material.getMaterial(351));
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
	public void PotionSplash(PotionSplashEvent ev){
		for(PotionEffect pe : ev.getPotion().getEffects()){
			if(pe.getType().getName().equalsIgnoreCase("HARM")){
				ev.setCancelled(true);
				break;
			}else if(pe.getType().getName().equalsIgnoreCase("Invisibility")){
				ev.setCancelled(true);
				break;
			}
		}
	}
	
	@EventHandler
	public void PlayerItemConsum(PlayerItemConsumeEvent ev){
		if(ev.getItem().getType()==Material.POTION){
			if(ev.getItem().getDurability()==8270){
				ev.setItem(null);
				ev.setCancelled(true);
			}else if(ev.getItem().getDurability()==8206){
				ev.setItem(null);
				ev.setCancelled(true);
			}
		}
	}
	
	ItemStack a;
	@EventHandler(priority=EventPriority.LOWEST)
	public void Pickup(PlayerPickupItemEvent ev){
		if(ev.getItem().getItemStack().getAmount()<0||ev.getItem().getItemStack().getAmount()>64){
			ev.getItem().remove();
	        ev.getPlayer().sendMessage("§cFEHLER: BuggUsing ist verboten!");
		}else if(ev.getItem().getItemStack().getType()==Material.POTION){
			ev.getPlayer().getInventory().addItem(ev.getItem().getItemStack());
			ev.getItem().remove();
			ev.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Drop(InventoryClickEvent ev){
		if(ev.getWhoClicked() instanceof Player){
			if(ev.getInventory()!=null&&ev.getCurrentItem()!=null){
				
				if(ev.getCurrentItem().getAmount()<0||ev.getCurrentItem().getAmount()>64){
					ev.getCurrentItem().setAmount(1);
					ev.getCurrentItem().setType(Material.AIR);
					((Player)ev.getWhoClicked()).sendMessage("§cFEHLER: BuggUsing ist verboten!");
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Drop(PlayerDropItemEvent ev){
		if(ev.getItemDrop().getItemStack().getAmount()<0||ev.getItemDrop().getItemStack().getAmount()>64){
			ev.getItemDrop().remove();
	        ev.getPlayer().sendMessage("§cFEHLER: BuggUsing ist verboten!");
		}
	}
	
	@EventHandler
	public void onClickinEnchant(EnchantItemEvent e){
		if(e.getItem().getAmount() > 1){
			e.setCancelled(true);
			e.getEnchanter().sendMessage("§cFEHLER: BuggUsing ist verboten!");
		}
	}
	
	@EventHandler
	public void onClickinAnvil(InventoryClickEvent e){
	    try{
	      if ((e.getInventory().getType() == InventoryType.ANVIL) && 
	        (e.getCurrentItem().getAmount() > 1)){
	        e.setCancelled(true);
	        Player ps = (Player)e.getWhoClicked();
	        ps.sendMessage("§cFEHLER: BuggUsing ist verboten!");
	      }
	    }
	    catch (Exception localException){}
	  }
	
	@EventHandler
	public void GildeHome(GildenPlayerTeleportEvent ev){
		if(!getManager().getAntiManager().is(ev.getPlayer())){
			ev.getPlayer().sendMessage(Language.getText(player, "PREFIX")+Language.getText(ev.getPlayer(), "ANIT_LOGOUT_FIGHT_CMD"));
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		ev.setDeathMessage(null);
		if(ev.getEntity() instanceof Player){
			Player v = (Player)ev.getEntity();
			v.sendMessage(Language.getText(ev.getEntity().getKiller(), "PREFIX")+Language.getText(ev.getEntity().getKiller(), "PVP_DEATH"));
			
			getManager().getStatsManager().setInt(v, getManager().getStatsManager().getInt(Stats.DEATHS, v)+1, Stats.DEATHS);
			if(ev.getEntity().getKiller() instanceof Player){
				double elo = manager.getStatsManager().getDouble(Stats.ELO, ev.getEntity().getKiller());
				ELO.eloCHANGEProzent(ev.getEntity().getKiller(), v,10, manager.getStatsManager());
				elo=manager.getStatsManager().getDouble(Stats.ELO, ev.getEntity().getKiller())-elo;
				getManager().getStatsManager().setInt(ev.getEntity().getKiller(), getManager().getStatsManager().getInt(Stats.KILLS, ev.getEntity().getKiller())+1, Stats.KILLS);
				ev.getEntity().getKiller().sendMessage(Language.getText(ev.getEntity().getKiller(), "PREFIX")+Language.getText(ev.getEntity().getKiller(), "PVP_KILL",new Object[]{v.getName(),elo} ));
				updateFame(ev.getEntity().getKiller());
			}else{
				ELO.eloCHANGE(v, manager.getStatsManager());
			}
			updateFame( ((Player)ev.getEntity()) );
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
	public void Command(PlayerCommandPreprocessEvent ev){
		String cmd = "";
	    if (ev.getMessage().contains(" ")){
	      String[] parts = ev.getMessage().split(" ");
	      cmd = parts[0];
	    }else{
	      cmd = ev.getMessage();
	    }
	    
	    if(cmd.equalsIgnoreCase("/minecraft:")){
	    	ev.setCancelled(true);
	    	return;
	    }
	     
		if(ev.getPlayer().isOp()){
			if(cmd.equalsIgnoreCase("/reload")){
				ev.setCancelled(true);
				restart();
			}else if(cmd.equalsIgnoreCase("/restart")){
				ev.setCancelled(true);
				restart();
			}else if(cmd.equalsIgnoreCase("/stop")){
				ev.setCancelled(true);
				restart();
			}
		}else{
			if(!getManager().getAntiManager().is(ev.getPlayer())){
				if(cmd.equalsIgnoreCase("etpyes")||cmd.equalsIgnoreCase("/essentials:")||cmd.equalsIgnoreCase("/homes")||cmd.equalsIgnoreCase("/ereturn")||cmd.equalsIgnoreCase("/return")||cmd.equalsIgnoreCase("/ewarp")||cmd.equalsIgnoreCase("/etpa")||cmd.equalsIgnoreCase("/tpaccet")||cmd.equalsIgnoreCase("/tpyes")||cmd.equalsIgnoreCase("/tpask")||cmd.equalsIgnoreCase("/etpaccept")||cmd.equalsIgnoreCase("/ewarp")||cmd.equalsIgnoreCase("/tpa")||cmd.equalsIgnoreCase("/eback")||cmd.equalsIgnoreCase("/ehome")||cmd.equalsIgnoreCase("/tpaccept")||cmd.equalsIgnoreCase("/back")||cmd.equalsIgnoreCase("/home")||cmd.equalsIgnoreCase("/spawn")||cmd.equalsIgnoreCase("/espawn")||cmd.equalsIgnoreCase("/warp")){
					ev.getPlayer().sendMessage(Language.getText(player, "PREFIX")+"§cDu kannst den Befehl §b"+cmd+"§c nicht in Kampf ausführen!");
					ev.setCancelled(true);
				}
			}else{
				if(cmd.equalsIgnoreCase("etpyes")||cmd.equalsIgnoreCase("/essentials:")||cmd.equalsIgnoreCase("/homes")||cmd.equalsIgnoreCase("/ereturn")||cmd.equalsIgnoreCase("/return")||cmd.equalsIgnoreCase("/ewarp")||cmd.equalsIgnoreCase("/etpa")||cmd.equalsIgnoreCase("/tpask")||cmd.equalsIgnoreCase("/etpaccept")||cmd.equalsIgnoreCase("/ewarp")||cmd.equalsIgnoreCase("/eback")||cmd.equalsIgnoreCase("/ehome")||cmd.equalsIgnoreCase("/espawn")){
					ev.setCancelled(true);
				}
			}
		}
	}
	
	public void restart(){
		RestartScheduler restart = new RestartScheduler(getManager().getInstance());
		restart.setAnti(getManager().getAntiManager());
		restart.setGilden(getManager().getGildenManager());
		restart.setStats(getManager().getStatsManager());
		restart.start();
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
	public void SendRanking(PlayerJoinEvent ev){
		setHologramm(ev.getPlayer());
		this.ranking_day.sendToPlayer(ev.getPlayer());
		this.ranking_week.sendToPlayer(ev.getPlayer());
		this.ranking_total.sendToPlayer(ev.getPlayer());
		this.ranking_month.sendToPlayer(ev.getPlayer());
		TabTitle.setHeaderAndFooter(ev.getPlayer(), "§eEpicPvP§8.§eeu §8| §aPvP Server", "§aTeamSpeak: §7ts.EpicPvP.eu §8| §eWebsite: §7EpicPvP.eu");
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		ev.setJoinMessage(null);
		 ev.getPlayer().sendMessage(Language.getText(ev.getPlayer(), "PREFIX")+Language.getText(ev.getPlayer(), "WHEREIS_TEXT","PvP"));
		 
		 if(vote_list.contains( UtilPlayer.getRealUUID(ev.getPlayer()) )){
			 if(UtilServer.getDeliveryPet()!=null){
				 UtilServer.getDeliveryPet().deliveryUSE(ev.getPlayer(), Material.PAPER, true);
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
			
			for(Player p : UtilServer.getPlayers()){
				ranking_day.clear(p);
				ranking_week.clear(p);
				ranking_month.clear(p);
				ranking_total.clear(p);
			}

			this.ranking_day=manager.getHologram().createNameTagMessage(CommandHologram.getToday(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_day(),"Tag"));
			this.ranking_week=manager.getHologram().createNameTagMessage(CommandHologram.getWeek(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_week(),"Woche"));
			this.ranking_month=manager.getHologram().createNameTagMessage(CommandHologram.getMonth(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_month(),"Monat"));
			this.ranking_total=manager.getHologram().createNameTagMessage(CommandHologram.getTotal(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking()));
			
			for(Player p : UtilServer.getPlayers()){
				this.ranking_day.sendToPlayer(p);
				this.ranking_week.sendToPlayer(p);
				this.ranking_month.sendToPlayer(p);
				this.ranking_total.sendToPlayer(p);
			}
		}
	}
	
	@EventHandler
	public void Rüstung(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.BLOCK)){
			if(ev.getPlayer().getItemInHand().getType() == Material.ARMOR_STAND){
				if(!UtilWorldGuard.canBuild(ev.getClickedBlock().getLocation(),ev.getPlayer())){
					ev.setCancelled(true);
				}
			}
		}
	}
	
	public void setHologramm(Player p){
		getManager().getStatsManager().ExistPlayer(p);
		if(holo.containsKey(p)){
			holo.get(p).clear(p);
			holo.get(p).remove();
			holo.remove(p);
		}
		
		holo.put(p, getManager().getHologram().sendText(p, CommandHologram.getPlayer(), new String[]{
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
