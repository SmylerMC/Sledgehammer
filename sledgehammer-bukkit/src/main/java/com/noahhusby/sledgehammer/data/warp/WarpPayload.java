package com.noahhusby.sledgehammer.data.warp;

import com.noahhusby.sledgehammer.SmartObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WarpPayload {
    private List<WarpGroup> groups;
    private String defaultPage;
    private String requestGroup;
    private boolean editAccess;
    private boolean local;

    public WarpPayload(String defaultPage, String requestGroup, boolean editAccess, boolean local, List<WarpGroup> groups) {
        this.groups = groups;
        this.defaultPage = defaultPage;
        this.requestGroup = requestGroup;
        this.editAccess = editAccess;
        this.local = local;
    }

    public String getDefaultPage() {
        return defaultPage;
    }

    public String getRequestGroup() {
        return requestGroup;
    }

    public boolean isEditAccess() {
        return editAccess;
    }

    public boolean isLocal() {
        return local;
    }

    public List<WarpGroup> getGroups() {
        return groups;
    }

    public static WarpPayload fromPayload(SmartObject object) {
        JSONArray groups = (JSONArray) object.get("groups");
        List<WarpGroup> warpGroups = new ArrayList<>();
        for(Object o : groups)
            warpGroups.add(WarpGroup.fromJson((JSONObject) o));

        return new WarpPayload(object.getString(), )
    }
}
