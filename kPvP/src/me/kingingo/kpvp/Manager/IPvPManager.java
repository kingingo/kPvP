package me.kingingo.kpvp.Manager;

import lombok.Getter;
import me.kingingo.kcore.Command.Admin.CommandBroadcast;
import me.kingingo.kcore.Command.Admin.CommandCMDMute;
import me.kingingo.kcore.Command.Admin.CommandChatMute;
import me.kingingo.kcore.Command.Admin.CommandDebug;
import me.kingingo.kcore.Command.Admin.CommandFly;
import me.kingingo.kcore.Command.Admin.CommandFlyspeed;
import me.kingingo.kcore.Command.Admin.CommandGive;
import me.kingingo.kcore.Command.Admin.CommandGiveAll;
import me.kingingo.kcore.Command.Admin.CommandInvsee;
import me.kingingo.kcore.Command.Admin.CommandItem;
import me.kingingo.kcore.Command.Admin.CommandMore;
import me.kingingo.kcore.Command.Admin.CommandPvPMute;
import me.kingingo.kcore.Command.Admin.CommandSocialspy;
import me.kingingo.kcore.Command.Admin.CommandToggle;
import me.kingingo.kcore.Command.Admin.CommandTp;
import me.kingingo.kcore.Command.Admin.CommandTpHere;
import me.kingingo.kcore.Command.Admin.CommandTppos;
import me.kingingo.kcore.Command.Admin.CommandTrackingRange;
import me.kingingo.kcore.Command.Admin.CommandVanish;
import me.kingingo.kcore.Command.Admin.CommandgBroadcast;
import me.kingingo.kcore.Command.Commands.CommandBack;
import me.kingingo.kcore.Command.Commands.CommandClearInventory;
import me.kingingo.kcore.Command.Commands.CommandEnderchest;
import me.kingingo.kcore.Command.Commands.CommandFeed;
import me.kingingo.kcore.Command.Commands.CommandKit;
import me.kingingo.kcore.Command.Commands.CommandMsg;
import me.kingingo.kcore.Command.Commands.CommandNacht;
import me.kingingo.kcore.Command.Commands.CommandR;
import me.kingingo.kcore.Command.Commands.CommandRepair;
import me.kingingo.kcore.Command.Commands.CommandSpawn;
import me.kingingo.kcore.Command.Commands.CommandTag;
import me.kingingo.kcore.Command.Commands.CommandWarp;
import me.kingingo.kcore.TeleportManager.TeleportManager;
import me.kingingo.kpvp.kPvP;

public abstract class IPvPManager {

	@Getter
	private kPvP PvP;
	@Getter
	private TeleportManager teleport;
	
	public IPvPManager(kPvP PvP){
		this.PvP=PvP;
		this.teleport=new TeleportManager(getPvP().getCmd(), getPvP().getPermManager(), 5);
	
		getPvP().getCmd().register(CommandDebug.class, new CommandDebug());
		getPvP().getCmd().register(CommandFly.class, new CommandFly(getPvP()));
		getPvP().getCmd().register(CommandR.class, new CommandR(getPvP()));
		getPvP().getCmd().register(CommandSocialspy.class, new CommandSocialspy(getPvP()));
		getPvP().getCmd().register(CommandCMDMute.class, new CommandCMDMute(getPvP()));	
		getPvP().getCmd().register(CommandPvPMute.class, new CommandPvPMute(getPvP()));	
		getPvP().getCmd().register(CommandChatMute.class, new CommandChatMute(getPvP()));
		getPvP().getCmd().register(CommandToggle.class, new CommandToggle(getPvP()));
		getPvP().getCmd().register(CommandTrackingRange.class, new CommandTrackingRange());
		getPvP().getCmd().register(CommandGiveAll.class, new CommandGiveAll());
		getPvP().getCmd().register(CommandgBroadcast.class, new CommandgBroadcast(getPvP().getPacketManager()));
		getPvP().getCmd().register(CommandMsg.class, new CommandMsg());
		getPvP().getCmd().register(CommandFeed.class, new CommandFeed());
		getPvP().getCmd().register(CommandRepair.class, new CommandRepair());
		getPvP().getCmd().register(CommandTag.class, new CommandTag());
		getPvP().getCmd().register(CommandNacht.class, new CommandNacht());
		getPvP().getCmd().register(CommandWarp.class, new CommandWarp(getTeleport()));
		getPvP().getCmd().register(CommandKit.class, new CommandKit(getPvP().getUserData(),getPvP().getCmd()));
		getPvP().getCmd().register(CommandSpawn.class, new CommandSpawn(getTeleport()));
		getPvP().getCmd().register(CommandClearInventory.class, new CommandClearInventory());
		getPvP().getCmd().register(CommandInvsee.class, new CommandInvsee(getPvP().getMysql()));
		getPvP().getCmd().register(CommandEnderchest.class, new CommandEnderchest(getPvP().getMysql()));
		getPvP().getCmd().register(CommandBroadcast.class, new CommandBroadcast());
		getPvP().getCmd().register(CommandTppos.class, new CommandTppos());
		getPvP().getCmd().register(CommandItem.class, new CommandItem());
		getPvP().getCmd().register(CommandTp.class, new CommandTp());
		getPvP().getCmd().register(CommandTpHere.class, new CommandTpHere());
		getPvP().getCmd().register(CommandVanish.class, new CommandVanish(getPvP()));
		getPvP().getCmd().register(CommandMore.class, new CommandMore());
		getPvP().getCmd().register(CommandFlyspeed.class, new CommandFlyspeed());
		getPvP().getCmd().register(CommandBack.class, new CommandBack(getPvP()));
		getPvP().getCmd().register(CommandGive.class, new CommandGive());
	}

	public void onDisable(){
		
	}
	
}
