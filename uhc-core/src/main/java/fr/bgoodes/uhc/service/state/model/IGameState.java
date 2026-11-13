package fr.bgoodes.uhc.service.state.model;

public interface IGameState {

    void start();

    default void onClock() {}

    default void end() {}
}
