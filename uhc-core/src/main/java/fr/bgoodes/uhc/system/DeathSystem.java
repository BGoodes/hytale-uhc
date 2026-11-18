package fr.bgoodes.uhc.system;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.participant.ParticipantDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeathSystem extends DeathSystems.OnDeathSystem {

    @Override
    public void onComponentAdded(
            @NotNull Ref<EntityStore> ref,
            @NotNull DeathComponent death,
            @NotNull Store<EntityStore> store,
            @NotNull CommandBuffer<EntityStore> commandBuffer
    ) {
        PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());
        TransformComponent transform = commandBuffer.getComponent(ref, TransformComponent.getComponentType());

        if (playerRef == null || transform == null || !UHC.get().getPlayerService().isParticipant(playerRef.getUuid())) {
            return;
        }

        HytaleServer.get().getEventBus()
                .dispatchFor(ParticipantDeathEvent.class)
                .dispatch(new ParticipantDeathEvent(
                        playerRef.getUuid(),
                        playerRef.getUsername(),
                        playerRef,
                        store.getExternalData().getWorld(),
                        transform.getPosition(),
                        death.getDeathCause(),
                        findKiller(death, store, playerRef)
                ));
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }

    private @Nullable PlayerRef findKiller(DeathComponent death, Store<EntityStore> store, PlayerRef victim) {
        Damage damage = death.getDeathInfo();

        if (damage == null || !(damage.getSource() instanceof Damage.EntitySource source) || !source.getRef().isValid()) {
            return null;
        }

        PlayerRef killer = store.getComponent(source.getRef(), PlayerRef.getComponentType());

        if (killer == null || killer.getUuid().equals(victim.getUuid())) {
            return null;
        }

        return killer;
    }
}
