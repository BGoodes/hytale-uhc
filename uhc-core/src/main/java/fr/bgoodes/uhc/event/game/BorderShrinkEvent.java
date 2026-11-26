package fr.bgoodes.uhc.event.game;

import fr.bgoodes.uhc.event.AnnouncementEvent;
import fr.bgoodes.uhc.service.hud.model.Title;
import fr.bgoodes.uhc.util.UHCMessageUtil;

public class BorderShrinkEvent extends AnnouncementEvent {

    private final double targetRadius;
    private final double speed;

    public BorderShrinkEvent(double targetRadius, double speed) {
        super(
                UHCMessageUtil.tr("uhc.game.border.chat").param("radius", Math.round(targetRadius)),
                new Title(UHCMessageUtil.tr("uhc.game.border.title"))
        );

        this.targetRadius = targetRadius;
        this.speed = speed;
    }

    public double getTargetRadius() {
        return targetRadius;
    }

    public double getSpeed() {
        return speed;
    }
}
