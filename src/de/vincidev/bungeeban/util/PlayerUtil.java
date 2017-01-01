package de.vincidev.bungeeban.util;

import de.vincidev.bungeeban.BungeeBan;
import net.md_5.bungee.BungeeCord;
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
        if (uuidCache.containsKey(playername)) {
            return uuidCache.get(playername);
        }
        if (BungeeCord.getInstance().getPlayer(playername) != null) {
            UUID uuid = BungeeCord.getInstance().getPlayer(playername).getUniqueId();
            uuidCache.put(playername, uuid);
            return uuid;
        }
        try {
            final URLConnection conn = new URL("https://api.mojang.com/users/profiles/minecraft/" + playername).openConnection();
            final StringBuilder response = new StringBuilder();
            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while (br.ready() && ((line = br.readLine()) != null)) {
                response.append(line);
            }
            br.close();

            final JSONObject profile = new JSONObject(response.toString());
            if (profile.get("id") != null && profile.get("name") != null) {
                String hexStringWithoutHyphens = profile.get("id").toString();
                // Use regex to format the hex string by inserting hyphens in the canonical format: 8-4-4-4-12
                String hexStringWithInsertedHyphens = hexStringWithoutHyphens.replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");
                final UUID uuid = UUID.fromString(hexStringWithInsertedHyphens);
                uuidCache.put(playername, uuid);
                uuidCache.put(profile.get("name").toString(), uuid);
                playernameCache.put(uuid, profile.get("name").toString());
                return uuid;
            } else {
                BungeeBan.getInstance().getLogger().warning("Error from api: " + response.toString());
            }
        } catch (Exception localException) {
            BungeeBan.getInstance().getLogger().warning("Could not read player uuid from api!");
            localException.printStackTrace();
        }
        return null;
    }

    public static String getPlayerName(UUID uuid) {
        if (playernameCache.containsKey(uuid)) {
            return playernameCache.get(uuid);
        }
        if (BungeeCord.getInstance().getPlayer(uuid) != null) {
            String name = BungeeCord.getInstance().getPlayer(uuid).getName();
            playernameCache.put(uuid, name);
            return name;
        }
        String responseText = "";
        try {
            final URLConnection conn = new URL("https://api.mojang.com/user/profiles/" + uuid.toString() + "/names").openConnection();
            final StringBuilder response = new StringBuilder();
            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while (br.ready() && ((line = br.readLine()) != null)) {
                response.append(line);
            }
            br.close();

            responseText = response.toString();

            final JSONArray profile = new JSONArray(response.toString());
            if (profile.get(profile.length() - 1) != null || profile.getJSONObject(profile.length() - 1) != null) {
                final JSONObject lastObj = profile.getJSONObject(profile.length() - 1);
                playernameCache.put(uuid, lastObj.getString("name"));
                if (!uuidCache.containsKey(lastObj.getString("name")))
                    uuidCache.put(lastObj.getString("name"), uuid);
                return lastObj.getString("name");
            } else {
                BungeeBan.getInstance().getLogger().warning("Error from api: " + response.toString());
            }
        } catch (Exception localException) {
            BungeeBan.getInstance().getLogger().warning("Could not read player name from api!");
            BungeeBan.getInstance().getLogger().warning(responseText);
            localException.printStackTrace();
        }
        return null;
    }

}
