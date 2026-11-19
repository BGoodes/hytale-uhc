package fr.bgoodes.uhc.service.hud;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.service.hud.model.UHCHud;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class HudService {

    private final Map<UUID, Map<String, UHCHud>> huds = new HashMap<>();

    public void show(@NotNull PlayerRef playerRef, @NotNull UHCHud hud) {
        withHudManager(playerRef, hudManager -> {
            hudManager.addCustomHud(playerRef, hud);
            put(playerRef.getUuid(), hud);
        });
    }

    public void hide(@NotNull PlayerRef playerRef, @NotNull String key) {
        withHudManager(playerRef, hudManager -> {
            hudManager.removeCustomHud(playerRef, key);
            remove(playerRef.getUuid(), key);
        });
    }

    public synchronized @Nullable UHCHud get(@NotNull UUID uuid, @NotNull String key) {
        Map<String, UHCHud> playerHuds = huds.get(uuid);
        return playerHuds == null ? null : playerHuds.get(key);
    }

    public void refreshAll() {
        List<UHCHud> active;

        synchronized (this) {
            active = huds.values().stream()
                    .flatMap(playerHuds -> playerHuds.values().stream())
                    .toList();
        }

        active.forEach(UHCHud::refresh);
    }

    public synchronized void forget(@NotNull UUID uuid) {
        huds.remove(uuid);
    }

    private synchronized void put(UUID uuid, UHCHud hud) {
        huds.computeIfAbsent(uuid, _ -> new HashMap<>()).put(hud.getKey(), hud);
    }

    private synchronized void remove(UUID uuid, String key) {
        Map<String, UHCHud> playerHuds = huds.get(uuid);

        if (playerHuds != null) {
            playerHuds.remove(key);
        }
    }

    private void withHudManager(PlayerRef playerRef, Consumer<HudManager> action) {
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

            if (player != null) {
                action.accept(player.getHudManager());
            }
        });
    }
}
