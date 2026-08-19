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
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.hud.HUDService;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.LobbyUtil;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

public class PlayingState implements IGameState {

    public static final String STATE_ID = "playing";

    private final HUDService hudService = UHC.get().getHudService();

    private GameContext gameContext;
    private int time = 0;

    @Override
    public void start(@NotNull GameContext context) {

        this.gameContext = context;
        World world = context.getWorld();

        // update gamerules
        context.getGamerules().allowNewPlayers(false);
        context.getGamerules().pvp(false);
        context.getGamerules().building(true);
        context.getGamerules().invincible(true);

        // teleport & gamemode adventure
        Teleport teleport = Teleport.createForPlayer(LobbyUtil.LOBBY_SPAWN);
        Store<EntityStore> store = world.getEntityStore().getStore();

        Universe.get().getPlayers().forEach(ref -> {
            gameContext.addPlayer(ref.getUuid());

            if (ref.isValid() && ref.getReference() != null) {
                Player.setGameMode(ref.getReference(), GameMode.Adventure, store);
                store.addComponent(ref.getReference(), Teleport.getComponentType(), teleport);

                // HUD
                Player player = store.getComponent(ref.getReference(), Player.getComponentType());
                if (player != null) {
                    hudService.addHUDToPlayer(ref, player);
                }

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
    public void onClock(@NotNull World world, @NotNull Store<EntityStore> store) {

        if (this.time == this.gameContext.getConfig().invincibilityTime) {
            this.gameContext.getGamerules().invincible(false);
            MessageUtil.broadcastMessage("Vous n'êtes désormais plus invincible.");
            MessageUtil.broadcastSound("SFX_Crow_Death");
        }

        if (this.time == this.gameContext.getConfig().pvpTime) {
            this.gameContext.getGamerules().pvp(true);
            MessageUtil.broadcastMessage("Le PVP est désormais activé.");
            MessageUtil.broadcastSound("SFX_Crow_Death");
        }

        if (this.time == this.gameContext.getConfig().borderShrinkTime) {
            this.gameContext.getGamerules().borderShrinking(true);
            MessageUtil.broadcastMessage("La bordure du monde va commencer à réduire.");
            MessageUtil.broadcastSound("SFX_Crow_Death");
        }

//        if (this.gameContext.getConnectedPlayerCount() <= 1) {
//            MessageUtil.broadcastMessage("Fin de la partie !!!");
//            MessageUtil.broadcastSound("SFX_Rex_Alerted");
//            UHC.get().getStateService().changeGameState(EndingState.STATE_ID);
//            return;
//        }

        this.hudService.update(world, store, time);

        this.time++;
    }
}
