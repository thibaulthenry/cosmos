package cosmos.services.properties.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.services.message.MessageService;
import cosmos.services.properties.WorldPropertiesService;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

@Singleton
public class WorldPropertiesServiceImpl implements WorldPropertiesService {

    private final MessageService messageService;

    @Inject
    public WorldPropertiesServiceImpl(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
    }

    @Override
    public void save(final WorldProperties worldProperties) {
        Sponge.getServer().getWorldManager().saveProperties(worldProperties).whenComplete((result, e) -> {
            if (e != null) {
                Cosmos.getLogger().error("An error occurred while saving " + worldProperties.getKey().asString() + "'s properties", e);
            }
        });
    }

    @Override
    public WorldProperties get(final CommandContext context) throws CommandException {
        return context.getOne(Parameter.key("world", WorldProperties.class))
                .map(Optional::of)
                .orElse(this.get(context.getCause().getAudience()))
                .orElseThrow(this.messageService.getMessage(context, "error.properties.not_found").asSupplier());
    }

    @Override
    public Optional<WorldProperties> get(final Audience audience) {
        return Optional.ofNullable(audience instanceof Locatable ? ((Locatable) audience).getServerLocation().getWorld().getProperties() : null);
    }
}
