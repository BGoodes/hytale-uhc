package fr.bgoodes.uhc.ecs.system;

import com.hypixel.hytale.builtin.crafting.component.BenchBlock;
import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpgradeBenchSystem extends RefSystem<ChunkStore> {

    @Override
    public void onEntityAdded(
            @NotNull Ref<ChunkStore> ref,
            @NotNull AddReason addReason,
            @NotNull Store<ChunkStore> store,
            @NotNull CommandBuffer<ChunkStore> commandBuffer
    ) {
        BenchBlock benchBlock = commandBuffer.getComponent(ref, BenchBlock.getComponentType());
        assert benchBlock != null;

        benchBlock.setTierLevel(10);
    }

    @Override
    public void onEntityRemove(
            @NotNull Ref<ChunkStore> ref,
            @NotNull RemoveReason removeReason,
            @NotNull Store<ChunkStore> store,
            @NotNull CommandBuffer<ChunkStore> commandBuffer
    ) {

    }

    @Override
    public @Nullable Query<ChunkStore> getQuery() {
        return Query.and(
                BenchBlock.getComponentType(),
                Query.not(ProcessingBenchBlock.getComponentType())
        );
    }
}
