package me.kingingo.kpvp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.PlayerStats.Stats;

public class kPvPListener extends kListener{

	@Getter
	private kPvP manager;
	
	public kPvPListener(JavaPlugin instance,kPvP manager){
		super(instance,"[kPvPListener]");
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		getManager().getStatsManager().SaveAllPlayerData(ev.getPlayer());
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		getManager().getHologram().sendText(ev.getPlayer(), getManager().getHologram_loc(), new String[]{
			"Name "+ev.getPlayer().getName(),
			"Gilde "+getManager().getGildenManager().getPlayerGilde(ev.getPlayer().getName()),
			"Kills "+getManager().getStatsManager().getInt(Stats.KILLS, ev.getPlayer()),
			"Tode "+getManager().getStatsManager().getInt(Stats.DEATHS, ev.getPlayer()),
			"KDR "+getManager().getStatsManager().getKDR(getManager().getStatsManager().getInt(Stats.KILLS, ev.getPlayer()), getManager().getStatsManager().getInt(Stats.DEATHS, ev.getPlayer())),
			"Ranking "+getManager().getStatsManager().getRank(Stats.KILLS, ev.getPlayer()),
		});
	}
	
}
