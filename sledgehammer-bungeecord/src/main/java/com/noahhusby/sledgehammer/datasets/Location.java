/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Location.java
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

package com.noahhusby.sledgehammer.datasets;

import com.noahhusby.lib.data.storage.Storable;
import org.json.simple.JSONObject;

public class Location implements Storable {
    public detail detailType;
    public String city = "";
    public String county = "";
    public String state = "";
    public String country = "";

    public Location() {}

    public Location(detail detailType, String city, String county, String state, String country) {
        this.detailType = detailType;
        if(city != null) this.city = city.toLowerCase();
        if(county != null) this.county = county.toLowerCase();
        if(state != null) this.state = state.toLowerCase();
        if(country != null) this.country = country.toLowerCase();
    }

    @Override
    public Storable load(JSONObject data) {
        return new Location(detail.valueOf((String) data.get("detailType")), (String)  data.get("city"),
                (String) data.get("county"), (String) data.get("state"), (String) data.get("country"));
    }

    @Override
    public JSONObject save(JSONObject data) {
        data.put("detailType", detailType.name());
        data.put("city", city);
        data.put("county", county);
        data.put("state", state);
        data.put("country", country);
        return data;
    }

    public enum detail {
        none, city, county, state, country
    }
}
