package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventRegistration;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.model.IGameState;

import java.util.ArrayList;
import java.util.List;

public class LobbyState implements IGameState {

    public static final String STATE_ID = "lobby";

    @Override
    public void onClock() {}

    @Override
    public void onEnter() {
        GameContext gameContext = UHC.get().getGameContext();

        gameContext.getGamerules().pvp(false);
        gameContext.getGamerules().building(false);
        gameContext.getGamerules().invincible(true);
    }

    @Override
    public void onExit() {
        World world = UHC.get().getGameContext().getWorld();

        // teleport & gm a
        Transform transform = new Transform(0, 305, 0);
        Teleport teleport = Teleport.createForPlayer(transform);
        Store<EntityStore> store = world.getEntityStore().getStore();

        Universe.get().getPlayers().forEach(ref -> {
            if (ref.getReference() != null) {
                Player.setGameMode(ref.getReference(), GameMode.Adventure, store);
                store.addComponent(ref.getReference(), Teleport.getComponentType(), teleport);
            }
        });
    }
}
