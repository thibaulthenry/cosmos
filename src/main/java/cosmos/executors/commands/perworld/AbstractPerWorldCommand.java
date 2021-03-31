package cosmos.executors.commands.perworld;

import cosmos.constants.CosmosKeys;
import cosmos.constants.PerWorldFeatures;
import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.listener.impl.perworld.AbstractPerWorldListener;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

abstract class AbstractPerWorldCommand extends AbstractCommand {

    AbstractPerWorldCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        final PerWorldFeatures feature = context.one(CosmosKeys.PER_WORLD_FEATURE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PER_WORLD_FEATURE));

        this.run(src, context, feature);
    }

    protected abstract void run(Audience src, CommandContext context, PerWorldFeatures feature) throws CommandException;

}
