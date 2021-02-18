package cosmos.registries.formatter.impl;

import com.google.inject.Singleton;
import cosmos.registries.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class ParameterKeyFormatter implements Formatter<Parameter.Key<?>> {

    @Override
    public TextComponent asText(final Parameter.Key<?> value) {
        return Component.text(value.key());
    }

}