package de.vincidev.bungeeban.util;

import de.vincidev.bungeeban.BungeeBan;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerUtil {

    private static HashMap<String, UUID> uuidCache = new HashMap<>();
    private static HashMap<UUID, String> playernameCache = new HashMap<>();

    public static UUID getUniqueId(String playername) {
        if(uuidCache.containsKey(playername)) {
            return uuidCache.get(playername);
        }
        try {
            URLConnection conn = new URL("https://" + BungeeBan.getConfigManager().getString("api") + ".mc-api.net/v3/uuid/" + playername).openConnection();
            String response = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(br.ready()) {
                response = response + br.readLine();
            }
            JSONObject json = new JSONObject(response);
            UUID uuid = UUID.fromString(json.getString("full_uuid"));
            uuidCache.put(playername, uuid);
            return uuid;
        } catch (Exception e) {
        }
        return null;
    }

    public static String getUUID(String playername) {
        UUID uuid = getUniqueId(playername);
        if(uuid != null) {
            return uuid.toString();
        }
        return null;
    }

    public static String getPlayername(UUID uuid) {
        if(playernameCache.containsKey(uuid)) {
            return playernameCache.get(uuid);
        }
        try {
            URLConnection conn = new URL("https://" + BungeeBan.getConfigManager().getString("api") + ".mc-api.net/v3/name/" + uuid.toString()).openConnection();
            String response = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(br.ready()) {
                response = response + br.readLine();
            }
            JSONObject json = new JSONObject(response);
            String name = json.getString("name");
            playernameCache.put(uuid, name);
            return name;
        } catch (Exception e) {
        }
        return null;
    }

    public static String getPlayername(String uuid) {
        return getPlayername(UUID.fromString(uuid));
    }

    public static HashMap<Long, String> getNameHistory(UUID uuid) {
        try {
            URLConnection conn = new URL("https://" + BungeeBan.getConfigManager().getString("api") + ".mc-api.net/v3/history/" + uuid.toString()).openConnection();
            String response = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(br.ready()) {
                response = response + br.readLine();
            }
            JSONArray arr = new JSONObject(response).getJSONArray("history");
            HashMap<Long, String> history = new HashMap<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject change = arr.getJSONObject(i);
                String name = change.getString("name");
                long changedAt = 0L;
                if(change.has("changedToAt")) {
                    changedAt = change.getLong("changedToAt");
                }
                history.put(changedAt, name);
            }
            return history;
        } catch (Exception e) {
        }
        return null;
    }

}
