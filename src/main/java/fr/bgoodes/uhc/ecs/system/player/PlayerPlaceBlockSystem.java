package fr.bgoodes.uhc.ecs.system.player;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PlayerPlaceBlockSystem extends EntityEventSystem<EntityStore, PlaceBlockEvent> {

    public PlayerPlaceBlockSystem() {
        super(PlaceBlockEvent.class);
    }

    @Override
    public void handle(
            int index,
            @NonNullDecl ArchetypeChunk<EntityStore> chunk,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl CommandBuffer<EntityStore> commandBuffer,
            @NonNullDecl PlaceBlockEvent event
    ) {
        if (event.isCancelled()) {
            return;
        }

        Player player = chunk.getComponent(index, Player.getComponentType());
        if (player == null || event.isCancelled()) return;

        GameContext gameContext = UHC.get().getGameContext();

        if (!gameContext.getGamerules().building() && player.getGameMode() == GameMode.Adventure) {
            event.setCancelled(true);
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }
}
