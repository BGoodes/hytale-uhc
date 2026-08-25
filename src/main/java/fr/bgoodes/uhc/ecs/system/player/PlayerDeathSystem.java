package fr.bgoodes.uhc.ecs.system.player;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.asset.type.gameplay.DeathConfig;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.util.SpectatorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerDeathSystem extends DeathSystems.OnDeathSystem {

    @Override
    public void onComponentAdded(
            @NotNull Ref<EntityStore> ref,
            @NotNull DeathComponent deathComponent,
            @NotNull Store<EntityStore> store,
            @NotNull CommandBuffer<EntityStore> commandBuffer
    ) {
        PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());
        assert playerRef != null;

        UHC.get().getGameContext().removePlayer(playerRef.getUuid());
    }

    @Override
    public void onComponentRemoved(
            @NotNull Ref<EntityStore> ref,
            @NotNull DeathComponent death,
            @NotNull Store<EntityStore> store,
            @NotNull CommandBuffer<EntityStore> commandBuffer
    ) {
        death.setItemsLossMode(DeathConfig.ItemsLossMode.ALL);

        PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());
        assert playerRef != null;

        if (UHC.get().getGameContext().getConfig().allowSpectators) {
            SpectatorUtil.addSpectator(playerRef, commandBuffer);
        } else {
            commandBuffer.removeEntity(ref, RemoveReason.REMOVE);
        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(PlayerRef.getComponentType());
    }
}
