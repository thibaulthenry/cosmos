package cosmos.services.serializer.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.serializer.impl.AdvancementSerializer;
import cosmos.registries.serializer.impl.ExperienceSerializer;
import cosmos.registries.serializer.impl.HealthSerializer;
import cosmos.registries.serializer.impl.HungerSerializer;
import cosmos.registries.serializer.impl.InventorySerializer;
import cosmos.registries.serializer.impl.PortalSerializer;
import cosmos.registries.serializer.impl.ScoreboardSerializer;
import cosmos.services.serializer.SerializerProvider;

@Singleton
public class SerializerProviderImpl implements SerializerProvider {

    private final AdvancementSerializer advancementSerializer;
    private final ExperienceSerializer experienceSerializer;
    private final HealthSerializer healthSerializer;
    private final HungerSerializer hungerSerializer;
    private final InventorySerializer inventorySerializer;
    private final PortalSerializer portalSerializer;
    private final ScoreboardSerializer scoreboardSerializer;

    @Inject
    public SerializerProviderImpl(final Injector injector) {
        this.advancementSerializer = injector.getInstance(AdvancementSerializer.class);
        this.experienceSerializer = injector.getInstance(ExperienceSerializer.class);
        this.healthSerializer = injector.getInstance(HealthSerializer.class);
        this.hungerSerializer = injector.getInstance(HungerSerializer.class);
        this.inventorySerializer = injector.getInstance(InventorySerializer.class);
        this.portalSerializer = injector.getInstance(PortalSerializer.class);
        this.scoreboardSerializer = injector.getInstance(ScoreboardSerializer.class);
    }

    @Override
    public AdvancementSerializer advancement() {
        return this.advancementSerializer;
    }

    @Override
    public ExperienceSerializer experience() {
        return this.experienceSerializer;
    }

    @Override
    public HealthSerializer health() {
        return this.healthSerializer;
    }

    @Override
    public HungerSerializer hunger() {
        return this.hungerSerializer;
    }

    @Override
    public InventorySerializer inventory() {
        return this.inventorySerializer;
    }

    @Override
    public PortalSerializer portal() {
        return this.portalSerializer;
    }

    @Override
    public ScoreboardSerializer scoreboard() {
        return this.scoreboardSerializer;
    }

}
