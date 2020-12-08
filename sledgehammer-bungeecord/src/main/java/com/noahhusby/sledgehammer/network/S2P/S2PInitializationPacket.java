/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PInitializationPacket.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class S2PInitializationPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.initID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        ServerInfo server = ProxyServer.getInstance().getServerInfo(info.getServer());

        Sledgehammer.debug(ChatConstants.logInitPacket + server.getName());
        ServerConfig.getInstance().initialize(server, data.toJSON());
    }
}
