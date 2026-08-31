package fr.bgoodes.uhc.util;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MessageUtil {

    private static final Message PREFIX = Message.join(
            Message.raw("UHC").color(Color.MAGENTA).bold(true),
            Message.raw(" » ").color(Color.DARK_GRAY).bold(true)
    );

    private MessageUtil() {}

    public static Message tr(@NotNull String key) {
        return Message.translation(key);
    }

    public static void send(@NotNull PlayerRef playerRef, @NotNull Message message) {
        send(playerRef, message, true);
    }

    public static void send(@NotNull PlayerRef playerRef, @NotNull Message message, boolean prefix) {
        playerRef.sendMessage(
                prefix ? Message.join(PREFIX, message) : message
        );
    }

    public static void reply(@NotNull CommandContext context, @NotNull Message message) {
        context.sendMessage(
                Message.join(PREFIX, message)
        );
    }
}
