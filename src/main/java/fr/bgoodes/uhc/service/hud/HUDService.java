package fr.bgoodes.uhc.service.hud;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;

public class HUDService {

    public void addHUDToPlayer(@NotNull PlayerRef playerRef, @NotNull Player player) {
        if (player.getHudManager().getCustomHud(TimerHUD.HUD_ID) == null) {
            player.getHudManager().addCustomHud(playerRef, new TimerHUD(playerRef));
        }
    }

    public void update(@NotNull World world, @NotNull Store<EntityStore> store, int time) {
        for (PlayerRef ref : world.getPlayerRefs()) {
            if (!ref.isValid() || ref.getReference() == null) {
                continue;
            }

            Player player = store.getComponent(ref.getReference(), Player.getComponentType());

            if (player == null) {
                continue;
            }

            CustomUIHud hud = player.getHudManager().getCustomHud(TimerHUD.HUD_ID);

            if (hud instanceof TimerHUD testHud) {
                testHud.refresh(time);
            }
        }
    }
}
