package cosmos.executors.commands.root;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.entity.BlockEntityArchetype;
import org.spongepowered.api.block.entity.BlockEntityTypes;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.StructureModes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3i;

@Singleton
public class Back extends AbstractCommand {

    @Inject
    public Back(final Injector injector) {
        //super(injector.getInstance(WorldOnline.class).build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
//        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD)
//                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.world.online"));

        //super.serviceProvider.data().portal().create(DataKeys.PORTAL_TYPE_WARP, ((ServerPlayer) src).getServerLocation(), Axis.X);

        ServerLocation loc = ((ServerPlayer) src).getServerLocation();

        BlockEntityArchetype blockEntityArchetype = BlockEntityArchetype.builder()
                .blockEntity(BlockEntityTypes.STRUCTURE)
                .add(Keys.STRUCTURE_MODE, StructureModes.SAVE.get())
                .add(Keys.STRUCTURE_IGNORE_ENTITIES, true)
                .add(Keys.STRUCTURE_AUTHOR, "Kazz")
                .add(Keys.STRUCTURE_POSITION, loc.getBlockPosition())
                .add(Keys.STRUCTURE_POWERED, true)
                .add(Keys.STRUCTURE_SHOW_BOUNDING_BOX, true)
                .add(Keys.STRUCTURE_SIZE, new Vector3i(10, 5, 10))
                .build();

        blockEntityArchetype.apply(loc);
    }

}
