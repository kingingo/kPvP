package eu.epicpvp.kpvp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.datenclient.client.Callback;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.ServerType;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.AntiLogout.AntiLogoutManager;
import eu.epicpvp.kcore.AntiLogout.AntiLogoutType;
import eu.epicpvp.kcore.AuktionsMarkt.AuktionsMarkt;
import eu.epicpvp.kcore.Command.Admin.CommandAddEpics;
import eu.epicpvp.kcore.Command.Admin.CommandBanned;
import eu.epicpvp.kcore.Command.Admin.CommandBroadcast;
import eu.epicpvp.kcore.Command.Admin.CommandCMDMute;
import eu.epicpvp.kcore.Command.Admin.CommandChatMute;
import eu.epicpvp.kcore.Command.Admin.CommandDebug;
import eu.epicpvp.kcore.Command.Admin.CommandFly;
import eu.epicpvp.kcore.Command.Admin.CommandFlyToggle;
import eu.epicpvp.kcore.Command.Admin.CommandFlyspeed;
import eu.epicpvp.kcore.Command.Admin.CommandGive;
import eu.epicpvp.kcore.Command.Admin.CommandGiveAll;
import eu.epicpvp.kcore.Command.Admin.CommandGiveGems;
import eu.epicpvp.kcore.Command.Admin.CommandHomeCheck;
import eu.epicpvp.kcore.Command.Admin.CommandItem;
import eu.epicpvp.kcore.Command.Admin.CommandK;
import eu.epicpvp.kcore.Command.Admin.CommandLocations;
import eu.epicpvp.kcore.Command.Admin.CommandMore;
import eu.epicpvp.kcore.Command.Admin.CommandPacketToggle;
import eu.epicpvp.kcore.Command.Admin.CommandPvPMute;
import eu.epicpvp.kcore.Command.Admin.CommandSocialspy;
import eu.epicpvp.kcore.Command.Admin.CommandToggle;
import eu.epicpvp.kcore.Command.Admin.CommandTp;
import eu.epicpvp.kcore.Command.Admin.CommandTpHere;
import eu.epicpvp.kcore.Command.Admin.CommandTppos;
import eu.epicpvp.kcore.Command.Admin.CommandTrackingRange;
import eu.epicpvp.kcore.Command.Admin.CommandURang;
import eu.epicpvp.kcore.Command.Admin.CommandVanish;
import eu.epicpvp.kcore.Command.Admin.CommandgBroadcast;
import eu.epicpvp.kcore.Command.Commands.CommandBack;
import eu.epicpvp.kcore.Command.Commands.CommandClearInventory;
import eu.epicpvp.kcore.Command.Commands.CommandDelHome;
import eu.epicpvp.kcore.Command.Commands.CommandEnchantmentTable;
import eu.epicpvp.kcore.Command.Commands.CommandEnderchest;
import eu.epicpvp.kcore.Command.Commands.CommandEpic;
import eu.epicpvp.kcore.Command.Commands.CommandExt;
import eu.epicpvp.kcore.Command.Commands.CommandFeed;
import eu.epicpvp.kcore.Command.Commands.CommandFill;
import eu.epicpvp.kcore.Command.Commands.CommandHandel;
import eu.epicpvp.kcore.Command.Commands.CommandHead;
import eu.epicpvp.kcore.Command.Commands.CommandHeal;
import eu.epicpvp.kcore.Command.Commands.CommandHome;
import eu.epicpvp.kcore.Command.Commands.CommandInvsee;
import eu.epicpvp.kcore.Command.Commands.CommandKit;
import eu.epicpvp.kcore.Command.Commands.CommandMoney;
import eu.epicpvp.kcore.Command.Commands.CommandMsg;
import eu.epicpvp.kcore.Command.Commands.CommandNacht;
import eu.epicpvp.kcore.Command.Commands.CommandNear;
import eu.epicpvp.kcore.Command.Commands.CommandPotion;
import eu.epicpvp.kcore.Command.Commands.CommandR;
import eu.epicpvp.kcore.Command.Commands.CommandRemoveEnchantment;
import eu.epicpvp.kcore.Command.Commands.CommandRenameItem;
import eu.epicpvp.kcore.Command.Commands.CommandRepair;
import eu.epicpvp.kcore.Command.Commands.CommandSetHome;
import eu.epicpvp.kcore.Command.Commands.CommandSonne;
import eu.epicpvp.kcore.Command.Commands.CommandSpawn;
import eu.epicpvp.kcore.Command.Commands.CommandSpawner;
import eu.epicpvp.kcore.Command.Commands.CommandSpawnmob;
import eu.epicpvp.kcore.Command.Commands.CommandStats;
import eu.epicpvp.kcore.Command.Commands.CommandSuffix;
import eu.epicpvp.kcore.Command.Commands.CommandTag;
import eu.epicpvp.kcore.Command.Commands.CommandWarp;
import eu.epicpvp.kcore.Command.Commands.CommandWorkbench;
import eu.epicpvp.kcore.Command.Commands.CommandkSpawn;
import eu.epicpvp.kcore.DeliveryPet.DeliveryObject;
import eu.epicpvp.kcore.DeliveryPet.DeliveryPet;
import eu.epicpvp.kcore.GemsShop.GemsShop;
import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Gilden.GildenType;
import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Hologram.nametags.NameTagType;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryBuy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.ItemShop.ItemShop;
import eu.epicpvp.kcore.JumpPad.CommandJump;
import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.PerkManager;
import eu.epicpvp.kcore.Kit.Command.CommandPerk;
import eu.epicpvp.kcore.Kit.Perks.PerkArrowPotionEffect;
import eu.epicpvp.kcore.Kit.Perks.PerkDoubleJump;
import eu.epicpvp.kcore.Kit.Perks.PerkDoubleXP;
import eu.epicpvp.kcore.Kit.Perks.PerkDropper;
import eu.epicpvp.kcore.Kit.Perks.PerkGetXP;
import eu.epicpvp.kcore.Kit.Perks.PerkGoldenApple;
import eu.epicpvp.kcore.Kit.Perks.PerkHat;
import eu.epicpvp.kcore.Kit.Perks.PerkHealPotion;
import eu.epicpvp.kcore.Kit.Perks.PerkItemName;
import eu.epicpvp.kcore.Kit.Perks.PerkNoFiredamage;
import eu.epicpvp.kcore.Kit.Perks.PerkNoHunger;
import eu.epicpvp.kcore.Kit.Perks.PerkNoPotion;
import eu.epicpvp.kcore.Kit.Perks.PerkNoWaterdamage;
import eu.epicpvp.kcore.Kit.Perks.PerkPotionClear;
import eu.epicpvp.kcore.Kit.Perks.PerkRunner;
import eu.epicpvp.kcore.Kit.Perks.PerkStrength;
import eu.epicpvp.kcore.Listener.Chat.ChatListener;
import eu.epicpvp.kcore.Listener.EnderChest.EnderChestListener;
import eu.epicpvp.kcore.Listener.Enderpearl.EnderpearlListener;
import eu.epicpvp.kcore.Listener.EntityClick.EntityClickListener;
import eu.epicpvp.kcore.Listener.FlyListener.FlyListener;
import eu.epicpvp.kcore.Listener.VoteListener.VoteListener;
import eu.epicpvp.kcore.Neuling.NeulingManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Pet.PetManager;
import eu.epicpvp.kcore.Pet.Commands.CommandPet;
import eu.epicpvp.kcore.Pet.Shop.PlayerPetHandler;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilEnt;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilLocation;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import eu.epicpvp.kpvp.Command.Commandifix;
import eu.epicpvp.kpvp.Listener.PerkListener;
import eu.epicpvp.kpvp.Listener.kPvPListener;
import lombok.Getter;

public class kPvPManager {

	@Getter
	private GildenManager gildenManager;
	@Getter
	private NeulingManager neulingManager;
	@Getter
	private AntiLogoutManager antiManager;
	@Getter
	private PerkManager perkManager;
	@Getter
	private StatsManager statsManager;
	@Getter
	private StatsManager money;
	@Getter
	private PetManager petManager;
	@Getter
	private PlayerPetHandler petHandler;
	@Getter
	private kPvP PvP;
	@Getter
	private TeleportManager teleport;

	public kPvPManager(kPvP PvP) {
		this.PvP = PvP;
		this.teleport = new TeleportManager(getPvP().getCmd(), getPvP().getPermissionManager());
		this.petManager = new PetManager(PvP);
		this.money = StatsManagerRepository.createStatsManager(GameType.Money);
		this.statsManager = StatsManagerRepository.createStatsManager(GameType.PVP);
		this.gildenManager = new GildenManager(getPvP().getMysql(), GildenType.PVP, getPvP().getCmd(), getStatsManager());
		this.gildenManager.setAsync(true);
		this.perkManager = new PerkManager(getPvP(), new Perk[]
		{ new PerkStrength(), new PerkNoPotion(PotionEffectType.POISON), new PerkArrowPotionEffect(), new PerkNoWaterdamage(), new PerkGoldenApple(), new PerkHat(), new PerkNoHunger(), new PerkHealPotion(1), new PerkNoFiredamage(), new PerkRunner(), new PerkDoubleJump(), new PerkDoubleXP(), new PerkDropper(), new PerkGetXP(), new PerkPotionClear(), new PerkItemName(getPvP().getCmd()) });
		new PerkListener(perkManager);
		this.neulingManager = new NeulingManager(getPvP(), getPvP().getCmd(), 20);
		this.antiManager = new AntiLogoutManager(getPvP(), AntiLogoutType.DROP_AMOR, 40);
		UtilServer.getGemsShop(new GemsShop(ServerType.PVP));
		this.petHandler = new PlayerPetHandler(ServerType.PVP, PvP.getMysql(), getPetManager(), PvP.getPermissionManager());
		this.petHandler.setAsync(true);
		new ItemShop(statsManager, getPvP().getCmd());

		if (getPvP().getAACHack() != null) {
			getPvP().getAACHack().setAntiLogoutManager(getAntiManager());
		}

		getPvP().getCmd().register(CommandDebug.class, new CommandDebug());
		getPvP().getCmd().register(CommandR.class, new CommandR(getPvP()));
		getPvP().getCmd().register(CommandSocialspy.class, new CommandSocialspy(getPvP()));
		getPvP().getCmd().register(CommandCMDMute.class, new CommandCMDMute(getPvP()));
		getPvP().getCmd().register(CommandPvPMute.class, new CommandPvPMute(getPvP()));
		getPvP().getCmd().register(CommandChatMute.class, new CommandChatMute(getPvP()));
		getPvP().getCmd().register(CommandToggle.class, new CommandToggle(getPvP()));
		getPvP().getCmd().register(CommandTrackingRange.class, new CommandTrackingRange());
		getPvP().getCmd().register(CommandGiveAll.class, new CommandGiveAll());
		getPvP().getCmd().register(CommandgBroadcast.class, new CommandgBroadcast(getPvP().getClient()));
		getPvP().getCmd().register(CommandMsg.class, new CommandMsg());
		getPvP().getCmd().register(CommandFeed.class, new CommandFeed());
		getPvP().getCmd().register(CommandRepair.class, new CommandRepair());
		getPvP().getCmd().register(CommandK.class, new CommandK());
		getPvP().getCmd().register(CommandTag.class, new CommandTag());
		getPvP().getCmd().register(CommandNacht.class, new CommandNacht());
		getPvP().getCmd().register(CommandWarp.class, new CommandWarp(getTeleport()));
		getPvP().getCmd().register(CommandKit.class, new CommandKit(getPvP().getUserData(), getPvP().getCmd()));
		getPvP().getCmd().register(CommandSpawn.class, new CommandSpawn(getTeleport()));
		getPvP().getCmd().register(CommandClearInventory.class, new CommandClearInventory());
		getPvP().getCmd().register(CommandInvsee.class, new CommandInvsee(getPvP().getMysql()));
		getPvP().getCmd().register(CommandEnderchest.class, new CommandEnderchest(getPvP().getMysql()));
		getPvP().getCmd().register(CommandBroadcast.class, new CommandBroadcast());
		getPvP().getCmd().register(CommandTppos.class, new CommandTppos());
		getPvP().getCmd().register(CommandItem.class, new CommandItem());
		getPvP().getCmd().register(CommandTp.class, new CommandTp());
		getPvP().getCmd().register(CommandTpHere.class, new CommandTpHere());
		getPvP().getCmd().register(CommandVanish.class, new CommandVanish(getPvP()));
		getPvP().getCmd().register(CommandMore.class, new CommandMore());
		getPvP().getCmd().register(CommandFlyspeed.class, new CommandFlyspeed());
		getPvP().getCmd().register(CommandFly.class, new CommandFly(getPvP()));
		getPvP().getCmd().register(CommandFlyToggle.class, new CommandFlyToggle());
		getPvP().getCmd().register(CommandBack.class, new CommandBack(getPvP()));
		getPvP().getCmd().register(CommandGive.class, new CommandGive());
		getPvP().getCmd().register(CommandFill.class, new CommandFill());
		getPvP().getCmd().register(CommandPet.class, new CommandPet(petHandler));
		getPvP().getCmd().register(CommandHandel.class, new CommandHandel(getPvP()));
		getPvP().getCmd().register(CommandPerk.class, new CommandPerk(perkManager));
		getPvP().getCmd().register(CommandLocations.class, new CommandLocations(PvP));
		getPvP().getCmd().register(CommandStats.class, new CommandStats(getStatsManager()));
		getPvP().getCmd().register(CommandURang.class, new CommandURang(getPvP().getPermissionManager()));
		getPvP().getCmd().register(CommandBanned.class, new CommandBanned(getPvP().getMysql()));
		getPvP().getCmd().register(Commandifix.class, new Commandifix());
		getPvP().getCmd().register(CommandMoney.class, new CommandMoney(getStatsManager(), PvP.getMysql(), ServerType.PVP));
		getPvP().getCmd().register(CommandJump.class, new CommandJump(getPvP()));
		getPvP().getCmd().register(CommandHeal.class, new CommandHeal());
		getPvP().getCmd().register(CommandHome.class, new CommandHome(getPvP().getUserData(), getTeleport(), getPvP().getCmd()));
		getPvP().getCmd().register(CommandSpawnmob.class, new CommandSpawnmob());
		getPvP().getCmd().register(CommandSpawner.class, new CommandSpawner());
		getPvP().getCmd().register(CommandSetHome.class, new CommandSetHome(getPvP().getUserData(), getPvP().getPermissionManager()));
		getPvP().getCmd().register(CommandSonne.class, new CommandSonne());
		getPvP().getCmd().register(CommandDelHome.class, new CommandDelHome(getPvP().getUserData()));
		getPvP().getCmd().register(CommandRenameItem.class, new CommandRenameItem());
		getPvP().getCmd().register(CommandkSpawn.class, new CommandkSpawn(getAntiManager()));
		getPvP().getCmd().register(CommandExt.class, new CommandExt());
		getPvP().getCmd().register(CommandHead.class, new CommandHead());
		getPvP().getCmd().register(CommandWorkbench.class, new CommandWorkbench());
		getPvP().getCmd().register(CommandSuffix.class, new CommandSuffix(getPvP().getUserData()));
		getPvP().getCmd().register(CommandNear.class, new CommandNear());
		getPvP().getCmd().register(CommandRemoveEnchantment.class, new CommandRemoveEnchantment());
		getPvP().getCmd().register(CommandEnchantmentTable.class, new CommandEnchantmentTable());
		getPvP().getCmd().register(CommandPotion.class, new CommandPotion(getPvP().getPermissionManager()));
		getPvP().getCmd().register(CommandPacketToggle.class, new CommandPacketToggle(getPvP().getInstance()));
		getPvP().getCmd().register(CommandAddEpics.class, new CommandAddEpics(getStatsManager()));
		getPvP().getCmd().register(CommandGiveGems.class, new CommandGiveGems(UtilServer.getGemsShop().getGems()));
		getPvP().getCmd().register(CommandHomeCheck.class, new CommandHomeCheck(getPvP()));
		getPvP().getCmd().register(CommandEpic.class, new CommandEpic());

		UtilServer.getDeliveryPet(new DeliveryPet(UtilInv.getBase(), null, new DeliveryObject[]
		{ new DeliveryObject(new String[]
				{ "", "§7Click for Vote!", "", "§ePvP Rewards:", "§7   200 Epics", "§7   1x Inventory Repair", "", "§eGame Rewards:", "§7   25 Gems", "§7   100 Coins", "", "§eSkyBlock Rewards:", "§7   200 Epics", "§7   2x Diamonds", "§7   2x Iron Ingot", "§7   2x Gold Ingot" }, PermissionType.DELIVERY_PET_VOTE, false, 28, "§aVote for EpicPvP", Material.PAPER, Material.REDSTONE_BLOCK, new Click() {

					@Override
					public void onClick(Player p, ActionType a, Object obj) {
						p.closeInventory();
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§7-----------------------------------------");
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + " ");
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "Vote Link:§a http://vote.EpicPvP.eu/");
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + " ");
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§7-----------------------------------------");
					}

				}, -1), new DeliveryObject(new String[]
				{ "§aOnly for §eVIP§a!", "", "§ePvP Rewards:", "§7   200 Epics", "§7   10 Level", "", "§eGame Rewards:", "§7   200 Coins", "§7   2x TTT Paesse", "", "§eSkyBlock Rewards:", "§7   200 Epics", "§7   2x Diamonds", "§7   2x Iron Ingot", "§7   2x Gold Ingot" }, PermissionType.DELIVERY_PET_VIP_WEEK, true, 11, "§cRank §eVIP§c Reward", Material.getMaterial(342), Material.MINECART, new Click() {

					@Override
					public void onClick(Player p, ActionType a, Object obj) {
						getStatsManager().addDouble(p, 200, StatsKey.MONEY);
						p.setLevel(p.getLevel() + 10);
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + TranslationHandler.getText(p, "MONEY_RECEIVE_FROM", new String[]
						{ "§bThe Delivery Jockey!", "200" }));
					}

				}, TimeSpan.DAY * 7), new DeliveryObject(new String[]
				{ "§aOnly for §6ULTRA§a!", "", "§ePvP Rewards:", "§7   300 Epics", "§7   15 Level", "", "§eGame Rewards:", "§7   300 Coins", "§7   2x TTT Paesse", "", "§eSkyBlock Rewards:", "§7   300 Epics", "§7   4x Diamonds", "§7   4x Iron Ingot", "§7   4x Gold Ingot" }, PermissionType.DELIVERY_PET_ULTRA_WEEK, true, 12, "§cRank §6ULTRA§c Reward", Material.getMaterial(342), Material.MINECART, new Click() {

					@Override
					public void onClick(Player p, ActionType a, Object obj) {
						getStatsManager().addDouble(p, 300, StatsKey.MONEY);
						p.setLevel(p.getLevel() + 15);
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + TranslationHandler.getText(p, "MONEY_RECEIVE_FROM", new String[]
						{ "§bThe Delivery Jockey!", "300" }));
					}

				}, TimeSpan.DAY * 7), new DeliveryObject(new String[]
				{ "§aOnly for §aLEGEND§a!", "", "§ePvP Rewards:", "§7   400 Epics", "§7   20 Level", "", "§eGame Rewards:", "§7   400 Coins", "§7   3x TTT Paesse", "", "§eSkyBlock Rewards:", "§7   400 Epics", "§7   6x Diamonds", "§7   6x Iron Ingot", "§7   6x Gold Ingot" }, PermissionType.DELIVERY_PET_LEGEND_WEEK, true, 13, "§cRank §5LEGEND§c Reward", Material.getMaterial(342), Material.MINECART, new Click() {

					@Override
					public void onClick(Player p, ActionType a, Object obj) {
						getStatsManager().addDouble(p, 400, StatsKey.MONEY);
						p.setLevel(p.getLevel() + 20);
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + TranslationHandler.getText(p, "MONEY_RECEIVE_FROM", new String[]
						{ "§bThe Delivery Jockey!", "400" }));
					}

				}, TimeSpan.DAY * 7), new DeliveryObject(new String[]
				{ "§aOnly for §bMVP§a!", "", "§ePvP Rewards:", "§7   500 Epics", "§7   25 Level", "", "§eGame Rewards:", "§7   500 Coins", "§7   3x TTT Paesse", "", "§eSkyBlock Rewards:", "§7   500 Epics", "§7   8x Diamonds", "§7   8x Iron Ingot", "§7   8x Gold Ingot" }, PermissionType.DELIVERY_PET_MVP_WEEK, true, 14, "§cRank §3MVP§c Reward", Material.getMaterial(342), Material.MINECART, new Click() {

					@Override
					public void onClick(Player p, ActionType a, Object obj) {
						getStatsManager().addDouble(p, 500, StatsKey.MONEY);
						p.setLevel(p.getLevel() + 25);
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + TranslationHandler.getText(p, "MONEY_RECEIVE_FROM", new String[]
						{ "§bThe Delivery Jockey!", "500" }));
					}

				}, TimeSpan.DAY * 7), new DeliveryObject(new String[]
				{ "§aOnly for §bMVP§c+§a!", "", "§ePvP Rewards:", "§7   600 Epics", "§7   30 Level", "", "§eGame Rewards:", "§7   600 Coins", "§7   4x TTT Paesse", "", "§eSkyBlock Rewards:", "§7   600 Epics", "§7   10x Diamonds", "§7   10x Iron Ingot", "§7   10x Gold Ingot" }, PermissionType.DELIVERY_PET_MVPPLUS_WEEK, true, 15, "§cRank §9MVP§e+§c Reward", Material.getMaterial(342), Material.MINECART, new Click() {

					@Override
					public void onClick(Player p, ActionType a, Object obj) {
						getStatsManager().addDouble(p, 600, StatsKey.MONEY);
						p.setLevel(p.getLevel() + 30);
						p.sendMessage(TranslationHandler.getText(p, "PREFIX") + TranslationHandler.getText(p, "MONEY_RECEIVE_FROM", new String[]
						{ "§bThe Delivery Jockey!", "600" }));
					}

				}, TimeSpan.DAY * 7), new DeliveryObject(new String[]
				{ "§7/twitter [TwitterName]", "", "§ePvP Rewards:", "§7   300 Epics", "§7   15 Level", "", "§eGame Rewards:", "§7   300 Coins", "", "§eSkyBlock Rewards:", "§7   300 Epics", "§7   15 Level" }, PermissionType.DELIVERY_PET_TWITTER, false, 34, "§cTwitter Reward", Material.getMaterial(351), 4, new Click() {

					@Override
					public void onClick(Player p, ActionType a, Object obj) {
						//						String s1 = getPvP().getMysql().getString("SELECT twitter FROM BG_TWITTER WHERE uuid='"+UtilPlayer.getRealUUID(p)+"'");
						//						if(s1.equalsIgnoreCase("null")){
						//							p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_ACC_NOT"));
						//						}else{
						//							getPvP().getPacketManager().SendPacket("DATA", new TWIITTER_IS_PLAYER_FOLLOWER(s1, p.getName()));
						//							p.sendMessage(Language.getText(p,"PREFIX")+Language.getText(p, "TWITTER_CHECK"));
						//						}
					}

				}, TimeSpan.DAY * 7), }, "§bThe Delivery Jockey!", EntityType.CHICKEN, CommandLocations.getLocation("DeliveryPet"), ServerType.PVP, getPvP().getHologram(), getPvP().getMysql()));

		new EnderChestListener(getPvP().getUserData());
		new kPvPListener(this);
		new ChatListener();
		new EnderpearlListener(getPvP());
		getPerkManager().setPerkEntity(CommandLocations.getLocation("perk"));
		setRandomCreature(CommandLocations.getLocation("random"));
		
		new VoteListener(getPvP(), true, new Callback<String>() {

			@Override
			public void call(String playerName, Throwable ex) {
				if (UtilPlayer.isOnline(playerName)) {
					Player player = Bukkit.getPlayer(playerName);
					if (UtilServer.getDeliveryPet() != null) {
						UtilServer.getDeliveryPet().deliveryUSE(player, "§aVote for EpicPvP", true);
					}

					getStatsManager().addDouble(player, 200, StatsKey.MONEY);
					UtilInv.repairInventory(player, true);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "VOTE_THX"));
				}
			}
		});
	}

	public void onDisable() {
		UtilServer.getGemsShop().onDisable();
		if (UtilServer.getDeliveryPet() != null) {
			UtilServer.getDeliveryPet().onDisable();
		}
		getStatsManager().saveAll();
		UtilServer.getGemsShop().getGems().saveAll();
		this.gildenManager.AllUpdateGilde();
	}

	public void setRandomCreature(Location loc) {
		IronGolem e = (IronGolem) loc.getWorld().spawnEntity(loc, EntityType.IRON_GOLEM);
		NameTagMessage m = new NameTagMessage(NameTagType.SERVER, e.getLocation().add(0, 2.8, 0), "§a§lRandom Teleporter");
		m.send();
		UtilEnt.setNoAI(e, true);
		UtilEnt.setSilent(e, true);

		Click c = new Click() {

			@Override
			public void onClick(Player p, ActionType arg1, Object arg2) {
				Location loc = new Location(p.getWorld(), UtilMath.RandomInt(100000, 10000), 0, UtilMath.RandomInt(100000, 10000));
				loc = UtilLocation.getLowestBlock(loc);

				p.teleport(loc);
			}

		};

		new EntityClickListener(getPvP(), new Click() {

			@Override
			public void onClick(Player p, ActionType a, Object o) {
				kConfig config = getPvP().getUserData().getConfig(p);

				if (config.contains("RandomPort")) {
					InventoryBuy buy = new InventoryBuy(new Click() {

						@Override
						public void onClick(Player arg0, ActionType arg1, Object arg2) {
							c.onClick(p, null, null);
						}

					}, "§a§lRandom Teleport", UtilServer.getGemsShop().getGems(), 25, 0);
					UtilInv.getBase().addAnother(buy);
					p.openInventory((Inventory) buy);

				} else {
					config.set("RandomPort", 1);
					c.onClick(p, null, null);
				}
			}

		}, e);
	}
}
