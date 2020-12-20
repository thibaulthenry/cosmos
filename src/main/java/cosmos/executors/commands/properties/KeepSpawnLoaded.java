package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

@Singleton
public class KeepSpawnLoaded extends AbstractPropertiesCommand {

    public KeepSpawnLoaded() {
        super(Parameter.bool().setKey(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties) throws CommandException {
        final Optional<Boolean> optionalInput = context.getOne(CosmosKeys.STATE);
        boolean value = properties.doesKeepSpawnLoaded();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setKeepSpawnLoaded(value);
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.keep-spawn-loaded.set" : "success.properties.keep-spawn-loaded.get")
                .replace("world", properties)
                .condition("value", value)
                .condition("tip", optionalInput.isPresent() && !value)
                .successColor()
                .sendTo(src);
    }
}
