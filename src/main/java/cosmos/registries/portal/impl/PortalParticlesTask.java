package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import cosmos.Cosmos;
import cosmos.registries.portal.CosmosPortal;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

import java.util.Optional;
import java.util.Random;

public class PortalParticlesTask {

    private static final Random RANDOM = new Random();

    private final Task particlesTask;

    private ScheduledTask scheduledParticlesTask;

    public PortalParticlesTask(final CosmosPortal portal) {
        final Optional<ParticleEffect> optionalParticles = portal.particles();
        final Optional<Ticks> optionalParticlesSpawnInterval = portal.particlesSpawnInterval();
        Preconditions.checkArgument(optionalParticles.isPresent(), "CosmosPortal has no particles");
        Preconditions.checkArgument(optionalParticlesSpawnInterval.filter(ticks -> ticks.ticks() > 0).isPresent(), "CosmosPortal particles spawn interval must be positive");
        final ParticleEffect particles = optionalParticles.get();

        this.particlesTask = Task.builder()
                .execute(() -> {
                    double randomFactorX, randomFactorZ, randomOffsetX, randomOffsetY, randomOffsetZ, randomVelocityX, randomVelocityY, randomVelocityZ;
                    randomOffsetX = randomOffsetY = randomOffsetZ = randomVelocityX = randomVelocityY = randomVelocityZ = 0d;

                    for (ServerLocation origin : portal.origins()) {
                        if (portal.particlesFluctuation().orElse(false)) {
                            randomFactorX = RANDOM.nextDouble() * 2.5d - 1.25d;
                            randomFactorZ = RANDOM.nextDouble() * 2.5d - 1.25d;
                            randomOffsetX = 0.5d * randomFactorX;
                            randomOffsetY = RANDOM.nextDouble();
                            randomOffsetZ = 0.5d * randomFactorZ;
                            randomVelocityX = RANDOM.nextDouble() * randomFactorX;
                            randomVelocityY = (RANDOM.nextDouble() - 0.5d) * 0.150d;
                            randomVelocityZ = RANDOM.nextDouble() * randomFactorZ;
                        }

                        final ParticleEffect particleEffect = ParticleEffect.builder()
                                .from(particles)
                                .offset(particles.option(ParticleOptions.OFFSET.get()).orElse(Vector3d.ZERO).add(randomOffsetX, randomOffsetY, randomOffsetZ))
                                .velocity(particles.option(ParticleOptions.VELOCITY.get()).orElse(Vector3d.ZERO).add(randomVelocityX, randomVelocityY, randomVelocityZ))
                                .build();

                        final int viewDistance = portal.particlesViewDistance().orElse(Integer.MAX_VALUE);
                        origin.world().spawnParticles(particleEffect, origin.position().add(0.5, 0.5, 0.5), viewDistance);
                    }
                })
                .interval(optionalParticlesSpawnInterval.get())
                // .name("cosmos-portal-particles-" + portal.key().formatted())
                .plugin(Cosmos.pluginContainer())
                .build();
    }

    public void cancel() {
        this.cancelTask(this.scheduledParticlesTask);
    }

    private boolean cancelTask(final ScheduledTask scheduledTask) {
        return scheduledTask != null && scheduledTask.cancel();
    }

    public void submit() {
        if (this.particlesTask != null) {
            this.scheduledParticlesTask = Sponge.asyncScheduler().submit(this.particlesTask);
        }
    }

}
