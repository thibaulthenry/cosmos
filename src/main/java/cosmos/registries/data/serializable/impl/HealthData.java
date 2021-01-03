package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class HealthData implements DataSerializable, ShareableSerializable<ServerPlayer> {

    // TODO Builder

    private final double health;
    private final double maxHealth;
    private final double absorption;

    public HealthData(final ServerPlayer player) {
        this.health = player.health().get();
        this.maxHealth = player.maxHealth().get();
        this.absorption = player.absorption().get();
    }

    public HealthData() {
        this.health = 20.0;
        this.maxHealth = 20.0;
        this.absorption = 0.0;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public void offer(final ServerPlayer data) {
        data.health().set(this.health);
        data.maxHealth().set(this.maxHealth);
        data.absorption().set(this.absorption);
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Healths.HEALTH, this.health)
                .set(Queries.Healths.MAX_HEALTH, this.maxHealth)
                .set(Queries.Healths.ABSORPTION, this.absorption);
    }
}
