package fr.bgoodes.uhc.service.hud.impl;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.service.hud.model.Title;
import fr.bgoodes.uhc.service.hud.model.UHCHud;
import org.jetbrains.annotations.NotNull;

public class TitleHud extends UHCHud {

    public static final String HUD_ID = "uhc:title";
    private static final int Z_ORDER = 10;

    private long hideAt;

    public TitleHud(@NotNull PlayerRef playerRef) {
        super(playerRef, HUD_ID, Z_ORDER);
    }

    @Override
    protected void build(@NotNull UICommandBuilder ui) {
        ui.append("Title.ui");
        ui.set("#TitleRoot.Visible", false);
    }

    public synchronized void show(@NotNull Title title) {
        UICommandBuilder ui = new UICommandBuilder();
        Message secondary = title.secondary();

        ui.set("#TitleRoot.Visible", true);
        ui.set("#PrimaryLabel.TextSpans", title.primary());
        ui.set("#SecondaryLabel.Visible", secondary != null);

        if (secondary != null) {
            ui.set("#SecondaryLabel.TextSpans", secondary);
        }

        update(false, ui);

        this.hideAt = System.currentTimeMillis() + title.duration() * 1000L;
    }

    @Override
    public synchronized void refresh() {
        if (hideAt == 0 || System.currentTimeMillis() < hideAt) {
            return;
        }

        UICommandBuilder ui = new UICommandBuilder();
        ui.set("#TitleRoot.Visible", false);
        update(false, ui);

        this.hideAt = 0;
    }
}
