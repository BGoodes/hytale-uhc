package fr.bgoodes.uhc.event.game;

import fr.bgoodes.uhc.event.AnnouncementEvent;
import fr.bgoodes.uhc.service.hud.model.Title;
import fr.bgoodes.uhc.util.UHCMessageUtil;

public class PvpEnabledEvent extends AnnouncementEvent {

    public PvpEnabledEvent() {
        super(
                UHCMessageUtil.tr("uhc.game.pvp.chat"),
                new Title(UHCMessageUtil.tr("uhc.game.pvp.title"))
        );
    }
}
