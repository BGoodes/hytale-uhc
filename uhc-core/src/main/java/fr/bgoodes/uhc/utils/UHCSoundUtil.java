package fr.bgoodes.uhc.utils;

import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;

public class UHCSoundUtil {

    public static void broadcast(String soundKey) {
        int soundIndex = SoundEvent.getAssetMap().getIndex(soundKey);

        Universe.get().getPlayers().forEach(playerRef -> {
            SoundUtil.playSoundEvent2dToPlayer(playerRef, soundIndex, SoundCategory.UI);
        });
    }
}
