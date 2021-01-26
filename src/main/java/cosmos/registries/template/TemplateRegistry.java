package cosmos.registries.template;

import cosmos.Cosmos;
import cosmos.registries.CosmosRegistry;

import java.util.Locale;
import java.util.ResourceBundle;

public class TemplateRegistry implements CosmosRegistry<String, Template> {

    private final Locale locale;
    private final ResourceBundle resourceBundle;

    public TemplateRegistry(final Locale locale, final ResourceBundle resourceBundle) {
        this.locale = locale;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public Template get(final String key) {
        try {
            return new Template(this.locale, this.resourceBundle.getString(key));
        } catch (Exception e) {
            Cosmos.getLogger().error("Failed to retrieve template from key " + key, e);
            return null;
        }
    }

    @Override
    public boolean has(final String key) {
        return this.resourceBundle.containsKey(key);
    }

}
