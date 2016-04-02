package de.vincidev.bungeeban.commands;

import de.vincidev.bungeeban.BungeeBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class TempmuteCommand extends Command {

    public TempmuteCommand(String name) {
        super(name);
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        BungeeCord.getInstance().getScheduler().runAsync(BungeeBan.getInstance(), new Runnable() {
            @Override
            public void run() {

            }
        });
    }

}
