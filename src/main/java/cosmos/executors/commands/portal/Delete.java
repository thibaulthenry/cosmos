package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.Directories;
import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class Delete extends AbstractCommand {

    public Delete() {
        super(CosmosParameters.PORTAL_ALL.get().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final CosmosPortal portal = context.one(CosmosKeys.PORTAL_COSMOS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_COSMOS));

        super.serviceProvider.portal().delete(src, portal.key());

        try {
            final Optional<Path> optionalPath = super.serviceProvider.finder().findCosmosPath(Directories.PORTALS, portal.key());

            if (optionalPath.isPresent()) {
                Files.deleteIfExists(optionalPath.get());
            }
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while deleting portal", e);
            throw super.serviceProvider.message().getError(src, "error.portal.delete", "portal", portal);
        }

        super.serviceProvider.message()
                .getMessage(src, "success.portal.delete")
                .replace("portal", portal)
                .green()
                .sendTo(src);
    }

}
