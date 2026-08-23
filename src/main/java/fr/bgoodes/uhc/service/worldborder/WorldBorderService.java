package fr.bgoodes.uhc.service.worldborder;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.context.GameContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class WorldBorderService {

    private final static int BORDER_HEIGHT = 1024;

    private final static Vector3f COLOR_INSIDE = new Vector3f(0.5f, 0.85f, 1.0f);
    private final static Vector3f COLOR_OUTSIDE = new Vector3f(1, 0, 0);

    private final GameContext gameContext;

    private int worldBorderSize;

    public WorldBorderService(@NotNull GameContext gameContext) {
        this.gameContext = gameContext;
        this.worldBorderSize = gameContext.getConfig().borderSizeMax;
    }

    public int getWorldBorderSize() {
        return this.worldBorderSize;
    }

    public void update(@NotNull World world, @NotNull Store<EntityStore> store) {

        // shrink border
        if (gameContext.getGamerules().borderShrinking() && this.worldBorderSize > gameContext.getConfig().borderSizeMin) {
            this.worldBorderSize--;
        }


        for (PlayerRef playerRef : world.getPlayerRefs()) {
            Ref<EntityStore> ref = playerRef.getReference();
            if (!playerRef.isValid() || ref == null || !ref.isValid()) continue;

            TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());

            if (transformComponent == null) continue;
            Vector3d position = transformComponent.getPosition();

            if (isOutsideBorder(position)) {
                this.displayBorder(playerRef, position, store);

                //damage
                Damage.EnvironmentSource damageSource = new Damage.EnvironmentSource("World Border");
                int damageCauseIndex = DamageCause.getAssetMap().getIndex("OutOfWorld");
                Damage damage = new Damage(damageSource, damageCauseIndex, gameContext.getConfig().borderDamage);
                DamageSystems.executeDamage(ref, store, damage);

            } else if (isNearBorder(position)) {
                this.displayBorder(playerRef, position, store);
            }
        }
    }

    private boolean isOutsideBorder(@NotNull Vector3d position) {
        return Math.abs(position.x) > this.worldBorderSize ||
                Math.abs(position.z) > this.worldBorderSize;
    }

    private boolean isNearBorder(@NotNull Vector3d position) {
        double distanceX = this.worldBorderSize - Math.abs(position.x);
        double distanceZ = this.worldBorderSize - Math.abs(position.z);

        return distanceX <= gameContext.getConfig().borderWarningDistance ||
                distanceZ <= gameContext.getConfig().borderWarningDistance;
    }

    private void displayBorder(
            @NotNull PlayerRef player,
            @NotNull Vector3d position,
            @NotNull Store<EntityStore> store
    ) {
        int distance = gameContext.getConfig().borderWarningDistance;

        int minX = Math.max(-worldBorderSize, (int) Math.floor(position.x - distance));
        int maxX = Math.min(worldBorderSize, (int) Math.floor(position.x + distance));
        int minY = (int) Math.floor(position.y - distance);
        int maxY = (int) Math.floor(position.y + distance);
        int minZ = Math.max(-worldBorderSize, (int) Math.floor(position.z - distance));
        int maxZ = Math.min(worldBorderSize, (int) Math.floor(position.z + distance));

        Set<Vector3i> positions = new HashSet<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {

                    if (Math.abs(x) == worldBorderSize || Math.abs(z) == worldBorderSize) {
                        if ((x + y + z) % 2 == 0) {
                            positions.add(new Vector3i(x, y, z));
                        }
                    }
                }
            }
        }

        assert player.getReference() != null;

        for (Vector3i p : positions) {

            float rotationYaw = 0.0f;

            if (Math.abs(p.x) == worldBorderSize) {
                rotationYaw = (float) Math.toRadians(90);
            }

            ParticleUtil.spawnParticleEffect(
                    "Block_Top_Glow",
                    p.x,
                    p.y,
                    p.z,
                    rotationYaw,
                    0.0f,
                    0.0f,
                    1.0f,
                    null,
                    null,
                    List.of(player.getReference()),
                    store
            );
        }
    }
}
