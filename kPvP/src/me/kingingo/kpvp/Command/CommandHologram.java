package me.kingingo.kpvp.Command;

import java.io.File;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.kConfig.kConfig;
import me.kingingo.kpvp.kPvP;
import me.kingingo.kpvp.kPvPListener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHologram implements CommandExecutor{
	
	@Getter
	private kPvP manager;
	private kConfig config;
	@Setter
	@Getter
	private static Location today;
	@Setter
	@Getter
	private static Location month;
	@Setter
	@Getter
	private static Location week;
	@Setter
	@Getter
	private static Location total;
	@Setter
	@Getter
	private static Location player;
	@Setter
	@Getter
	private static Location delivery;
	
	public CommandHologram(kPvP manager){
		this.manager=manager;
		this.config=new kConfig(new File("plugins"+File.separator+manager.getInstance().getPlugin(manager.getInstance().getClass()).getName()+File.separator+"holograms.yml"));
		
		if(config.getString("Hologram.today")!=null&&Bukkit.getWorld(config.getString("Hologram.today.world"))!=null){
			if(config.isSet("Hologram.today")){
				setToday(config.getLocation("Hologram.today"));
			}else{
				today=Bukkit.getWorld("world").getSpawnLocation();
			}
		}
		
		if(config.getString("Hologram.month")!=null&&Bukkit.getWorld(config.getString("Hologram.month.world"))!=null){
			if(config.isSet("Hologram.month")){
				month=config.getLocation("Hologram.month");
			}else{
				month=Bukkit.getWorld("world").getSpawnLocation();
			}
		}
		
		if(config.getString("Hologram.total")!=null&&Bukkit.getWorld(config.getString("Hologram.total.world"))!=null){
			if(config.isSet("Hologram.total")){
				total=config.getLocation("Hologram.total");
			}else{
				total=Bukkit.getWorld("world").getSpawnLocation();
			}
		}
		
		if(config.getString("Hologram.player")!=null&&Bukkit.getWorld(config.getString("Hologram.player.world"))!=null){
			if(config.isSet("Hologram.player")){
				player=config.getLocation("Hologram.player");
			}else{
				player=Bukkit.getWorld("world").getSpawnLocation();
			}
		}
		
		if(config.getString("Hologram.week")!=null&&Bukkit.getWorld(config.getString("Hologram.week.world"))!=null){
			if(config.isSet("Hologram.week")){
				week=config.getLocation("Hologram.week");
			}else{
				week=Bukkit.getWorld("world").getSpawnLocation();
			}
		}
		
		if(config.getString("DeliveryPet")!=null&&Bukkit.getWorld(config.getString("DeliveryPet.world"))!=null){
			if(config.isSet("DeliveryPet")){
				delivery=config.getLocation("DeliveryPet");
			}else{
				delivery=Bukkit.getWorld("world").getSpawnLocation();
			}
		}
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setholo", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(p.isOp()){
			if(args.length==0){
				p.sendMessage(Language.getText(p, "PREFIX")+"/setholo today");
				p.sendMessage(Language.getText(p, "PREFIX")+"/setholo week");
				p.sendMessage(Language.getText(p, "PREFIX")+"/setholo month");
				p.sendMessage(Language.getText(p, "PREFIX")+"/setholo total");
				p.sendMessage(Language.getText(p, "PREFIX")+"/setholo player");
				p.sendMessage(Language.getText(p, "PREFIX")+"/setholo delivery");
			}else{
				if(args[0].equalsIgnoreCase("today")){
					config.setLocation("Hologram.today", p.getLocation());
					config.save();
					setToday(p.getLocation());
					for(Player pl : UtilServer.getPlayers())kPvPListener.ranking_day.clear(pl);
					kPvPListener.ranking_day=manager.getHologram().createNameTagMessage(CommandHologram.getToday(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_day(),"Tag"));
					for(Player pl : UtilServer.getPlayers())kPvPListener.ranking_day.sendToPlayer(pl);
					p.sendMessage(Language.getText(p, "PREFIX")+"§a Das Today Ranking Hologram wurde hier gesetzt!");
				}else if(args[0].equalsIgnoreCase("month")){
					config.setLocation("Hologram.month", p.getLocation());
					config.save();
					setMonth(p.getLocation());
					for(Player pl : UtilServer.getPlayers())kPvPListener.ranking_month.clear(pl);
					kPvPListener.ranking_month=manager.getHologram().createNameTagMessage(CommandHologram.getMonth(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_month(),"Monat"));
					for(Player pl : UtilServer.getPlayers())kPvPListener.ranking_month.sendToPlayer(pl);
					p.sendMessage(Language.getText(p, "PREFIX")+"§a Das Monats Ranking Hologram wurde hier gesetzt!");
				}else if(args[0].equalsIgnoreCase("week")){
					config.setLocation("Hologram.week", p.getLocation());
					config.save();
					setWeek(p.getLocation());
					for(Player pl : UtilServer.getPlayers())kPvPListener.ranking_week.clear(pl);
					kPvPListener.ranking_week=manager.getHologram().createNameTagMessage(CommandHologram.getWeek(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking_week(),"Woche"));
					for(Player pl : UtilServer.getPlayers())kPvPListener.ranking_week.sendToPlayer(pl);
					p.sendMessage(Language.getText(p, "PREFIX")+"§a Das Wochen Ranking Hologram wurde hier gesetzt!");
				}else if(args[0].equalsIgnoreCase("total")){
					config.setLocation("Hologram.total", p.getLocation());
					config.save();
					setTotal(p.getLocation());
					for(Player pl : UtilServer.getPlayers())kPvPListener.ranking_total.clear(pl);
					kPvPListener.ranking_total=manager.getHologram().createNameTagMessage(CommandHologram.getTotal(), manager.getStatsManager().getRankingMessage(CommandStats.getRanking()));
					for(Player pl : UtilServer.getPlayers())kPvPListener.ranking_total.sendToPlayer(pl);
					p.sendMessage(Language.getText(p, "PREFIX")+"§a Das Ranking Hologram wurde hier gesetzt!");
				}else if(args[0].equalsIgnoreCase("player")){
					config.setLocation("Hologram.player", p.getLocation());
					config.save();
					setPlayer(p.getLocation());
					p.sendMessage(Language.getText(p, "PREFIX")+"§a Das Player Stats Hologram wurde hier gesetzt!");
				}else if(args[0].equalsIgnoreCase("delivery")){
					config.setLocation("DeliveryPet", p.getLocation());
					config.save();
					setDelivery(p.getLocation());
					p.sendMessage(Language.getText(p, "PREFIX")+"§a Die Location für das DeliveryPet wurde gesetzt!");
				}
			}
		}
		return false;
	}
	
}
