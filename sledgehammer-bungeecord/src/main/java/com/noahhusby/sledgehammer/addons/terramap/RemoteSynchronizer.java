package com.noahhusby.sledgehammer.addons.terramap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.P2CPlayerSyncPacket;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.P2CRegistrationExpiresPacket;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

public class RemoteSynchronizer {

	public Map<UUID, RegisteredForUpdatePlayer> playersToUpdate = new HashMap<UUID, RegisteredForUpdatePlayer>();

	public void syncPlayers() {
		if(this.playersToUpdate.size() <= 0) return;
		long ctime = System.currentTimeMillis();
		List<SledgehammerPlayer> players = new ArrayList<SledgehammerPlayer>();
		for(SledgehammerPlayer player: PlayerManager.getInstance().getPlayers()) {
			if(!player.onEarthServer()) continue;
			//TODO Handle display preferences and sync configuration
			//if(!TerramapServerPreferences.shouldDisplayPlayer(player.getPersistentID())) continue;
			//if(terraPlayer.isSpectator() && !TerramapConfig.ServerConfig.synchronizeSpectators) continue;
			players.add(player);
		}
		SledgehammerPlayer[] players2send2 = new SledgehammerPlayer[this.playersToUpdate.size()];
		int i = 0;
		for(RegisteredForUpdatePlayer player: this.playersToUpdate.values()) {
			players2send2[i++] = player.player;
		}
		P2CPlayerSyncPacket pkt = new P2CPlayerSyncPacket(players.toArray(new SledgehammerPlayer[players.size()]));
		TerramapAddon.instance.mapSyncChannel.send(pkt, players2send2);
		for(RegisteredForUpdatePlayer player: this.playersToUpdate.values()) {
			if(ctime - player.lastRegisterTime > ConfigHandler.terramapSyncTimeout - 10000 && !player.noticeSent) {
				Sledgehammer.logger.fine("Sending registration expires notice to " + player.player.getName());
				TerramapAddon.instance.mapSyncChannel.send(new P2CRegistrationExpiresPacket(), player.player);
				player.noticeSent = true;
			}
		}
		for(RegisteredForUpdatePlayer player: this.playersToUpdate.values()) {
			if(ctime - player.lastRegisterTime > ConfigHandler.terramapSyncTimeout) {
				Sledgehammer.logger.fine("Unregistering " + player.player.getName() + " from map update as it did not renew its registration");
				this.playersToUpdate.remove(player.player.getUniqueId());
				TerramapAddon.instance.mapSyncChannel.send(new P2CRegistrationExpiresPacket(), player.player);
			}
		}
	}
	
	public void registerPlayer(SledgehammerPlayer player) {
		Sledgehammer.logger.fine("Registering player for map updates: " + player.getName());
		TerramapAddon.instance.synchronizer.playersToUpdate.put(player.getUniqueId(), new RegisteredForUpdatePlayer(player, System.currentTimeMillis()));
	}
	
	public void unregisterPlayer(SledgehammerPlayer player) {
		Sledgehammer.logger.fine("Unregistering player for map updates: " + player.getName());
		TerramapAddon.instance.synchronizer.playersToUpdate.remove(player.getUniqueId());
	}

	public static class RegisteredForUpdatePlayer {

		public SledgehammerPlayer player;
		public long lastRegisterTime;
		boolean noticeSent = false;

		public RegisteredForUpdatePlayer(SledgehammerPlayer player, long time) {
			this.player = player;
			this.lastRegisterTime = time;
		}

	}

}
