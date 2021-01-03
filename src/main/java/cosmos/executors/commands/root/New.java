package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.WorldTemplate;

import java.util.Collections;
import java.util.List;

@Singleton
public class New extends AbstractCommand {

    public New() {
        super(
                Parameter.string().setKey(CosmosKeys.NAME).build()
                //CosmosParameters.DIMENSION_OPTIONAL
        );
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("create");
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey newKey = context.getOne(CosmosKeys.NAME)
                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.value")); // todo

        final WorldTemplate worldTemplate = WorldTemplate.overworld().asBuilder().key(newKey).build();
//        SeparatedStructureConfig s = SeparatedStructureConfig.of(10, 100, 100);
//        StructureGenerationConfig sg = StructureGenerationConfig.builder().addStructure(Structures.IGLOO.get(), s).build();
//        NoiseConfig ng = NoiseConfig.nether();
//        NoiseGeneratorConfig ngg = NoiseGeneratorConfig.builder().structureConfig(sg).bedrockFloorY(-10).noiseConfig(ng).defaultFluid(BlockState.builder().blockType(BlockTypes.BASALT).build()).build();
//
//        MultiNoiseBiomeConfig mnbg = MultiNoiseBiomeConfig.builder()
//                .addBiome(AttributedBiome.of(Biomes.STONE_SHORE, BiomeAttributes.of(1, 1, 1, 1,1)))
//                .addBiome(AttributedBiome.of(Biomes.JUNGLE_EDGE, BiomeAttributes.of(1, 1, 1, 1,1)))
//                .build();
//        BiomeProvider bp = BiomeProvider.multiNoise(mnbg);
//        ChunkGenerator cg = ChunkGenerator.noise(bp, ngg);

        Sponge.getServer().getWorldManager().saveTemplate(worldTemplate).join();

//        final ResourceKey worldKey = context.getOne(CosmosKeys.NAME)
//                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
//                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.value")); // TODO
//
//        if (Sponge.getServer().getWorldManager().world(worldKey).isPresent()) {
//            //throw Outputs.EXISTING_WORLD.asException(worldName);
//        }
//
//        //final DimensionType dimensionType = context.getOne(CosmosParameters.DIMENSION_OPTIONAL).orElse(DimensionTypes.OVERWORLD.get());
//        // final GeneratorType generatorType = args.<GeneratorType>getOne(ArgKeys.WORLD_GENERATOR.t).orElse(GeneratorTypes.DEFAULT);
//        // Collection<WorldGeneratorModifier> worldGeneratorModifiers = args.getAll(ArgKeys.WORLD_MODIFIERS.t);
//
//        final WorldArchetype worldArchetype = WorldArchetype.builder()
//                .dimensionType(dimensionType)
//                .build();
//
//        final ServerWorldProperties properties;
//
//        try {
//            properties = Sponge.getServer().getWorldManager().createProp(worldKey, worldArchetype).join();
//        } catch (Exception ignored) {
//            //todo throw Outputs.CREATING_WORLD.asException(worldName);
//            return;
//        }
//
//        properties.setEnabled(true);
//        properties.setGenerateSpawnOnLoad(true);
//        //worldProperties.setGeneratorType(generatorType);
//        //worldProperties.setGeneratorModifiers(worldGeneratorModifiers);
//
//        this.serviceProvider.world().saveProperties(src, properties);
//
//        // todo src.sendMessage(Outputs.CREATE_WORLD.asText(worldProperties));
    }

}
