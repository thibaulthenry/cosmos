package cosmos.executors.modules.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.commands.portal.modify.particles.BlockState;
import cosmos.executors.commands.portal.modify.particles.Color;
import cosmos.executors.commands.portal.modify.particles.Direction;
import cosmos.executors.commands.portal.modify.particles.ItemStackSnapshot;
import cosmos.executors.commands.portal.modify.particles.Offset;
import cosmos.executors.commands.portal.modify.particles.PotionEffectType;
import cosmos.executors.commands.portal.modify.particles.Quantity;
import cosmos.executors.commands.portal.modify.particles.Type;
import cosmos.executors.commands.portal.modify.particles.Velocity;
import cosmos.executors.modules.AbstractModule;

public class Particles extends AbstractModule {

    @Inject
    Particles(final Injector injector) {
        super(
                false,
                injector.getInstance(BlockState.class),
                injector.getInstance(Color.class),
                injector.getInstance(Direction.class),
                injector.getInstance(ItemStackSnapshot.class),
                injector.getInstance(Offset.class),
                injector.getInstance(PotionEffectType.class),
                injector.getInstance(Quantity.class),
                injector.getInstance(Type.class),
                injector.getInstance(Velocity.class)
        );
    }

}
