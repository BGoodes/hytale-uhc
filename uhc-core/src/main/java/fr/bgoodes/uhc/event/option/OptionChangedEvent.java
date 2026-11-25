package fr.bgoodes.uhc.event.option;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OptionChangedEvent implements IEvent<Void> {

    private final @Nullable PlayerRef source;
    private final Option<?> option;
    private final Object oldValue;

    public OptionChangedEvent(@Nullable PlayerRef source, @NotNull Option<?> option, @NotNull Object oldValue) {
        this.source = source;
        this.option = option;
        this.oldValue = oldValue;
    }

    public @Nullable PlayerRef getSource() {
        return source;
    }

    public @NotNull Option<?> getOption() {
        return option;
    }

    public @NotNull Object getOldValue() {
        return oldValue;
    }
}
