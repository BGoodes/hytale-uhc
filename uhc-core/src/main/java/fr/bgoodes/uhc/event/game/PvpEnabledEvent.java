package fr.bgoodes.uhc.event.game;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.Nullable;

public class PvpEnabledEvent implements IEvent<Void> {

    private @Nullable Message message = UHCMessageUtil.tr("uhc.game.pvp.enabled.chat");

    public @Nullable Message getMessage() {
        return message;
    }

    public void setMessage(@Nullable Message message) {
        this.message = message;
    }
}
