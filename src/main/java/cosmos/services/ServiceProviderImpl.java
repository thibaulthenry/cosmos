package cosmos.services;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.services.formatter.FormatterService;
import cosmos.services.io.BackupService;
import cosmos.services.io.ConfigurationService;
import cosmos.services.io.FinderService;
import cosmos.services.listener.ListenerService;
import cosmos.services.message.MessageService;
import cosmos.services.pagination.PaginationService;
import cosmos.services.parameter.ParameterService;
import cosmos.services.perworld.ScoreboardService;
import cosmos.services.portal.PortalService;
import cosmos.services.registry.RegistryProvider;
import cosmos.services.serializer.SerializerProvider;
import cosmos.services.template.TemplateService;
import cosmos.services.time.TimeService;
import cosmos.services.transportation.TransportationService;
import cosmos.services.validation.ValidationService;
import cosmos.services.world.WorldService;

@Singleton
public class ServiceProviderImpl implements ServiceProvider {

    private final BackupService backupService;
    private final ConfigurationService configurationService;
    private final FinderService finderService;
    private final FormatterService formatService;
    private final ListenerService listenerService;
    private final MessageService messageService;
    private final PaginationService paginationService;
    private final ParameterService parameterService;
    private final PortalService portalService;
    private final RegistryProvider registryProvider;
    private final ScoreboardService scoreboardService;
    private final SerializerProvider serializerProvider;
    private final TemplateService templateService;
    private final TimeService timeService;
    private final TransportationService transportationService;
    private final ValidationService validationService;
    private final WorldService worldService;

    @Inject
    public ServiceProviderImpl(final Injector injector) {
        this.backupService = injector.getInstance(BackupService.class);
        this.configurationService = injector.getInstance(ConfigurationService.class);
        this.finderService = injector.getInstance(FinderService.class);
        this.formatService = injector.getInstance(FormatterService.class);
        this.listenerService = injector.getInstance(ListenerService.class);
        this.messageService = injector.getInstance(MessageService.class);
        this.paginationService = injector.getInstance(PaginationService.class);
        this.parameterService = injector.getInstance(ParameterService.class);
        this.portalService = injector.getInstance(PortalService.class);
        this.registryProvider = injector.getInstance(RegistryProvider.class);
        this.scoreboardService = injector.getInstance(ScoreboardService.class);
        this.serializerProvider = injector.getInstance(SerializerProvider.class);
        this.templateService = injector.getInstance(TemplateService.class);
        this.timeService = injector.getInstance(TimeService.class);
        this.transportationService = injector.getInstance(TransportationService.class);
        this.validationService = injector.getInstance(ValidationService.class);
        this.worldService = injector.getInstance(WorldService.class);
    }

    @Override
    public BackupService backup() {
        return this.backupService;
    }

    @Override
    public ConfigurationService configuration() {
        return this.configurationService;
    }

    @Override
    public FinderService finder() {
        return this.finderService;
    }

    @Override
    public FormatterService format() {
        return this.formatService;
    }

    @Override
    public ListenerService listener() {
        return this.listenerService;
    }

    @Override
    public MessageService message() {
        return this.messageService;
    }

    @Override
    public PaginationService pagination() {
        return this.paginationService;
    }

    @Override
    public ParameterService parameter() {
        return this.parameterService;
    }

    @Override
    public PortalService portal() {
        return this.portalService;
    }

    @Override
    public RegistryProvider registry() {
        return this.registryProvider;
    }

    @Override
    public ScoreboardService scoreboard() {
        return this.scoreboardService;
    }

    @Override
    public SerializerProvider serializer() {
        return serializerProvider;
    }

    @Override
    public TemplateService template() {
        return this.templateService;
    }

    @Override
    public TimeService time() {
        return this.timeService;
    }

    @Override
    public TransportationService transportation() {
        return this.transportationService;
    }

    @Override
    public ValidationService validation() {
        return this.validationService;
    }

    @Override
    public WorldService world() {
        return this.worldService;
    }

}
