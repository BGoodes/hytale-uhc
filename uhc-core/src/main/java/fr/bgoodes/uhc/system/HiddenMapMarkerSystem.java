package fr.bgoodes.uhc.system;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.section.ChunkSection;
import com.hypixel.hytale.server.core.universe.world.meta.state.BlockMapMarker;
import com.hypixel.hytale.server.core.universe.world.meta.state.BlockMapMarkersResource;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.util.Set;

public class HiddenMapMarkerSystem extends RefSystem<ChunkStore> {

    private static final Set<String> HIDDEN_MARKERS = Set.of("server.items.Forgotten_Temple_Portal_Enter.name");

    private final Set<Dependency<ChunkStore>> dependencies = Set.of(
            new SystemDependency<>(Order.AFTER, BlockMapMarker.OnAddRemove.class)
    );

    @Override
    public void onEntityAdded(
            @NotNull Ref<ChunkStore> ref,
            @NotNull AddReason addReason,
            @NotNull Store<ChunkStore> store,
            @NotNull CommandBuffer<ChunkStore> commandBuffer
    ) {
        BlockMapMarker marker = commandBuffer.getComponent(ref, BlockMapMarker.getComponentType());

        if (marker == null || !HIDDEN_MARKERS.contains(marker.getName())) {
            return;
        }

        BlockModule.BlockStateInfo blockInfo = commandBuffer.getComponent(ref, BlockModule.BlockStateInfo.getComponentType());

        if (blockInfo == null || !blockInfo.getSectionRef().isValid()) {
            return;
        }

        ChunkSection section = commandBuffer.getComponent(blockInfo.getSectionRef(), ChunkSection.getComponentType());

        if (section == null) {
            return;
        }

        int index = blockInfo.getIndex();
        Vector3i position = new Vector3i(
                ChunkUtil.worldCoordFromLocalCoord(section.getX(), ChunkUtil.xFromIndex(index)),
                ChunkUtil.worldCoordFromLocalCoord(section.getY(), ChunkUtil.yFromIndex(index)),
                ChunkUtil.worldCoordFromLocalCoord(section.getZ(), ChunkUtil.zFromIndex(index))
        );

        commandBuffer.getResource(BlockMapMarkersResource.getResourceType()).removeMarker(position);
    }

    @Override
    public void onEntityRemove(
            @NotNull Ref<ChunkStore> ref,
            @NotNull RemoveReason removeReason,
            @NotNull Store<ChunkStore> store,
            @NotNull CommandBuffer<ChunkStore> commandBuffer
    ) {}

    @Override
    public @NotNull Set<Dependency<ChunkStore>> getDependencies() {
        return dependencies;
    }

    @Override
    public @Nullable Query<ChunkStore> getQuery() {
        return BlockMapMarker.getComponentType();
    }
}
