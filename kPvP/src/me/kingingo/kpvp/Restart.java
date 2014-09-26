package me.kingingo.kpvp;

import java.io.IOException;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilBG;
import me.kingingo.kcore.Util.UtilDisplay;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Restart extends kListener{

	@Getter
	private kPvP instance;
	int start=35;
	
	public Restart(kPvP instance){
		super(instance.getInstance(),"[RestartScript]");
		this.instance=instance;
		getInstance().getGildenManager().setOnDisable(true);
		getInstance().getStatsManager().setOnDisable(true);
		getInstance().getAntiManager().setOnDisable(true);
	}
	
	public void broadcast(String msg){
		Bukkit.broadcastMessage(msg);
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		start--;
		for(Player p : UtilServer.getPlayers())UtilDisplay.displayTextBar(p, Text.RESTART_IN.getText(start));
		
		switch(start){
		case 30:
			broadcast(Text.PREFIX.getText()+Text.RESTART_IN.getText(start));
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
			break;
		case 25:
			broadcast(Text.PREFIX.getText()+Text.RESTART_IN.getText(start));
			for(Player p : UtilServer.getPlayers())UtilBG.sendToServer(p, "falldown", getInstance().getInstance());
			break;
		case 23:
			getInstance().getStatsManager().SaveAllData();
			getInstance().getGildenManager().AllUpdateGilde();
		break;
		case 20:broadcast(Text.PREFIX.getText()+Text.RESTART_IN.getText(start));
		for(Player p : UtilServer.getPlayers())UtilBG.sendToServer(p, "falldown", getInstance().getInstance());
			break;
		case 10:broadcast(Text.PREFIX.getText()+Text.RESTART_IN.getText(start));break;
		case 5:broadcast(Text.PREFIX.getText()+Text.RESTART_IN.getText(start));break;
		case 4:broadcast(Text.PREFIX.getText()+Text.RESTART_IN.getText(start));break;
		case 3:broadcast(Text.PREFIX.getText()+Text.RESTART_IN.getText(start));break;
		case 2:broadcast(Text.PREFIX.getText()+Text.RESTART_IN.getText(start));break;
		case 1:broadcast(Text.PREFIX.getText()+Text.RESTART_IN.getText(start));break;
		case 0: 
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
			try {
				Runtime.getRuntime().exec("./start.sh");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
}
