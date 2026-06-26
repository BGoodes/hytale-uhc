package fr.bgoodes.uhc;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import com.hypixel.hytale.server.core.universe.world.worldmap.markers.MarkersCollector;
import org.jetbrains.annotations.NotNull;

public class PlayerMarkerProvider implements WorldMapManager.MarkerProvider {

    @Override
    public void update(@NotNull World world, @NotNull Player player, @NotNull MarkersCollector markersCollector) {

    }
}
