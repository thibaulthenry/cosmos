package cosmos.services;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.services.formatter.FormatterService;
import cosmos.services.io.BackupService;
import cosmos.services.io.FinderService;
import cosmos.services.message.MessageService;
import cosmos.services.pagination.PaginationService;
import cosmos.services.template.TemplateService;
import cosmos.services.time.TimeService;
import cosmos.services.validation.ValidationService;
import cosmos.services.world.WorldPropertiesService;
import cosmos.services.world.WorldService;

@Singleton
public class ServiceProviderImpl implements ServiceProvider {

    private final BackupService backupService;
    private final FinderService finderService;
    private final FormatterService formatService;
    private final MessageService messageService;
    private final PaginationService paginationService;
    private final TemplateService templateService;
    private final TimeService timeService;
    private final ValidationService validationService;
    private final WorldPropertiesService worldPropertiesService;
    private final WorldService worldService;

    @Inject
    public ServiceProviderImpl(final Injector injector) {
        this.backupService = injector.getInstance(BackupService.class);
        this.finderService = injector.getInstance(FinderService.class);
        this.formatService = injector.getInstance(FormatterService.class);
        this.messageService = injector.getInstance(MessageService.class);
        this.paginationService = injector.getInstance(PaginationService.class);
        this.templateService = injector.getInstance(TemplateService.class);
        this.timeService = injector.getInstance(TimeService.class);
        this.validationService = injector.getInstance(ValidationService.class);
        this.worldPropertiesService = injector.getInstance(WorldPropertiesService.class);
        this.worldService = injector.getInstance(WorldService.class);
    }

    @Override
    public BackupService backup() {
        return this.backupService;
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
    public MessageService message() {
        return this.messageService;
    }

    @Override
    public PaginationService pagination() {
        return this.paginationService;
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
    public ValidationService validation() {
        return this.validationService;
    }

    @Override
    public WorldPropertiesService worldProperties() {
        return this.worldPropertiesService;
    }

    @Override
    public WorldService world() {
        return this.worldService;
    }

}
