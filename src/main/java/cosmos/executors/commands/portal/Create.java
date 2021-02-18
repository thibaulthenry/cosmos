package cosmos.executors.commands.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.world.WorldAll;
import cosmos.executors.parameters.impl.world.WorldOnline;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.portal.PortalType;

@Singleton
public class Create extends AbstractCommand {

    @Inject
    public Create(final Injector injector) {
        super(
                Parameter.string().setKey(CosmosKeys.NAME).build()
                // todo trigger block parameter
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey newKey = context.getOne(CosmosKeys.NAME)
                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.NAME));

        super.serviceProvider.portal().create(src, newKey, BlockTypes.VOID_AIR.get()); // todo

        // todo clear selection

        // todo
    }

}
