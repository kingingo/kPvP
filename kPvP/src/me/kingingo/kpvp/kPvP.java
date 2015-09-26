package me.kingingo.kpvp;

import lombok.Getter;
import me.kingingo.kcore.AACHack.AACHack;
import me.kingingo.kcore.AntiLogout.AntiLogoutManager;
import me.kingingo.kcore.AntiLogout.AntiLogoutType;
import me.kingingo.kcore.Client.Client;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Command.Admin.CommandBanned;
import me.kingingo.kcore.Command.Admin.CommandBroadcast;
import me.kingingo.kcore.Command.Admin.CommandCMDMute;
import me.kingingo.kcore.Command.Admin.CommandChatMute;
import me.kingingo.kcore.Command.Admin.CommandFly;
import me.kingingo.kcore.Command.Admin.CommandFlyspeed;
import me.kingingo.kcore.Command.Admin.CommandGive;
import me.kingingo.kcore.Command.Admin.CommandGiveAll;
import me.kingingo.kcore.Command.Admin.CommandGroup;
import me.kingingo.kcore.Command.Admin.CommandInvsee;
import me.kingingo.kcore.Command.Admin.CommandItem;
import me.kingingo.kcore.Command.Admin.CommandMore;
import me.kingingo.kcore.Command.Admin.CommandPermissionTest;
import me.kingingo.kcore.Command.Admin.CommandPvPMute;
import me.kingingo.kcore.Command.Admin.CommandSocialspy;
import me.kingingo.kcore.Command.Admin.CommandToggle;
import me.kingingo.kcore.Command.Admin.CommandTp;
import me.kingingo.kcore.Command.Admin.CommandTpHere;
import me.kingingo.kcore.Command.Admin.CommandTppos;
import me.kingingo.kcore.Command.Admin.CommandTrackingRange;
import me.kingingo.kcore.Command.Admin.CommandURang;
import me.kingingo.kcore.Command.Admin.CommandUnBan;
import me.kingingo.kcore.Command.Admin.CommandVanish;
import me.kingingo.kcore.Command.Admin.CommandgBroadcast;
import me.kingingo.kcore.Command.Commands.CommandBack;
import me.kingingo.kcore.Command.Commands.CommandClearInventory;
import me.kingingo.kcore.Command.Commands.CommandDelHome;
import me.kingingo.kcore.Command.Commands.CommandEnderchest;
import me.kingingo.kcore.Command.Commands.CommandExt;
import me.kingingo.kcore.Command.Commands.CommandFeed;
import me.kingingo.kcore.Command.Commands.CommandHead;
import me.kingingo.kcore.Command.Commands.CommandHeal;
import me.kingingo.kcore.Command.Commands.CommandHome;
import me.kingingo.kcore.Command.Commands.CommandKit;
import me.kingingo.kcore.Command.Commands.CommandMoney;
import me.kingingo.kcore.Command.Commands.CommandMsg;
import me.kingingo.kcore.Command.Commands.CommandNacht;
import me.kingingo.kcore.Command.Commands.CommandR;
import me.kingingo.kcore.Command.Commands.CommandRenameItem;
import me.kingingo.kcore.Command.Commands.CommandRepair;
import me.kingingo.kcore.Command.Commands.CommandSetHome;
import me.kingingo.kcore.Command.Commands.CommandSonne;
import me.kingingo.kcore.Command.Commands.CommandSpawn;
import me.kingingo.kcore.Command.Commands.CommandSpawner;
import me.kingingo.kcore.Command.Commands.CommandSpawnmob;
import me.kingingo.kcore.Command.Commands.CommandTag;
import me.kingingo.kcore.Command.Commands.CommandTreasureChest;
import me.kingingo.kcore.Command.Commands.CommandWarp;
import me.kingingo.kcore.Command.Commands.CommandWorkbench;
import me.kingingo.kcore.Command.Commands.CommandXP;
import me.kingingo.kcore.Command.Commands.CommandkSpawn;
import me.kingingo.kcore.DeliveryPet.DeliveryObject;
import me.kingingo.kcore.DeliveryPet.DeliveryPet;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Enum.ServerType;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.JumpPad.CommandJump;
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
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.Chat.ChatListener;
import me.kingingo.kcore.Listener.Command.ListenerCMD;
import me.kingingo.kcore.Listener.EnderChest.EnderChestListener;
import me.kingingo.kcore.Listener.Enderpearl.EnderpearlListener;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Neuling.NeulingManager;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Packets.TWIITTER_IS_PLAYER_FOLLOWER;
import me.kingingo.kcore.Permission.GroupTyp;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.SignShop.SignShop;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.TeleportManager.TeleportManager;
import me.kingingo.kcore.Update.Updater;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilException;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.friend.FriendManager;
import me.kingingo.kcore.memory.MemoryFix;
import me.kingingo.kpvp.Command.CommandHologram;
import me.kingingo.kpvp.Command.CommandStats;
import me.kingingo.kpvp.Command.Commandifix;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
	private SignShop Shop;
	@Getter
	private PacketManager packetManager;
	@Getter
	private AACHack aACHack;
	@Getter
	private UserDataConfig userData;
	@Getter
	private TeleportManager teleport;
	
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
		this.hologram=new Hologram(this);
		this.friendManager=new FriendManager(this,mysql,cmd);
		this.neulingManager=new NeulingManager(this,cmd,20);
		this.antiManager=new AntiLogoutManager(this,AntiLogoutType.KILL,40);
		this.aACHack=new AACHack("PVP",getMysql(), getPacketManager());
		getAACHack().setAntiLogoutManager(getAntiManager());
		this.userData=new UserDataConfig(this);
		this.teleport=new TeleportManager(getCmd(), getPermManager(), 5);
		this.statsManager=new StatsManager(this,mysql,GameType.PVP);
		this.gildenManager=new GildenManager(mysql,GildenType.PVP,cmd,statsManager);
		this.perkManager=new PerkManager(this,null,new Perk[]{new PerkArrowPotionEffect(),new PerkNoWaterdamage(),new PerkGoldenApple(),new PerkHat(),new PerkNoHunger(),new PerkHealPotion(1),new PerkNoFiredamage(),new PerkRunner(0.35F),new PerkDoubleJump(),new PerkDoubleXP(),new PerkDropper(),new PerkGetXP(),new PerkPotionClear(),new PerkItemName(cmd)});
		new PerkListener(perkManager);
		
		this.cmd.register(CommandTreasureChest.class, new CommandTreasureChest(this));
		this.cmd.register(CommandFly.class, new CommandFly(this));
		this.cmd.register(CommandPerk.class, new CommandPerk(perkManager));
		this.cmd.register(CommandCMDMute.class, new CommandCMDMute(this));	
		this.cmd.register(CommandPvPMute.class, new CommandPvPMute(this));	
		this.cmd.register(CommandChatMute.class, new CommandChatMute(this));
		this.cmd.register(CommandToggle.class, new CommandToggle(this));
		this.cmd.register(CommandTrackingRange.class, new CommandTrackingRange());
		this.cmd.register(CommandHologram.class, new CommandHologram(this));
		this.cmd.register(CommandStats.class, new CommandStats(getGildenManager(),getStatsManager()));
		this.cmd.register(CommandURang.class, new CommandURang(permManager,mysql));
		this.cmd.register(CommandUnBan.class, new CommandUnBan(mysql));
		this.cmd.register(CommandBanned.class, new CommandBanned(mysql));
//		this.cmd.register(CommandXP.class, new CommandXP());
		this.cmd.register(CommandGiveAll.class, new CommandGiveAll());
		this.cmd.register(CommandGroup.class, new CommandGroup(permManager));
		this.cmd.register(Commandifix.class, new Commandifix());
		this.cmd.register(CommandgBroadcast.class, new CommandgBroadcast(packetManager));
		this.cmd.register(CommandPermissionTest.class, new CommandPermissionTest(getPermManager()));
		this.cmd.register(CommandMoney.class, new CommandMoney(getStatsManager(),ServerType.PVP));
		this.cmd.register(CommandMsg.class, new CommandMsg());
		this.cmd.register(CommandR.class, new CommandR(this));
		this.cmd.register(CommandSocialspy.class, new CommandSocialspy(this));
		this.cmd.register(CommandJump.class, new CommandJump(this));
		this.cmd.register(CommandFeed.class, new CommandFeed());
		this.cmd.register(CommandRepair.class, new CommandRepair());
		this.cmd.register(CommandTag.class, new CommandTag());
		this.cmd.register(CommandNacht.class, new CommandNacht());
		this.cmd.register(CommandHeal.class, new CommandHeal());
		this.cmd.register(CommandHome.class, new CommandHome(getUserData(), teleport,this.cmd));
		this.cmd.register(CommandSpawnmob.class, new CommandSpawnmob());
		this.cmd.register(CommandSpawner.class, new CommandSpawner());
		this.cmd.register(CommandSetHome.class, new CommandSetHome(getUserData(), getPermManager()));
		this.cmd.register(CommandSonne.class, new CommandSonne());
		this.cmd.register(CommandDelHome.class, new CommandDelHome(getUserData()));
		this.cmd.register(CommandWarp.class, new CommandWarp(getTeleport()));
		this.cmd.register(CommandKit.class, new CommandKit(getUserData(),cmd));
		this.cmd.register(CommandSpawn.class, new CommandSpawn(getTeleport()));
		this.cmd.register(CommandClearInventory.class, new CommandClearInventory());
		this.cmd.register(CommandRenameItem.class, new CommandRenameItem());
		this.cmd.register(CommandInvsee.class, new CommandInvsee(mysql));
		this.cmd.register(CommandEnderchest.class, new CommandEnderchest(mysql));
		this.cmd.register(CommandBroadcast.class, new CommandBroadcast());
		this.cmd.register(CommandTppos.class, new CommandTppos());
		this.cmd.register(CommandItem.class, new CommandItem());
		this.cmd.register(CommandTp.class, new CommandTp());
		this.cmd.register(CommandTpHere.class, new CommandTpHere());
		this.cmd.register(CommandVanish.class, new CommandVanish(this));
		this.cmd.register(CommandkSpawn.class, new CommandkSpawn());
		this.cmd.register(CommandMore.class, new CommandMore());
		this.cmd.register(CommandFlyspeed.class, new CommandFlyspeed());
		this.cmd.register(CommandBack.class, new CommandBack(this));
		this.cmd.register(CommandGive.class, new CommandGive());
		this.cmd.register(CommandExt.class, new CommandExt());
		this.cmd.register(CommandHead.class, new CommandHead());
		this.cmd.register(CommandWorkbench.class, new CommandWorkbench());
		
		UtilServer.createDeliveryPet(new DeliveryPet(null,new DeliveryObject[]{
				new DeliveryObject(new String[]{"","§7Click for Vote!","","§ePvP Rewards:","§7   200 Epics","§7   1x Inventory Repair","","§eGame Rewards:","§7   150 Coins","","§eSkyBlock Rewards:","§7   200 Epics","§7   2x Diamonds","§7   2x Iron Ingot","§7   2x Gold Ingot"},null,false,10,"§aVote for EpicPvP",Material.PAPER,new Click(){

					@Override
					public void onClick(Player p, ActionType a,Object obj) {
						p.closeInventory();
						p.sendMessage(Language.getText(p,"PREFIX")+"§7-----------------------------------------");
						p.sendMessage(Language.getText(p,"PREFIX")+" ");
						p.sendMessage(Language.getText(p,"PREFIX")+"Vote Link:§a http://goo.gl/wxdAj4");
						p.sendMessage(Language.getText(p,"PREFIX")+" ");
						p.sendMessage(Language.getText(p,"PREFIX")+"§7-----------------------------------------");
					}
					
				},-1),
				new DeliveryObject(new String[]{"§aOnly for Premium Players!","","§ePvP Rewards:","§7   200 Epics","§7   10 Level","","§eGame Rewards:","§7   200 Coins","","§eSkyBlock Rewards:","§7   200 Epics","§7   2x Diamonds","§7   2x Iron Ingot","§7   2x Gold Ingot"},kPermission.RANK_COINS_DAILY,true,12,"§cRank Day Reward",Material.EMERALD,new Click(){

					@Override
					public void onClick(Player p, ActionType a,Object obj) {
						getStatsManager().addDouble(p, 200, Stats.MONEY);
						p.setLevel(p.getLevel()+10);
						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "MONEY_RECEIVE_FROM", new String[]{"§bThe Delivery Jockey!","200"}));
					}
					
				},TimeSpan.DAY),
				new DeliveryObject(new String[]{"§aOnly for Premium Players!","","§ePvP Rewards:","§7   5000 Epics","§7   5x Golden Apple","","§eGame Rewards:","§7   5000 Coins","§7   5x TTT Paesse","","§eSkyBlock Rewards:","§7   5000 Epics","§7   15x Diamonds","§7   15x Iron Ingot","§7   15x Gold Ingot"},kPermission.RANK_COINS_MONTH,true,14,"§cRank Month Reward",Material.EMERALD_BLOCK,new Click(){

					@Override
					public void onClick(Player p, ActionType a,Object obj) {
						getStatsManager().addDouble(p, 5000, Stats.MONEY);
						p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE,5,(byte)1));
						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "MONEY_RECEIVE_FROM", new String[]{"§bThe Delivery Jockey!","5000"}));
					}
					
				},TimeSpan.DAY*30),
				new DeliveryObject(new String[]{"§7/twitter [TwitterName]","","§ePvP Rewards:","§7   300 Epics","§7   15 Level","","§eGame Rewards:","§7   300 Coins","","§eSkyBlock Rewards:","§7   300 Epics","§7   15 Level"},null,false,16,"§cTwitter Reward",Material.getMaterial(351),4,new Click(){

					@Override
					public void onClick(Player p, ActionType a,Object obj) {
						String s1 = getMysql().getString("SELECT twitter FROM BG_TWITTER WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
						if(s1.equalsIgnoreCase("null")){
							p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_ACC_NOT"));
						}else{
							getPacketManager().SendPacket("DATA", new TWIITTER_IS_PLAYER_FOLLOWER(s1, p.getName()));
							p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_CHECK"));
						}
					}
					
				},TimeSpan.DAY*7),
		},"§bThe Delivery Jockey!",EntityType.CHICKEN,CommandHologram.getDelivery(),ServerType.PVP,getHologram(),getMysql())
		);
		
		new EnderChestListener(this.userData);
		this.Shop=new SignShop(this,this.cmd,this.statsManager);
		new kPvPListener(this);
		new ListenerCMD(this);
		new EnderpearlListener(this);
		new ChatListener(this, gildenManager,permManager);
		UtilServer.createLagListener(cmd);
		}catch(Exception e){
			UtilException.catchException(e, "pvp", Bukkit.getIp(), mysql);
		}
	}
	
	public void onDisable(){
		this.statsManager.SaveAllData();
		this.gildenManager.AllUpdateGilde();
		this.mysql.close();
		this.client.disconnect(false);
		updater.stop();
		if(UtilServer.getDeliveryPet()!=null){
			UtilServer.getDeliveryPet().onDisable();
		}
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
