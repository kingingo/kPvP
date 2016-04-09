package eu.epicpvp.kpvp.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilTime;

public class Commandifix implements CommandExecutor{
	
	private Player player;
	private String s;
	private Long l;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "ifix", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(PermissionType.REPAIR.getPermissionToString())){
			
			s=UtilTime.getTimeManager().check(cmd.getName(), player);
			if(s!=null){
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "USE_BEFEHL_TIME",s));
			}else{
				
				l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
				if( l!=0 ){
					UtilTime.getTimeManager().add(cmd.getName(), player, l);
				}
				
				if(args.length==0){
					if(player.hasPermission(PermissionType.REPAIR_HAND.getPermissionToString())){
						UtilItem.RepairItem(player.getItemInHand());
						player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "REPAIR_HAND"));
					}
				}else{
					if(args[0].equalsIgnoreCase("all")){
						if(player.hasPermission(PermissionType.REPAIR_ALL.getPermissionToString())){
							UtilInv.repairInventory(player, false);
							player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "REPAIR_ALL"));
						}
					}
				}
			}
		}
		return false;
	}

}
