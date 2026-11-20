package fr.bgoodes.uhc.page.impl.page.core;

import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CoreCategory(@NotNull Message name, @NotNull List<Option<?>> options) {}
