package cosmos.executors.commands.backup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.models.backup.BackupArchetype;
import cosmos.models.enums.Units;
import cosmos.models.parameters.CosmosKeys;
import cosmos.models.parameters.impl.backup.BackupChoices;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Tag extends AbstractCommand {

    @Inject
    public Tag(final Injector injector) {
        super(
                injector.getInstance(BackupChoices.class).builder().build(),
                Parameter.string().setKey(CosmosKeys.TAG).build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final BackupArchetype backupArchetype = context.getOne(CosmosKeys.BACKUP)
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.choices.backup"));

        final String tag = context.getOne(CosmosKeys.TAG)
                .map(input -> input.replaceAll("_", "-"))
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.value"));

        if (this.serviceProvider.validation().doesOverflowMaxLength(tag, Units.NAME_MAX_LENGTH)) {
            throw this.serviceProvider.message()
                    .getMessage(src, "error.overflow.tag")
                    .replace("tag", tag)
                    .errorColor()
                    .asException();
        }

        backupArchetype.setTag(tag);

        try {
            this.serviceProvider.backup().tag(backupArchetype);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while tagging backup", e);
            throw this.serviceProvider.message()
                    .getMessage(src, "error.backup.tag")
                    .replace("backup", backupArchetype)
                    .errorColor()
                    .asException();
        }

        this.serviceProvider.message()
                .getMessage(src, "success.backup.tag")
                .replace("backup", backupArchetype)
                .replace("tag", tag)
                .successColor()
                .sendTo(src);
    }

}
