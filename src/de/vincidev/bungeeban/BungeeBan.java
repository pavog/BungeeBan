package de.vincidev.bungeeban;

import de.vincidev.bungeeban.util.ConfigManager;
import de.vincidev.bungeeban.util.SQL;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBan extends Plugin {

    public static String PREFIX = "", CONSOLE_PREFIX = "";

    private static BungeeBan instance;
    private static SQL sql;
    private static ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();
        sql = new SQL("bungeeban.db");
        sql.openConnection();
        sql.createTableIfNotExists("BungeeBan_Bans", "UUID VARCHAR(100), BanEnd LONG, BanReason VARCHAR(256), BannedBy VARCHAR(100)");
        sql.createTableIfNotExists("BungeeBan_IPBans", "IP VARCHAR(100), BanEnd LONG, BanReason VARCHAR(256), BannedBy VARCHAR(100)");
        sql.createTableIfNotExists("BungeeBan_Mutes", "UUID VARCHAR(100), MuteEnd LONG, MuteReason VARCHAR(256), MutedBy VARCHAR(100)");
        sql.createTableIfNotExists("BungeeBan_IPMutes", "IP VARCHAR(100), MuteEnd LONG, MuteReason VARCHAR(256), MutedBy VARCHAR(100)");
    }

    @Override
    public void onDisable() {
        sql.closeConnection();
    }

    public static BungeeBan getInstance() {
        return instance;
    }

    public static SQL getSQL() {
        return sql;
    }

    public static void setSQL(SQL sql) {
        BungeeBan.sql = sql;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
