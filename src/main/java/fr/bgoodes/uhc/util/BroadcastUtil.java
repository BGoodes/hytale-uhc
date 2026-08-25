package fr.bgoodes.uhc.util;

import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BroadcastUtil {

    private BroadcastUtil() {}

    public static void message(@NotNull Message message) {
        Universe.get().getPlayers().forEach(
                player -> MessageUtil.send(player, message)
        );

        UHC.LOGGER.atInfo().log(message.toString());
    }

    public static void title(@NotNull Message primary, @Nullable Message secondary, boolean major) {
        Universe.get().getPlayers().forEach(player ->
                EventTitleUtil.showEventTitleToPlayer(
                        player,
                        primary,
                        secondary == null ? Message.empty() : secondary,
                        major
                )
        );
    }

    public static void sound(@NotNull String soundKey) {
        int soundIndex = SoundEvent.getAssetMap().getIndex(soundKey);

        Universe.get().getPlayers().forEach(ref -> {
            SoundUtil.playSoundEvent2dToPlayer(ref, soundIndex, SoundCategory.UI);
        });
    }
}
