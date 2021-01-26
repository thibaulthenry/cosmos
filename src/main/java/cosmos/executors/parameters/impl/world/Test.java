package cosmos.executors.parameters.impl.world;

import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.entity.Entity;

import java.util.List;
import java.util.Optional;

public class Test implements ValueParameter<List<Entity>> {

    private final ValueParameter<List<Entity>> entitiesValueParameter;

    public Test() {
        this.entitiesValueParameter = ResourceKeyedValueParameters.MANY_ENTITIES.get();
    }

    @Override
    public Optional<? extends List<Entity>> getValue(Parameter.Key<? super List<Entity>> parameterKey, ArgumentReader.Mutable reader, CommandContext.Builder context) throws ArgumentParseException {
        return entitiesValueParameter.getValue(parameterKey, reader, context);
    }

    @Override
    public List<String> complete(CommandContext context, String currentInput) {
        return entitiesValueParameter.complete(context, currentInput);
    }

}
