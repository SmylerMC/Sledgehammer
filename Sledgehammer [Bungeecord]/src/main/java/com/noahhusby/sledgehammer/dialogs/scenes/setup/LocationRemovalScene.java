/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationRemovalScene.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.dialogs.components.location.LocationRemovalComponent;
import com.noahhusby.sledgehammer.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

public class LocationRemovalScene extends DialogScene {

    private ServerInfo server;
    private DialogScene scene;

    public LocationRemovalScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new LocationRemovalComponent(server));
    }

    @Override
    public TextElement[] getTitle() {
        return new TextElement[]{new TextElement("Editing Locations - ", ChatColor.GRAY),
        new TextElement(server.getName(), ChatColor.RED)};
    }

    @Override
    public void onFinish() {
        List<Location> locations = ServerConfig.getInstance().getLocationsFromServer(server.getName());
        List<Location> newLocations = ServerConfig.getInstance().getLocationsFromServer(server.getName());
        newLocations.remove(locations.get(Integer.parseInt(getValue("locationremove"))));

        Server s = ServerConfig.getInstance().getServer(server.getName());
        s.locations = newLocations;

        ServerConfig.getInstance().pushServer(s);
        if(scene != null) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), scene);
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}