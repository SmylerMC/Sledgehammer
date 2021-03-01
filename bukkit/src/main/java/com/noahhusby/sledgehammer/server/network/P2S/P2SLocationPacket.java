/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - P2SLocationPacket.java
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

package com.noahhusby.sledgehammer.server.network.P2S;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.network.P2SPacket;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import net.buildtheearth.terraplusplus.TerraConstants;
import net.buildtheearth.terraplusplus.control.TerraTeleport;
import net.buildtheearth.terraplusplus.dataset.IScalarDataset;
import net.buildtheearth.terraplusplus.dep.net.daporkchop.lib.common.ref.Ref;
import net.buildtheearth.terraplusplus.generator.EarthGeneratorPipelines;
import net.buildtheearth.terraplusplus.generator.EarthGeneratorSettings;
import net.buildtheearth.terraplusplus.generator.GeneratorDatasets;
import net.buildtheearth.terraplusplus.projection.OutOfProjectionBoundsException;
import net.buildtheearth.terraplusplus.projection.dymaxion.BTEDymaxionProjection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class P2SLocationPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.locationID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        Player player = SledgehammerUtil.getPlayerFromName(info.getSender());
        if(player == null) {
            throwNoSender();
            return;
        }

        String lat = data.get("lat").getAsString();
        String lon = data.get("lon").getAsString();

        int xOffset = 0;
        int zOffset = 0;

        try {
            xOffset = Integer.parseInt(data.get("xOffset").getAsString());
            zOffset = Integer.parseInt(data.get("zOffset").getAsString());
        } catch (Exception ignored) { }

        double proj[] = SledgehammerUtil.fromGeo(Double.parseDouble(lon), Double.parseDouble(lat));

        int x = (int) Math.floor(proj[0]) + xOffset;
        int z = (int) Math.floor(proj[1]) + zOffset;

        int altitude = 0;

        if(SledgehammerUtil.hasTerraPlusPlus()) {
            double[] adjustedProj = SledgehammerUtil.toGeo(x, z);
            double adjustedLon = adjustedProj[0];
            double adjustedLat = adjustedProj[1];
            GeneratorDatasets datasets = new GeneratorDatasets(SledgehammerUtil.getBTEDefaultSettings());
            CompletableFuture<Double> altFuture;
            try {
               altFuture = datasets.<IScalarDataset>getCustom(EarthGeneratorPipelines.KEY_DATASET_HEIGHTS)
                        .getAsync(adjustedLon, adjustedLat)
                        .thenApply(a -> a + 1.0d);
            } catch (OutOfProjectionBoundsException e) {
                altFuture = CompletableFuture.completedFuture(0.0);
            }
            altFuture.thenAccept(a -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),String.format("minecraft:tp %s %s %s %s", player.getName(), x, altitude, z)));
        } else {
            int y = Constants.scanHeight;

            while(player.getWorld().getBlockAt(x, y, z).getType() != Material.AIR) {
                y += Constants.scanHeight;
            }

            while(player.getWorld().getBlockAt(x, y, z).getType() == Material.AIR) {
                y -= 1;
            }

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),String.format("minecraft:tp %s %s %s %s", player.getName(), x, y+1, z));
        }
    }
}