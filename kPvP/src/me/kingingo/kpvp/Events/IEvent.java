package me.kingingo.kpvp.Events;

import me.kingingo.kpvp.Manager.kEventManager;

import org.bukkit.entity.Player;

public abstract interface IEvent {
	public abstract String getEventName();
	public abstract kEventManager getManager();
	public abstract void setManager(kEventManager manager);
	public abstract void cancel();
	public abstract boolean onCommand(Player player,String[] args);
}
