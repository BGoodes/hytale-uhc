package fr.bgoodes.uhc.event.game;

import com.hypixel.hytale.event.ICancellable;
import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.service.hud.model.Title;
import fr.bgoodes.uhc.service.team.Team;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

public class GameEndedEvent implements IEvent<Void>, ICancellable {

    private final @Nullable Team winner;

    private @Nullable Message message;
    private @Nullable Title title;

    private boolean cancelled;

    public GameEndedEvent(@Nullable Team winner) {
        this.winner = winner;
        this.message = defaultMessage(winner);
        this.title = new Title(UHCMessageUtil.tr("uhc.game.ended.title"), defaultMessage(winner), 6);
        this.cancelled = false;
    }

    public @Nullable Team getWinner() {
        return winner;
    }

    public @Nullable Message getMessage() {
        return message;
    }

    public void setMessage(@Nullable Message message) {
        this.message = message;
    }

    public @Nullable Title getTitle() {
        return title;
    }

    public void setTitle(@Nullable Title title) {
        this.title = title;
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
            return UHCMessageUtil.tr("uhc.game.ended.draw");
        }

        Color color = winner.getColor();

        return UHCMessageUtil.tr("uhc.game.ended.winner.chat")
                .param("name", Message.raw(winner.getName()))
                .color(color != null ? color : Color.YELLOW);
    }
}
