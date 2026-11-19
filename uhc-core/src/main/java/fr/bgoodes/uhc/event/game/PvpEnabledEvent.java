package fr.bgoodes.uhc.event.game;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.service.hud.model.Title;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.Nullable;

public class PvpEnabledEvent implements IEvent<Void> {

    private @Nullable Message message = UHCMessageUtil.tr("uhc.game.pvp.enabled.chat");
    private @Nullable Title title = new Title(UHCMessageUtil.tr("uhc.game.pvp.enabled.title"));

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
}
