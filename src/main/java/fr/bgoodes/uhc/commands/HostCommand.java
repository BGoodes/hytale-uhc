package fr.bgoodes.uhc.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.permission.PermissionService;
import fr.bgoodes.uhc.service.state.impl.StartingState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class HostCommand extends AbstractCommandCollection {

    public HostCommand(@NotNull String name, @NotNull String description) {
        super(name, description);

        this.requirePermission("uhc.host.claim");

        this.addSubCommand(new ClaimCommand("claim", "Claim the UHC as host"));
        this.addSubCommand(new StartCommand("start", "Start the UHC"));
    }

    private static class ClaimCommand extends AbstractPlayerCommand {

        private final PermissionService permissionService = UHC.get().getPermissionService();

        public ClaimCommand(@NotNull String name, @NotNull String description) {
            super(name, description);

            this.requirePermission("uhc.host.claim");
        }

        @Override
        protected void execute(
                @NotNull CommandContext commandContext,
                @NotNull Store<EntityStore> store,
                @NotNull Ref<EntityStore> ref,
                @NotNull PlayerRef playerRef,
                @NotNull World world
        ) {
            if (!permissionService.hasHost()) {
                permissionService.setHost(playerRef.getUuid());
                playerRef.sendMessage(Message.raw("Vous êtes désormais l'host de la partie.").color(Color.magenta));
            } else {
                playerRef.sendMessage(Message.raw("Il y a déjà un host dans la partie.").color(Color.red));
            }
        }
    }

    private static class StartCommand extends AbstractAsyncCommand {

        private boolean started = false;

        public StartCommand(@NotNull String name, @NotNull String description) {
            super(name, description);

            this.requirePermission("uhc.host.start");
        }

        @Override
        protected @NotNull CompletableFuture<Void> executeAsync(@NotNull CommandContext context) {

            if (!started) {
                started = true;
                World world = UHC.get().getGameContext().getWorld();
                world.execute(() -> {
                    UHC.get().getStateService().changeGameState(StartingState.STATE_ID);
                });
            } else {
                context.sendMessage(Message.raw("La partie est déjà lancé.").color(Color.red));
            }


            return CompletableFuture.completedFuture(null);
        }
    }
}
