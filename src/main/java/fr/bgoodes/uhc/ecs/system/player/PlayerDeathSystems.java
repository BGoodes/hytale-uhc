package fr.bgoodes.uhc.ecs.system.player;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.modules.entity.damage.event.KillFeedEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.util.DeathUtil;
import fr.bgoodes.uhc.util.SpectatorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerDeathSystems {

    public static class Eliminate extends DeathSystems.OnDeathSystem {

        @Override
        public void onComponentAdded(
                @NotNull Ref<EntityStore> ref,
                @NotNull DeathComponent death,
                @NotNull Store<EntityStore> store,
                @NotNull CommandBuffer<EntityStore> commandBuffer
        ) {
            PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());
            assert playerRef != null;

            Message killerName = resolveKillerName(ref, death, store);
            DeathUtil.eliminate(playerRef.getUuid(), playerRef.getUsername(), killerName);

            SpectatorUtil.addSpectator(playerRef, store);
        }

        @Nullable
        private Message resolveKillerName(@NotNull Ref<EntityStore> ref, @NotNull DeathComponent death, @NotNull Store<EntityStore> store) {
            Damage deathInfo = death.getDeathInfo();
            if (deathInfo == null) return null;
            if (!(deathInfo.getSource() instanceof Damage.EntitySource entitySource) || !ref.isValid()) return null;

            Ref<EntityStore> killerRef = entitySource.getRef();
            KillFeedEvent.KillerMessage killerMessageEvent = new KillFeedEvent.KillerMessage(deathInfo, ref);
            store.invoke(killerRef, killerMessageEvent);
            if (killerMessageEvent.isCancelled()) return null;

            return killerMessageEvent.getMessage();
        }

        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return PlayerRef.getComponentType();
        }
    }

    public static class CancelKillFeed extends EntityEventSystem<EntityStore, KillFeedEvent.Display> {

        public CancelKillFeed() {
            super(KillFeedEvent.Display.class);
        }

        @Override
        public void handle(
                int i,
                @NotNull ArchetypeChunk<EntityStore> archetypeChunk,
                @NotNull Store<EntityStore> store,
                @NotNull CommandBuffer<EntityStore> commandBuffer,
                @NotNull KillFeedEvent.Display display)
        {
            display.setCancelled(true);
        }

        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return PlayerRef.getComponentType();
        }
    }
}
