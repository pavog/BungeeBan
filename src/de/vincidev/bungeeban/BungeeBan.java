package de.vincidev.bungeeban;

import de.vincidev.bungeeban.util.SQLite;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBan extends Plugin {

    public static String PREFIX = "", CONSOLE_PREFIX = "";

    private static BungeeBan instance;
    private static SQLite sqlite;

    @Override
    public void onEnable() {
        instance = this;
        sqlite = new SQLite("bungeeban.db");
        sqlite.openConnection();
        sqlite.createTableIfNotExists("BungeeBan_Bans", "UUID VARCHAR(100), BanEnd LONG, BanReason VARCHAR(256), BannedBy VARCHAR(100)");
        sqlite.createTableIfNotExists("BungeeBan_IPBans", "IP VARCHAR(100), BanEnd LONG, BanReason VARCHAR(256), BannedBy VARCHAR(100)");
        sqlite.createTableIfNotExists("BungeeBan_Mutes", "UUID VARCHAR(100), MuteEnd LONG, MuteReason VARCHAR(256), MutedBy VARCHAR(100)");
        sqlite.createTableIfNotExists("BungeeBan_IPMutes", "IP VARCHAR(100), MuteEnd LONG, MuteReason VARCHAR(256), MutedBy VARCHAR(100)");
    }

    @Override
    public void onDisable() {
        sqlite.closeConnection();
    }

    public static BungeeBan getInstance() {
        return instance;
    }

    public static SQLite getSqlite() {
        return sqlite;
    }

}
