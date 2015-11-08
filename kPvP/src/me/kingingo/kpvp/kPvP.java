package me.kingingo.kpvp;

import lombok.Getter;
import me.kingingo.kcore.AACHack.AACHack;
import me.kingingo.kcore.Client.Client;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.GemsShop.GemsShop;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.BungeeCordFirewall.BungeeCordFirewallListener;
import me.kingingo.kcore.Listener.Command.ListenerCMD;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Permission.GroupTyp;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Update.Updater;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.UtilException;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.memory.MemoryFix;
import me.kingingo.kpvp.Listener.Listener;
import me.kingingo.kpvp.Manager.IPvPManager;
import me.kingingo.kpvp.Manager.kPvPManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class kPvP extends JavaPlugin{

	@Getter
	private Client client;
	@Getter
	private JavaPlugin instance;
	@Getter
	private Updater updater;
	@Getter
	private MySQL mysql;
	@Getter
	private CommandHandler cmd;
	@Getter
	public PermissionManager permManager;
	@Getter
	private PacketManager packetManager;
	@Getter
	private AACHack aACHack;
	@Getter
	private UserDataConfig userData;
	@Getter
	private Hologram hologram;
	@Getter
	private IPvPManager manager;
	
	public void onEnable(){
		try{
		loadConfig();
		this.mysql=new MySQL(getConfig().getString("Config.MySQL.User"),getConfig().getString("Config.MySQL.Password"),getConfig().getString("Config.MySQL.Host"),getConfig().getString("Config.MySQL.DB"),this);
		Language.load(mysql);
		this.instance=this;
		this.updater=new Updater(this);
		this.client = new Client(getConfig().getString("Config.Client.Host"),getConfig().getInt("Config.Client.Port"),"PvP",this,updater);
		this.cmd=new CommandHandler(this);
		this.packetManager=new PacketManager(this,client);
		this.permManager=new PermissionManager(this,GroupTyp.PVP,packetManager,mysql);
		this.hologram=new Hologram(this);
		this.hologram.RemoveText();
		this.userData=new UserDataConfig(this);
		
		switch(getConfig().getString("Config.Mode")){
		case "Event":
			
			break;
		default:
			this.manager=new kPvPManager(this);
			break;
		}

		new BungeeCordFirewallListener(mysql, "pvp");
		new Listener(getManager());
		new MemoryFix(this);
		new ListenerCMD(this);
		UtilServer.createLagListener(cmd);
		}catch(Exception e){
			UtilException.catchException(e, "pvp", Bukkit.getIp(), mysql);
		}
	}
	
	public void onDisable(){
		this.manager.onDisable();
		this.mysql.close();
		this.client.disconnect(false);
		updater.stop();
		saveConfig();
	}
	
	public void loadConfig(){
		getConfig().addDefault("Config.Mode", "Server");
		getConfig().addDefault("Config.MySQL.Host", "NONE");
	    getConfig().addDefault("Config.MySQL.DB", "NONE");
	    getConfig().addDefault("Config.MySQL.User", "NONE");
	    getConfig().addDefault("Config.MySQL.Password", "NONE");
	    getConfig().addDefault("Config.Client.Host", "79.133.55.5");
	    getConfig().addDefault("Config.Client.Port", 9051);
	    getConfig().options().copyDefaults(true);
	    saveConfig();
	  }
	
}
