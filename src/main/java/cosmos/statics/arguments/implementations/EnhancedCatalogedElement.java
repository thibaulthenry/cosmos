package cosmos.statics.arguments.implementations;

import cosmos.statics.arguments.implementations.selector.ExactMatchingCommandElement;
import cosmos.statics.finders.FinderRegistry;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class EnhancedCatalogedElement<T extends CatalogType> extends ExactMatchingCommandElement {

    private final Class<T> catalogType;

    public EnhancedCatalogedElement(Text key, Class<T> catalogType) {
        super(key);
        this.catalogType = catalogType;
    }

    @Override
    protected Iterable<String> getChoices() {
        return FinderRegistry.getIdOrShortIdOrNameIds(catalogType);
    }

    @Override
    protected Iterable<String> getOnCompleteChoices() {
        return FinderRegistry.getRegistryShortIds(catalogType);
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        Optional<T> ret = FinderRegistry.findByIdOrShortIdOrName(choice, catalogType);

        if (!ret.isPresent()) {
            throw new IllegalArgumentException("Invalid input " + choice + " was found");
        }

        return ret.get();
    }
}
