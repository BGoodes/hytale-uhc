package fr.bgoodes.uhc.service.state;

import fr.bgoodes.uhc.service.state.model.IGameState;

import java.util.HashMap;
import java.util.Map;

public class StateService {

    private IGameState currentState = EMPTY_STATE;
    private final Map<String, IGameState> gameStates = new HashMap<>();

    private static final IGameState EMPTY_STATE = () -> {};

    public synchronized void registerGameState(String id, IGameState iGameState) {
        if (gameStates.putIfAbsent(id, iGameState) != null) {
            throw new IllegalArgumentException("Game state already registered: %s".formatted(id));
        }
    }

    public synchronized void replaceGameState(String id, IGameState iGameState) {
        if (!gameStates.containsKey(id)) {
            throw new IllegalArgumentException("Unknown game state: %s".formatted(id));
        }

        gameStates.put(id, iGameState);
    }

    public synchronized void changeGameState(String id) {
        IGameState newState = gameStates.get(id);

        if (newState == null) {
            throw new IllegalArgumentException("Unknown game state: %s".formatted(id));
        }

        currentState.end();
        currentState = newState;
        newState.start();
    }

    public synchronized void onClock() {
        currentState.onClock();
    }
}
