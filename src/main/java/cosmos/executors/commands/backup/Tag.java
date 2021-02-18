package cosmos.executors.commands.backup;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.Units;
import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.backup.BackupArchetype;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Tag extends AbstractCommand {

    public Tag() {
        super(
                CosmosParameters.BACKUP.get().build(),
                Parameter.string().key(CosmosKeys.TAG).build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final BackupArchetype backupArchetype = context.one(CosmosKeys.BACKUP)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.backup", "param", CosmosKeys.BACKUP));

        final String tag = context.one(CosmosKeys.TAG)
                .map(input -> input.replaceAll("_", "-"))
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value"));

        if (super.serviceProvider.validation().doesOverflowMaxLength(tag, Units.NAME_MAX_LENGTH)) {
            throw super.serviceProvider.message().getError(src, "error.invalid.backup.overflow", "tag", tag);
        }

        try {
            super.serviceProvider.backup().tag(backupArchetype, tag);
        } catch (final Exception e) {
            Cosmos.logger().error("An unexpected error occurred while tagging backup", e);
            throw super.serviceProvider.message().getError(src, "error.backup.tag", "backup", backupArchetype);
        }

        super.serviceProvider.message()
                .getMessage(src, "success.backup.tag")
                .replace("backup", backupArchetype)
                .replace("tag", tag)
                .green()
                .sendTo(src);
    }

}
