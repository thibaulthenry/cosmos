package cosmos.services;

import com.google.inject.ImplementedBy;
import cosmos.services.formatter.FormatterService;
import cosmos.services.io.BackupService;
import cosmos.services.io.FinderService;
import cosmos.services.message.MessageService;
import cosmos.services.pagination.PaginationService;
import cosmos.services.template.TemplateService;
import cosmos.services.time.TimeService;
import cosmos.services.transportation.TransportationService;
import cosmos.services.validation.ValidationService;
import cosmos.services.world.WorldService;

@ImplementedBy(ServiceProviderImpl.class)
public interface ServiceProvider {

    BackupService backup();

    FinderService finder();

    FormatterService format();

    MessageService message();

    PaginationService pagination();

    TemplateService template();

    TimeService time();

    TransportationService transportation();

    ValidationService validation();

    WorldService world();


}
