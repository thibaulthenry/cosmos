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

@Singleton
public class Rename extends AbstractCommand {

    @Inject
    public Rename(final Injector injector) {
        super(
                injector.getInstance(WorldAll.class).builder().build(),
                Parameter.string().setKey(CosmosKeys.NAME).build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD_KEY)
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.world"));

        final ResourceKey renameKey = context.getOne(CosmosKeys.NAME)
                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
                .orElseThrow(
                        this.serviceProvider.message()
                                .getMessage(src, "error.invalid.value")
                                .replace("parameter", CosmosKeys.NAME)
                                .asSupplier()
                );

        this.serviceProvider.world().rename(src, worldKey, renameKey, true);

        this.serviceProvider.message()
                .getMessage(src, "success.root.rename")
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
