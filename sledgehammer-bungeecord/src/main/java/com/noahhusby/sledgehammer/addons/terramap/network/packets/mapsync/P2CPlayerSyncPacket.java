package com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync;

import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.addons.terramap.network.ForgeChannel;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.IForgePacket;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * Sent at regular intervals to players that registered with a {@link C2PRegisterForUpdatePacket}.
 * Contains the display name (JSON formatted), uuid, longitude, latitude, Azimuth and gamemode.
 * The interval at which packets are sent can be configured in {@link ConfigHandler#terramapSyncInterval}
 * 
 * @see com.noahhusby.sledgehammer.addons.terramap.RemoteSynchronizer
 * @author SmylerMC
 *
 */
public class P2CPlayerSyncPacket implements IForgePacket {

	public SledgehammerPlayer[] players;

	public P2CPlayerSyncPacket(SledgehammerPlayer[] players) {
		this.players = players;
	}

	@Override
	public void encode(ByteBuf buf) {
		buf.writeInt(this.players.length);
		for(SledgehammerPlayer player: this.players) {
			double[] coordinates = SledgehammerUtil.toGeo(Double.parseDouble(player.getLocation().x), Double.parseDouble(player.getLocation().z));
			buf.writeLong(player.getUniqueId().getLeastSignificantBits());
			buf.writeLong(player.getUniqueId().getMostSignificantBits());
			String playerDisplayName = ComponentSerializer.toString(TextComponent.fromLegacyText(player.getDisplayName()));
			ForgeChannel.writeStringToBuf(playerDisplayName, buf);
			buf.writeDouble(coordinates[0]);
			buf.writeDouble(coordinates[1]);
			buf.writeFloat(0); //TODO Terramap azimuth
			ForgeChannel.writeStringToBuf("unknown", buf); //TODO Terramap gamemode
		}

	}

	@Override
	public void decode(ByteBuf buf) {
		// We will never receive this here
	}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		// We will never receive this here
		return ConfigHandler.terramapSyncPlayers;
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		// We will never receive this here
		return ConfigHandler.terramapSyncPlayers;
	}

}
