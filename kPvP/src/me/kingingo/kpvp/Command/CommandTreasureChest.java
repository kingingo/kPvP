package me.kingingo.kpvp.Command;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.TreasureChest.TreasureChest;
import me.kingingo.kcore.TreasureChest.TreasureChest.TreasureChestHandler;
import me.kingingo.kcore.TreasureChest.TreasureChestType;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kpvp.kPvP;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandTreasureChest implements CommandExecutor{
	
	@Getter
	private kPvP manager;
	private TreasureChest chest;
	
	public CommandTreasureChest(kPvP manager){
		this.manager=manager;
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "treasurechest", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(chest==null)chest=new TreasureChest(manager.getInstance(),TreasureChestType.SKY);
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(args.length==0){
				if(p.isOp()){
					open(p);
				}
			}
		}else{
			if(UtilPlayer.isOnline( args[0] )){
				Player p = Bukkit.getPlayer(args[0]);
				open(p);
				System.out.println("[TreasureChest] Der Spieler "+p.getName()+" hat die TreasureChest erhalten");
			}else{
				System.out.println("[TreasureChest] Der Spieler ist offline!");
			}
		}
		return false;
	}
	
	public void open(Player p){
		chest.give(p, new TreasureChestHandler() {
			
			@Override
			public void onTreasureChest(Player player, Location loc) {
				
			}
			
			@Override
			public InventoryBase getInventory() {
				InventoryBase base = new InventoryBase(getManager().getInstance(), "Lotto:");
				base.setMain(new InventoryLotto(getManager().getInstance(), new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object obj) {
						if(obj instanceof ItemStack){
							player.getInventory().addItem( ((ItemStack)obj) );
							player.closeInventory();
						}
					}
					
				}, new ItemStack[]{new ItemStack(1),new ItemStack(2),new ItemStack(3),new ItemStack(4),new ItemStack(5),new ItemStack(6),new ItemStack(7),new ItemStack(8)}));
				return base;
			}
		} );
	}
	
}
