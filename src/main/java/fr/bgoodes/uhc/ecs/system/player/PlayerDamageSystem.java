package fr.bgoodes.uhc.ecs.system.player;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.ecs.components.Spectator;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class PlayerDamageSystem extends DamageEventSystem {

    @Override
    public void handle(
            int index,
            @NotNull ArchetypeChunk<EntityStore> chunk,
            @NotNull Store<EntityStore> store,
            @NotNull CommandBuffer<EntityStore> commandBuffer,
            @NotNull Damage damage
    ) {
        if (damage.isCancelled()) {
            return;
        }

        GameContext gameContext = UHC.get().getGameContext();

        if (gameContext.getGamerules().invincible()) {
            damage.setCancelled(true);
            return;
        }

        if (damage.getSource() instanceof Damage.EntitySource entitySource) {
            Ref<EntityStore> sourceRef = entitySource.getRef();
            Player damager = commandBuffer.getComponent(sourceRef, Player.getComponentType());
            PlayerRef damagerRef = commandBuffer.getComponent(sourceRef, PlayerRef.getComponentType());

            // Player damage per players
            if (sourceRef.isValid() && damager != null && damagerRef != null) {

                //spectator
                if (commandBuffer.getComponent(sourceRef, Spectator.getComponentType()) != null) {
                    damage.setCancelled(true);
                    return;
                }

                if (!gameContext.getGamerules().pvp()) {
                    MessageUtil.send(damagerRef, MessageUtil.tr("uhc.gameState.playing.pvp.off").color(Color.red));
                    damage.setCancelled(true);
                }
            }
        }
    }

    @Override
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getFilterDamageGroup();
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(Player.getComponentType());
    }
}
