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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class kPvPListener extends kListener{

	@Getter
	private kPvP manager;
	
	public kPvPListener(kPvP manager){
		super(manager.getInstance(),"[kPvPListener]");
		this.manager=manager;
	}
	
	@EventHandler
	public void Enderpearl(PlayerTeleportEvent ev){
		if(ev.getCause()==TeleportCause.ENDER_PEARL&&!ev.getPlayer().isPermissionSet("kpvp.enderpearl")){
			ev.setCancelled(true);
		}
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
				manager.getGildenManager().setOnDisable(true);
				manager.getStatsManager().setOnDisable(true);
				manager.getAntiManager().setOnDisable(true);
				for(Player player : UtilServer.getPlayers()){
					UtilBG.sendToServer(player, "falldown", manager);
				}
				
				Bukkit.getScheduler().scheduleAsyncDelayedTask(manager.getInstance(), new Runnable(){

		  			@Override
		  			public void run() {
		  				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
		  			}
		      		 
		      	 }, 25*20);
				
			}else if(cmd.equalsIgnoreCase("/restart")){
				manager.getGildenManager().setOnDisable(true);
				manager.getStatsManager().setOnDisable(true);
				manager.getAntiManager().setOnDisable(true);
				for(Player player : UtilServer.getPlayers()){
					UtilBG.sendToServer(player, "falldown", manager);
				}
			}else if(cmd.equalsIgnoreCase("/stop")){
				manager.getGildenManager().setOnDisable(true);
				manager.getStatsManager().setOnDisable(true);
				manager.getAntiManager().setOnDisable(true);
				for(Player player : UtilServer.getPlayers()){
					UtilBG.sendToServer(player, "falldown", manager);
				}
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
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_16)return;
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
