package me.kingingo.kpvp.Command;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.PlayerStats.StatsManager;
import me.kingingo.kpvp.kPvP;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHologram implements CommandExecutor{
	
	@Getter
	private kPvP manager;
	
	public CommandHologram(kPvP manager){
		this.manager=manager;
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setholo", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(p.isOp()){
			manager.getConfig().set("Config.Hologram.X", p.getLocation().getX());
			manager.getConfig().set("Config.Hologram.Y", p.getLocation().getY());
			manager.getConfig().set("Config.Hologram.Z", p.getLocation().getZ());
			manager.saveConfig();
			manager.setHologram_loc(p.getLocation());
			p.sendMessage(Text.PREFIX.getText()+"§a Das Hologramm wurde gesetzt!");
		}
		return false;
	}
	
}
