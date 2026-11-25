package fr.bgoodes.uhc.util;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.util.MessageUtil;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class UHCMessageUtil {

    private static final Message PREFIX = Message.join(
            Message.raw("UHC").color(Color.MAGENTA).bold(true),
            Message.raw(" » ").color(Color.GRAY).bold(true)
    );

    private UHCMessageUtil() {}

    public static @NotNull Message tr(@NotNull String key) {
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

    public static void broadcast(@NotNull Message message) {
        broadcast(message, true);
    }

    public static void broadcast(@NotNull Message message, boolean prefix) {
        Universe.get().getPlayers().forEach(
                playerRef -> UHCMessageUtil.send(playerRef, message, prefix)
        );
        UHC.LOGGER.atInfo().log(MessageUtil.toAnsiString(message).toString());
    }
}
