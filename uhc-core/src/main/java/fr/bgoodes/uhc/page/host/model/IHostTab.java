package fr.bgoodes.uhc.page.host.model;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import fr.bgoodes.uhc.page.host.HostPageData;
import org.jetbrains.annotations.NotNull;

public interface IHostTab {

    @NotNull Message getTitle();

    @NotNull String getDocument();

    void build(@NotNull UICommandBuilder ui, @NotNull UIEventBuilder events);

    default void handle(@NotNull HostPageData data, @NotNull UICommandBuilder ui, @NotNull UIEventBuilder events) {}
}
