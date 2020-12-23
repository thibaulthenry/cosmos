package cosmos.services;

import com.google.inject.ImplementedBy;
import cosmos.services.formatter.FormatterService;
import cosmos.services.io.FinderService;
import cosmos.services.message.MessageService;
import cosmos.services.pagination.PaginationService;
import cosmos.services.template.TemplateService;
import cosmos.services.world.WorldPropertiesService;
import cosmos.services.world.WorldService;

@ImplementedBy(ServiceProviderImpl.class)
public interface ServiceProvider {

    FinderService finder();

    FormatterService format();

    MessageService message();

    PaginationService pagination();

    WorldPropertiesService worldProperties();

    WorldService world();

    TemplateService template();

}
