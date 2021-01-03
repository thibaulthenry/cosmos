package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class EnableCommandBlocks extends AbstractPropertiesCommand {

    public EnableCommandBlocks() {
        super(Parameter.bool().setKey(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<Boolean> optionalInput = context.getOne(CosmosKeys.STATE);
        boolean value = properties.commands();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setCommands(value);
            this.serviceProvider.world().saveProperties(src, properties);
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.enable-command-blocks.set" : "success.properties.enable-command-blocks.get")
                .replace("world", properties)
                .condition("value", value)
                .green()
                .sendTo(src);
    }
}
