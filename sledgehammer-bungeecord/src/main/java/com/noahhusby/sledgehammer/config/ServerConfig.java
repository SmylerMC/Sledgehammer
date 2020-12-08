/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerConfig.java
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

package com.noahhusby.sledgehammer.config;

import com.google.common.collect.Maps;
import com.noahhusby.lib.data.storage.StorageList;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.network.P2S.P2SInitializationPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServerConfig {
    private static ServerConfig instance;

    public static ServerConfig getInstance() {
        if(instance == null) instance = new ServerConfig();
        return instance;
    }

    private StorageList<SledgehammerServer> servers = new StorageList<>(SledgehammerServer.class);
    private StorageList<ServerGroup> groups = new StorageList<>(ServerGroup.class);
    public Map<String, String> initializedServers = Maps.newHashMap();

    /**
     * Gets all registered {@link SledgehammerServer}
     * @return List of {@link SledgehammerServer}
     */
    public StorageList<SledgehammerServer> getServers() {
        return servers;
    }

    /**
     * Initialize a sledgehammer server
     * @param serverInfo {@link ServerInfo}
     * @param data Incoming data from init packet
     */
    public void initialize(ServerInfo serverInfo, JSONObject data) {
        String version = (String) data.get("version");
        String name = serverInfo.getName();
        initializedServers.remove(name);
        initializedServers.put(serverInfo.getName(), version);
        SledgehammerServer s = getServer(name);
        if(s != null) {
            s.initialize(version);
        }
    }

    /**
     * Returns a list of Bungeecord servers
     * @return {@link LinkedList<ServerInfo>}
     */
    public LinkedList<ServerInfo> getBungeeServers() {
        LinkedList<ServerInfo> bungeeServers = new LinkedList<>();

        Map<String, ServerInfo> serversTemp = ProxyServer.getInstance().getServers();
        for(Map.Entry<String, ServerInfo> s : serversTemp.entrySet()) {
            bungeeServers.add(s.getValue());
        }

        return bungeeServers;
    }

    /**
     * Updates and saves {@link SledgehammerServer} to storage
     * @param server {@link SledgehammerServer}
     */
    public void pushServer(SledgehammerServer server) {
        servers.removeIf(s -> s.getName().equalsIgnoreCase(server.getName()));
        servers.add(server);
        servers.save();
    }

    /**
     * Removes a server from memory and storage
     * @param server {@link SledgehammerServer}
     */
    public void removeServer(SledgehammerServer server) {
        servers.remove(server);
        servers.save();
    }

    /**
     * Gets {@link SledgehammerServer} by name
     * @param name Name of server
     * @return {@link SledgehammerServer}
     */
    public SledgehammerServer getServer(String name) {
        for(SledgehammerServer s : servers) {
            if(s.getName().equalsIgnoreCase(name)) return s;

        }

        return null;
    }

    /**
     * Gets list of {@link Location} from {@link SledgehammerServer}
     * @param server Name of server
     * @return {@link ArrayList<Location>}
     */
    public List<Location> getLocationsFromServer(String server) {
        for(SledgehammerServer s : servers)
            if(s.getName().equalsIgnoreCase(server)) return s.getLocations();

        return null;
    }

    public StorageList<ServerGroup> getGroups() {
        return groups;
    }

    /**
     * Checks for pre-initialized servers and updates the SledgehammerServer object
     */
    public void checkReadyServers() {
        for(SledgehammerServer server : servers) {
            if(server.isInitialized() || server.getServerInfo().getPlayers().isEmpty()) continue;
            for(String s : initializedServers.keySet()) {
                if(s.equalsIgnoreCase(server.getName())) {
                    server.initialize(initializedServers.get(s));
                    return;
                }
            }
        }
    }

    /**
     * Sends initialization packet on join
     * @param e {@link net.md_5.bungee.api.event.ServerConnectedEvent}
     */
    public void onServerJoin(ServerConnectedEvent e) {
        SledgehammerServer s = getServer(e.getServer().getInfo().getName());
        if(s == null) return;
        if(s.isInitialized() || initializedServers.containsKey(s.getName())) return;
        SledgehammerNetworkManager.getInstance().send(new P2SInitializationPacket(e.getPlayer().getName(), e.getServer().getInfo().getName()));
    }
}
