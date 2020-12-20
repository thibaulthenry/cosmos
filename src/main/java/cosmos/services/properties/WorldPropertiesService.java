package cosmos.services.properties;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.properties.impl.WorldPropertiesServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

@ImplementedBy(WorldPropertiesServiceImpl.class)
public interface WorldPropertiesService extends CosmosService {

    void save(WorldProperties worldProperties) throws CommandException;

    WorldProperties get(CommandContext context) throws CommandException;

    Optional<WorldProperties> get(Audience src);

}
