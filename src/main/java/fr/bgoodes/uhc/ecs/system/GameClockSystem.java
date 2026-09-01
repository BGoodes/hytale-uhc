package fr.bgoodes.uhc.ecs.system;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.DelayedSystem;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.hud.HUDService;
import fr.bgoodes.uhc.service.state.StateService;
import fr.bgoodes.uhc.service.worldborder.WorldBorderService;
import org.jetbrains.annotations.NotNull;

public class GameClockSystem extends DelayedSystem<EntityStore> {

    private final StateService stateService = UHC.get().getStateService();
    private final WorldBorderService worldBorderService = UHC.get().getWorldBorderService();
    private final HUDService hudService = UHC.get().getHudService();

    public GameClockSystem() {
        super(1);
    }

    @Override
    public void delayedTick(float dt, int systemIndex, @NotNull Store<EntityStore> store) {
        World world = store.getExternalData().getWorld();

        if (!world.equals(UHC.get().getGameContext().getWorld())) {
            return;
        }

        stateService.getCurrentState().onClock(world, store);
        worldBorderService.update(world, store);
        hudService.advanceTitles(world, store);
    }
}
