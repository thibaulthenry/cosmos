package cosmos.executors.commands.backup;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.backup.BackupArchetype;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Save extends AbstractCommand {

    public Save() {
        super(
                CosmosParameters.WORLD_ONLINE.get().build(),
                Parameter.string().key(CosmosKeys.TAG).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.one(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.WORLD));

        final BackupArchetype backupArchetype = new BackupArchetype(worldKey);
        context.one(CosmosKeys.TAG).ifPresent(backupArchetype::tag);

        try {
            super.serviceProvider.backup().save(backupArchetype);
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while saving backup", e);
            throw super.serviceProvider.message().getError(src, "error.backup.save", "backup", backupArchetype);
        }

        super.serviceProvider.message()
                .getMessage(src, "success.backup.save")
                .replace("backup", backupArchetype)
                .replace("world", backupArchetype.worldKey())
                .green()
                .sendTo(src);
    }

}
