package fr.bgoodes.uhc.service.hud;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class HUDService {

    public void addHUDToPlayer(@NotNull PlayerRef playerRef, @NotNull Player player) {
        player.getHudManager().addCustomHud(playerRef, new TimerHUD(playerRef));
        player.getHudManager().addCustomHud(playerRef, new TitleHUD(playerRef));
    }

    // timer UHD management
    public void updateTimer(
            @NotNull World world,
            @NotNull Store<EntityStore> store,
            @Nullable Message phaseLabel,
            long secondsRemaining,
            long totalElapsedSeconds
    ) {
        forEachHud(world, store, TimerHUD.HUD_ID, TimerHUD.class,
                hud -> {
                    hud.setVisible(true);
                    hud.refresh(phaseLabel, secondsRemaining, totalElapsedSeconds);
                });
    }

    public void showTimer(@NotNull World world, @NotNull Store<EntityStore> store) {
        forEachHud(world, store, TimerHUD.HUD_ID, TimerHUD.class, hud -> hud.setVisible(true));
    }

    public void hideTimer(@NotNull World world, @NotNull Store<EntityStore> store) {
        forEachHud(world, store, TimerHUD.HUD_ID, TimerHUD.class, hud -> hud.setVisible(false));
    }

    // title management
    public void showTitle(
            @NotNull World world,
            @NotNull Store<EntityStore> store,
            @NotNull Message primary,
            @Nullable Message secondary)
    {
        showTitle(world, store, primary, secondary, 4);
    }

    public void showTitle(
            @NotNull World world,
            @NotNull Store<EntityStore> store,
            @NotNull Message primary,
            @Nullable Message secondary,
            long durationSeconds
    ) {
        forEachHud(world, store, TitleHUD.HUD_ID, TitleHUD.class,
                hud -> hud.show(primary, secondary, durationSeconds));
    }

    public void advanceTitles(@NotNull World world, @NotNull Store<EntityStore> store) {
        forEachHud(world, store, TitleHUD.HUD_ID, TitleHUD.class, TitleHUD::onSecondElapsed);
    }

    private <T extends CustomUIHud> void forEachHud(
            @NotNull World world,
            @NotNull Store<EntityStore> store,
            @NotNull String hudId,
            @NotNull Class<T> hudType,
            @NotNull Consumer<T> action
    ) {
        for (PlayerRef ref : world.getPlayerRefs()) {
            if (!ref.isValid() || ref.getReference() == null) {
                continue;
            }

            Player player = store.getComponent(ref.getReference(), Player.getComponentType());

            if (player == null) {
                continue;
            }

            CustomUIHud hud = player.getHudManager().getCustomHud(hudId);

            if (hudType.isInstance(hud)) {
                action.accept(hudType.cast(hud));
            }
        }
    }
}
