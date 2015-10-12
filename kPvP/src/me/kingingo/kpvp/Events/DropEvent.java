package me.kingingo.kpvp.Events;

import java.io.File;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.kConfig.kConfig;
import me.kingingo.kpvp.Manager.kEventManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class DropEvent extends kListener implements IEvent{

	@Getter
	@Setter
	public kEventManager manager;
	private kConfig config;
	private PetManager petManager;
	
	public DropEvent(kEventManager manager){
		super(manager.getPvP(),"DropEvent");
		this.manager = manager;
		this.petManager = new PetManager(manager.getPvP());
		this.config=new kConfig(new File("plugins"+File.separator+manager.getPvP().getPlugin(manager.getPvP().getClass()).getName()+File.separator+"DropEvent.yml"));
		
		if(config.getString("Events.Spawn")!=null&&Bukkit.getWorld(config.getString("Events.Spawn.world"))!=null){
			if(config.isSet("Events.Spawn")){
				Location l = config.getLocation("Events.Spawn");
				Bukkit.getWorld("world").setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());
			}
		}
	}
	
	public boolean onCommand(Player player,String[] args){
		if(args[0].equalsIgnoreCase("setspawn")){
			this.config.setLocation("Events.Spawn", player.getLocation());
			Bukkit.getWorld("world").setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
			player.sendMessage(Language.getText(player, "PREFIX")+"§aDer Spawn fürs DropEvent wurde gesetzt!");
		}else if(args[0].equalsIgnoreCase("add")){
			
		}else if(args[0].equalsIgnoreCase("remove")){
			
		}else if(args[0].equalsIgnoreCase("list")){
			
		}
		return false;
	}
	
	public void cancel(){
		this.config.save();
	}
	
	public String getEventName() {
		return "DropEvent";
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		ev.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
	}

}
