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

import java.util.Collections;
import java.util.List;

@Singleton
public class Delete extends AbstractCommand {

    @Inject
    public Delete(final Injector injector) {
        super(injector.getInstance(WorldOffline.class).build());
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("remove");
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.world.offline"));

        super.serviceProvider.world().delete(src, worldKey, false);

        super.serviceProvider.message()
                .getMessage(src, "success.root.delete")
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
