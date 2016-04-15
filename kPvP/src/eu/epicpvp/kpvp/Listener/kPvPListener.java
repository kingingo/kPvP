package eu.epicpvp.kpvp.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.AntiLogout.Events.AntiLogoutAddPlayerEvent;
import eu.epicpvp.kcore.AntiLogout.Events.AntiLogoutDelPlayerEvent;
import eu.epicpvp.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Scoreboard.Events.PlayerSetScoreboardEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsCreateEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.TabTitle;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kpvp.kPvPManager;
import lombok.Getter;

public class kPvPListener extends kListener{

	@Getter
	private kPvPManager manager;
	private HashMap<Player,String> pet_respawn = new HashMap<>();
	
	public kPvPListener(kPvPManager manager){
		super(manager.getPvP(),"[kPvPListener]");
		this.manager=manager;
	}
	
	@EventHandler
	public void Enderpearl(PlayerTeleportEvent ev){
		if(ev.getCause()==TeleportCause.ENDER_PEARL&& (!ev.getPlayer().isPermissionSet("kpvp.enderpearl")) ){
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerTeleport(PlayerTeleportEvent ev){
		if(!getManager().getAntiManager().is(ev.getPlayer())){
			ev.getPlayer().sendMessage(TranslationHandler.getText(ev.getPlayer(), "PREFIX")+TranslationHandler.getText(ev.getPlayer(), "ANIT_LOGOUT_FIGHT"));
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void GildeHome(GildenPlayerTeleportEvent ev){
		if(!getManager().getAntiManager().is(ev.getPlayer())){
			ev.getPlayer().sendMessage(TranslationHandler.getText(ev.getPlayer(), "PREFIX")+TranslationHandler.getText(ev.getPlayer(), "ANIT_LOGOUT_FIGHT_CMD","/gilde home"));
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void AntiLogoutdelPlayer(AntiLogoutDelPlayerEvent ev){
		if(pet_respawn.containsKey(ev.getPlayer())){
			manager.getPetHandler().loadPetSettings(ev.getPlayer(), pet_respawn.get(ev.getPlayer()));
			pet_respawn.remove(ev.getPlayer());
		}
	}

	@EventHandler
	public void AntiLogoutAddPlayer(AntiLogoutAddPlayerEvent ev){
		if(manager.getPetManager().hasPlayer(ev.getPlayer())){
			pet_respawn.put(ev.getPlayer(), getManager().getPetHandler().toString( manager.getPetManager().GetPet(ev.getPlayer()) ));
			manager.getPetManager().RemovePet(ev.getPlayer(), true);
		}
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		ev.setDeathMessage(null);
		if(ev.getEntity() instanceof Player){
			Player v = (Player)ev.getEntity();
			v.sendMessage(TranslationHandler.getText(v, "PREFIX")+TranslationHandler.getText(v, "PVP_DEATH"));
			
			getManager().getStatsManager().addInt(v, 1, StatsKey.DEATHS);
			if(ev.getEntity().getKiller()!=null&&ev.getEntity().getKiller() instanceof Player){
				getManager().getStatsManager().addInt(ev.getEntity().getKiller(), 1, StatsKey.KILLS);
				
				ev.getEntity().getKiller().sendMessage(TranslationHandler.getText(ev.getEntity().getKiller(), "PREFIX")+TranslationHandler.getText(ev.getEntity().getKiller(), "PVP_KILL",new Object[]{v.getName()} ));
			}
		}
	}
	
	@EventHandler
	public void NEW(PlayerStatsCreateEvent ev){
		if(ev.getManager().getType() != GameType.Money){
			if(UtilPlayer.isOnline(ev.getPlayerId())){
				Player player = UtilPlayer.searchExact(ev.getPlayerId());
				getManager().getNeulingManager().add(player);
				player.teleport(player.getWorld().getSpawnLocation());
			}
		}
	}
	
	@EventHandler
	public void saveStats(PlayerQuitEvent ev){
		ev.setQuitMessage(null);
		getManager().getStatsManager().SaveAllPlayerData(ev.getPlayer());
	}
	
	@EventHandler
	public void AddBoard(PlayerSetScoreboardEvent ev){
		UtilPlayer.setScoreboardGems(ev.getPlayer(), UtilServer.getGemsShop().getGems());
	}
	
	@EventHandler
	public void SendHolo(PlayerJoinEvent ev){
		TabTitle.setHeaderAndFooter(ev.getPlayer(), "§eEpicPvP§8.§eeu §8| §aPvP Server", "§aTeamSpeak: §7ts.EpicPvP.eu §8| §eWebsite: §7EpicPvP.eu");
	}
	
	@EventHandler
	public void pet(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Creature){
			if(getManager().getPetManager().isPet( ((Creature)ev.getEntity()) )){
				ev.setCancelled(true);
			}
		}else if(ev.getDamager() instanceof Creature){
			if(getManager().getPetManager().isPet( ((Creature)ev.getDamager()) )){
				ev.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		ev.setJoinMessage(null);
		getManager().getStatsManager().loadPlayer(ev.getPlayer());
		getManager().getMoney().loadPlayer(ev.getPlayer());
		getManager().getGildenManager().loadPlayer(ev.getPlayer());
		ev.getPlayer().sendMessage(TranslationHandler.getText(ev.getPlayer(), "PREFIX")+TranslationHandler.getText(ev.getPlayer(), "WHEREIS_TEXT","PvP"));
	}
}
