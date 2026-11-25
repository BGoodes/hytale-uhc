package fr.bgoodes.uhc.listener.option;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.modules.voice.VoiceModule;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.option.OptionChangedEvent;
import fr.bgoodes.uhc.service.config.impl.game.GameConfig;

public class VoiceOptionListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(OptionChangedEvent.class, this::onChanged);
    }

    private void onChanged(OptionChangedEvent event) {
        GameConfig config = UHC.get().getGameConfig();

        if (event.getOption() == config.proximityChat) {
            VoiceModule.get().setVoiceEnabled(config.proximityChat.get());
        } else if (event.getOption() == config.proximityChatDistance) {
            VoiceModule.get().setMaxHearingDistance(config.proximityChatDistance.get());
        }
    }
}
