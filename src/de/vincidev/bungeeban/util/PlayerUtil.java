package de.vincidev.bungeeban.util;

import de.vincidev.bungeeban.BungeeBan;
import net.md_5.bungee.BungeeCord;
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
            URLConnection conn = new URL("https://mcapi.ca/profile/" + playername).openConnection();
            final StringBuilder response = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while (br.ready() && ((line = br.readLine()) != null)) {
                response.append(line);
            }

            final JSONObject profile = new JSONObject(response.toString());
            if (profile.getString("uuid") != null) {
                final UUID uuid = UUID.fromString(profile.getString("uuid"));
                uuidCache.put(playername, uuid);
                return uuid;
            }
        } catch (Exception localException) {
            BungeeBan.getInstance().getLogger().warning("Could not read from api!");
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
        try {
            URLConnection conn = new URL("https://mcapi.ca/profile/" + uuid.toString()).openConnection();
            final StringBuilder response = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while (br.ready() && ((line = br.readLine()) != null)) {
                response.append(line);
            }

            final JSONObject profile = new JSONObject(response.toString());
            if (profile.getString("name") != null) {
                playernameCache.put(uuid, profile.getString("name"));
                return profile.getString("name");
            }
        } catch (Exception localException) {
            BungeeBan.getInstance().getLogger().warning("Could not read from api!");
        }
        return null;
    }

}
