package fr.bgoodes.uhc.event.option;

import com.hypixel.hytale.event.ICancellable;
import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OptionChangeEvent implements IEvent<Void>, ICancellable {

    private final @Nullable PlayerRef source;
    private final Option<?> option;
    private final Object newValue;

    private boolean cancelled;

    public <T> OptionChangeEvent(@Nullable PlayerRef source, @NotNull Option<T> option, @NotNull T newValue) {
        this.source = source;
        this.option = option;
        this.newValue = newValue;
        this.cancelled = false;
    }

    public @Nullable PlayerRef getSource() {
        return source;
    }

    public @NotNull Option<?> getOption() {
        return option;
    }

    public @NotNull Object getNewValue() {
        return newValue;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
