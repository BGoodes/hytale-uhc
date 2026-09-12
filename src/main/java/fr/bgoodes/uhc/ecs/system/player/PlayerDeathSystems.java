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
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.permission.PermissionService;
import fr.bgoodes.uhc.util.BroadcastUtil;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class PlayerDeathSystems {

    public static class RemovePlayer extends DeathSystems.OnDeathSystem {

        private final PermissionService permissionService = UHC.get().getPermissionService();

        @Override
        public void onComponentAdded(
                @NotNull Ref<EntityStore> ref,
                @NotNull DeathComponent death,
                @NotNull Store<EntityStore> store,
                @NotNull CommandBuffer<EntityStore> commandBuffer
        ) {
            PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());
            assert playerRef != null;

            UHC.get().getPlayerService().removeParticipant(playerRef.getUuid());
            permissionService.addSpectator(playerRef.getUuid());
        }

        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return PlayerRef.getComponentType();
        }
    }

    public static class DeathMessage extends DeathSystems.OnDeathSystem {

        @Override
        public void onComponentAdded(
                @NotNull Ref<EntityStore> ref,
                @NotNull DeathComponent deathComponent,
                @NotNull Store<EntityStore> store,
                @NotNull CommandBuffer<EntityStore> commandBuffer
        ) {
            Damage deathInfo = deathComponent.getDeathInfo();
            if (deathInfo == null) return;

            PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());
            assert playerRef != null;

            Message playerName = Message.raw(playerRef.getUsername());
            Message killerName = null;

            if (deathInfo.getSource() instanceof Damage.EntitySource entitySource) {
                Ref<EntityStore> killerRef = entitySource.getRef();
                if (ref.isValid()) {
                    KillFeedEvent.KillerMessage killerMessageEvent = new KillFeedEvent.KillerMessage(deathInfo, ref);
                    store.invoke(killerRef, killerMessageEvent);
                    if (killerMessageEvent.isCancelled()) return;

                    killerName = killerMessageEvent.getMessage();
                }
            }

            Message deathMessage = MessageUtil.tr(killerName != null ? "uhc.death.killed" : "uhc.death.player")
                    .param("player", playerName);

            if (killerName != null) {
                deathMessage.param("killer", killerName);
            }
            BroadcastUtil.message(deathMessage.color(Color.ORANGE).bold(true), false);
            BroadcastUtil.sound("SFX_Pistol_Fire");
        }

        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return PlayerRef.getComponentType();
        }
    }

    public class CancelKillFeed extends EntityEventSystem<EntityStore, KillFeedEvent.Display> {

        protected CancelKillFeed() {
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
