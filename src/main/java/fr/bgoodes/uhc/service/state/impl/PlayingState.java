package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.LobbyUtil;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

public class PlayingState implements IGameState {

    public static final String STATE_ID = "playing";

    private GameContext gameContext;

    private int time;

    @Override
    public void start(@NotNull GameContext context) {

        this.gameContext = context;
        World world = context.getWorld();

        // update gamerules
        context.getGamerules().pvp(false);
        context.getGamerules().building(true);
        context.getGamerules().invincible(true);

        // teleport & gamemode adventure
        Teleport teleport = Teleport.createForPlayer(LobbyUtil.LOBBY_SPAWN);
        Store<EntityStore> store = world.getEntityStore().getStore();

        Universe.get().getPlayers().forEach(ref -> {
            if (ref.getReference() != null) {
                Player.setGameMode(ref.getReference(), GameMode.Adventure, store);
                store.addComponent(ref.getReference(), Teleport.getComponentType(), teleport);

                // heal
                EntityStatMap statMap = store.getComponent(ref.getReference(), EntityStatMap.getComponentType());
                if (statMap != null) {
                    statMap.maximizeStatValue(DefaultEntityStatTypes.getHealth());
                }
            }
        });

        //delete lobby
        LobbyUtil.remove(world);

        // time set day
        WorldTimeResource worldTimeResource = world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());
        worldTimeResource.setDayTime(0.2, world, world.getEntityStore().getStore());

        this.time = 0;
    }

    @Override
    public void onClock() {

        if (time == 60) {
            this.gameContext.getGamerules().invincible(false);
            MessageUtil.broadcastMessage("Vous n'êtes désormais plus invincible.");
            MessageUtil.broadcastSound("SFX_Crow_Death");
        }

        if (time == 10 * 60) {
            this.gameContext.getGamerules().pvp(true);
            MessageUtil.broadcastMessage("Le PVP est désormais activé.");
            MessageUtil.broadcastSound("SFX_Crow_Death");
        }

        time++;
    }
}
