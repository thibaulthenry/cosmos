package cosmos.services.serializer.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.serializer.impl.*;
import cosmos.services.serializer.SerializerProvider;

@Singleton
public class SerializerProviderImpl implements SerializerProvider {

    private final AdvancementsSerializer advancementsSerializer;
    private final ExperiencesSerializer experiencesSerializer;
    private final HealthsSerializer healthsSerializer;
    private final HungersSerializer hungersSerializer;
    private final InventoriesSerializer inventoriesSerializer;
    private final ScoreboardsSerializer scoreboardsSerializer;

    @Inject
    public SerializerProviderImpl(final Injector injector) {
        this.advancementsSerializer = injector.getInstance(AdvancementsSerializer.class);
        this.experiencesSerializer = injector.getInstance(ExperiencesSerializer.class);
        this.healthsSerializer = injector.getInstance(HealthsSerializer.class);
        this.hungersSerializer = injector.getInstance(HungersSerializer.class);
        this.inventoriesSerializer = injector.getInstance(InventoriesSerializer.class);
        this.scoreboardsSerializer = injector.getInstance(ScoreboardsSerializer.class);
    }

    @Override
    public AdvancementsSerializer advancements() {
        return this.advancementsSerializer;
    }

    @Override
    public ExperiencesSerializer experiences() {
        return this.experiencesSerializer;
    }

    @Override
    public HealthsSerializer healths() {
        return this.healthsSerializer;
    }

    @Override
    public HungersSerializer hungers() {
        return this.hungersSerializer;
    }

    @Override
    public InventoriesSerializer inventories() {
        return this.inventoriesSerializer;
    }

    @Override
    public ScoreboardsSerializer scoreboards() {
        return this.scoreboardsSerializer;
    }
}
