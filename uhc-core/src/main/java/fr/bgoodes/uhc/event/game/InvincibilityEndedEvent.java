package fr.bgoodes.uhc.event.game;

import fr.bgoodes.uhc.event.AnnouncementEvent;
import fr.bgoodes.uhc.service.hud.model.Title;
import fr.bgoodes.uhc.util.UHCMessageUtil;

public class InvincibilityEndedEvent extends AnnouncementEvent {

    public InvincibilityEndedEvent() {
        super(
                UHCMessageUtil.tr("uhc.game.invincibility.ended.chat"),
                new Title(UHCMessageUtil.tr("uhc.game.invincibility.ended.title"))
        );
    }
}
