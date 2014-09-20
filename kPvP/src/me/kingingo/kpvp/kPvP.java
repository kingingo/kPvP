package me.kingingo.kpvp;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Addons.AddonRealTime;
import me.kingingo.kcore.AntiLogout.AntiLogoutManager;
import me.kingingo.kcore.AntiLogout.AntiLogoutType;
import me.kingingo.kcore.Client.Client;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Command.Admin.CommandMuteAll;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Neuling.NeulingManager;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.PlayerStats.StatsManager;
import me.kingingo.kcore.SignShop.SignShop;
import me.kingingo.kcore.Update.Updater;
import me.kingingo.kcore.friend.FriendManager;
import me.kingingo.kcore.memory.MemoryFix;
import me.kingingo.kpvp.Command.CommandBanned;
import me.kingingo.kpvp.Command.CommandHologram;
import me.kingingo.kpvp.Command.CommandStats;
import me.kingingo.kpvp.Command.CommandURang;
import me.kingingo.kpvp.Command.CommandUnBan;
import me.kingingo.kpvp.Command.CommandkSpawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
	public static PermissionManager permManager;
	@Getter
	private GildenManager gildenManager;
	@Getter
	private FriendManager friendManager;
	@Getter
	private NeulingManager neulingManager;
	@Getter
	private AntiLogoutManager antiManager;
	@Getter
	private StatsManager statsManager;
	@Getter
	private Hologram hologram;
	@Getter
	@Setter
	private Location hologram_loc;
	@Getter
	private SignShop Shop;
	
	public void onEnable(){
		loadConfig();
		this.instance=this;
		updater=new Updater(this);
		this.client = new Client(getConfig().getString("Config.Client.Host"),getConfig().getInt("Config.Client.Port"),"PvP",this,updater);
		mysql=new MySQL(getConfig().getString("Config.MySQL.User"),getConfig().getString("Config.MySQL.Password"),getConfig().getString("Config.MySQL.Host"),getConfig().getString("Config.MySQL.DB"),this);
		permManager=new PermissionManager(this,mysql);
		permManager.setSetAllowTab(false);
		cmd=new CommandHandler(this);
		new MemoryFix(this);
		
		this.hologram_loc=new Location(Bukkit.getWorld("world"),getConfig().getDouble("Config.Hologram.X"),getConfig().getDouble("Config.Hologram.Y"),getConfig().getDouble("Config.Hologram.Z"));
		this.hologram_loc.getWorld().loadChunk(this.hologram_loc.getWorld().getChunkAt(this.hologram_loc));
		this.hologram=new Hologram(this);
		this.gildenManager=new GildenManager(this,mysql,GildenType.PVP,cmd);
		this.friendManager=new FriendManager(this,mysql,cmd);
		this.neulingManager=new NeulingManager(this,cmd,20);
		this.antiManager=new AntiLogoutManager(this,AntiLogoutType.KILL,30);
		this.statsManager=new StatsManager(this,mysql,GameType.PVP);
		this.Shop=new SignShop(this,statsManager);
		new AddonRealTime(this,Bukkit.getWorld("world"));
		cmd.register(CommandMuteAll.class, new CommandMuteAll(getPermManager()));
		cmd.register(CommandHologram.class, new CommandHologram(this));
		cmd.register(CommandStats.class, new CommandStats(getGildenManager(),getStatsManager()));
		cmd.register(CommandkSpawn.class, new CommandkSpawn());
		cmd.register(CommandURang.class, new CommandURang());
		cmd.register(CommandUnBan.class, new CommandUnBan(mysql));
		cmd.register(CommandBanned.class, new CommandBanned(mysql));
		new kPvPListener(this);
	}
	
	public void onDisable(){
		this.statsManager.SaveAllData();
		this.gildenManager.AllUpdateGilde();
		this.mysql.close();
		this.client.disconnect(false);
	}
	
	public void loadConfig(){
		getConfig().addDefault("Config.MySQL.Host", "NONE");
	    getConfig().addDefault("Config.MySQL.DB", "NONE");
	    getConfig().addDefault("Config.MySQL.User", "NONE");
	    getConfig().addDefault("Config.MySQL.Password", "NONE");
	    getConfig().addDefault("Config.Client.Host", "79.133.55.5");
	    getConfig().addDefault("Config.Client.Port", 9051);
	    getConfig().addDefault("Config.Hologram.X", 0);
	    getConfig().addDefault("Config.Hologram.Y", 0);
	    getConfig().addDefault("Config.Hologram.Z", 0);
	    getConfig().options().copyDefaults(true);
	    saveConfig();
	  }
	
}
