package fr.bgoodes.uhc.listener;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.math.shape.Box2D;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ShutdownReason;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldConfig;
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;
import com.hypixel.hytale.server.core.universe.world.events.AllWorldsLoadedEvent;
import com.hypixel.hytale.server.core.universe.world.events.RemoveWorldEvent;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.utils.LobbyUtil;

public class WorldListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(AddWorldEvent.class, this::onAddWorld);
        eventRegistry.registerGlobal(RemoveWorldEvent.class, this::onRemoveWorld);
        eventRegistry.register(AllWorldsLoadedEvent.class, this::onAllWorldsLoaded);
    }

    private void onAddWorld(AddWorldEvent event) {
        if (!isGameWorld(event.getWorld())) {
            return;
        }

        WorldConfig worldConfig = event.getWorld().getWorldConfig();
        int radius = UHC.get().getServerConfig().getPregenerateRadius();

        worldConfig.setDeleteOnUniverseStart(true);

        if (radius > 0) {
            worldConfig.getChunkConfig().setPregenerateRegion(new Box2D(-radius, -radius, radius, radius));
        }

        worldConfig.markChanged();
    }

    private void onRemoveWorld(RemoveWorldEvent event) {
        if (isGameWorld(event.getWorld())) {
            event.setCancelled(true);
        }
    }

    private void onAllWorldsLoaded(AllWorldsLoadedEvent event) {
        World world = Universe.get().getDefaultWorld();

        if (world == null) {
            String reason = "No default world found. Check the config.json";

            UHC.LOGGER.atSevere().log(reason);
            HytaleServer.get().shutdownServer(ShutdownReason.MOD_ERROR.withMessage(Message.raw(reason)));
            return;
        }

        UHC.get().getWorldService().setGameWorld(world);
        LobbyUtil.generate(world);
    }

    private boolean isGameWorld(World world) {
        return world.getName().equalsIgnoreCase(HytaleServer.get().getConfig().getDefaults().getWorld());
    }
}
