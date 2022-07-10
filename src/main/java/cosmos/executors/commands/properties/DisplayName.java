package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class DisplayName extends AbstractPropertiesCommand {

    public DisplayName() {
        super(
                CosmosParameters.TEXTS_ALL.get().optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<Component> optionalInput = super.serviceProvider.parameter().findComponent(context);
        Optional<Component> optionalValue = properties.displayName();
        Component value = optionalValue.orElse(Component.empty());

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setDisplayName(value);
            // todo crash when save properties
            super.serviceProvider.world().saveProperties(src, properties);
        } else if (!optionalValue.isPresent()) {
            throw super.serviceProvider.message().getError(src, "error.invalid.value", "parameter", "text");
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.display-name.set" : "success.properties.display-name.get")
                .replace("value", value)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
