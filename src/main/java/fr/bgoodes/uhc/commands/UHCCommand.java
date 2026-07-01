package fr.bgoodes.uhc.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.DebugShape;
import com.hypixel.hytale.protocol.packets.player.DisplayDebug;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.state.impl.StartingState;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

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
