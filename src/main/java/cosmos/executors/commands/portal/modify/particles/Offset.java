package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.math.vector.Vector3d;

@Singleton
public class Offset extends AbstractParticlesModifyCommand {

    @Inject
    public Offset() {
        super(Parameter.vector3d().setKey(CosmosKeys.X_Y_Z).build());
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final Vector3d offset = context.getOne(CosmosKeys.X_Y_Z)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.X_Y_Z));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.OFFSET, offset).build();
    }

}
