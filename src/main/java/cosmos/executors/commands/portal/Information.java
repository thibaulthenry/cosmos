package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Collections;
import java.util.List;

@Singleton
public class Information extends AbstractCommand {

    public Information() {
        super(CosmosParameters.PORTAL_ALL.get().build());
    }

    @Override
    protected List<String> additionalAliases() {
        return Collections.singletonList("info");
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {

    }

}
