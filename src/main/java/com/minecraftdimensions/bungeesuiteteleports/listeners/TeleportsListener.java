package com.minecraftdimensions.bungeesuiteteleports.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.minecraftdimensions.bungeesuiteteleports.managers.TeleportsManager;

public class TeleportsListener implements Listener {
	
	@EventHandler
	public void playerConnect (PlayerJoinEvent e){
		if(TeleportsManager.pendingTeleports.containsKey(e.getPlayer().getName())){
			Player t = TeleportsManager.pendingTeleports.get(e.getPlayer().getName());
			TeleportsManager.pendingTeleports.remove(e.getPlayer().getName());
			if(!t.isOnline()){
				e.getPlayer().sendMessage("Player is no longer online");
				return;
			}
			TeleportsManager.ignoreTeleport.add(e.getPlayer());
			e.getPlayer().teleport(t);
			
		}else if (TeleportsManager.pendingTeleportLocations.containsKey(e.getPlayer().getName())){
			Location l = TeleportsManager.pendingTeleportLocations.get(e.getPlayer().getName());
			TeleportsManager.ignoreTeleport.add(e.getPlayer());
			e.getPlayer().teleport(l);
		}
	}
	
	@EventHandler
	public void playerTeleport(PlayerTeleportEvent e){
		if(e.isCancelled()){
			return;
		}
		if(!e.getCause().equals(TeleportCause.PLUGIN)){
			return;
		}
		if(TeleportsManager.ignoreTeleport.contains(e.getPlayer())){
			TeleportsManager.ignoreTeleport.remove(e.getPlayer());
			return;
		}
		TeleportsManager.sendTeleportBackLocation(e.getPlayer());	
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent e){
		TeleportsManager.sendTeleportBackLocation(e.getPlayer());	
	}
	
	@EventHandler
	public void playerDeath(PlayerDeathEvent e){
		TeleportsManager.sendDeathBackLocation(e.getEntity());	
	}

	
}