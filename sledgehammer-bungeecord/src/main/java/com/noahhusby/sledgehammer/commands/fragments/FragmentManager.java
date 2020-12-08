/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - FragmentManager.java
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

package com.noahhusby.sledgehammer.commands.fragments;

import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class FragmentManager {

    List<ICommandFragment> commandFragments = new ArrayList<>();
    private String title = "";
    private String commandBase = "";

    protected void registerCommandFragment(ICommandFragment c) {
        commandFragments.add(c);
    }

    protected void setTitle(String t) {
        this.title = " "+t;
    }

    protected void setCommandBase(String b) {
        this.commandBase = "/"+b+" ";
    }

    protected void executeFragment(CommandSender sender, String[] args) {
        executeFragment(sender, args, 0);
    }

    protected void executeFragment(CommandSender sender, String[] args, int index) {
        if (args.length > index) {
            if (index == 0) {
                ArrayList<String> dataList = new ArrayList<>();
                for (int x = 1; x < args.length; x++) dataList.add(args[x]);

                String[] data = dataList.toArray(new String[dataList.size()]);
                for (ICommandFragment f : commandFragments) {
                    if (f.getName().equals(args[0].toLowerCase())) {
                        f.execute(sender, data);
                        return;
                    }
                }
            } else {
                for (ICommandFragment f : commandFragments) {
                    if (f.getName().equals(args[index].toLowerCase())) {
                        f.execute(sender, args);
                        return;
                    }
                }
            }
        }

        displayCommands(sender);
    }

    private void displayCommands(CommandSender sender) {
        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement(title+":", ChatColor.GRAY)));
        for(ICommandFragment f : commandFragments) {

            List<TextElement> message = new ArrayList<>();
            message.add(new TextElement(commandBase, ChatColor.YELLOW));
            message.add(new TextElement(f.getName()+" ", ChatColor.GREEN));
            if(f.getArguments() != null) {
                for(int x = 0; x < f.getArguments().length; x++) {
                    String argument = f.getArguments()[x];
                    if(argument.startsWith("<")) {
                        message.add(new TextElement(argument+" ", ChatColor.RED));
                    } else {
                        message.add(new TextElement(argument+" ", ChatColor.GRAY));
                    }
                }
            }
            message.add(new TextElement("- ", ChatColor.GRAY));
            message.add(new TextElement(f.getPurpose(), ChatColor.BLUE));

            sender.sendMessage(ChatHelper.makeTextComponent(message.toArray(new TextElement[message.size()])));
        }
    }
}
