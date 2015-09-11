package me.kingingo.kpvp.Command;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.StatsManager.Ranking;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.TimeSpan;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStats implements CommandExecutor{
	
	@Getter
	private GildenManager gildenManager;
	@Getter
	private StatsManager statsManager;
	@Getter
	private static Ranking ranking_day;
	@Getter
	private static Ranking ranking_week;
	@Getter
	private static Ranking ranking_month;
	@Getter
	private static Ranking ranking;
	
	public CommandStats(GildenManager gildenmanager,StatsManager statsmanager){
		this.gildenManager=gildenmanager;
		this.statsManager=statsmanager;
		this.ranking_day=new Ranking(statsManager, Stats.ELO, TimeSpan.DAY, 10);
		this.statsManager.addRanking(ranking_day);
		this.ranking_week=new Ranking(statsManager, Stats.ELO, TimeSpan.DAY*7, 10);
		this.statsManager.addRanking(ranking_week);
		this.ranking_month=new Ranking(statsManager, Stats.ELO, TimeSpan.DAY*30, 10);
		this.statsManager.addRanking(ranking_month);
		this.ranking=new Ranking(statsManager, Stats.ELO, -1, 10);
		this.statsManager.addRanking(ranking);
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "stats", alias = {"kdr","money"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			p.sendMessage(Language.getText(p, "STATS_PREFIX"));
			p.sendMessage(Language.getText(p, "STATS_FAME")+getStatsManager().getDouble(Stats.ELO, p));
			p.sendMessage(Language.getText(p, "STATS_KILLS")+getStatsManager().getInt(Stats.KILLS, p));
			p.sendMessage(Language.getText(p, "STATS_DEATHS")+getStatsManager().getInt(Stats.DEATHS, p));
			p.sendMessage(Language.getText(p, "STATS_MONEY")+getStatsManager().getDouble(Stats.MONEY, p));
			p.sendMessage(Language.getText(p, "STATS_KDR")+getStatsManager().getKDR(getStatsManager().getInt(Stats.KILLS, p), getStatsManager().getInt(Stats.DEATHS, p)));
			if(getGildenManager().isPlayerInGilde(p)){
				p.sendMessage(Language.getText(p, "STATS_GILDE")+getGildenManager().getPlayerGilde(p));
			}
			p.sendMessage(Language.getText(p, "STATS_RANKING")+getStatsManager().getRank(Stats.KILLS, p));
		}else if(args[0].equalsIgnoreCase("ranking")){
			if(args.length==1){
				getStatsManager().SendRankingMessage(p, ranking);
			}else{
				if(args[1].equalsIgnoreCase("day")||args[1].equalsIgnoreCase("tag")){
					getStatsManager().SendRankingMessage(p, ranking_day, "Tag");
				}else if(args[1].equalsIgnoreCase("week")||args[1].equalsIgnoreCase("woche")){
					getStatsManager().SendRankingMessage(p, ranking_week, "Woche");
				}else if(args[1].equalsIgnoreCase("month")||args[1].equalsIgnoreCase("Monat")){
					getStatsManager().SendRankingMessage(p, ranking_month, "Monat");
				}else{
					getStatsManager().SendRankingMessage(p, ranking);
				}
			}
			
		}
		return false;
	}
	
}
