package me.kingingo.kpvp;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.AACHack.AACHack;
import me.kingingo.kcore.AntiLogout.AntiLogoutManager;
import me.kingingo.kcore.AntiLogout.AntiLogoutType;
import me.kingingo.kcore.Client.Client;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Command.Admin.CommandBanned;
import me.kingingo.kcore.Command.Admin.CommandCMDMute;
import me.kingingo.kcore.Command.Admin.CommandChatMute;
import me.kingingo.kcore.Command.Admin.CommandFly;
import me.kingingo.kcore.Command.Admin.CommandGiveAll;
import me.kingingo.kcore.Command.Admin.CommandGroup;
import me.kingingo.kcore.Command.Admin.CommandPermissionsExConverter;
import me.kingingo.kcore.Command.Admin.CommandPvPMute;
import me.kingingo.kcore.Command.Admin.CommandToggle;
import me.kingingo.kcore.Command.Admin.CommandTrackingRange;
import me.kingingo.kcore.Command.Admin.CommandURang;
import me.kingingo.kcore.Command.Admin.CommandUnBan;
import me.kingingo.kcore.Command.Admin.CommandgBroadcast;
import me.kingingo.kcore.Command.Commands.CommandTreasureChest;
import me.kingingo.kcore.Command.Commands.CommandXP;
import me.kingingo.kcore.Command.Commands.CommandkSpawn;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.PerkManager;
import me.kingingo.kcore.Kit.Command.CommandPerk;
import me.kingingo.kcore.Kit.Perks.PerkArrowPotionEffect;
import me.kingingo.kcore.Kit.Perks.PerkDoubleJump;
import me.kingingo.kcore.Kit.Perks.PerkDoubleXP;
import me.kingingo.kcore.Kit.Perks.PerkDropper;
import me.kingingo.kcore.Kit.Perks.PerkGetXP;
import me.kingingo.kcore.Kit.Perks.PerkGoldenApple;
import me.kingingo.kcore.Kit.Perks.PerkHat;
import me.kingingo.kcore.Kit.Perks.PerkHealPotion;
import me.kingingo.kcore.Kit.Perks.PerkItemName;
import me.kingingo.kcore.Kit.Perks.PerkNoFiredamage;
import me.kingingo.kcore.Kit.Perks.PerkNoHunger;
import me.kingingo.kcore.Kit.Perks.PerkNoWaterdamage;
import me.kingingo.kcore.Kit.Perks.PerkPotionClear;
import me.kingingo.kcore.Kit.Perks.PerkRunner;
import me.kingingo.kcore.Kit.Perks.PerkTNT;
import me.kingingo.kcore.Kit.Perks.PerkWalkEffect;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.Chat.ChatListener;
import me.kingingo.kcore.Listener.Command.ListenerCMD;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Neuling.NeulingManager;
import me.kingingo.kcore.Nick.NickManager;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Permission.GroupTyp;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.SignShop.SignShop;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Update.Updater;
import me.kingingo.kcore.Util.UtilException;
import me.kingingo.kcore.friend.FriendManager;
import me.kingingo.kcore.memory.MemoryFix;
import me.kingingo.kpvp.Command.CommandHologram;
import me.kingingo.kpvp.Command.CommandStats;
import me.kingingo.kpvp.Command.Commandifix;

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
	private PerkManager perkManager;
	@Getter
	private StatsManager statsManager;
	@Getter
	private Hologram hologram;
	@Getter
	@Setter
	private Location hologram_loc;
	@Getter
	private SignShop Shop;
	@Getter
	private PacketManager packetManager;
	@Getter
	private AACHack aACHack;
	
	public void onEnable(){
		try{
		loadConfig();
		this.mysql=new MySQL(getConfig().getString("Config.MySQL.User"),getConfig().getString("Config.MySQL.Password"),getConfig().getString("Config.MySQL.Host"),getConfig().getString("Config.MySQL.DB"),this);
		Language.load(mysql);
		this.instance=this;
		this.updater=new Updater(this);
		this.client = new Client(getConfig().getString("Config.Client.Host"),getConfig().getInt("Config.Client.Port"),"PvP",this,updater);
		this.cmd=new CommandHandler(this);
		new MemoryFix(this);
		this.packetManager=new PacketManager(this,client);
		permManager=new PermissionManager(this,GroupTyp.PVP,packetManager,mysql);
		this.hologram_loc=new Location(Bukkit.getWorld("world"),getConfig().getDouble("Config.Hologram.X"),getConfig().getDouble("Config.Hologram.Y"),getConfig().getDouble("Config.Hologram.Z"));
		this.hologram_loc.getWorld().loadChunk(this.hologram_loc.getWorld().getChunkAt(this.hologram_loc));
		this.hologram=new Hologram(this);
		this.friendManager=new FriendManager(this,mysql,cmd);
		this.neulingManager=new NeulingManager(this,cmd,20);
		this.antiManager=new AntiLogoutManager(this,AntiLogoutType.KILL,18);
		this.aACHack=new AACHack("PVP",getMysql(), getPacketManager());
		getAACHack().setAntiLogoutManager(getAntiManager());
		this.statsManager=new StatsManager(this,mysql,GameType.PVP);
		this.gildenManager=new GildenManager(mysql,GildenType.PVP,cmd,statsManager);
		this.perkManager=new PerkManager(this,null,new Perk[]{new PerkArrowPotionEffect(),new PerkNoWaterdamage(),new PerkGoldenApple(),new PerkHat(),new PerkNoHunger(),new PerkHealPotion(1),new PerkNoFiredamage(),new PerkRunner(0.35F),new PerkDoubleJump(),new PerkDoubleXP(),new PerkDropper(),new PerkGetXP(),new PerkPotionClear(),new PerkItemName(cmd)});
		new PerkListener(perkManager);
		cmd.register(CommandTreasureChest.class, new CommandTreasureChest(this));
		cmd.register(CommandFly.class, new CommandFly(this));
		cmd.register(CommandPerk.class, new CommandPerk(perkManager));
		cmd.register(CommandCMDMute.class, new CommandCMDMute(this));	
		cmd.register(CommandPvPMute.class, new CommandPvPMute(this));	
		cmd.register(CommandChatMute.class, new CommandChatMute(this));
		cmd.register(CommandToggle.class, new CommandToggle(this));
		cmd.register(CommandTrackingRange.class, new CommandTrackingRange());
		cmd.register(CommandHologram.class, new CommandHologram(this));
		cmd.register(CommandStats.class, new CommandStats(getGildenManager(),getStatsManager()));
		cmd.register(CommandkSpawn.class, new CommandkSpawn());
		cmd.register(CommandURang.class, new CommandURang(permManager,mysql));
		cmd.register(CommandUnBan.class, new CommandUnBan(mysql));
		cmd.register(CommandBanned.class, new CommandBanned(mysql));
		cmd.register(CommandXP.class, new CommandXP());
		cmd.register(CommandGiveAll.class, new CommandGiveAll());
		cmd.register(CommandGroup.class, new CommandGroup(permManager));
		cmd.register(Commandifix.class, new Commandifix());
		cmd.register(CommandgBroadcast.class, new CommandgBroadcast(packetManager));
		this.Shop=new SignShop(this,statsManager);
		new kPvPListener(this);
		new ListenerCMD(this);
		new ChatListener(this, gildenManager,permManager);
		}catch(Exception e){
			UtilException.catchException(e, "pvp", Bukkit.getIp(), mysql);
		}
	}
	
	public void onDisable(){
		this.statsManager.SaveAllData();
		this.gildenManager.AllUpdateGilde();
		this.mysql.close();
		this.client.disconnect(false);
		saveConfig();
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
