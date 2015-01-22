package me.kingingo.kpvp.Command;

import java.util.HashMap;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Util.TimeSpan;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandkSpawn implements CommandExecutor{

	private HashMap<String,Long> timer = new HashMap<>();
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "kspawn", alias = {"ksp"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			if(p.hasPermission("kpvp.kspawn")){
				if(!timer.containsKey(p.getName())){
					timer.put(p.getName(), System.currentTimeMillis());
					p.teleport(p.getWorld().getSpawnLocation());
				}else{
					if((timer.get(p.getName())+TimeSpan.HOUR)<System.currentTimeMillis()){
						timer.remove(p);
						timer.put(p.getName(), System.currentTimeMillis());
						p.teleport(p.getWorld().getSpawnLocation());
					}else{
						p.sendMessage(Text.PREFIX.getText()+"§cDu kannst den Befehl nur jede 1 Std benutzten!");
					}
				}
			}
		}
		return false;
	}
	
}
