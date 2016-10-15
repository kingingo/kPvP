package eu.epicpvp.kpvp;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.datenclient.client.ClientWrapper;
import eu.epicpvp.datenserver.definitions.connection.ClientType;
import eu.epicpvp.datenserver.definitions.permissions.GroupTyp;
import eu.epicpvp.kcore.AACHack.AACHack;
import eu.epicpvp.kcore.AuktionsMarkt.AuktionsMarkt;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Command.Admin.CommandLocations;
import eu.epicpvp.kcore.Hologram.Hologram;
import eu.epicpvp.kcore.Listener.AntiCrashListener.AntiCrashListener;
import eu.epicpvp.kcore.Listener.BungeeCordFirewall.BungeeCordFirewallListener;
import eu.epicpvp.kcore.Listener.Command.ListenerCMD;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Particle.WingShop;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Update.Updater;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.UtilException;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import eu.epicpvp.kcore.deliverychest.DeliveryChest;
import eu.epicpvp.kcore.deliverychest.DeliveryChestHandler;
import eu.epicpvp.kcore.deliverychest.ItemModifier;
import eu.epicpvp.kpvp.Listener.Listener;
import lombok.Getter;

public class kPvP extends JavaPlugin {

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

	public void onEnable() {
		try {
			UtilServer.setPluginInstance(this);
			loadConfig();
			this.mysql = new MySQL(getConfig().getString("Config.MySQL.User"), getConfig().getString("Config.MySQL.Password"), getConfig().getString("Config.MySQL.Host"), getConfig().getString("Config.MySQL.DB"), this);
			this.instance = this;
			this.updater = new Updater(this);
			this.client = UtilServer.createClient(this, ClientType.OTHER, getConfig().getString("Config.Client.Host"), getConfig().getInt("Config.Client.Port"), "PvP");
			this.cmd = new CommandHandler(this);
			this.permissionManager = new PermissionManager(this, GroupTyp.PVP);
			this.aACHack=new AACHack("pvp");
			this.hologram = new Hologram(this);
			this.hologram.RemoveText();
			this.userData = new UserDataConfig(this);
			this.manager = new kPvPManager(this);

			UtilTime.setTimeManager(getPermissionManager());
			new BungeeCordFirewallListener(UtilServer.getCommandHandler());
			new Listener(getManager());
			new ListenerCMD(this);
			new AntiCrashListener(getClient(), getMysql());
			WingShop wings = new WingShop(this);
			wings.setEntity(CommandLocations.getLocation("wingshop"));
			UtilServer.getLagListener(); //Init if not already init
			
			new DeliveryChest(this, UtilServer.getUserData(), new ItemModifier() {
				
				@Override
				public void modify(ItemStack itemStack) {
					switch(itemStack.getType()){
					case DIAMOND_HELMET:
						for(Enchantment en : UtilItem.enchantmentsHelm())itemStack.addEnchantment(en, en.getMaxLevel());
						break;
					case DIAMOND_CHESTPLATE:
						for(Enchantment en : UtilItem.enchantmentsChestplate())itemStack.addEnchantment(en, en.getMaxLevel());
						break;
					case DIAMOND_LEGGINGS:
						for(Enchantment en : UtilItem.enchantmentsLeggings())itemStack.addEnchantment(en, en.getMaxLevel());
						break;
					case DIAMOND_BOOTS:
						for(Enchantment en : UtilItem.enchantmentsBoots())itemStack.addEnchantment(en, en.getMaxLevel());
						break;
					case DIAMOND_SWORD:
						for(Enchantment en : UtilItem.enchantmentsSword())itemStack.addEnchantment(en, en.getMaxLevel());
						break;
					case DIAMOND_AXE:
						for(Enchantment en : UtilItem.enchantmentsAxt())itemStack.addEnchantment(en, en.getMaxLevel());
						break;
					case BOW:
						for(Enchantment en : UtilItem.enchantmentsBow())itemStack.addEnchantment(en, en.getMaxLevel());
						break;
					}
				}
			}, true);
		} catch (Exception e) {
			UtilException.catchException(e, "pvp", Bukkit.getIp(), mysql);
		}
	}

	public void onDisable() {
		this.manager.onDisable();
		UtilServer.disable();
		saveConfig();
	}

	public void loadConfig() {
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
