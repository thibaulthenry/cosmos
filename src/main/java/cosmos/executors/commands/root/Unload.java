package cosmos.executors.commands.root;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.world.WorldOnline;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Unload extends AbstractCommand {

    @Inject
    public Unload(final Injector injector) {
        super(injector.getInstance(WorldOnline.class).builder().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD_KEY)
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.world.online"));

        this.serviceProvider.world().unload(src, worldKey, false);

        this.serviceProvider.message()
                .getMessage(src, "success.root.unload")
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
