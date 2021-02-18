package cosmos.services;

import com.google.inject.ImplementedBy;
import cosmos.services.data.DataService;
import cosmos.services.formatter.FormatterService;
import cosmos.services.io.BackupService;
import cosmos.services.io.ConfigurationService;
import cosmos.services.io.FinderService;
import cosmos.services.listener.ListenerService;
import cosmos.services.message.MessageService;
import cosmos.services.pagination.PaginationService;
import cosmos.services.perworld.PerWorldProvider;
import cosmos.services.portal.PortalService;
import cosmos.services.registry.RegistryProvider;
import cosmos.services.serializer.SerializerProvider;
import cosmos.services.template.TemplateService;
import cosmos.services.time.TimeService;
import cosmos.services.transportation.TransportationService;
import cosmos.services.validation.ValidationService;
import cosmos.services.world.WorldService;

@ImplementedBy(ServiceProviderImpl.class)
public interface ServiceProvider {

    BackupService backup();

    ConfigurationService configuration();

    DataService data();

    FinderService finder();

    FormatterService format();

    ListenerService listener();

    MessageService message();

    PaginationService pagination();

    PerWorldProvider perWorld();

    PortalService portal();

    RegistryProvider registry();

    SerializerProvider serializer();

    TemplateService template();

    TimeService time();

    TransportationService transportation();

    ValidationService validation();

    WorldService world();

}
