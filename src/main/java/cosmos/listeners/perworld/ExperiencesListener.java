package cosmos.listeners.perworld;

import cosmos.commands.perworld.Bypass;
import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.DefaultData;
import cosmos.constants.PerWorldCommands;
import cosmos.listeners.ScheduledAsyncSaveListener;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.serializers.ExperiencesSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.entity.ExperienceHolderData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.World;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;


public class ExperiencesListener extends AbstractPerWorldListener implements ScheduledAsyncSaveListener {

    private static Optional<Path> getExperiencesPath(World world, Player player) {
        String fileName = world.getUniqueId() + "_" + player.getUniqueId() + ".dat";
        return FinderFile.getCosmosPath(FinderFile.EXPERIENCES_DIRECTORY_NAME, fileName);
    }

    @Listener
    public void onDisconnectClientConnectionEvent(ClientConnectionEvent.Disconnect event, @First Player player) {
        save(player.getWorld(), player);
    }

    @Listener(order = Order.POST)
    @IsCancelled(Tristate.FALSE)
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void onDeathDestructEntityEventEvent(DestructEntityEvent.Death event, @First Player player) {
        if (event.getKeepInventory()) {
            save(player.getWorld(), player, player.getOrCreate(ExperienceHolderData.class).get());
        } else {
            Sponge.getDataManager()
                    .deserialize(ExperienceHolderData.class, DefaultData.DEFAULT_EXPERIENCE_HOLDER_DATA)
                    .ifPresent(experienceHolderData -> save(player.getWorld(), player, experienceHolderData));
        }
    }

    @Listener
    public void onJoinClientConnectionEvent(ClientConnectionEvent.Join event, @First Player player) {
        share(player.getWorld(), player);
    }

    @Listener
    @SuppressWarnings("MethodMayBeStatic")
    public void onWorldEnter(MoveEntityEvent.Teleport event, @First Player player) {
        World from = event.getFromTransform().getExtent();
        World to = event.getToTransform().getExtent();

        if (from.getUniqueId().equals(to.getUniqueId())) {
            return;
        }

        save(from, player);

        if (!GroupRegister.find(Tuple.of(PerWorldCommands.EXPERIENCES, from.getName())).map(group -> group.contains(to.getName())).orElse(false)) {
            share(to, player);
        }
    }

    @Listener
    public void onRespawnPlayerEvent(RespawnPlayerEvent event, @First Player player) {
        share(event.getToTransform().getExtent(), event.getTargetEntity());
    }

    @Listener
    public void onStoppingServerEvent(GameStoppingServerEvent event) {
        save();
    }

    @Override
    public void save() {
        Sponge.getServer().getOnlinePlayers()
                .stream()
                .filter(player -> !player.isRemoved())
                .forEach(player -> save(player.getWorld(), player));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void save(World world, Player player) {
        save(world, player, player.getOrCreate(ExperienceHolderData.class).get());
    }

    private void save(World world, Player player, ExperienceHolderData experienceHolderData) {
        if (Bypass.doesBypass(PerWorldCommands.EXPERIENCES, player)) {
            return;
        }

        GroupRegister.find(Tuple.of(PerWorldCommands.EXPERIENCES, world.getName()))
                .orElse(Collections.singleton(world.getName()))
                .stream()
                .map(worldName -> Sponge.getServer().getWorld(worldName).flatMap(w -> getExperiencesPath(w, player)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(path -> ExperiencesSerializer.serialize(path, experienceHolderData));
    }

    private void share(World world, Player player) {
        if (Bypass.doesBypass(PerWorldCommands.EXPERIENCES, player)) {
            return;
        }

        getExperiencesPath(world, player)
                .flatMap(ExperiencesSerializer::deserialize)
                .ifPresent(player::offer);
    }

}
