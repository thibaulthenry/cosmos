package cosmos.executors.commands.portal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.portal.PortalAll;
import cosmos.executors.parameters.impl.portal.PortalFrame;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Collections;
import java.util.List;

@Singleton
public class Information extends AbstractCommand {

    @Inject
    public Information() {
        super(new PortalAll().key(CosmosKeys.PORTAL_COSMOS).build());
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("info");
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {

    }

}
