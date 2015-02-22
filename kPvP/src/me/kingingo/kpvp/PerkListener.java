package me.kingingo.kpvp;

import me.kingingo.kcore.Kit.PerkManager;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.Permission;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PerkListener extends kListener{
	
	public PerkManager perkManager;
	
	public PerkListener(PerkManager perkManager){
		super(kPvP.permManager.getInstance(),"PerkListener");
		this.perkManager=perkManager;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Join(PlayerJoinEvent ev){
		if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_ALL)){
			perkManager.addPlayer(ev.getPlayer());
		}else{
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_NO_HUNGER))perkManager.addPlayer("noHunger", ev.getPlayer());
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_DOUBLE_XP))perkManager.addPlayer("DoubleXP", ev.getPlayer());
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_DROPPER))perkManager.addPlayer("Dropper", ev.getPlayer());
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_GET_XP))perkManager.addPlayer("GetXP", ev.getPlayer());
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_GOLENAPPLE))perkManager.addPlayer("PotionClear", ev.getPlayer());
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_NO_FIRE))perkManager.addPlayer("noFiredamage", ev.getPlayer());
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_HEALER))perkManager.addPlayer("HealPotion", ev.getPlayer());
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_ITEM_NAME))perkManager.addPlayer("ItemName", ev.getPlayer());
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_JUMP))perkManager.addPlayer("DoubleJump", ev.getPlayer());
			if(kPvP.permManager.hasPermission(ev.getPlayer(), Permission.PERK_RUNNER))perkManager.addPlayer("Runner", ev.getPlayer());	
		}
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		perkManager.removePlayer(ev.getPlayer());
	}
	
}
