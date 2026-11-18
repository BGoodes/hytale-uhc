package fr.bgoodes.uhc.service.hud;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.hud.model.StateHud;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HudService {

    private final Map<UUID, StateHud> huds = new HashMap<>();

    public void show(@NotNull PlayerRef playerRef) {
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref == null || !ref.isValid()) {
            return;
        }

        Store<EntityStore> store = ref.getStore();

        store.getExternalData().getWorld().execute(() -> {
            if (!ref.isValid()) {
                return;
            }

            Player player = store.getComponent(ref, Player.getComponentType());

            if (player == null) {
                return;
            }

            HudManager hudManager = player.getHudManager();
            StateHud hud = UHC.get().getStateService().getCurrentState().createHud(playerRef);

            if (hud == null) {
                hudManager.removeCustomHud(playerRef, StateHud.HUD_ID);
            } else {
                hudManager.addCustomHud(playerRef, hud);
            }

            setHud(playerRef.getUuid(), hud);
        });
    }

    public void showAll() {
        Universe.get().getPlayers().forEach(this::show);
    }

    public void refreshAll() {
        List<StateHud> active;

        synchronized (this) {
            active = List.copyOf(huds.values());
        }

        active.forEach(StateHud::refresh);
    }

    public synchronized void forget(@NotNull UUID uuid) {
        huds.remove(uuid);
    }

    private synchronized void setHud(UUID uuid, @Nullable StateHud hud) {
        if (hud == null) {
            huds.remove(uuid);
        } else {
            huds.put(uuid, hud);
        }
    }
}
