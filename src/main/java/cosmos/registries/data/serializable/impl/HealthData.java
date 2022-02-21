package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class HealthData implements ShareableSerializable<ServerPlayer> {

    private final double absorption;
    private final double health;
    private final double maxHealth;

    public HealthData(final ServerPlayer player) {
        this.absorption = player.absorption().get();
        this.health = player.health().get();
        this.maxHealth = player.maxHealth().get();
    }

    public HealthData(final double absorption, final double health, final double maxHealth) {
        this.absorption = absorption;
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public HealthData() {
        this.absorption = 0.0;
        this.health = 20.0;
        this.maxHealth = 20.0;
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public void share(final ServerPlayer data) {
        data.offer(Keys.ABSORPTION, this.absorption);
        data.offer(Keys.HEALTH, this.health);
        data.offer(Keys.MAX_HEALTH, this.maxHealth);
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Health.ABSORPTION, this.absorption)
                .set(Queries.Health.HEALTH, this.health)
                .set(Queries.Health.MAX_HEALTH, this.maxHealth);
    }

}
