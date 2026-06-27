package fr.bgoodes.uhc.util;

import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MessageUtil {

    private MessageUtil() {}

    public static void message(@NotNull PlayerRef player, @NotNull String message) {
        message(player, message, Color.white);
    }

    public static void message(@NotNull PlayerRef player, @NotNull String message, Color color) {
        player.sendMessage(
                Message.join(
                        Message.raw("UHC").color(Color.MAGENTA).bold(true),
                        Message.raw(" » ").color(Color.DARK_GRAY).bold(true),
                        Message.raw(message).color(color)
                )
        );
    }

    public static void broadcastMessage(@NotNull String message) {
        Universe.get().getPlayers().forEach(ref -> {
            message(ref, message);
        });

        UHC.LOGGER.atInfo().log(message);
    }

    public static void broadcastTitle(@NotNull String primaryTitle, @NotNull String secondaryTitle, boolean isMajor) {
        Universe.get().getPlayers().forEach(ref -> {
            EventTitleUtil.showEventTitleToPlayer(
                    ref,
                    Message.raw(primaryTitle),
                    Message.raw(secondaryTitle),
                    isMajor
            );
        });

        UHC.LOGGER.atInfo().log("%s / %s", primaryTitle, secondaryTitle);
    }

    public static void broadcastSound(@NotNull String soundKey) {
        int soundIndex = SoundEvent.getAssetMap().getIndex(soundKey);

        Universe.get().getPlayers().forEach(ref -> {
            SoundUtil.playSoundEvent2dToPlayer(ref, soundIndex, SoundCategory.UI);
        });
    }
}
