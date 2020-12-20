package cosmos.services.template;

import com.google.inject.ImplementedBy;
import cosmos.registries.template.TemplateRegistry;
import cosmos.services.CosmosService;
import cosmos.services.template.impl.TemplateServiceImpl;

import java.util.Locale;

@ImplementedBy(TemplateServiceImpl.class)
public interface TemplateService extends CosmosService {

    TemplateRegistry getTemplateRegistry(final Locale locale);

}
