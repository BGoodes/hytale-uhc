package fr.bgoodes.uhc.service.hud;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.util.TimeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TimerHUD extends CustomUIHud {

    public static final String HUD_ID = "uhc_timer";

    public TimerHUD(@NotNull PlayerRef playerRef) {
        super(playerRef, HUD_ID);
    }

    @Override
    protected void build(@NotNull UICommandBuilder ui) {
        ui.append("TimerHud.ui");
        ui.set("#TimerRoot.Visible", false);
    }

    public void setVisible(boolean visible) {
        UICommandBuilder ui = new UICommandBuilder();
        ui.set("#TimerRoot.Visible", visible);
        update(false, ui);
    }

    public void refresh(@Nullable Message phaseLabel, long secondsRemaining, long totalElapsedSeconds) {
        boolean hasNextPhase = phaseLabel != null;
        String totalTime = TimeUtil.formatDuration(totalElapsedSeconds);

        UICommandBuilder ui = new UICommandBuilder();
        ui.set("#TwoColumnGroup.Visible", hasNextPhase);
        ui.set("#CenteredGroup.Visible", !hasNextPhase);

        if (hasNextPhase) {
            ui.set("#PhaseLabel.TextSpans", phaseLabel);
            ui.set("#TimerLabel.Text", TimeUtil.formatDuration(secondsRemaining));
            ui.set("#TotalTimeLabel.Text", totalTime);
        } else {
            ui.set("#TotalTimeLabelCentered.Text", totalTime);
        }

        update(false, ui);
    }
}
