package cosmos.services.perworld.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.services.perworld.AdvancementsService;
import cosmos.services.perworld.ExperiencesService;
import cosmos.services.perworld.HealthsService;
import cosmos.services.perworld.HungersService;
import cosmos.services.perworld.InventoriesService;
import cosmos.services.perworld.PerWorldProvider;
import cosmos.services.perworld.ScoreboardsService;

@Singleton
public class PerWorldProviderImpl implements PerWorldProvider {

    private final AdvancementsService advancementsService;
    private final ExperiencesService experiencesService;
    private final HealthsService healthsService;
    private final HungersService hungersService;
    private final InventoriesService inventoriesService;
    private final ScoreboardsService scoreboardsService;

    @Inject
    public PerWorldProviderImpl(final Injector injector) {
        this.advancementsService = injector.getInstance(AdvancementsService.class);
        this.experiencesService = injector.getInstance(ExperiencesService.class);
        this.healthsService = injector.getInstance(HealthsService.class);
        this.hungersService = injector.getInstance(HungersService.class);
        this.inventoriesService = injector.getInstance(InventoriesService.class);
        this.scoreboardsService = injector.getInstance(ScoreboardsService.class);
    }

    @Override
    public AdvancementsService advancements() {
        return this.advancementsService;
    }

    @Override
    public ExperiencesService experiences() {
        return this.experiencesService;
    }

    @Override
    public HealthsService healths() {
        return this.healthsService;
    }

    @Override
    public HungersService hungers() {
        return this.hungersService;
    }

    @Override
    public InventoriesService inventories() {
        return this.inventoriesService;
    }

    @Override
    public ScoreboardsService scoreboards() {
        return this.scoreboardsService;
    }
}
