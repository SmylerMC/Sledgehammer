/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerServerComponent.java
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

package com.noahhusby.sledgehammer.dialogs.components.setup;

import com.noahhusby.sledgehammer.ChatUtil;
import com.noahhusby.sledgehammer.dialogs.components.DialogComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class SledgehammerServerComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "sledgehammer_server";
    }

    @Override
    public String getPrompt() {
        return "Is this server running sledgehammer?";
    }

    @Override
    public TextComponent getExplanation() {
        return ChatUtil.combine(ChatColor.GRAY, "Enter ", ChatColor.BLUE, "Yes [Y]", ChatColor.GRAY, " or ",
                ChatColor.BLUE, "No [N]");
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
    }

    @Override
    public boolean validateResponse(String v) {
        String vm = v.toLowerCase().trim();
        return vm.equals("yes") || vm.equals("no") || vm.equals("y") || vm.equals("n");
    }

    @Override
    public boolean isManual() {
        return true;
    }
}
