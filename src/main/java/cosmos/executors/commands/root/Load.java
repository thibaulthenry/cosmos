package cosmos.executors.commands.root;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.world.WorldOffline;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Load extends AbstractCommand {

    @Inject
    public Load(final Injector injector) {
        super(injector.getInstance(WorldOffline.class).build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.world.offline"));

        super.serviceProvider.world().load(src, worldKey, false);

        super.serviceProvider.message()
                .getMessage(src, "success.root.load")
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
