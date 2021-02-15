/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GUIActionTracker.java
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

package com.noahhusby.sledgehammer.gui;

import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class GUIActionTracker {
    @Getter private final SledgehammerPlayer player;
    @Getter private final String salt;
}
