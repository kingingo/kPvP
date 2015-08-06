package me.kingingo.kpvp.Command;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStats implements CommandExecutor{
	
	@Getter
	private GildenManager gildenManager;
	@Getter
	private StatsManager statsManager;
	
	public CommandStats(GildenManager gildenmanager,StatsManager statsmanager){
		this.gildenManager=gildenmanager;
		this.statsManager=statsmanager;
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "stats", alias = {"kdr","money"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			p.sendMessage(Language.getText(p, "STATS_PREFIX"));
			p.sendMessage(Language.getText(p, "STATS_KILLS")+getStatsManager().getInt(Stats.KILLS, p));
			p.sendMessage(Language.getText(p, "STATS_DEATHS")+getStatsManager().getInt(Stats.DEATHS, p));
			p.sendMessage(Language.getText(p, "STATS_MONEY")+getStatsManager().getDouble(Stats.MONEY, p));
			p.sendMessage(Language.getText(p, "STATS_KDR")+getStatsManager().getKDR(getStatsManager().getInt(Stats.KILLS, p), getStatsManager().getInt(Stats.DEATHS, p)));
			if(getGildenManager().isPlayerInGilde(p)){
				p.sendMessage(Language.getText(p, "STATS_GILDE")+getGildenManager().getPlayerGilde(p));
			}
			p.sendMessage(Language.getText(p, "STATS_RANKING")+getStatsManager().getRank(Stats.KILLS, p));
		}else if(args[0].equalsIgnoreCase("ranking")){
			statsManager.Ranking(p);
		}
//		else{
//			String player=args[0];
//			if(player.equalsIgnoreCase(p.getName()))return false;
//			if(!getStatsManager().ExistPlayer(player)){
//				p.sendMessage("§cDieser Spieler Exestiert nicht!");
//				return false;
//			}
//			int k = getStatsManager().getIntWithString(Stats.KILLS, player);
//			int d = getStatsManager().getIntWithString(Stats.DEATHS, player);
//			p.sendMessage(Text.STATS_PREFIX.getText());
//			p.sendMessage(Text.STATS_KILLS.getText()+k);
//			p.sendMessage(Text.STATS_DEATHS.getText()+d);
//			p.sendMessage(Text.STATS_MONEY.getText()+getStatsManager().getDoubleWithString(Stats.MONEY, player));
//			p.sendMessage(Text.STATS_KDR.getText()+getStatsManager().getKDR(k, d));
//			UUID uuid = UtilPlayer.getUUID(player, statsManager.getMysql());
//			if(uuid!=null&&getGildenManager().isPlayerInGilde(uuid)){
//				p.sendMessage(Text.STATS_GILDE.getText()+getGildenManager().getPlayerGilde(uuid));
//			}
//			p.sendMessage(Text.STATS_RANKING.getText()+getStatsManager().getRankWithString(Stats.KILLS, player));
//		}
		return false;
	}
	
}
