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
import fr.bgoodes.uhc.context.GameContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

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

        if (gameContext.getGamerules().borderShrinking() && this.worldBorderSize > gameContext.getConfig().borderSizeMin) {
            this.worldBorderSize--;
        }

        for (PlayerRef playerRef : world.getPlayerRefs()) {
            Ref<EntityStore> ref = playerRef.getReference();
            if (!playerRef.isValid() || ref == null || !ref.isValid()) continue;

            TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());

            if (transformComponent == null) continue;
            Vector3d pos = transformComponent.getPosition();

            if (isOutsideBorder(pos)) {
                this.displayWorldBorder(playerRef, false);

                //damage
                Damage.EnvironmentSource damageSource = new Damage.EnvironmentSource("World Border");
                int damageCauseIndex = DamageCause.getAssetMap().getIndex("OutOfWorld");
                Damage damage = new Damage(damageSource, damageCauseIndex, gameContext.getConfig().borderDamage);
                DamageSystems.executeDamage(ref, store, damage);

            } else if (isNearBorder(pos)) {
                this.displayWorldBorder(playerRef, true);
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

        return distanceX <= gameContext.getConfig().borderWarningDistance ||
                distanceZ <= gameContext.getConfig().borderWarningDistance;
    }

    private void displayWorldBorder(PlayerRef ref, boolean inside) {
        Matrix4f transform = new Matrix4f()
                .translation(0, BORDER_HEIGHT / 2f, 0)
                .scale(this.worldBorderSize * 2, BORDER_HEIGHT, this.worldBorderSize * 2);

        float[] matrix = new float[16];
        transform.get(matrix);

        DisplayDebug displayDebug = new DisplayDebug(
                DebugShape.Cube,
                matrix,
                inside ? COLOR_INSIDE : COLOR_OUTSIDE,
                1f, // time = 1s
                (byte)0,
                null,
                0.2f // opacity
        );

        ref.getPacketHandler().write(displayDebug);
    }
}
