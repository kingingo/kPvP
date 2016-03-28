package eu.epicpvp.kpvp;

import lombok.Getter;
import eu.epicpvp.kcore.AACHack.AACHack;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Hologram.Hologram;
import eu.epicpvp.kcore.ItemShop.ItemShop;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Listener.AntiCrashListener.AntiCrashListener;
import eu.epicpvp.kcore.Listener.BungeeCordFirewall.BungeeCordFirewallListener;
import eu.epicpvp.kcore.Listener.Command.ListenerCMD;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Update.Updater;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.UtilException;
import eu.epicpvp.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.connection.ClientType;
import eu.epicpvp.kpvp.Listener.Listener;

public class kPvP extends JavaPlugin{

	@Getter
	private ClientWrapper client;
	@Getter
	private JavaPlugin instance;
	@Getter
	private Updater updater;
	@Getter
	private MySQL mysql;
	@Getter
	private CommandHandler cmd;
	@Getter
	public PermissionManager permissionManager;
	@Getter
	private AACHack aACHack;
	@Getter
	private UserDataConfig userData;
	@Getter
	private Hologram hologram;
	@Getter
	private kPvPManager manager;
	
	public void onEnable(){
		try{
		loadConfig();
		this.mysql=new MySQL(getConfig().getString("Config.MySQL.User"),getConfig().getString("Config.MySQL.Password"),getConfig().getString("Config.MySQL.Host"),getConfig().getString("Config.MySQL.DB"),this);
		Language.load(mysql);
		this.instance=this;
		this.updater=new Updater(this);
		this.client = UtilServer.createClient(this,ClientType.OTHER, getConfig().getString("Config.Client.Host"), getConfig().getInt("Config.Client.Port"), "PvP");
		this.cmd=new CommandHandler(this);
		this.permissionManager=new PermissionManager(this);
		this.hologram=new Hologram(this);
		this.hologram.RemoveText();
		this.userData=new UserDataConfig(this);
		this.manager=new kPvPManager(this);

		new BungeeCordFirewallListener(mysql,cmd, "pvp");
		new Listener(getManager());
		new ListenerCMD(this);
		UtilServer.createLagListener(cmd);
		
		
		}catch(Exception e){
			UtilException.catchException(e, "pvp", Bukkit.getIp(), mysql);
		}
	}
	
	public void onDisable(){
		this.manager.onDisable();
		this.mysql.close();
		this.client.getHandle().disconnect();
		updater.stop();
		UtilServer.getUpdaterAsync().stop();
		saveConfig();
	}
	
	public void loadConfig(){
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
