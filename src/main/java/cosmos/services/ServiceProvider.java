package cosmos.services;

import com.google.inject.ImplementedBy;
import cosmos.services.formatter.FormatterService;
import cosmos.services.message.MessageService;
import cosmos.services.properties.WorldPropertiesService;
import cosmos.services.template.TemplateService;

@ImplementedBy(ServiceProviderImpl.class)
public interface ServiceProvider {

    FormatterService format();

    MessageService message();

    WorldPropertiesService worldProperties();

    TemplateService template();

}
