/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpListFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.warps;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.permissions.PermissionRequest;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import net.md_5.bungee.api.CommandSender;

public class WarpListFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean local = ConfigHandler.localWarp;
        boolean hasPerms = PermissionHandler.getInstance().isAdmin(sender) || local && sender.hasPermission("sledgehammer.listwarp");
        if(!hasPerms) {
            PermissionHandler.getInstance().check(code -> {
                run(sender, code == PermissionRequest.PermissionCode.PERMISSION);
            }, SledgehammerPlayer.getPlayer(sender), "sledgehammer.listwarp");
        } else {
            run(sender, true);
        }
    }

    public void run(CommandSender sender, boolean permission) {
        if(!permission) {
            sender.sendMessage(ChatConstants.noPermission);
            return;
        }

        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(sender);
        sender.sendMessage(WarpHandler.getInstance().getWarpList(player.getServer().getInfo().getName()));
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getPurpose() {
        return "";
    }

    @Override
    public String[] getArguments() {
        return new String[]{};
    }
}
