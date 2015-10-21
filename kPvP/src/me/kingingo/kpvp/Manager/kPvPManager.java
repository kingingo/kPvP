package me.kingingo.kpvp.Manager;

import lombok.Getter;
import me.kingingo.kcore.AntiLogout.AntiLogoutManager;
import me.kingingo.kcore.AntiLogout.AntiLogoutType;
import me.kingingo.kcore.Command.Admin.CommandBanned;
import me.kingingo.kcore.Command.Admin.CommandGroup;
import me.kingingo.kcore.Command.Admin.CommandLocations;
import me.kingingo.kcore.Command.Admin.CommandPermissionTest;
import me.kingingo.kcore.Command.Admin.CommandURang;
import me.kingingo.kcore.Command.Admin.CommandUnBan;
import me.kingingo.kcore.Command.Commands.CommandDelHome;
import me.kingingo.kcore.Command.Commands.CommandExt;
import me.kingingo.kcore.Command.Commands.CommandFill;
import me.kingingo.kcore.Command.Commands.CommandHandel;
import me.kingingo.kcore.Command.Commands.CommandHead;
import me.kingingo.kcore.Command.Commands.CommandHeal;
import me.kingingo.kcore.Command.Commands.CommandHome;
import me.kingingo.kcore.Command.Commands.CommandMoney;
import me.kingingo.kcore.Command.Commands.CommandRenameItem;
import me.kingingo.kcore.Command.Commands.CommandSetHome;
import me.kingingo.kcore.Command.Commands.CommandSonne;
import me.kingingo.kcore.Command.Commands.CommandSpawner;
import me.kingingo.kcore.Command.Commands.CommandSpawnmob;
import me.kingingo.kcore.Command.Commands.CommandWorkbench;
import me.kingingo.kcore.Command.Commands.CommandXP;
import me.kingingo.kcore.Command.Commands.CommandkSpawn;
import me.kingingo.kcore.DeliveryPet.DeliveryObject;
import me.kingingo.kcore.DeliveryPet.DeliveryPet;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Enum.ServerType;
import me.kingingo.kcore.GemsShop.GemsShop;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Inventory.InventoryBase;
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
import me.kingingo.kcore.Listener.EnderChest.EnderChestListener;
import me.kingingo.kcore.Listener.Enderpearl.EnderpearlListener;
import me.kingingo.kcore.Neuling.NeulingManager;
import me.kingingo.kcore.Packet.Packets.TWIITTER_IS_PLAYER_FOLLOWER;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.Pet.Commands.CommandPet;
import me.kingingo.kcore.Pet.Shop.PlayerPetHandler;
import me.kingingo.kcore.SignShop.SignShop;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.friend.FriendManager;
import me.kingingo.kpvp.kPvP;
import me.kingingo.kpvp.Command.CommandStats;
import me.kingingo.kpvp.Command.Commandifix;
import me.kingingo.kpvp.Listener.PerkListener;
import me.kingingo.kpvp.Listener.kPvPListener;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class kPvPManager extends IPvPManager{

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
	private SignShop Shop;
	@Getter
	private StatsManager statsManager;
	@Getter
	private GemsShop gems;
	@Getter
	private PetManager petManager;
	@Getter
	private InventoryBase base;
	@Getter
	private PlayerPetHandler petHandler;
	
	public kPvPManager(kPvP PvP){
		super(PvP);
		this.base=new InventoryBase(PvP);
		this.petManager=new PetManager(PvP);
		this.statsManager=new StatsManager(getPvP(),getPvP().getMysql(),GameType.PVP);
		this.gildenManager=new GildenManager(getPvP().getMysql(),GildenType.PVP,getPvP().getCmd(),getStatsManager());
		this.perkManager=new PerkManager(getPvP(),null,new Perk[]{new PerkArrowPotionEffect(),new PerkNoWaterdamage(),new PerkGoldenApple(),new PerkHat(),new PerkNoHunger(),new PerkHealPotion(1),new PerkNoFiredamage(),new PerkRunner(0.35F),new PerkDoubleJump(),new PerkDoubleXP(),new PerkDropper(),new PerkGetXP(),new PerkPotionClear(),new PerkItemName(getPvP().getCmd())});
		new PerkListener(perkManager);
		this.friendManager=new FriendManager(getPvP(),getPvP().getMysql(),getPvP().getCmd());
		this.neulingManager=new NeulingManager(getPvP(),getPvP().getCmd(),20);
		this.antiManager=new AntiLogoutManager(getPvP(),AntiLogoutType.KILL,40);
		this.Shop=new SignShop(getPvP(),getPvP().getCmd(),getStatsManager());
		this.gems=new GemsShop(PvP.getHologram(),PvP.getCmd(), getBase(),PvP.getPermManager(), ServerType.PVP);
		this.petHandler = new PlayerPetHandler(ServerType.PVP, getPetManager(), getBase(), PvP.getPermManager());
		
		if(getPvP().getAACHack()!=null){
			getPvP().getAACHack().setAntiLogoutManager(getAntiManager());
		}

		getPvP().getCmd().register(CommandFill.class, new CommandFill());
		getPvP().getCmd().register(CommandPet.class, new CommandPet(petHandler));
		getPvP().getCmd().register(CommandHandel.class, new CommandHandel(getPvP()));
		getPvP().getCmd().register(CommandPerk.class, new CommandPerk(perkManager,getBase()));
		getPvP().getCmd().register(CommandLocations.class, new CommandLocations(PvP));
		getPvP().getCmd().register(CommandStats.class, new CommandStats(getGildenManager(),getStatsManager()));
		getPvP().getCmd().register(CommandURang.class, new CommandURang(getPvP().getPermManager(),getPvP().getMysql()));
		getPvP().getCmd().register(CommandUnBan.class, new CommandUnBan(getPvP().getMysql()));
		getPvP().getCmd().register(CommandBanned.class, new CommandBanned(getPvP().getMysql()));
		getPvP().getCmd().register(CommandXP.class, new CommandXP());
		getPvP().getCmd().register(CommandGroup.class, new CommandGroup(getPvP().getPermManager()));
		getPvP().getCmd().register(Commandifix.class, new Commandifix());
		getPvP().getCmd().register(CommandPermissionTest.class, new CommandPermissionTest(getPvP().getPermManager()));
		getPvP().getCmd().register(CommandMoney.class, new CommandMoney(getStatsManager(),ServerType.PVP));
		getPvP().getCmd().register(CommandJump.class, new CommandJump(getPvP()));
		getPvP().getCmd().register(CommandHeal.class, new CommandHeal());
		getPvP().getCmd().register(CommandHome.class, new CommandHome(getPvP().getUserData(), getTeleport(),getPvP().getCmd()));
		getPvP().getCmd().register(CommandSpawnmob.class, new CommandSpawnmob());
		getPvP().getCmd().register(CommandSpawner.class, new CommandSpawner());
		getPvP().getCmd().register(CommandSetHome.class, new CommandSetHome(getPvP().getUserData(), getPvP().getPermManager()));
		getPvP().getCmd().register(CommandSonne.class, new CommandSonne());
		getPvP().getCmd().register(CommandDelHome.class, new CommandDelHome(getPvP().getUserData()));
		getPvP().getCmd().register(CommandRenameItem.class, new CommandRenameItem());
		getPvP().getCmd().register(CommandkSpawn.class, new CommandkSpawn());
		getPvP().getCmd().register(CommandExt.class, new CommandExt());
		getPvP().getCmd().register(CommandHead.class, new CommandHead());
		getPvP().getCmd().register(CommandWorkbench.class, new CommandWorkbench());
		
		UtilServer.createDeliveryPet(new DeliveryPet(getBase(),null,new DeliveryObject[]{
			new DeliveryObject(new String[]{"","§7Click for Vote!","","§ePvP Rewards:","§7   200 Epics","§7   1x Inventory Repair","","§eGame Rewards:","§7   25 Gems","§7   100 Coins","","§eSkyBlock Rewards:","§7   200 Epics","§7   2x Diamonds","§7   2x Iron Ingot","§7   2x Gold Ingot"},null,false,10,"§aVote for EpicPvP",Material.PAPER,new Click(){

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
						String s1 = getPvP().getMysql().getString("SELECT twitter FROM BG_TWITTER WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
						if(s1.equalsIgnoreCase("null")){
							p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_ACC_NOT"));
						}else{
							getPvP().getPacketManager().SendPacket("DATA", new TWIITTER_IS_PLAYER_FOLLOWER(s1, p.getName()));
							p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_CHECK"));
						}
					}
					
				},TimeSpan.DAY*7),
		},"§bThe Delivery Jockey!",EntityType.CHICKEN,CommandLocations.getLocation("DeliveryPet"),ServerType.PVP,getPvP().getHologram(),getPvP().getMysql())
		);
		

		new EnderChestListener(getPvP().getUserData());
		new kPvPListener(this);
		new ChatListener(getPvP(), gildenManager,getPvP().getPermManager());
		new EnderpearlListener(getPvP());
	}
	
	public void onDisable(){
		getGems().onDisable();
		if(UtilServer.getDeliveryPet()!=null){
			UtilServer.getDeliveryPet().onDisable();
		}
		getStatsManager().SaveAllData();
		this.gildenManager.AllUpdateGilde();
	}
}
