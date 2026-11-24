package fr.bgoodes.uhc.service.config.impl.game;

import fr.bgoodes.uhc.service.config.model.UHCConfig;
import fr.bgoodes.uhc.service.config.impl.option.BooleanOption;
import fr.bgoodes.uhc.service.config.impl.option.FloatOption;
import fr.bgoodes.uhc.service.config.impl.option.IntOption;

public class GameConfig extends UHCConfig {

    public static final String CONFIG_ID = "uhc";

    public final BooleanOption spawnCenter = add(new BooleanOption("SpawnCenter", false));
    public final BooleanOption teamSpawnTogether = add(new BooleanOption("TeamSpawnTogether", true));

    public final IntOption invincibilityTime = add(new IntOption("InvincibilityTime", 60, 0, 3600));
    public final IntOption pvpTime = add(new IntOption("PvpTime", 600, 0, 7200));

    public final IntOption borderStartRadius = add(new IntOption("BorderStartRadius", 1600, 50, 10000));
    public final FloatOption borderDamage = add(new FloatOption("BorderDamage", 10f, 0f, 100f));
    public final BorderPhasesOption borderPhases = add(new BorderPhasesOption("BorderPhases", new BorderPhase(2400, 100, 1)));

    public final IntOption disconnectTimeout = add(new IntOption("DisconnectTimeout", 300, 0, 3600));

    public final BooleanOption proximityChat = add(new BooleanOption("ProximityChat", true));
    public final IntOption proximityChatDistance = add(new IntOption("ProximityChatDistance", 50, 5, 200));
}
