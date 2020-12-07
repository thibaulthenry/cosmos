package cosmos.services.template.impl;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;
import cosmos.registries.template.TemplateRegistry;
import cosmos.services.template.TemplateService;
import org.spongepowered.api.Sponge;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

@Singleton
public class TemplateServiceImpl implements TemplateService {

    private static final String TEMPLATES_BUNDLE = "templates";
    private static final String TEMPLATES_BUNDLE_LOCAL = "templates_{0}.properties";

    private static final Set<Locale> KNOWN_LOCALES = ImmutableSet.<Locale>builder()
            .add(Locale.ROOT)
            .add(Locale.FRANCE)
            .build();

    private final Map<Locale, TemplateRegistry> templateRegistryMap = new HashMap<>();
    private final TemplateRegistry defaultTemplateRegistry;

    public TemplateServiceImpl() {
        this.defaultTemplateRegistry = new TemplateRegistry(ResourceBundle.getBundle(TEMPLATES_BUNDLE, Locale.ROOT));
    }

    public TemplateRegistry getTemplateRegistry(Locale locale) {
        final Locale languageLocale = new Locale(locale.getLanguage());
        return this.templateRegistryMap.computeIfAbsent(languageLocale, key -> {
            final String templateFileName = MessageFormat.format(TEMPLATES_BUNDLE_LOCAL, languageLocale.getLanguage());
            return Sponge.getAssetManager().getAsset(templateFileName)
                    .map(ignored -> new TemplateRegistry(ResourceBundle.getBundle(TEMPLATES_BUNDLE_LOCAL, Locale.ROOT)))
                    .orElse(this.defaultTemplateRegistry);
        });
    }


}
