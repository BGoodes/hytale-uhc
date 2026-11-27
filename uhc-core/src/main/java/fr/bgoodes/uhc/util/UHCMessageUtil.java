package fr.bgoodes.uhc.util;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.MessageUtil;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class UHCMessageUtil {

    public static final Color PREFIX_COLOR = new Color(0xAA00AA);
    public static final Color SUCCESS_COLOR = new Color(0x55FF55);
    public static final Color ERROR_COLOR = new Color(0xFF5555);

    private static final Message PREFIX = Message.join(
            Message.raw("UHC").color(PREFIX_COLOR).bold(true),
            Message.raw(" » ").color(Color.DARK_GRAY).bold(true)
    );

    private UHCMessageUtil() {}

    public static @NotNull Message tr(@NotNull String key) {
        return Message.translation(key);
    }

    public static void send(@NotNull PlayerRef playerRef, @NotNull Message message) {
        send(playerRef, message, true);
    }

    public static void send(@NotNull PlayerRef playerRef, @NotNull Message message, boolean prefix) {
        playerRef.sendMessage(format(message, prefix));
    }

    public static void reply(@NotNull CommandContext context, @NotNull Message message) {
        context.sendMessage(format(message, true));
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

    public static @NotNull Message tag(@NotNull String symbol, @NotNull Color color) {
        return Message.join(
                Message.raw("[").color(Color.DARK_GRAY),
                Message.raw(symbol).color(color).bold(true),
                Message.raw("] ").color(Color.DARK_GRAY)
        );
    }

    private static Message format(Message message, boolean prefix) {
        return prefix ? Message.join(PREFIX, message) : message;
    }
}
