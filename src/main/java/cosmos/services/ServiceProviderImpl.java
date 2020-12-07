package cosmos.services;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.services.formatter.FormatterService;
import cosmos.services.message.MessageService;
import cosmos.services.properties.WorldPropertiesService;
import cosmos.services.template.TemplateService;

@Singleton
public class ServiceProviderImpl implements ServiceProvider {

    private final FormatterService formatService;
    private final MessageService messageService;
    private final TemplateService templateService;
    private final WorldPropertiesService worldPropertiesService;

    @Inject
    public ServiceProviderImpl(final Injector injector) {
        this.formatService = injector.getInstance(FormatterService.class);
        this.messageService = injector.getInstance(MessageService.class);
        this.templateService = injector.getInstance(TemplateService.class);
        this.worldPropertiesService = injector.getInstance(WorldPropertiesService.class);
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
    public TemplateService template() {
        return this.templateService;
    }

    @Override
    public WorldPropertiesService worldProperties() {
        return this.worldPropertiesService;
    }

}
