package cosmos.services.parameter;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.parameter.impl.ParameterServiceImpl;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Optional;

@ImplementedBy(ParameterServiceImpl.class)
public interface ParameterService extends CosmosService {

    int extremum(CommandContext context, Parameter.Key<Integer> integerKey, boolean negativeBound) throws CommandException;

    Optional<Integer> findExtremum(CommandContext context, Parameter.Key<Integer> integerKey, boolean negativeBound);

    Optional<Component> findComponent(CommandContext context);

}
