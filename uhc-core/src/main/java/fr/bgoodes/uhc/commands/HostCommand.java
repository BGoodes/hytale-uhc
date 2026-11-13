package fr.bgoodes.uhc.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.state.impl.StartingState;
import fr.bgoodes.uhc.utils.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class HostCommand extends AbstractCommandCollection {

    public HostCommand(String name, String description) {
        super(name, description);

        this.requirePermission("uhc.host.claim");

        this.addSubCommand(new StartCommand("start", "Start the UHC"));
    }

    private static class StartCommand extends AbstractAsyncCommand {

        private boolean started = false;

        public StartCommand(String name, String description) {
            super(name, description);

            this.requirePermission("uhc.host.start");
        }

        @Override
        protected @NotNull CompletableFuture<Void> executeAsync(@NotNull CommandContext context) {

            if (!started) {
                started = true;
                UHC.get().getStateService().changeGameState(StartingState.STATE_ID);

            } else {
                UHCMessageUtil.reply(context, UHCMessageUtil.tr("uhc.host.start.failure").color(Color.red));
            }


            return CompletableFuture.completedFuture(null);
        }
    }
}
