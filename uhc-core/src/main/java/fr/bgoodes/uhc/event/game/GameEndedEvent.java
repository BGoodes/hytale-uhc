package fr.bgoodes.uhc.event.game;

import com.hypixel.hytale.event.ICancellable;
import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.event.AnnouncementEvent;
import fr.bgoodes.uhc.service.hud.model.Title;
import fr.bgoodes.uhc.service.team.Team;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

public class GameEndedEvent extends AnnouncementEvent implements ICancellable {

    private final @Nullable Team winner;

    private boolean cancelled;

    public GameEndedEvent(@Nullable Team winner) {
        super(
                defaultMessage(winner),
                new Title(UHCMessageUtil.tr("uhc.game.end.title"), defaultMessage(winner), 6)
        );

        this.winner = winner;
        this.cancelled = false;
    }

    public @Nullable Team getWinner() {
        return winner;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static Message defaultMessage(@Nullable Team winner) {
        if (winner == null) {
            return UHCMessageUtil.tr("uhc.game.end.draw");
        }

        Color color = winner.getColor();

        return UHCMessageUtil.tr("uhc.game.end.winner")
                .param("name", winner.getName())
                .color(color != null ? color : Color.YELLOW);
    }
}
