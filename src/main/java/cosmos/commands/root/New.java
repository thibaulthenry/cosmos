package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;

public class New extends AbstractCommand {

    public New() {
        super(
                GenericArguments.string(ArgKeys.NEW_NAME.t),
                GenericArguments.optional(
                        Arguments.enhancedCatalogedElement(ArgKeys.WORLD_DIMENSION, DimensionType.class)
                ),
                GenericArguments.optional(
                        Arguments.enhancedCatalogedElement(ArgKeys.WORLD_GENERATOR, GeneratorType.class)
                ),
                GenericArguments.optional(
                        Arguments.enhancedCatalogedElement(ArgKeys.WORLD_MODIFIERS, WorldGeneratorModifier.class)
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne(ArgKeys.NEW_NAME.t).orElseThrow(Outputs.INVALID_WORLD_NAME.asSupplier());

        if (Sponge.getServer().getWorldProperties(worldName).isPresent()) {
            throw Outputs.EXISTING_WORLD.asException(worldName);
        }

        DimensionType dimensionType = args.<DimensionType>getOne(ArgKeys.WORLD_DIMENSION.t).orElse(DimensionTypes.OVERWORLD);
        GeneratorType generatorType = args.<GeneratorType>getOne(ArgKeys.WORLD_GENERATOR.t).orElse(GeneratorTypes.DEFAULT);
        Collection<WorldGeneratorModifier> worldGeneratorModifiers = args.getAll(ArgKeys.WORLD_MODIFIERS.t);

        WorldArchetype worldArchetype = WorldArchetype.builder()
                .dimension(dimensionType)
                .build(worldName + Instant.now(), worldName);

        WorldProperties worldProperties;
        try {
            worldProperties = Sponge.getServer().createWorldProperties(worldName, worldArchetype);
        } catch (IOException ignored) {
            throw Outputs.CREATING_WORLD.asException(worldName);
        }

        worldProperties.setEnabled(true);
        worldProperties.setGenerateSpawnOnLoad(true);
        worldProperties.setGeneratorType(generatorType);
        worldProperties.setGeneratorModifiers(worldGeneratorModifiers);

        FinderWorldProperties.saveProperties(worldProperties);
        src.sendMessage(Outputs.CREATE_WORLD.asText(worldProperties));
    }
}
