package fr.bgoodes.uhc.event.game;

import fr.bgoodes.uhc.event.AnnouncementEvent;
import fr.bgoodes.uhc.util.UHCMessageUtil;

public class BorderShrunkEvent extends AnnouncementEvent {

    private final double radius;

    public BorderShrunkEvent(double radius) {
        super(
                UHCMessageUtil.tr("uhc.game.border.reached").param("radius", Math.round(radius)),
                null
        );

        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}
