package fr.bgoodes.uhc.util;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;

public class BroadcastUtil {

    private BroadcastUtil() {}

    public static void message(@NotNull String message) {
        Universe.get().getPlayers().forEach(ref -> {
            ref.sendMessage(Message.raw(message));
        });

        UHC.LOGGER.atInfo().log(message);
    }

    public static void title(@NotNull String primaryTitle, @NotNull String secondaryTitle, boolean isMajor) {
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

    public static void sound(@NotNull String soundKey) {
        int soundIndex = SoundEvent.getAssetMap().getIndex(soundKey);

        Universe.get().getPlayers().forEach(ref -> {
            SoundUtil.playSoundEvent2dToPlayer(ref, soundIndex, SoundCategory.UI);
        });
    }
}
