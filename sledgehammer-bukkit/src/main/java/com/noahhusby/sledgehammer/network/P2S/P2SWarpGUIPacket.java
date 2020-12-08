/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - P2SWarpGUIPacket.java
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

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.gui.inventories.warp.WarpInventoryController;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class P2SWarpGUIPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.warpGUIID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        Player p = Bukkit.getPlayer(info.getSender());
        if(p == null) {
            throwNoSender();
            return;
        }

        if(!p.isOnline()) {
            throwNoSender();
            return;
        }

        boolean local = data.getBoolean("local");
        boolean editAccess = data.getBoolean("editAccess");
        String requestGroup = data.getString("requestGroup");
        String defaultPage = data.getString("defaultPage");

        switch (defaultPage) {
            default:
            case "group":
                break;
            case "all":
                break;
            case "pinned":
                break;
        }

        GUIRegistry.register(new WarpInventoryController(p, data));
    }
}
