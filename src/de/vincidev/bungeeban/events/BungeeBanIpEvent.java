package de.vincidev.bungeeban.events;

import net.md_5.bungee.api.plugin.Event;

public class BungeeBanIpEvent extends Event {

    private String ip;
    private long banEnd;
    private String banReason;
    private String bannedBy;

    public BungeeBanIpEvent(String ip, long banEnd, String banReason, String bannedBy) {
        this.ip = ip;
        this.banEnd = banEnd;
        this.banReason = banReason;
        this.bannedBy = bannedBy;
    }

    public long getBanEnd() {
        return banEnd;
    }

    public String getBannedBy() {
        return bannedBy;
    }

    public String getBanReason() {
        return banReason;
    }

    public String getIp() {
        return ip;
    }
}
