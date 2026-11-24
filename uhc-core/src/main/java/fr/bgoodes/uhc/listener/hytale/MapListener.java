package fr.bgoodes.uhc.listener.hytale;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.util.UHCTeamUtil;

import java.util.UUID;

public class MapListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        Player player = ref.getStore().getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = ref.getStore().getComponent(ref, PlayerRef.getComponentType());

        if (player == null || playerRef == null) {
            return;
        }

        UUID viewer = playerRef.getUuid();
        player.getWorldMapTracker().setPlayerMapFilter(other -> !UHCTeamUtil.isVisibleOnMap(viewer, other.getUuid()));
    }
}
