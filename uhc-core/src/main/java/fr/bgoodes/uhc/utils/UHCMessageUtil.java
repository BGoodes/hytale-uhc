package fr.bgoodes.uhc.utils;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

import java.awt.*;

public class UHCMessageUtil {

    private static final Message PREFIX = Message.join(
            Message.raw("UHC").color(Color.MAGENTA).bold(true),
            Message.raw(" » ").color(Color.DARK_GRAY).bold(true)
    );

    private UHCMessageUtil() {}

    public static Message tr(String key) {
        return Message.translation(key);
    }

    public static void send(PlayerRef playerRef, Message message) {
        send(playerRef, message, true);
    }

    public static void send(PlayerRef playerRef, Message message, boolean prefix) {
        playerRef.sendMessage(
                prefix ? Message.join(PREFIX, message) : message
        );
    }

    public static void reply(CommandContext context, Message message) {
        context.sendMessage(
                Message.join(PREFIX, message)
        );
    }

    public static void broadcast(Message message) {
        message(message, true);
    }

    public static void message(Message message, boolean prefix) {
        Universe.get().getPlayers().forEach(
                playerRef -> UHCMessageUtil.send(playerRef, message, prefix)
        );
    }
}
