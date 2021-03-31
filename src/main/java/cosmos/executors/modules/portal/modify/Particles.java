package cosmos.executors.modules.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.portal.modify.particles.Block;
import cosmos.executors.commands.portal.modify.particles.Color;
import cosmos.executors.commands.portal.modify.particles.Direction;
import cosmos.executors.commands.portal.modify.particles.Fluctuation;
import cosmos.executors.commands.portal.modify.particles.Item;
import cosmos.executors.commands.portal.modify.particles.Offset;
import cosmos.executors.commands.portal.modify.particles.PotionType;
import cosmos.executors.commands.portal.modify.particles.Quantity;
import cosmos.executors.commands.portal.modify.particles.SpawnInterval;
import cosmos.executors.commands.portal.modify.particles.Type;
import cosmos.executors.commands.portal.modify.particles.Velocity;
import cosmos.executors.commands.portal.modify.particles.ViewDistance;
import cosmos.executors.modules.AbstractModule;

@Singleton
public class Particles extends AbstractModule {

    @Inject
    Particles(final Injector injector) {
        super(
                CosmosParameters.PORTAL_ALL.get().build(),
                false,
                injector.getInstance(Block.class),
                injector.getInstance(Color.class),
                injector.getInstance(Direction.class),
                injector.getInstance(Fluctuation.class),
                injector.getInstance(Item.class),
                injector.getInstance(Offset.class),
                injector.getInstance(PotionType.class),
                injector.getInstance(Quantity.class),
                injector.getInstance(SpawnInterval.class),
                injector.getInstance(Type.class),
                injector.getInstance(Velocity.class),
                injector.getInstance(ViewDistance.class)
        );
    }

}
