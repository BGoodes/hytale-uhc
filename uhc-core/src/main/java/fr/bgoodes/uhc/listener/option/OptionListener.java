package fr.bgoodes.uhc.listener.option;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.event.option.OptionChangedEvent;
import fr.bgoodes.uhc.service.config.model.Option;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;

public class OptionListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.EARLY, OptionChangeEvent.class, this::onCheck);
        eventRegistry.register(EventPriority.LATE, OptionChangeEvent.class, this::onApply);
    }

    private void onCheck(OptionChangeEvent event) {
        if (!UHC.get().getFlagService().isEnabled(UHCFlag.CONFIG) || !unchecked(event.getOption()).isValid(event.getNewValue())) {
            event.setCancelled(true);
        }
    }

    private void onApply(OptionChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Option<?> option = event.getOption();
        Object oldValue = option.get();

        if (unchecked(option).set(event.getNewValue())) {
            HytaleServer.get().getEventBus()
                    .dispatchFor(OptionChangedEvent.class)
                    .dispatch(new OptionChangedEvent(event.getSource(), option, oldValue));
        }
    }

    @SuppressWarnings("unchecked")
    private static Option<Object> unchecked(Option<?> option) {
        return (Option<Object>) option;
    }
}
