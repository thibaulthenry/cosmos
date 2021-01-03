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
                injector.getInstance(WorldOffline.class).builder().build(),
                Parameter.string().setKey(CosmosKeys.TAG).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD_KEY)
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.world"));

        final BackupArchetype backupArchetype = BackupArchetype.fromKey(worldKey)
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.backup"));

        context.getOne(CosmosKeys.TAG).ifPresent(backupArchetype::setTag);

        try {
            this.serviceProvider.backup().save(backupArchetype);
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while saving backup", e);
            throw this.serviceProvider.message()
                    .getMessage(src, "error.backup.save")
                    .replace("backup", backupArchetype)
                    .asException();
        }

        this.serviceProvider.message()
                .getMessage(src, "success.backup.save")
                .replace("world", backupArchetype.getWorldKey())
                .replace("backup", backupArchetype)
                .green()
                .sendTo(src);
    }

}
