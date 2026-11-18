package fr.bgoodes.uhc.event.game;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.Nullable;

public class BorderShrunkEvent implements IEvent<Void> {

    private final double radius;

    private @Nullable Message message;

    public BorderShrunkEvent(double radius) {
        this.radius = radius;
        this.message = UHCMessageUtil.tr("uhc.game.border.shrunk.chat")
                .param("radius", Math.round(radius));
    }

    public double getRadius() {
        return radius;
    }

    public @Nullable Message getMessage() {
        return message;
    }

    public void setMessage(@Nullable Message message) {
        this.message = message;
    }
}
