package fr.bgoodes.uhc.event.game;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.Nullable;

public class BorderShrinkEvent implements IEvent<Void> {

    private final double targetRadius;
    private final double speed;

    private @Nullable Message message;

    public BorderShrinkEvent(double targetRadius, double speed) {
        this.targetRadius = targetRadius;
        this.speed = speed;
        this.message = UHCMessageUtil.tr("uhc.game.border.shrinking.chat")
                .param("radius", Math.round(targetRadius));
    }

    public double getTargetRadius() {
        return targetRadius;
    }

    public double getSpeed() {
        return speed;
    }

    public @Nullable Message getMessage() {
        return message;
    }

    public void setMessage(@Nullable Message message) {
        this.message = message;
    }
}
