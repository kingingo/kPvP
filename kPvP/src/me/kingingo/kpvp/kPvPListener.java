package me.kingingo.kpvp;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.PlayerStats.Event.PlayerStatsChangeEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilBG;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class kPvPListener extends kListener{

	@Getter
	private kPvP manager;
	
	public kPvPListener(kPvP manager){
		super(manager,"[kPvPListener]");
		this.manager=manager;
	}
	
	@EventHandler
	public void Command(PlayerCommandPreprocessEvent ev){
		if(ev.getPlayer().isOp()){
			String cmd = "";
			 
		     if (ev.getMessage().contains(" ")){
		       String[] parts = ev.getMessage().split(" ");
		       cmd = parts[0];
		     }else{
		       cmd = ev.getMessage();
		     }
			if(cmd.equalsIgnoreCase("/reload")){
				ev.setCancelled(true);
				manager.getGildenManager().setOnDisable(true);
				manager.getStatsManager().setOnDisable(true);
				for(Player player : UtilServer.getPlayers()){
					UtilBG.sendToServer(player, "falldown", manager);
				}
				
				Bukkit.getScheduler().scheduleAsyncDelayedTask(manager, new Runnable(){

		  			@Override
		  			public void run() {
		  				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
		  			}
		      		 
		      	 }, 4*20);
				
			}else if(cmd.equalsIgnoreCase("/restart")){
				manager.getGildenManager().setOnDisable(true);
				manager.getStatsManager().setOnDisable(true);
				for(Player player : UtilServer.getPlayers()){
					UtilBG.sendToServer(player, "falldown", manager);
				}
			}else if(cmd.equalsIgnoreCase("/stop")){
				manager.getGildenManager().setOnDisable(true);
				manager.getStatsManager().setOnDisable(true);
				for(Player player : UtilServer.getPlayers()){
					UtilBG.sendToServer(player, "falldown", manager);
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
	public void Hologramm(UpdateEvent ev){
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
			"§6KDR§b "+getManager().getStatsManager().getKDR(getManager().getStatsManager().getInt(Stats.KILLS, p), getManager().getStatsManager().getInt(Stats.DEATHS, p)),
		});
	}
	
}
