package fr.bgoodes.uhc.event.game;

import fr.bgoodes.uhc.event.AnnouncementEvent;
import fr.bgoodes.uhc.service.hud.model.Title;
import fr.bgoodes.uhc.util.UHCMessageUtil;

public class GameStartedEvent extends AnnouncementEvent {

    public GameStartedEvent() {
        super(
                UHCMessageUtil.tr("uhc.game.start.chat"),
                new Title(UHCMessageUtil.tr("uhc.game.start.title"))
        );
    }
}
