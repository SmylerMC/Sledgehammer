/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PSetwarpPacket.java
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

package com.noahhusby.sledgehammer.proxy.network.S2P;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.network.S2PPacket;

public class S2PSetwarpPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.setwarpID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        //TODO: Points
        Point p = SledgehammerUtil.GSON.fromJson(data.get("point"), Point.class);
        WarpHandler.getInstance().incomingLocationResponse(info.getSender(), p);
    }
}