package cosmos.services.portal.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.portal.PortalDispatcherRegistry;
import cosmos.registries.portal.PortalRegistry;
import cosmos.registries.portal.PortalSelectionRegistry;
import cosmos.registries.portal.impl.PortalDispatcher;
import cosmos.services.message.MessageService;
import cosmos.services.portal.PortalService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.FallingBlock;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Singleton
public class PortalServiceImpl implements PortalService {

    private final MessageService messageService;
    private final PortalDispatcherRegistry portalDispatcherRegistry;
    private final PortalRegistry portalRegistry;
    private final PortalSelectionRegistry portalSelectionRegistry;

    @Inject
    public PortalServiceImpl(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
        this.portalDispatcherRegistry = injector.getInstance(PortalDispatcherRegistry.class);
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
        this.portalSelectionRegistry = injector.getInstance(PortalSelectionRegistry.class);
    }

    @Override
    public void create(final Audience src, final ResourceKey key, final CosmosPortalType type) throws CommandException {
        if (this.portalRegistry.has(key)) {
            throw new CommandException(Component.text("already")); // todo
        }

        if (!(src instanceof Identifiable)) {
            throw this.messageService.getError(src, "error.invalid.value"); // todo
        }

        final UUID uuid = ((Identifiable) src).getUniqueId();
        final Optional<Set<ServerLocation>> optionalSelection = this.portalSelectionRegistry.find(uuid);

        if (!optionalSelection.isPresent() || optionalSelection.get().isEmpty()) {
            // todo please select using tools
            return;
        }

        final Set<ServerLocation> selection = optionalSelection.get();

        final CosmosPortal portal = type.builder()
                .origins(selection)
                .trigger(type.defaultTrigger())
                .key(key)
                .build();

        this.fill(src, portal);
        this.portalRegistry.register(key, portal);
        this.portalSelectionRegistry.unregister(uuid); // todo check
    }

    @Override
    public void delete(final Audience src, final ResourceKey key) throws CommandException {
        if (!this.portalRegistry.has(key)) {
            throw new CommandException(Component.text("not exist")); // todo
        }

        if (!this.portalRegistry.unregister(key).isPresent()) {
            throw new CommandException(Component.text("an error occured")); // todo
        }
    }

    @Override
    public void fill(final Audience src, final CosmosPortal portal) {
        Sponge.getServer().getCauseStackManager().pushCause(Cosmos.getPluginContainer());
        portal.origins().forEach(location -> location.setBlockType(portal.trigger(), BlockChangeFlags.ALL));
    }

    @Override
    public void fill(final Audience src, final ResourceKey key) throws CommandException {
        if (!this.portalRegistry.has(key)) {
            throw new CommandException(Component.text("not exist")); // todo
        }

        this.fill(src, this.portalRegistry.value(key));
    }

    @Override
    public void highlight(final BlockType blockType, final Ticks duration, final String tag, final ServerLocation... locations) {
        Arrays.stream(locations).forEach(location -> {
            final FallingBlock fallingBlock = location.createEntity(EntityTypes.FALLING_BLOCK.get());

            // todo issue for tags

            fallingBlock.offer(Keys.BLOCK_STATE, BlockState.builder().blockType(blockType).build());
            fallingBlock.offer(Keys.CAN_DROP_AS_ITEM, false);
            fallingBlock.offer(Keys.CAN_PLACE_AS_BLOCK, false);
            fallingBlock.offer(Keys.FALL_TIME, Ticks.of(Math.max(0L, 600 - duration.getTicks())));
            fallingBlock.offer(Keys.INVULNERABLE, true);
            fallingBlock.offer(Keys.IS_GLOWING, true);
            fallingBlock.offer(Keys.IS_GRAVITY_AFFECTED, false);
            fallingBlock.offer(Keys.ON_GROUND, false);

            location.spawnEntity(fallingBlock);
        });
    }

    @Override
    public void highlight(final BlockType blockType, final Audience src, final Ticks duration) throws CommandException {
        if (!(src instanceof Identifiable)) {
            throw this.messageService.getError(src, "error.invalid.value"); // todo
        }

        this.portalSelectionRegistry.find(((Identifiable) src).getUniqueId()).ifPresent(locations ->
                this.highlight(blockType, duration, "cosmos-portal-x", locations.toArray(new ServerLocation[0])) // todo tag
        );
    }

    @Override
    public boolean link(final ResourceKey worldOriginKey, final PortalType portalType, final ResourceKey worldDestinationKey) throws CommandException {
        if (worldOriginKey.equals(worldDestinationKey)) {
            throw new CommandException(Component.empty()); // todo
        }

        return this.portalDispatcherRegistry.find(worldOriginKey)
                .flatMap(portalDispatcher -> this.portalDispatcherRegistry.register(worldOriginKey, new PortalDispatcher()))
                .map(CosmosRegistryEntry::value)
                .map(portalDispatcher -> portalDispatcher.addLink(portalType, worldDestinationKey))
                .orElse(false);
    }

    @Override
    public boolean unlink(final ResourceKey worldOriginKey, final PortalType portalType) {
        return this.portalDispatcherRegistry.find(worldOriginKey)
                .map(portalDispatcher -> portalDispatcher.removeLink(portalType))
                .orElse(false);
    }

}
