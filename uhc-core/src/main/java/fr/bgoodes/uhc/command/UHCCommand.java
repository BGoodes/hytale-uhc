package fr.bgoodes.uhc.command;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.page.team.TeamPage;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCTeamUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class UHCCommand extends AbstractCommandCollection {

    public UHCCommand() {
        super("uhc", "UHC gamemode commands");

        this.requireNoPermission();

        this.addSubCommand(new HostCommand("host", "Configure the UHC"));
        this.addSubCommand(new TeamCommand("team", "Choose your team"));
        this.addSubCommand(new TestCommand("test", "Test command"));
    }

    private static class TeamCommand extends AbstractPlayerCommand {

        public TeamCommand(@NotNull String name, @NotNull String description) {
            super(name, description);

            this.requireNoPermission();
        }

        @Override
        protected void execute(
                @NotNull CommandContext commandContext,
                @NotNull Store<EntityStore> store,
                @NotNull Ref<EntityStore> ref,
                @NotNull PlayerRef playerRef,
                @NotNull World world
        ) {
            if (!UHCTeamUtil.isChoiceOpen()) {
                UHCMessageUtil.send(playerRef, UHCMessageUtil.tr("uhc.command.team.closed").color(Color.RED));
                return;
            }

            Player player = store.getComponent(ref, Player.getComponentType());

            if (player != null) {
                player.getPageManager().openCustomPage(ref, store, new TeamPage(playerRef));
            }
        }
    }

    private static class TestCommand extends AbstractPlayerCommand {

        public TestCommand(String name, String description) {
            super(name, description);
        }

        @Override
        protected void execute(
                @NotNull CommandContext commandContext,
                @NotNull Store<EntityStore> store,
                @NotNull Ref<EntityStore> ref,
                @NotNull PlayerRef playerRef,
                @NotNull World world)
        {

        }
    }
}
