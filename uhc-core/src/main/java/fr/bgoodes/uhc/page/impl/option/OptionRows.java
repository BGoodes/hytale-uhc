package fr.bgoodes.uhc.page.impl.option;

import fr.bgoodes.uhc.page.model.IOptionRow;
import fr.bgoodes.uhc.service.config.impl.option.BooleanOption;
import fr.bgoodes.uhc.service.config.impl.option.FloatOption;
import fr.bgoodes.uhc.service.config.impl.option.IntOption;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OptionRows {

    private OptionRows() {}

    //TODO: refacto this ugly thing
    public static @Nullable IOptionRow create(@NotNull Option<?> option) {
        if (option instanceof IntOption intOption) {
            return new IntOptionRow(intOption);
        }

        if (option instanceof FloatOption floatOption) {
            return new FloatOptionRow(floatOption);
        }

        if (option instanceof BooleanOption booleanOption) {
            return new BooleanOptionRow(booleanOption);
        }

        return null;
    }
}
