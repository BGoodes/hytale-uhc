package fr.bgoodes.uhc.mode.example.states;

import fr.bgoodes.uhc.mode.example.ExampleUHC;
import fr.bgoodes.uhc.service.state.impl.PlayingState;

public class ExamplePlayingState extends PlayingState {

    @Override
    public void onClock() {
        super.onClock();
        ExampleUHC.LOGGER.atInfo().log("TEST EXAMPLE UHC");
    }
}
