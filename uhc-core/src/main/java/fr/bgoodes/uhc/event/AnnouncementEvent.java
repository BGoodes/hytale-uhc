package fr.bgoodes.uhc.event;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.service.hud.model.Title;
import org.jetbrains.annotations.Nullable;

public abstract class AnnouncementEvent implements IEvent<Void> {

    public static final String DEFAULT_SOUND = "SFX_Drum_Stinger";

    private @Nullable Message message;
    private @Nullable Title title;
    private @Nullable String sound;

    protected AnnouncementEvent(@Nullable Message message, @Nullable Title title) {
        this(message, title, DEFAULT_SOUND);
    }

    protected AnnouncementEvent(@Nullable Message message, @Nullable Title title, @Nullable String sound) {
        this.message = message;
        this.title = title;
        this.sound = sound;
    }

    public @Nullable Message getMessage() {
        return message;
    }

    public void setMessage(@Nullable Message message) {
        this.message = message;
    }

    public @Nullable Title getTitle() {
        return title;
    }

    public void setTitle(@Nullable Title title) {
        this.title = title;
    }

    public @Nullable String getSound() {
        return sound;
    }

    public void setSound(@Nullable String sound) {
        this.sound = sound;
    }
}
