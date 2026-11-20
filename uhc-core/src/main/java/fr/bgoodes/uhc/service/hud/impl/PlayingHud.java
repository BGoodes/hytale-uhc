package fr.bgoodes.uhc.service.hud.impl;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.config.impl.game.BorderPhase;
import fr.bgoodes.uhc.service.config.impl.game.GameConfig;
import fr.bgoodes.uhc.service.hud.model.UHCHud;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.service.state.impl.PlayingState;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class PlayingHud extends UHCHud {

    private final PlayingState state;

    public PlayingHud(@NotNull PlayerRef playerRef, @NotNull PlayingState state) {
        super(playerRef, IGameState.HUD_KEY);
        this.state = state;
    }

    @Override
    protected void build(@NotNull UICommandBuilder ui) {
        ui.append("UHC/Hud/Game.ui");
        ui.set("#NextEventLabel.Visible", false);
    }

    @Override
    public void refresh() {
        int time = state.getTime();
        NextEvent next = findNextEvent(time);
        UICommandBuilder ui = new UICommandBuilder();

        ui.set("#TimeLabel.Text", UHCTimeUtil.formatDuration(time));
        ui.set("#NextEventLabel.Visible", next != null);

        if (next != null) {
            ui.set("#NextEventLabel.TextSpans", UHCMessageUtil.tr(next.key())
                    .param("time", UHCTimeUtil.formatDuration(next.remaining())));
        }

        update(false, ui);
    }

    private @Nullable NextEvent findNextEvent(int time) {
        GameConfig config = UHC.get().getGameConfig();
        NextEvent next = null;

        next = earliest(next, time, config.invincibilityTime.get(), "uhc.game.invincibility.countdown.hud");
        next = earliest(next, time, config.pvpTime.get(), "uhc.game.pvp.countdown.hud");

        for (BorderPhase phase : config.borderPhases.get()) {
            next = earliest(next, time, phase.time(), "uhc.game.border.countdown.hud");
        }

        return next;
    }

    private @Nullable NextEvent earliest(@Nullable NextEvent current, int time, int eventTime, String key) {
        int remaining = eventTime - time;

        if (remaining <= 0 || (current != null && current.remaining() <= remaining)) {
            return current;
        }

        return new NextEvent(key, remaining);
    }

    private record NextEvent(String key, int remaining) {}
}
