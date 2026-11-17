package fr.bgoodes.uhc.system;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.FlagService;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

public class DamageSystem extends DamageEventSystem {

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

        FlagService flagService = UHC.get().getFlagService();

        if (!flagService.isEnabled(UHCFlag.DAMAGE)) {
            damage.setCancelled(true);
            return;
        }

        if (flagService.isEnabled(UHCFlag.PVP)) {
            return;
        }

        if (!(damage.getSource() instanceof Damage.EntitySource entitySource)) {
            return;
        }

        Ref<EntityStore> sourceRef = entitySource.getRef();

        if (!sourceRef.isValid()) {
            return;
        }

        PlayerRef damagerRef = commandBuffer.getComponent(sourceRef, PlayerRef.getComponentType());

        if (damagerRef != null) {
            UHCMessageUtil.send(damagerRef, UHCMessageUtil.tr("uhc.game.pvp.denied.chat").color(Color.RED));
            damage.setCancelled(true);
        }
    }

    @Override
    public @Nullable SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getFilterDamageGroup();
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }
}
