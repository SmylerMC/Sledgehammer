package com.noahhusby.sledgehammer.addons.terramap.network;

import java.util.HashMap;
import java.util.Map;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.ForgePacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;

public class ForgeChannel {

	private String channelName;
	private Map<Integer, Class<? extends ForgePacket>> packetMap = new HashMap<Integer, Class<? extends ForgePacket>>();
	private Map<Class<? extends ForgePacket>, Integer> discriminatorMap = new HashMap<Class<? extends ForgePacket>, Integer>();
	
	public ForgeChannel(String channelName) {
		this.channelName = channelName;
	}

	public void process(PluginMessageEvent event) {
		if(!event.getTag().equals(this.channelName)) {
			Sledgehammer.logger.warning("Asked to process a channel from channel: " + event.getTag() + " in " + this.channelName + " channel handler!");
			return;
		}
		try {
			ByteBuf stream = Unpooled.copiedBuffer(event.getData());
			int discriminator = stream.readByte();
			ProxiedPlayer player;
			Server server;
			boolean player2server = true;
			if(event.getSender() instanceof ProxiedPlayer && event.getReceiver() instanceof Server) {
				player = (ProxiedPlayer)event.getSender();
				server = (Server)event.getReceiver();
				player2server = true;
			} else if(event.getSender() instanceof Server && event.getReceiver() instanceof ProxiedPlayer) {
				player = (ProxiedPlayer)event.getReceiver();
				server = (Server)event.getSender();
				player2server = false;
			} else {
				Sledgehammer.logger.warning(
						"Got an unknow combination of sender/receiver in Forge channel " + this.channelName + " channel. " +
						"Sender " + event.getSender().getClass() +
						", Receiver: " + event.getReceiver().getClass() +
						", Packet discriminator " + discriminator);
				return;
			}
			Class<? extends ForgePacket> clazz = packetMap.get(discriminator);
			if(clazz == null) {
				if(player2server) {
					throw new PacketEncodingException("Received an unregistered packet from player" + player.getName() + "/" + player.getUniqueId() + "/" + player.getSocketAddress() + " for server" + server.getInfo().getName() + "! Discriminator: " + discriminator);
				} else if(event.getSender() instanceof Server && event.getReceiver() instanceof ProxiedPlayer) {
					throw new PacketEncodingException("Received an unregistered packet from server " + server.getInfo().getName() + " for player " + player.getName() + "/" + player.getUniqueId() + "/" + player.getSocketAddress() + "! Discriminator: " + discriminator);
				}
			}
			ForgePacket packetHandler = clazz.newInstance();
			packetHandler.decode(stream);
			boolean cancel = false;
			if(player2server) {
				cancel = packetHandler.processFromClient(this.channelName, player, server);
			} else {
				cancel = packetHandler.processFromServer(this.channelName, server, player);
			}
			if(cancel) event.setCancelled(cancel);
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to process a Forge packet!");
			e.printStackTrace();
		}
	}
	
	public void send(ForgePacket pkt, ProxiedPlayer to) {
		try {
			to.sendData(this.channelName, this.encode(pkt));
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to send a Forge packet to player " + to.getName() + "/" + to.getUniqueId() + " in channel " + this.channelName + " : " + e);
		}
	}
	
	public void send(ForgePacket pkt, ProxiedPlayer... to) {
		int sent = 0;
		try {
			byte[] data = this.encode(pkt);
			for(ProxiedPlayer player: to) {
				player.sendData(this.channelName, data);
				sent++;
			}
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to send a Forge packet to " + (to.length - sent) + "players in channel " + this.channelName + " : " + e);
		}
	}
	
	public void send(ForgePacket pkt, Server to) {
		try {
			to.sendData(this.channelName, this.encode(pkt));
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to send a Forge packet to server " + to.getInfo().getName() + " in channel " + this.channelName + " : " + e);
		}
	}
	
	private byte[] encode(ForgePacket pkt) throws PacketEncodingException {
		if(!discriminatorMap.containsKey(pkt.getClass())) {
			throw new PacketEncodingException("Could not encode packet of class " + pkt.getClass().getCanonicalName() + " as it has not been registered to this channel");
		}
		int discriminator = discriminatorMap.get(pkt.getClass());
		ByteBuf stream = Unpooled.buffer();
		stream.writeByte(discriminator);
		pkt.encode(stream);
		return stream.array();
	}

	public void registerPacket(int discriminator, Class<? extends ForgePacket> clazz) {
		packetMap.put(discriminator, clazz);
		discriminatorMap.put(clazz, discriminator);
	}

}
