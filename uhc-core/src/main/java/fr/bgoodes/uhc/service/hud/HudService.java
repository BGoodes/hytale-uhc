package fr.bgoodes.uhc.service.hud;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.hud.impl.TitleHud;
import fr.bgoodes.uhc.service.hud.model.StateHud;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HudService {

    private final Map<UUID, StateHud> huds = new HashMap<>();
    private final Map<UUID, TitleHud> titleHuds = new HashMap<>();

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

            if (!(hudManager.getCustomHud(TitleHud.HUD_ID) instanceof TitleHud)) {
                TitleHud titleHud = new TitleHud(playerRef);
                hudManager.addCustomHud(playerRef, titleHud);
                setTitleHud(playerRef.getUuid(), titleHud);
            }

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
        List<TitleHud> titles;

        synchronized (this) {
            active = List.copyOf(huds.values());
            titles = List.copyOf(titleHuds.values());
        }

        active.forEach(StateHud::refresh);
        titles.forEach(TitleHud::refresh);
    }

    public synchronized @Nullable TitleHud getTitleHud(@NotNull UUID uuid) {
            return titleHuds.get(uuid);
    }

    public synchronized void forget(@NotNull UUID uuid) {
        huds.remove(uuid);
        titleHuds.remove(uuid);
    }

    private synchronized void setTitleHud(UUID uuid, TitleHud hud) {
        titleHuds.put(uuid, hud);
    }

    private synchronized void setHud(UUID uuid, @Nullable StateHud hud) {
        if (hud == null) {
            huds.remove(uuid);
        } else {
            huds.put(uuid, hud);
        }
    }
}
