package fr.bgoodes.uhc.page.model;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.page.HostPageData;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IOptionRow {

    String ACTION = "Option";

    @NotNull Option<?> getOption();

    @NotNull String getDocument();

    void build(@NotNull String selector, @NotNull UICommandBuilder ui, @NotNull UIEventBuilder events);

    void refresh(@NotNull String selector, @NotNull UICommandBuilder ui);

    @Nullable OptionChangeEvent request(@NotNull HostPageData data, @NotNull PlayerRef source);
}
