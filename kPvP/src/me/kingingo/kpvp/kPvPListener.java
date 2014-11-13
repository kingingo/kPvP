package me.kingingo.kpvp;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.PlayerStats.Event.PlayerStatsCreateEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.TabTitle;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class kPvPListener extends kListener{

	@Getter
	private kPvP manager;
	private PermissionManager pex=null;
	private HashMap<Player,NameTagMessage> holo = new HashMap<>();
	
	public kPvPListener(kPvP manager){
		super(manager.getInstance(),"[kPvPListener]");
		this.manager=manager;
		this.pex = PermissionsEx.getPermissionManager();
	}
	
	@EventHandler
	public void Packet(PacketReceiveEvent ev){
		
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
			}
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
			ev.getPlayer().sendMessage(Text.PREFIX.getText()+"§cDu kannst den Befehl §b/gilden home§c nicht in Kampf ausführen!");
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		ev.setDeathMessage(null);
		if(ev.getEntity() instanceof Player){
			Player v = (Player)ev.getEntity();
			getManager().getStatsManager().setInt(v, getManager().getStatsManager().getInt(Stats.DEATHS, v)+1, Stats.DEATHS);
			if(ev.getEntity().getKiller() instanceof Player)getManager().getStatsManager().setInt(ev.getEntity().getKiller(), getManager().getStatsManager().getInt(Stats.KILLS, ev.getEntity().getKiller())+1, Stats.KILLS);
		}
	}
	
	@EventHandler
	public void NEW(PlayerStatsCreateEvent ev){
		getManager().getNeulingManager().add(ev.getPlayer());
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
	     
		if(ev.getPlayer().isOp()){
			if(cmd.equalsIgnoreCase("/reload")){
				ev.setCancelled(true);
				getManager().getRestart().start();
			}else if(cmd.equalsIgnoreCase("/restart")){
				ev.setCancelled(true);
				getManager().getRestart().start();
			}else if(cmd.equalsIgnoreCase("/stop")){
				ev.setCancelled(true);
				getManager().getRestart().start();
			}
		}else{
			if(!getManager().getAntiManager().is(ev.getPlayer())){
				if(cmd.equalsIgnoreCase("/ewarp")||cmd.equalsIgnoreCase("/tpa")||cmd.equalsIgnoreCase("/eback")||cmd.equalsIgnoreCase("/ehome")||cmd.equalsIgnoreCase("/tpaccept")||cmd.equalsIgnoreCase("/back")||cmd.equalsIgnoreCase("/home")||cmd.equalsIgnoreCase("/spawn")||cmd.equalsIgnoreCase("/espawn")||cmd.equalsIgnoreCase("/warp")){
					ev.getPlayer().sendMessage(Text.PREFIX.getText()+"§cDu kannst den Befehl §b"+cmd+"§c nicht in Kampf ausführen!");
					ev.setCancelled(true);
				}
			}
		}
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
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		ev.setJoinMessage(null);
		setHologramm(ev.getPlayer());
		TabTitle.setHeaderAndFooter(ev.getPlayer(), "§eEPICPVP §7-§e PvP Server", "§eShop.EpicPvP.de");
		if(ev.getPlayer().getName().length()>13){
			ev.getPlayer().setPlayerListName(pex.getUser(ev.getPlayer()).getPrefix().substring(0, 2).replaceAll("&", "§")+ev.getPlayer().getName().substring(0, 13));
		}else{
			ev.getPlayer().setPlayerListName(pex.getUser(ev.getPlayer()).getPrefix().substring(0, 2).replaceAll("&", "§")+ev.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_08)return;
		for(Player p : UtilServer.getPlayers())setHologramm(p);
	}
	
	public void setHologramm(Player p){
		if(UtilPlayer.getVersion(p)>=47)return;
		getManager().getStatsManager().ExistPlayer(p);
		if(holo.containsKey(p)){
			holo.get(p).clear(p);
			holo.remove(p);
		}
		holo.put(p, getManager().getHologram().sendText(p, getManager().getHologram_loc(), new String[]{
			"§6Name§a "+p.getName(),
			"§6Gilde§b "+getManager().getGildenManager().getPlayerGilde(p.getName()),
			"§6Ranking§b "+getManager().getStatsManager().getRank(Stats.KILLS, p),
			"§6Kills§b "+getManager().getStatsManager().getInt(Stats.KILLS, p),
			"§6Tode§b "+getManager().getStatsManager().getInt(Stats.DEATHS, p),
			"§6Money§b "+getManager().getStatsManager().getDouble(Stats.MONEY, p),
			"§6KDR§b "+getManager().getStatsManager().getKDR(getManager().getStatsManager().getInt(Stats.KILLS, p), getManager().getStatsManager().getInt(Stats.DEATHS, p)),
		}));
	}
	
}
