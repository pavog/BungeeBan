package de.vincidev.bungeeban.events;

import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class BungeeMuteEvent extends Event {

    private UUID muted;
    private long muteEnd;
    private String muteReason;
    private String mutedBy;

    public BungeeMuteEvent(UUID muted, long muteEnd, String muteReason, String mutedBy) {
        this.muted = muted;
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

    public UUID getmuted() {
        return muted;
    }
}
