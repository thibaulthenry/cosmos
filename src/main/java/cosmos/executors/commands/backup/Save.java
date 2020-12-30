package cosmos.executors.commands.backup;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.models.backup.BackupArchetype;
import cosmos.models.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.ServerWorldProperties;

@Singleton
public class Save extends AbstractCommand {

    public Save() {
        super(
                Parameter.worldProperties().setKey(CosmosKeys.WORLD).build(), // TODO Unloaded or disabled
                Parameter.string().setKey(CosmosKeys.TAG).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ServerWorldProperties properties = context.getOne(CosmosKeys.WORLD)
//                .map(Optional::of) todo
//                .orElse(
//                        args.<String>getOne(ArgKeys.DISABLED_WORLD.t)
//                                .flatMap(FinderWorldProperties::getDisabledWorldProperties)
//                )
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.value")); // todo

        final BackupArchetype backupArchetype = new BackupArchetype(properties);

        context.getOne(CosmosKeys.TAG).ifPresent(backupArchetype::setTag);

        try {
            this.serviceProvider.backup().save(backupArchetype);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while saving backup", e);
            throw this.serviceProvider.message()
                    .getMessage(src, "error.backup.save")
                    .replace("backup", backupArchetype)
                    .errorColor()
                    .asException();
        }

        this.serviceProvider.message()
                .getMessage(src, "success.backup.save")
                .replace("world", backupArchetype.getWorldKey())
                .replace("backup", backupArchetype)
                .successColor()
                .sendTo(src);
    }

}
