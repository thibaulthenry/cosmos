package cosmos.executors.commands.backup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.models.backup.BackupArchetype;
import cosmos.models.parameters.CosmosKeys;
import cosmos.models.parameters.impl.backup.BackupChoices;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Optional;

@Singleton
public class Reset extends AbstractCommand {

    @Inject
    public Reset(final Injector injector) {
        super(injector.getInstance(BackupChoices.class).builder().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final BackupArchetype backupArchetype = context.getOne(CosmosKeys.BACKUP)
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.choices.backup"));

        final Optional<ServerWorld> optionalLinkedWorld = backupArchetype.getLinkedWorld();

        if (optionalLinkedWorld.isPresent()) {
            this.serviceProvider.world().unload(src, optionalLinkedWorld.get().getProperties(), false);
        }

        this.serviceProvider.world().restore(src, backupArchetype, true);

        this.serviceProvider.world().load(src, backupArchetype.getWorldKey(), true);

        this.serviceProvider.message()
                .getMessage(src, "success.backup.reset")
                .replace("world", backupArchetype.getWorldKey())
                .replace("backup", backupArchetype)
                .successColor()
                .sendTo(src);
    }

}
