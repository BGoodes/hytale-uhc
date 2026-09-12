package fr.bgoodes.uhc.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.util.BroadcastUtil;
import org.jetbrains.annotations.NotNull;

public class UHCCommand extends AbstractCommandCollection {

    public UHCCommand() {
        super("uhc", "UHC management commands");

        this.requireNoPermission();

        this.addSubCommand(new HostCommand("host", "Configure the UHC"));
        this.addSubCommand(new TestCommand("test", "Test command"));
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

        }
    }
}
