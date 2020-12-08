/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PTestLocationPacket.java
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
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.json.simple.JSONObject;

public class S2PTestLocationPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.testLocationID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        new Thread(() -> {
            JSONObject point = (JSONObject) data.get("point");

            int zoom = Math.round((long) data.get("zoom"));
            double[] proj = SledgehammerUtil.toGeo(Double.parseDouble((String) point.get("x")), Double.parseDouble((String) point.get("z")));
            Location online = OpenStreetMaps.getInstance().getLocation(proj[0], proj[1], zoom);
            CommandSender player = ProxyServer.getInstance().getPlayer(info.getSender());

            if (online == null) {
                player.sendMessage(ChatHelper.makeAdminTextComponent(
                        new TextElement("This is not a valid location in the projection!", ChatColor.RED)));
                return;
            }

            player.sendMessage(ChatHelper.makeAdminTextComponent(
                    new TextElement("Testing location at ", ChatColor.GRAY), new TextElement(proj[0] + ", " + proj[1], ChatColor.BLUE),
                    new TextElement(" (Zoom: " + zoom + ")", ChatColor.GRAY)));
            player.sendMessage(ChatHelper.makeTextComponent(new TextElement("Online: ", ChatColor.RED)));
            if (!online.city.equals("")) {
                player.sendMessage(ChatHelper.makeTextComponent(new TextElement("City - ", ChatColor.GRAY), new TextElement(online.city, ChatColor.BLUE)));
            }
            if (!online.county.equals("")) {
                player.sendMessage(ChatHelper.makeTextComponent(new TextElement("County - ", ChatColor.GRAY), new TextElement(online.county, ChatColor.BLUE)));
            }
            if (!online.state.equals("")) {
                player.sendMessage(ChatHelper.makeTextComponent(new TextElement("State - ", ChatColor.GRAY), new TextElement(online.state, ChatColor.BLUE)));
            }
            if (!online.country.equals("")) {
                player.sendMessage(ChatHelper.makeTextComponent(new TextElement("Country - ", ChatColor.GRAY), new TextElement(online.country, ChatColor.BLUE)));
            }
            if (!ConfigHandler.useOfflineMode) {
                player.sendMessage(ChatHelper.makeTextComponent(
                        new TextElement("Offline: ", ChatColor.RED), new TextElement("Disabled", ChatColor.DARK_RED)));
            } else {
                Location offline = OpenStreetMaps.getInstance().getOfflineLocation(proj[0], proj[1]);
                player.sendMessage(ChatHelper.makeTextComponent(new TextElement("Offline: ", ChatColor.RED)));
                if (!offline.city.equals("")) {
                    player.sendMessage(ChatHelper.makeTextComponent(new TextElement("City - ", ChatColor.GRAY), new TextElement(offline.city, ChatColor.BLUE)));
                }
                if (!offline.county.equals("")) {
                    player.sendMessage(ChatHelper.makeTextComponent(new TextElement("County - ", ChatColor.GRAY), new TextElement(offline.county, ChatColor.BLUE)));
                }
                if (!offline.state.equals("")) {
                    player.sendMessage(ChatHelper.makeTextComponent(new TextElement("State - ", ChatColor.GRAY), new TextElement(offline.state, ChatColor.BLUE)));
                }
                if (!offline.country.equals("")) {
                    player.sendMessage(ChatHelper.makeTextComponent(new TextElement("Country - ", ChatColor.GRAY), new TextElement(offline.country, ChatColor.BLUE)));
                }
            }

        }).start();
    }
}
