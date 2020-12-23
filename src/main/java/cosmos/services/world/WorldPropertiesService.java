package cosmos.services.world;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.world.impl.WorldPropertiesServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerWorldProperties;

import java.util.Optional;

@ImplementedBy(WorldPropertiesServiceImpl.class)
public interface WorldPropertiesService extends CosmosService {

    void save(ServerWorldProperties worldProperties) throws CommandException;

    ServerWorldProperties get(CommandContext context) throws CommandException;

    Optional<ServerWorldProperties> get(Audience src);

}
