package me.kingingo.kpvp;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.PlayerStats.Event.PlayerStatsCreateEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilBG;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class kPvPListener extends kListener{

	@Getter
	private kPvP manager;
	private PermissionManager pex=null;
	
	public kPvPListener(kPvP manager){
		super(manager.getInstance(),"[kPvPListener]");
		this.manager=manager;
		this.pex = PermissionsEx.getPermissionManager();
	}
	
	@EventHandler
	public void Enderpearl(PlayerTeleportEvent ev){
		if(ev.getCause()==TeleportCause.ENDER_PEARL&&!ev.getPlayer().isPermissionSet("kpvp.enderpearl")){
			ev.setCancelled(true);
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
				new Restart(getManager());
			}else if(cmd.equalsIgnoreCase("/restart")){
				ev.setCancelled(true);
				new Restart(getManager());
			}else if(cmd.equalsIgnoreCase("/stop")){
				ev.setCancelled(true);
				new Restart(getManager());
			}
		}else{
			if(!getManager().getAntiManager().is(ev.getPlayer())){
				if(cmd.equalsIgnoreCase("/tpa")||cmd.equalsIgnoreCase("/home")||cmd.equalsIgnoreCase("/spawn")||cmd.equalsIgnoreCase("/warp")){
					ev.getPlayer().sendMessage(Text.PREFIX.getText()+"§cDu kannst den Befehl §b"+cmd+"§c nicht in Kampf ausführen!");
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		ev.setQuitMessage(null);
		getManager().getStatsManager().SaveAllPlayerData(ev.getPlayer());
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		ev.setJoinMessage(null);
		setHologramm(ev.getPlayer());
		
		if(ev.getPlayer().getName().length()>13){
			ev.getPlayer().setPlayerListName(pex.getUser(ev.getPlayer()).getPrefix().substring(0, 2).replaceAll("&", "§")+ev.getPlayer().getName().substring(0, 13));
		}else{
			ev.getPlayer().setPlayerListName(pex.getUser(ev.getPlayer()).getPrefix().substring(0, 2).replaceAll("&", "§")+ev.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_04)return;
		for(Player p : UtilServer.getPlayers())setHologramm(p);
	}
	
	public void setHologramm(Player p){
		getManager().getStatsManager().ExistPlayer(p);
		getManager().getHologram().RemoveText(p);
		getManager().getHologram().sendText(p, getManager().getHologram_loc(), new String[]{
			"§6Name§a "+p.getName(),
			"§6Gilde§b "+getManager().getGildenManager().getPlayerGilde(p.getName()),
			"§6Ranking§b "+getManager().getStatsManager().getRank(Stats.KILLS, p),
			"§6Kills§b "+getManager().getStatsManager().getInt(Stats.KILLS, p),
			"§6Tode§b "+getManager().getStatsManager().getInt(Stats.DEATHS, p),
			"§6Money§b "+getManager().getStatsManager().getDouble(Stats.MONEY, p),
			"§6KDR§b "+getManager().getStatsManager().getKDR(getManager().getStatsManager().getInt(Stats.KILLS, p), getManager().getStatsManager().getInt(Stats.DEATHS, p)),
		});
	}
	
}
