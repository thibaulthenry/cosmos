package cosmos.executors.modules.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.commands.portal.modify.particles.Block;
import cosmos.executors.commands.portal.modify.particles.Color;
import cosmos.executors.commands.portal.modify.particles.Direction;
import cosmos.executors.commands.portal.modify.particles.Item;
import cosmos.executors.commands.portal.modify.particles.Offset;
import cosmos.executors.commands.portal.modify.particles.PotionType;
import cosmos.executors.commands.portal.modify.particles.Quantity;
import cosmos.executors.commands.portal.modify.particles.Type;
import cosmos.executors.commands.portal.modify.particles.Velocity;
import cosmos.executors.modules.AbstractModule;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.builders.portal.PortalAll;

public class Particles extends AbstractModule {

    @Inject
    Particles(final Injector injector) {
        super(
                CosmosParameters.Builder.PORTAL_ALL.get().build(),
                false,
                injector.getInstance(Block.class),
                injector.getInstance(Color.class),
                injector.getInstance(Direction.class),
                injector.getInstance(Item.class),
                injector.getInstance(Offset.class),
                injector.getInstance(PotionType.class),
                injector.getInstance(Quantity.class),
                injector.getInstance(Type.class),
                injector.getInstance(Velocity.class)
        );
    }

}
