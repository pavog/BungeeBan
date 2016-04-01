package de.vincidev.bungeeban.events;

import net.md_5.bungee.api.plugin.Event;

public class BungeeMuteIpEvent extends Event {

    private String ip;
    private long muteEnd;
    private String muteReason;
    private String mutedBy;

    public BungeeMuteIpEvent(String ip, long muteEnd, String muteReason, String mutedBy) {
        this.ip = ip;
        this.muteEnd = muteEnd;
        this.muteReason = muteReason;
        this.mutedBy = mutedBy;
    }

    public long getmuteEnd() {
        return muteEnd;
    }

    public String getmutedBy() {
        return mutedBy;
    }

    public String getmuteReason() {
        return muteReason;
    }

    public String getIp() {
        return ip;
    }

}
