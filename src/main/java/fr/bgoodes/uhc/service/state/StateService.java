package fr.bgoodes.uhc.service.state;

import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.exception.IllegalGameState;
import fr.bgoodes.uhc.service.state.impl.EndingState;
import fr.bgoodes.uhc.service.state.impl.LobbyState;
import fr.bgoodes.uhc.service.state.impl.PlayingState;
import fr.bgoodes.uhc.service.state.impl.StartingState;
import fr.bgoodes.uhc.service.state.model.IGameState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class StateService {

    @NotNull
    private IGameState currentState;

    @NotNull
    private final GameContext gameContext;

    @NotNull
    private final Map<String, IGameState> statesMap = new HashMap<>();

    public StateService(@NotNull GameContext gameContext) {
        this.gameContext = gameContext;

        registerGameState(LobbyState.STATE_ID, new LobbyState());
        registerGameState(StartingState.STATE_ID, new StartingState());
        registerGameState(PlayingState.STATE_ID, new PlayingState());
        registerGameState(EndingState.STATE_ID, new EndingState());

        changeGameState(LobbyState.STATE_ID);
    }

    public @NotNull IGameState getCurrentState() {
        return currentState;
    }

    public void registerGameState(@NotNull String id, @NotNull IGameState gameState) {
        this.statesMap.put(id, gameState);
    }

    public void unregisterGameState(@NotNull String stateId) {
        this.statesMap.remove(stateId);
    }

    // must be on default world thread
    public void changeGameState(@NotNull String stateId) {
        @Nullable IGameState newState = this.statesMap.get(stateId);

        if (newState == null)
            throw new IllegalGameState(stateId);

        newState.start(this.gameContext);
        this.currentState = newState;
    }
}
