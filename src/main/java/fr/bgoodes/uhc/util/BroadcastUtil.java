package fr.bgoodes.uhc.util;

import com.hypixel.hytale.protocol.ItemWithAllMetadata;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.hypixel.hytale.server.core.util.NotificationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BroadcastUtil {

    private BroadcastUtil() {}

    public static void message(@NotNull Message message) {
        Universe.get().getPlayers().forEach(
                playerRef -> MessageUtil.send(playerRef, message)
        );
    }

    public static void title(@NotNull Message primary, @Nullable Message secondary, boolean major) {
        Universe.get().getPlayers().forEach(playerRef ->
                EventTitleUtil.showEventTitleToPlayer(
                        playerRef,
                        primary,
                        secondary == null ? Message.empty() : secondary,
                        major
                )
        );
    }

    public static void notification(@NotNull Message message, @Nullable String iconId, @Nullable NotificationStyle style) {
        notification(message, null, iconId, style);
    }

    public static void notification(
            @NotNull Message primary,
            @Nullable Message secondary,
            @Nullable String iconId,
            @Nullable NotificationStyle style
    ) {
        Universe.get().getPlayers().forEach(playerRef -> {
            PacketHandler packetHandler = playerRef.getPacketHandler();

            ItemWithAllMetadata icon = null;
            if (iconId != null) {
                icon = new ItemStack(iconId, 1).toPacket();
            }

            NotificationUtil.sendNotification(
                    packetHandler,
                    primary,
                    secondary,
                    icon,
                    style == null ? NotificationStyle.Default : style
             );
        });
    }

    public static void sound(@NotNull String soundKey) {
        int soundIndex = SoundEvent.getAssetMap().getIndex(soundKey);

        Universe.get().getPlayers().forEach(playerRef -> {
            SoundUtil.playSoundEvent2dToPlayer(playerRef, soundIndex, SoundCategory.UI);
        });
    }
}
