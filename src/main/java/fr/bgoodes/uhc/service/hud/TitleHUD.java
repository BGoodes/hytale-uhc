package fr.bgoodes.uhc.service.hud;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TitleHUD extends CustomUIHud {

    public static final String HUD_ID = "uhc_title";

    private long secondsUntilHide = -1;

    public TitleHUD(@NotNull PlayerRef playerRef) {
        super(playerRef, HUD_ID);
    }

    @Override
    protected void build(@NotNull UICommandBuilder ui) {
        ui.append("Title.ui");
        ui.set("#TitleRoot.Visible", false);
    }

    public void show(@NotNull Message primary, @Nullable Message secondary, long durationSeconds) {
        UICommandBuilder ui = new UICommandBuilder();
        ui.set("#TitleRoot.Visible", true);
        ui.set("#PrimaryLabel.TextSpans", primary);

        ui.set("#SecondaryLabel.Visible", secondary != null);
        if (secondary != null) {
            ui.set("#SecondaryLabel.TextSpans", secondary);
        }

        update(false, ui);

        this.secondsUntilHide = durationSeconds;
    }

    public void onSecondElapsed() {
        if (secondsUntilHide <= 0) {
            return;
        }

        secondsUntilHide--;

        if (secondsUntilHide == 0) {
            UICommandBuilder ui = new UICommandBuilder();
            ui.set("#TitleRoot.Visible", false);

            update(false, ui);
        }
    }
}
