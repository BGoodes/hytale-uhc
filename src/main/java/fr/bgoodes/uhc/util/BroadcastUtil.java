package fr.bgoodes.uhc.util;

import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import org.jetbrains.annotations.NotNull;

public class BroadcastUtil {

    private BroadcastUtil() {}

    public static void message(@NotNull Message message) {
        message(message, true);
    }

    public static void message(@NotNull Message message, boolean prefix) {
        Universe.get().getPlayers().forEach(
                playerRef -> MessageUtil.send(playerRef, message, prefix)
        );
    }

    public static void sound(@NotNull String soundKey) {
        int soundIndex = SoundEvent.getAssetMap().getIndex(soundKey);

        Universe.get().getPlayers().forEach(playerRef -> {
            SoundUtil.playSoundEvent2dToPlayer(playerRef, soundIndex, SoundCategory.UI);
        });
    }
}
