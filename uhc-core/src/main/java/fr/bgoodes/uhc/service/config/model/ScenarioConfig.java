package fr.bgoodes.uhc.service.config.model;

import fr.bgoodes.uhc.service.config.impl.option.BooleanOption;

public abstract class ScenarioConfig extends UHCConfig {

    public final BooleanOption enabled = add(new BooleanOption("Enabled", false));
}
