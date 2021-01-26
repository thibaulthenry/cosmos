package cosmos.executors.commands.root;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.world.WorldAll;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collections;
import java.util.List;

@Singleton
public class Copy extends AbstractCommand {

    @Inject
    public Copy(final Injector injector) {
        super(
                injector.getInstance(WorldAll.class).build(),
                Parameter.string().setKey(CosmosKeys.NAME).build()
        );
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("copy");
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.world.online"));

        final ResourceKey copyKey = context.getOne(CosmosKeys.NAME)
                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.NAME));

        super.serviceProvider.world().copy(src, worldKey, copyKey, true);

        super.serviceProvider.message()
                .getMessage(src, "success.root.copy")
                .replace("world", worldKey)
                .replace("copy", copyKey)
                .green()
                .sendTo(src);
    }

}
