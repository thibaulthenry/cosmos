package cosmos.executors.commands.backup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.world.WorldOffline;
import cosmos.registries.backup.BackupArchetype;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Save extends AbstractCommand {

    @Inject
    public Save(final Injector injector) {
        super(
                injector.getInstance(WorldOffline.class).build(),
                Parameter.string().setKey(CosmosKeys.TAG).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.WORLD));

        final BackupArchetype backupArchetype = new BackupArchetype(worldKey);

        context.getOne(CosmosKeys.TAG).ifPresent(backupArchetype::setTag);

        try {
            super.serviceProvider.backup().save(backupArchetype);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while saving backup", e);
            throw super.serviceProvider.message().getError(src, "error.backup.save", "backup", backupArchetype);
        }

        super.serviceProvider.message()
                .getMessage(src, "success.backup.save")
                .replace("backup", backupArchetype)
                .replace("world", backupArchetype.getWorldKey())
                .green()
                .sendTo(src);
    }

}
