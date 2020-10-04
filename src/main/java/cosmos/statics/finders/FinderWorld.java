package cosmos.statics.finders;

import cosmos.constants.ArgKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;

public class FinderWorld {

    public static Collection<World> getLoadedWorlds() {
        return Sponge.getServer().getWorlds();
    }

    public static Optional<World> getGivenWorldOrElse(CommandSource src, CommandContext args, ArgKeys argName) {
        return args.<World>getOne(argName.t)
                .map(Optional::of)
                .orElse(getSourceWorld(src))
                .map(Optional::of)
                .orElse(Sponge.getServer().getWorld(Sponge.getServer().getDefaultWorldName()));
    }

    static Optional<World> getSourceWorld(CommandSource src) {
        return src instanceof Locatable ?
                Optional.of(((Locatable) src).getLocation().getExtent()) :
                Optional.empty();
    }

}
