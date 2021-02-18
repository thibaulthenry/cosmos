package cosmos.executors.commands.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.world.WorldAll;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collections;
import java.util.List;

@Singleton
public class Information extends AbstractCommand {

    @Inject
    public Information(final Injector injector) {
        super(
                Parameter.string().setKey(CosmosKeys.NAME).build(),
                injector.getInstance(WorldAll.class).key(CosmosKeys.WORLD_DESTINATION).build(),
                Parameter.vector3d().setKey(CosmosKeys.X_Y_Z).build()
        );
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("info");
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {

    }

}
