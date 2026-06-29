package fr.bgoodes.uhc.service.worldborder;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.DebugShape;
import com.hypixel.hytale.protocol.packets.player.DisplayDebug;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class WorldBorderService {

    private final static int BORDER_HEIGHT = 1024;

    private final static Vector3f COLOR_INSIDE = new Vector3f(0.5f, 0.85f, 1.0f);
    private final static Vector3f COLOR_OUTSIDE = new Vector3f(1, 0, 0);

    private static final int BORDER_DAMAGE = 10;

    private static final double BORDER_WARNING_DISTANCE = 30;

    private int worldBorderSize;

    public WorldBorderService(int worldBorderSize) {
        this.worldBorderSize = worldBorderSize;
    }

    public int getWorldBorderSize() {
        return this.worldBorderSize;
    }

    public void setWorldBorderSize(int worldBorderSize) {
        this.worldBorderSize = worldBorderSize;
    }

    public void update(@NotNull World world, @NotNull Store<EntityStore> store) {
        for (PlayerRef playerRef : world.getPlayerRefs()) {
            Ref<EntityStore> ref = playerRef.getReference();

            if (playerRef.isValid() && ref != null && ref.isValid()) {
                TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());

                if (transformComponent != null) {
                    Vector3d playerPosition = transformComponent.getPosition();

                    if (isOutsideBorder(playerPosition)) {
                        this.displayWorldBorder(playerRef, false);

                        Damage.EnvironmentSource damageSource = new Damage.EnvironmentSource("World Border");
                        int damageCauseIndex = DamageCause.getAssetMap().getIndex("OutOfWorld");
                        Damage damage = new Damage(damageSource, damageCauseIndex, BORDER_DAMAGE);
                        DamageSystems.executeDamage(ref, store, damage);

                    } else if (isNearBorder(playerPosition)) {
                        this.displayWorldBorder(playerRef, true);
                    }
                }
            }
        }
    }

    private boolean isOutsideBorder(Vector3d position) {
        return Math.abs(position.x) > this.worldBorderSize ||
                Math.abs(position.z) > this.worldBorderSize;
    }

    private boolean isNearBorder(Vector3d position) {
        double distanceX = this.worldBorderSize - Math.abs(position.x);
        double distanceZ = this.worldBorderSize - Math.abs(position.z);

        return distanceX <= BORDER_WARNING_DISTANCE ||
                distanceZ <= BORDER_WARNING_DISTANCE;
    }

    private void displayWorldBorder(PlayerRef ref, boolean inside) {
        Matrix4f transform = new Matrix4f()
                .translation(0, BORDER_HEIGHT/2f, 0)
                .scale(this.worldBorderSize*2, BORDER_HEIGHT, this.worldBorderSize*2);

        float[] matrix = new float[16];
        transform.get(matrix);

        DisplayDebug displayDebug = new DisplayDebug(
                DebugShape.Cube,
                matrix,
                inside ? COLOR_INSIDE : COLOR_OUTSIDE,
                1f, // time = 1s
                (byte)0,
                null,
                0.2f    // opacity
        );

        ref.getPacketHandler().write(displayDebug);
    }
}
