package de.vincidev.bungeeban.events;

import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class BungeeUnmuteEvent extends Event {

    private UUID unmuted;
    private String unmutedBy;

    public BungeeUnmuteEvent(UUID unmuted, String unmutedBy) {
        this.unmuted = unmuted;
        this.unmutedBy = unmutedBy;
    }

    public String getUnmutedBy() {
        return unmutedBy;
    }

    public UUID getUnmuted() {
        return unmuted;
    }
}
