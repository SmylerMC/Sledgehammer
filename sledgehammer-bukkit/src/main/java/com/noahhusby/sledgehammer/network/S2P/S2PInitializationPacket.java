/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - S2PInitializationPacket.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class S2PInitializationPacket extends S2PPacket {

    private final Player player;

    public S2PInitializationPacket(Player player) {
        this.player = player;
    }

    @Override
    public String getPacketID() {
        return Constants.initID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("version", Constants.VERSION);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }
}
