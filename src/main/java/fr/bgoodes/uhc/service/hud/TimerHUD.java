package fr.bgoodes.uhc.service.hud;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.util.TimeUtil;
import org.jetbrains.annotations.NotNull;

public class TimerHUD extends CustomUIHud {

    public static final String HUD_ID = "test";

    public TimerHUD(@NotNull PlayerRef playerRef) {
        super(playerRef, HUD_ID);
    }

    @Override
    protected void build(@NotNull UICommandBuilder ui) {
        ui.append("TestHud.ui");
        ui.set("#TimerLabel.Text", "0s");
    }

    public void refresh(long time) {
        UICommandBuilder ui = new UICommandBuilder();
        ui.set("#TimerLabel.Text", TimeUtil.formatDuration(time));

        update(false, ui);
    }
}
