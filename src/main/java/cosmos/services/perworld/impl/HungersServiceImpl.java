package cosmos.services.perworld.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Directories;
import cosmos.services.io.FinderService;
import cosmos.services.perworld.HungersService;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerWorld;

import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class HungersServiceImpl implements HungersService {

    @Inject
    private FinderService finderService;

    @Override
    public Optional<Path> getPath(final ServerWorld world, final ServerPlayer player) {
        final String fileName = world.getUniqueId() + "_" + player.getUniqueId() + ".dat";
        return this.finderService.getCosmosPath(Directories.HUNGERS_DIRECTORY_NAME, fileName);
    }

    @Override
    public Optional<Path> getPath(final ServerPlayer player) {
        return this.getPath(player.getServerLocation().getWorld(), player);
    }
}
