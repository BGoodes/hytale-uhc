package fr.bgoodes.uhc.system;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.CancellableEcsEvent;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockSystems {

    private BlockSystems() {}

    public static class BreakBlock extends BuildFlagSystem<BreakBlockEvent> {

        public BreakBlock() {
            super(BreakBlockEvent.class);
        }
    }

    public static class DamageBlock extends BuildFlagSystem<DamageBlockEvent> {

        public DamageBlock() {
            super(DamageBlockEvent.class);
        }
    }

    public static class PlaceBlock extends BuildFlagSystem<PlaceBlockEvent> {

        public PlaceBlock() {
            super(PlaceBlockEvent.class);
        }
    }

    private abstract static class BuildFlagSystem<T extends CancellableEcsEvent> extends EntityEventSystem<EntityStore, T> {

        protected BuildFlagSystem(Class<T> eventType) {
            super(eventType);
        }

        @Override
        public void handle(
                int index,
                @NotNull ArchetypeChunk<EntityStore> chunk,
                @NotNull Store<EntityStore> store,
                @NotNull CommandBuffer<EntityStore> commandBuffer,
                @NotNull T event
        ) {
            if (event.isCancelled()) {
                return;
            }

            Player player = chunk.getComponent(index, Player.getComponentType());

            if (player == null || player.getGameMode() != GameMode.Adventure) {
                return;
            }

            if (!UHC.get().getFlagService().isEnabled(UHCFlag.BUILD)) {
                event.setCancelled(true);
            }
        }

        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return Player.getComponentType();
        }
    }
}
