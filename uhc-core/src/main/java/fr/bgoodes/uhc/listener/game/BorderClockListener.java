package fr.bgoodes.uhc.listener.game;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.game.BorderShrunkEvent;
import fr.bgoodes.uhc.event.ClockEvent;
import fr.bgoodes.uhc.service.WorldBorderService;
import org.joml.Vector3d;

import java.util.List;

public class BorderClockListener {

    private static final String DAMAGE_SOURCE = "World Border";
    private static final String DAMAGE_CAUSE = "OutOfWorld"; // TODO: create a custom damage cause

    private static final String PARTICLE = "Block_Top_Glow";
    private static final int DISPLAY_DISTANCE = 20;

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(ClockEvent.class, _ -> onClock());
    }

    private void onClock() {
        WorldBorderService border = UHC.get().getWorldBorderService();

        if (Double.isInfinite(border.getRadius())) {
            return;
        }

        if (border.update(1)) {
            HytaleServer.get().getEventBus()
                    .dispatchFor(BorderShrunkEvent.class)
                    .dispatch(new BorderShrunkEvent(border.getRadius()));
        }

        World world = UHC.get().getWorldService().getGameWorld();
        world.execute(() -> check(world, border));
    }

    private void check(World world, WorldBorderService border) {
        double radius = border.getRadius();

        for (PlayerRef playerRef : world.getPlayerRefs()) {
            Ref<EntityStore> ref = playerRef.getReference();

            if (ref == null || !ref.isValid()) {
                continue;
            }

            Store<EntityStore> store = ref.getStore();
            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());

            if (transform == null) {
                continue;
            }

            Vector3d position = transform.getPosition();

            if (radius - Math.abs(position.x) <= DISPLAY_DISTANCE || radius - Math.abs(position.z) <= DISPLAY_DISTANCE) {
                display(ref, position, (int) Math.floor(radius), store);
            }

            if (border.isOutside(position) && UHC.get().getPlayerService().isParticipant(playerRef.getUuid())) {
                Damage damage = new Damage(
                        new Damage.EnvironmentSource(DAMAGE_SOURCE),
                        DamageCause.getAssetMap().getIndex(DAMAGE_CAUSE),
                        UHC.get().getGameConfig().borderDamage.get()
                );

                DamageSystems.executeDamage(ref, store, damage);
            }
        }
    }

    private void display(Ref<EntityStore> ref, Vector3d position, int border, Store<EntityStore> store) {
        List<Ref<EntityStore>> viewers = List.of(ref);

        int minX = Math.max(-border, (int) Math.floor(position.x - DISPLAY_DISTANCE));
        int maxX = Math.min(border, (int) Math.floor(position.x + DISPLAY_DISTANCE));
        int minY = (int) Math.floor(position.y - DISPLAY_DISTANCE);
        int maxY = (int) Math.floor(position.y + DISPLAY_DISTANCE);
        int minZ = Math.max(-border, (int) Math.floor(position.z - DISPLAY_DISTANCE));
        int maxZ = Math.min(border, (int) Math.floor(position.z + DISPLAY_DISTANCE));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    boolean onBorder = Math.abs(x) == border || Math.abs(z) == border;

                    if (!onBorder || (x + y + z) % 2 != 0) {
                        continue;
                    }

                    float yaw = Math.abs(x) == border ? (float) Math.toRadians(90) : 0.0f;

                    ParticleUtil.spawnParticleEffect(PARTICLE, x, y, z, yaw, 0.0f, 0.0f, null, viewers, store);
                }
            }
        }
    }
}
