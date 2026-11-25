package fr.bgoodes.uhc.listener.hytale;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.team.Team;

import java.awt.Color;

public class ChatListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.registerAsyncGlobal(PlayerChatEvent.class, future -> future.thenApply(this::onChat));
    }

    private PlayerChatEvent onChat(PlayerChatEvent event) {
        event.setFormatter(this::format);
        return event;
    }

    private Message format(PlayerRef sender, String content) {
        Team team = UHC.get().getTeamService().getPlayerTeam(sender.getUuid());
        Message message = Message.empty();

        if (team != null && team.getColor() != null) {
            message.insert(Message.raw("• ").color(team.getColor()));
        }

        return message
                .insert(Message.raw(sender.getUsername()))
                .insert(Message.raw(" » ").color(Color.GRAY))
                .insert(content);
    }
}
