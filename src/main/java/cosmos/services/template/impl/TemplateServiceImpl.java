package cosmos.services.template.impl;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import cosmos.registries.template.TemplateRegistry;
import cosmos.services.template.TemplateService;
import cosmos.services.template.UTF8Control;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Singleton
public class TemplateServiceImpl implements TemplateService {

    private static final String TEMPLATES_BUNDLE = "templates";

    private final TemplateRegistry defaultTemplateRegistry;
    private final Map<String, Locale> knownLocales = ImmutableMap.of("fr", new Locale("fr"));
    private final Map<Locale, TemplateRegistry> templateRegistryMap = new HashMap<>();
    private final ResourceBundle.Control utf8Control;

    public TemplateServiceImpl() {
        this.utf8Control = new UTF8Control();
        this.defaultTemplateRegistry = new TemplateRegistry(
                Locale.ROOT,
                ResourceBundle.getBundle(TemplateServiceImpl.TEMPLATES_BUNDLE, Locale.ROOT, this.utf8Control)
        );
    }

    public TemplateRegistry templateRegistry(final Locale locale) {
        final Locale languageLocale = this.knownLocales.getOrDefault(locale.getLanguage(), Locale.ROOT);

        if (this.templateRegistryMap.containsKey(languageLocale)) {
            return this.templateRegistryMap.get(languageLocale);
        }

        TemplateRegistry templateRegistry;

        try {
            templateRegistry = new TemplateRegistry(languageLocale, ResourceBundle.getBundle(TemplateServiceImpl.TEMPLATES_BUNDLE, languageLocale, this.utf8Control));
        } catch (final Exception ignored) {
            templateRegistry = this.defaultTemplateRegistry;
        }

        this.templateRegistryMap.put(languageLocale, templateRegistry);

        return templateRegistry;
    }

}
