package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.models.parameters.CosmosKeys;
import cosmos.models.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.dimension.DimensionType;
import org.spongepowered.api.world.dimension.DimensionTypes;
import org.spongepowered.api.world.server.ServerWorldProperties;

@Singleton
public class New extends AbstractCommand {

    public New() {
        super(
                Parameter.string().setKey(CosmosKeys.NAME).build(),
                CosmosParameters.DIMENSION_OPTIONAL
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.NAME)
                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.value")); // TODO

        if (Sponge.getServer().getWorldManager().getWorld(worldKey).isPresent()) {
            //throw Outputs.EXISTING_WORLD.asException(worldName);
        }

        final DimensionType dimensionType = context.getOne(CosmosParameters.DIMENSION_OPTIONAL).orElse(DimensionTypes.OVERWORLD.get());
        // final GeneratorType generatorType = args.<GeneratorType>getOne(ArgKeys.WORLD_GENERATOR.t).orElse(GeneratorTypes.DEFAULT);
        // Collection<WorldGeneratorModifier> worldGeneratorModifiers = args.getAll(ArgKeys.WORLD_MODIFIERS.t);

        final WorldArchetype worldArchetype = WorldArchetype.builder()
                .dimensionType(dimensionType)
                .build();

        final ServerWorldProperties properties;

        try {
            properties = Sponge.getServer().getWorldManager().createProperties(worldKey, worldArchetype).join();
        } catch (Exception ignored) {
            //todo throw Outputs.CREATING_WORLD.asException(worldName);
            return;
        }

        properties.setEnabled(true);
        properties.setGenerateSpawnOnLoad(true);
        //worldProperties.setGeneratorType(generatorType);
        //worldProperties.setGeneratorModifiers(worldGeneratorModifiers);

        this.serviceProvider.world().saveProperties(src, properties);

        // todo src.sendMessage(Outputs.CREATE_WORLD.asText(worldProperties));
    }

}
