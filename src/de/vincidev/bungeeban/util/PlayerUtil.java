package de.vincidev.bungeeban.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.vincidev.bungeeban.BungeeBan;
import net.md_5.bungee.BungeeCord;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerUtil {

    private final static String API_URL_BASE = "https://#REGION#.mc-api.net/v3/#REQUEST#";

    private static Map<UUID, Map<RequestType, Object>> cache = new HashMap<UUID, Map<RequestType, Object>>();

    /**
     * Get the Unique Id of the player
     *
     * @param name the name of the player
     * @return the Unique Id of the player
     */
    public static UUID getUniqueId(String name) {
        if(cacheRetrieve(name, RequestType.UUID) != null) {
            return UUID.fromString((String) cacheRetrieve(name, RequestType.UUID));
        }
        if(BungeeCord.getInstance().getPlayer(name) != null) {
            UUID uuid = BungeeCord.getInstance().getPlayer(name).getUniqueId();
            cacheStore(uuid, RequestType.UUID, name);
            return uuid;
        }
        JsonObject json = requestAPI(name, RequestType.UUID);
        if(json.get("full_uuid") != null) {
            UUID uuid = UUID.fromString(json.get("full_uuid").toString());
            cacheStore(uuid, RequestType.UUID, name);
            return uuid;
        }
        return null;
    }

    /**
     * Get the Unique Id of a player as a string
     *
     * @param name the name of the player
     * @return the Unique Id of the player as a string
     */
    public static String getUniqueIdAsString(String name) {
        UUID uuid = getUniqueId(name);
        if(uuid != null) {
            return uuid.toString();
        }
        return null;
    }

    /**
     * Get the name of a player from their Unique Id
     *
     * @param uuid the unique id of the player
     * @return the name of the player
     */
    public static String getPlayerName(UUID uuid) {
        if(cacheRetrieve(uuid.toString(), RequestType.NAME) != null) {
            return (String) cacheRetrieve(uuid, RequestType.NAME);
        }
        if(BungeeCord.getInstance().getPlayer(uuid) != null) {
            String name = BungeeCord.getInstance().getPlayer(uuid).getName();
            cacheStore(uuid, RequestType.NAME, name);
            return name;
        }
        JsonObject json = requestAPI(uuid.toString(), RequestType.NAME);
        if(json.get("name") != null) {
            String name = json.get("name").toString();
            cacheStore(uuid, RequestType.NAME, name);
            return name;
        }
        return null;
    }

    /**
     * Get the name of a player from their Unique Id
     *
     * @param uuid the Unique Id of the player
     * @return the name of the player
     */
    public static String getPlayerName(String uuid) {
        return getPlayerName(UUID.fromString(uuid));
    }

    public static Map<Long, String> getNameHistory(UUID uuid) {
        Map<Long, String> cache = (Map) cacheRetrieve(uuid, RequestType.HISTORY);
        if(cache != null) {
            return cache;
        }
        JsonObject json = requestAPI(uuid.toString(), RequestType.HISTORY);
        if(json.get("error") != null) {
            return null;
        }
        JsonArray namesJson = json.getAsJsonArray("history");
        HashMap<Long, String> history = new HashMap<Long, String>();
        for(JsonElement element : namesJson) {
            JsonObject obj = element.getAsJsonObject();
            if(obj.get("changedToAt") != null) {
                history.put(Long.parseLong(obj.get("changedToAt").toString()), obj.get("name").toString());
            } else {
                history.put(0L, obj.get("name").toString());
            }
        }
        cacheStore(uuid, RequestType.HISTORY, history);
        return history;
    }

    /**
     * Get the name of a player from their Unique Id
     *
     * @param uuid the Unique Id of the player
     * @return the name of the player
     */
    public static Map<Long, String> getNameHistory(String uuid) { return getNameHistory(UUID.fromString(uuid)); }

    /**
     * Store data in the cache
     * @param uuid the Unique Id of the player
     * @param type the type of data being stored
     * @param store the data object
     */
    private static void cacheStore(UUID uuid, RequestType type, Object store) {
        if(cache.containsKey(uuid)) {
            Map<RequestType, Object> map = cache.get(uuid);
            map.remove(type);
            map.put(type, store);
            return;
        }
        Map<RequestType, Object> map = new HashMap<RequestType, Object>();
        map.put(type, store);
        cache.put(uuid, map);
    }

    /**
     * Retrieve the Name or History of a player from the cache
     *
     * @param uuid the Unique Id of the player
     * @param type the RequestType
     * @return the result of the request
     */
    private static Object cacheRetrieve(UUID uuid, RequestType type) {
        if(cache.containsKey(uuid)) {
            Map<RequestType, Object> map = cache.get(uuid);
            return map.get(type);
        }
        return null;
    }

    /**
     * Retrieve the Unique Id or History of a player from the cache
     *
     * @param name the name of the player
     * @param type the RequestType
     * @return the result of the request
     */
    private static Object cacheRetrieve(String name, RequestType type) {
        for(UUID uuid : cache.keySet()) {
            Map<RequestType, Object> map = cache.get(uuid);
            if(map.containsKey(RequestType.NAME) && ((String) map.get(RequestType.NAME)).toLowerCase().equals(name.toLowerCase())){
                return map.get(type);
            }
        }
        return null;
    }

    /**
     * Connect to the API and get the response for the request type and variable
     *
     * @param variable the UUID or Name of the player
     * @param type the RequestType
     * @return a JsonObject the API returns
     */
    public static JsonObject requestAPI(String variable, RequestType type) {
        JsonObject json = new JsonObject();
        try {
            String url = API_URL_BASE
                    .replace("#REGION#", BungeeBan.getConfigManager().getString("api"))
                    .replace("#REQUEST#", type.toString())
                    + "/"
                    + variable;
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            json = new JsonParser().parse(new InputStreamReader((InputStream) connection.getContent())).getAsJsonObject();
        } catch (Exception ex) {
            json.addProperty("error", ex.getMessage());
        }
        return json;
    }

}

enum RequestType {
    UUID("uuid"),
    NAME("name"),
    HISTORY("history");

    private final String name;

    RequestType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
