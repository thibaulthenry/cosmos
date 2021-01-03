package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class HungerData implements DataSerializable, ShareableSerializable<ServerPlayer> {

    // TODO Builder

    private final double exhaustion;
    private final int foodLevel;
    private final double maxExhaustion;
    private final int maxFoodLevel;
    private final double saturation;

    public HungerData(final ServerPlayer player) {
        this.exhaustion = player.exhaustion().get();
        this.foodLevel = player.foodLevel().get();
        this.maxExhaustion = player.get(Keys.MAX_EXHAUSTION).orElse(0.0);
        this.maxFoodLevel = player.get(Keys.MAX_FOOD_LEVEL).orElse(0);
        this.saturation = player.saturation().get();
    }

    public HungerData() {
        this.exhaustion = 0.0;
        this.foodLevel = 20;
        this.maxExhaustion = -1.0;
        this.maxFoodLevel = -1;
        this.saturation = 5.0;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public void offer(final ServerPlayer data) {
        data.exhaustion().set(this.exhaustion);
        data.foodLevel().set(this.foodLevel);
        data.offer(Keys.MAX_EXHAUSTION, this.maxExhaustion);
        data.offer(Keys.MAX_FOOD_LEVEL, this.maxFoodLevel);
        data.saturation().set(this.saturation);
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Hungers.EXHAUSTION, this.exhaustion)
                .set(Queries.Hungers.FOOD_LEVEL, this.foodLevel)
                .set(Queries.Hungers.MAX_EXHAUSTION, this.maxExhaustion)
                .set(Queries.Hungers.MAX_FOOD_LEVEL, this.maxFoodLevel)
                .set(Queries.Hungers.SATURATION, this.saturation);
    }
}
