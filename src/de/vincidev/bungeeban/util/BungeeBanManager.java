package de.vincidev.bungeeban.util;

import de.vincidev.bungeeban.BungeeBan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BungeeBanManager {

    public static boolean isBanned(UUID uuid) {
        ResultSet rs = BungeeBan.getSQL().getResult("SELECT * FROM BungeeBan_Bans WHERE UUID='" + uuid.toString() + "'");
        try {
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isIPBanned(String ip) {
        ResultSet rs = BungeeBan.getSQL().getResult("SELECT * FROM BungeeBan_IPBans WHERE IP='" + ip + "'");
        try {
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void unban(UUID uuid) {
        BungeeBan.getSQL().update("DELETE FROM BungeeBan_Bans WHERE UUID='" + uuid.toString() + "'");
    }

    public static void unbanIP(String ip) {
        BungeeBan.getSQL().update("DELETE FROM BungeeBan_IP Bans WHERE IP='" + ip + "'");
    }

    public static void ban(UUID uuid, long seconds, String banReason, String bannedBy) {
        if(!isBanned(uuid)) {
            long end = -1L;
            if(seconds > 0) {
                end = System.currentTimeMillis() + (seconds*1000);
            }
            BungeeBan.getSQL().update("INSERT INTO BungeeBan_Bans(UUID, BanEnd, BanReason, BannedBy) " +
                    "VALUES('" + uuid.toString() + "', '" + end + "', '" + banReason + "', '" + bannedBy + "')");
        }
    }

}
