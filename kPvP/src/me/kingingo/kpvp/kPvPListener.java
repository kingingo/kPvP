package me.kingingo.kpvp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.PLAYER_VOTE;
import me.kingingo.kcore.Packet.Packets.WORLD_CHANGE_DATA;
import me.kingingo.kcore.Permission.Event.PlayerLoadPermissionEvent;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.Event.PlayerStatsCreateEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.RestartScheduler;
import me.kingingo.kcore.Util.TabTitle;
import me.kingingo.kcore.Util.UtilELO;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilScoreboard;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilWorldGuard;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
	private ArrayList<UUID> vote_list = new ArrayList<>();
	
	public kPvPListener(kPvP manager){
		super(manager.getInstance(),"[kPvPListener]");
		this.manager=manager;
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
				manager.getStatsManager().setDouble(player, manager.getStatsManager().getDouble(Stats.MONEY, player)+500, Stats.MONEY);
				UtilInv.repairInventory(player, true);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "VOTE_THX"));
			}else{
				vote_list.add(vote.getUuid());
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
			getManager().getStatsManager().setInt(v, getManager().getStatsManager().getInt(Stats.DEATHS, v)+1, Stats.DEATHS);
			if(ev.getEntity().getKiller() instanceof Player){
				getManager().getStatsManager().setDouble(ev.getEntity().getKiller(), UtilELO.eloBerechnen(getManager().getStatsManager().getDouble(Stats.ELO, ev.getEntity().getKiller()), getManager().getStatsManager().getDouble(Stats.ELO, ((Player)ev.getEntity()))), Stats.ELO);
				getManager().getStatsManager().setInt(ev.getEntity().getKiller(), getManager().getStatsManager().getInt(Stats.KILLS, ev.getEntity().getKiller())+1, Stats.KILLS);
			}
			getManager().getStatsManager().setDouble(v, getManager().getStatsManager().getDouble(Stats.ELO, v), Stats.TIME_ELO);
			getManager().getStatsManager().setInt(player, (int)System.currentTimeMillis(), Stats.TIME);
			getManager().getStatsManager().setDouble(v, UtilELO.START_WERT, Stats.ELO);
			updateFame(ev.getEntity().getKiller());
			updateFame( ((Player)ev.getEntity()) );
		}
	}
	
	@EventHandler
	public void NEW(PlayerStatsCreateEvent ev){
		getManager().getNeulingManager().add(ev.getPlayer());
		getManager().getStatsManager().setDouble(ev.getPlayer(), UtilELO.START_WERT, Stats.ELO);
		getManager().getStatsManager().setDouble(ev.getPlayer(), 0, Stats.TIME_ELO);
		getManager().getStatsManager().setInt(ev.getPlayer(), (int)System.currentTimeMillis(), Stats.TIME);
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
	public void Quit(PlayerQuitEvent ev){
		ev.setQuitMessage(null);
		if(holo.containsKey(ev.getPlayer())){
			holo.get(ev.getPlayer()).clear(ev.getPlayer());
			holo.remove(ev.getPlayer());
		}
		getManager().getStatsManager().SaveAllPlayerData(ev.getPlayer());
	}
	
	public void updateFame(Player player){
		for(Player p : UtilServer.getPlayers()){
			UtilScoreboard.setScore(player.getScoreboard(), p.getName(), DisplaySlot.BELOW_NAME, UtilNumber.toInt(getManager().getStatsManager().getDouble(Stats.ELO, player)));
		}
	}
	
	@EventHandler
	public void loadPerm(PlayerLoadPermissionEvent ev){
		for(Player player : UtilServer.getPlayers()){
			if(player.getScoreboard()==null)player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			if(player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME)==null)UtilScoreboard.addBoard(player.getScoreboard(), DisplaySlot.BELOW_NAME, "§bFame");		
			UtilScoreboard.setScore(player.getScoreboard(), ev.getPlayer().getName(), DisplaySlot.BELOW_NAME, UtilNumber.toInt(getManager().getStatsManager().getDouble(Stats.ELO, player)));
		}
		updateFame(ev.getPlayer());
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		ev.setJoinMessage(null);
		setHologramm(ev.getPlayer());
		TabTitle.setHeaderAndFooter(ev.getPlayer(), "§eEpicPvP§8.§eeu §8| §aPvP Server", "§aTeamSpeak: §7ts.EpicPvP.eu §8| §eWebsite: §7EpicPvP.eu");
		 ev.getPlayer().sendMessage(Language.getText(ev.getPlayer(), "PREFIX")+Language.getText(ev.getPlayer(), "WHEREIS_TEXT","PvP"));
		 
		 if(vote_list.contains( UtilPlayer.getRealUUID(ev.getPlayer()) )){
			 vote_list.remove(UtilPlayer.getRealUUID(ev.getPlayer()));
			 manager.getStatsManager().setDouble(ev.getPlayer(), manager.getStatsManager().getDouble(Stats.MONEY, ev.getPlayer())+500, Stats.MONEY);
			 UtilInv.repairInventory(ev.getPlayer(), true);
			 ev.getPlayer().sendMessage(Language.getText(ev.getPlayer(), "PREFIX")+Language.getText(ev.getPlayer(), "VOTE_THX"));
		 }
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_08)return;
		for(Player p : UtilServer.getPlayers())setHologramm(p);
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
			holo.remove(p);
		}
		holo.put(p, getManager().getHologram().sendText(p, getManager().getHologram_loc(), new String[]{
			"§6Name§a "+p.getName(),
			"§6Gilde§b "+getManager().getGildenManager().getPlayerGilde(p),
			"§6Ranking§b "+getManager().getStatsManager().getRank(Stats.KILLS, p),
			"§6Kills§b "+getManager().getStatsManager().getInt(Stats.KILLS, p),
			"§6Fame§b "+getManager().getStatsManager().getInt(Stats.ELO, p),
			"§6Tode§b "+getManager().getStatsManager().getInt(Stats.DEATHS, p),
			"§6Money§b "+getManager().getStatsManager().getDouble(Stats.MONEY, p),
			"§6KDR§b "+getManager().getStatsManager().getKDR(getManager().getStatsManager().getInt(Stats.KILLS, p), getManager().getStatsManager().getInt(Stats.DEATHS, p)),
		}));
	}
	
}
