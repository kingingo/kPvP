package me.kingingo.kpvp.Command;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kpvp.Events.DropEvent;
import me.kingingo.kpvp.Manager.kEventManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEvent implements CommandExecutor{
	
	private kEventManager manager;
	
	public CommandEvent(kEventManager manager){
		this.manager=manager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "event", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.isOp()){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/Event [EventType]");
			}else{
				if(args[0].equalsIgnoreCase("stop")){
					if(manager.getEvent() != null){
						manager.getEvent().cancel();
						player.sendMessage(Language.getText(player, "PREFIX")+"§aDas Event wurde beendet!");
					}
				}else if(manager.getEvent()==null || manager.getEvent()!=null && !manager.getEvent().onCommand(player, args)){
					if(manager.getEvent() == null){
						switch(args[0]){
						case "DropEvent":
							manager.setEvent(new DropEvent(manager));
							player.sendMessage(Language.getText(player, "PREFIX")+"§aDas Drop Event wurde eingeleitet...");
							return true;
						}

						player.sendMessage(Language.getText(player, "PREFIX")+"§cDieser EventType konnte nicht gefunden werden!");
					}
				}
			}
		}
		return false;
	}

}
