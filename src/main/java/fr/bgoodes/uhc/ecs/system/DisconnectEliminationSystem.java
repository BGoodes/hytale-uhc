package fr.bgoodes.uhc.ecs.system;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.DelayedSystem;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.player.PlayerService;
import fr.bgoodes.uhc.util.DeathUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DisconnectEliminationSystem extends DelayedSystem<EntityStore> {

    private final PlayerService playerService = UHC.get().getPlayerService();

    public DisconnectEliminationSystem() {
        super(1);
    }

    @Override
    public void delayedTick(float dt, int systemIndex, @NotNull Store<EntityStore> store) {
        World world = store.getExternalData().getWorld();

        if (!world.equals(UHC.get().getGameContext().getWorld())) {
            return;
        }

        Map<UUID, Integer> countdowns = playerService.getDisconnectCountdowns();
        List<UUID> expired = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : countdowns.entrySet()) {
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                expired.add(entry.getKey());
            } else {
                entry.setValue(remaining);
            }
        }

        for (UUID uuid : expired) {
            countdowns.remove(uuid);
            eliminate(uuid, world);
        }
    }

    private void eliminate(@NotNull UUID playerUuid, @NotNull World world) {
        Universe.get().getPlayerStorage().load(playerUuid).thenAccept(holder ->
                world.execute(() -> {

                    Nameplate nameplate = holder.getComponent(Nameplate.getComponentType());
                    String username = nameplate != null ? nameplate.getText() : playerUuid.toString();

                    DeathUtil.eliminate(playerUuid, username, null);
                    DeathUtil.dropStoredInventory(holder, world.getEntityStore().getStore());
                })
        );
    }
}
