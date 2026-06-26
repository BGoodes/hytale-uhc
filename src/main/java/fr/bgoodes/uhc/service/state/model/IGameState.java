package fr.bgoodes.uhc.service.state.model;

public interface IGameState {

    void onClock();

    void onEnter();
    
    void onExit();
}
