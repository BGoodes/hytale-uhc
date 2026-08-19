package fr.bgoodes.uhc.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.state.impl.StartingState;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class UHCCommand extends AbstractCommandCollection {

    public UHCCommand() {
        super("uhc", "UHC management commands");

        addSubCommand(new StartCommand("start", "Start the UHC"));
    }

    private static class StartCommand extends AbstractAsyncCommand {

        private boolean started = false;

        public StartCommand(@NotNull String name, @NotNull String description) {
            super(name, description);
        }

        @Override
        protected @NotNull CompletableFuture<Void> executeAsync(@NotNull CommandContext commandContext) {

            if (!started) {
                started = true;
                World world = UHC.get().getGameContext().getWorld();
                world.execute(() -> {
                    UHC.get().getStateService().changeGameState(StartingState.STATE_ID);
                });
            }


            return CompletableFuture.completedFuture(null);
        }
    }
}
