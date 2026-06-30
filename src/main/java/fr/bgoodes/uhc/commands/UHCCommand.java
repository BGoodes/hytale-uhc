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

        addSubCommand(new TestCommand("test", "Test command"));
        addSubCommand(new StartCommand("start", "Start the UHC"));
    }

    private static class TestCommand extends AbstractPlayerCommand {

        public TestCommand(@NotNull String name, @NotNull String description) {
            super(name, description);
        }

        @Override
        protected void execute(
                @NotNull CommandContext commandContext,
                @NotNull Store<EntityStore> store,
                @NotNull Ref<EntityStore> ref,
                @NotNull PlayerRef playerRef,
                @NotNull World world
        ) {

            int border = 500; // +- border
            int height = 1024;
            Vector3f color = new Vector3f(0.5f, 0.85f, 1.0f); //sky

            Matrix4f transform = new Matrix4f()
                    .translation(0, height/2f, 0)
                    .scale(border*2, height, border*2);

            float[] matrix = new float[16];
            transform.get(matrix);

            TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());
            if (transformComponent != null) {
                Vector3d position = transformComponent.getPosition();

                boolean inside = Math.abs(position.x) <= border && Math.abs(position.z) <= border;

                if (!inside) {
                    color = new Vector3f(1, 0, 0); // red
                }
            }

            DisplayDebug displayDebug = new DisplayDebug(
                    DebugShape.Cube,
                    matrix,
                    color,
                    10.0f,                      //10s
                    (byte)0,
                    null,
                    0.2f                             //opacity
            );

            playerRef.getPacketHandler().write(displayDebug);
        }
    }

    // should it be async?
    private static class StartCommand extends AbstractAsyncCommand {

        public StartCommand(@NotNull String name, @NotNull String description) {
            super(name, description);
        }

        @Override
        protected @NotNull CompletableFuture<Void> executeAsync(@NotNull CommandContext commandContext) {

            World world = UHC.get().getGameContext().getWorld();
            world.execute(() -> {
                UHC.get().getStateService().changeGameState(StartingState.STATE_ID);
            });

            return CompletableFuture.completedFuture(null);
        }
    }
}
