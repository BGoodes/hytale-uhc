package fr.bgoodes.uhc.service.team;

import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Locale;

public enum TeamColor {

    RED(new Color(0xFF5555)),
    BLUE(new Color(0x5555FF)),
    GREEN(new Color(0x55FF55)),
    YELLOW(new Color(0xFFFF55)),
    PURPLE(new Color(0xAA55FF)),
    ORANGE(new Color(0xFFAA00)),
    CYAN(new Color(0x55FFFF)),
    PINK(new Color(0xFF55FF)),
    WHITE(new Color(0xFFFFFF)),
    GRAY(new Color(0xAAAAAA)),
    DARK_RED(new Color(0xAA0000)),
    DARK_GREEN(new Color(0x00AA00)),
    DARK_BLUE(new Color(0x2E4FD6)),
    TEAL(new Color(0x00AAAA)),
    MAGENTA(new Color(0xAA00AA)),
    BROWN(new Color(0xA0522D));

    private final Color color;

    TeamColor(Color color) {
        this.color = color;
    }

    public @NotNull String getId() {
        return name().toLowerCase(Locale.ROOT);
    }

    public @NotNull Color getColor() {
        return color;
    }

    public @NotNull Message getName() {
        return UHCMessageUtil.tr("uhc.team.color." + getId());
    }
}
