package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParser;
import org.spongepowered.api.world.server.WorldTemplate;

import java.util.Collections;
import java.util.List;

@Singleton
public class New extends AbstractCommand {

    public New() {
        super(Parameter.string().setKey(CosmosKeys.NAME).build());
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("create");
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey newKey = context.getOne(CosmosKeys.NAME)
                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.NAME));

        Sponge.getServer().getWorldManager().saveTemplate(WorldTemplate.overworld().asBuilder().key(newKey).build());
    }

}
