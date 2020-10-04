package cosmos.statics.finders;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FinderRegistry {

    public static <T extends CatalogType> Optional<T> findByIdOrShortIdOrName(String idOrShortIdOrName, Collection<T> catalog) {
        return catalog
                .stream()
                .filter(element -> {
                    if (idOrShortIdOrName.contains(":")) {
                        return idOrShortIdOrName.equals(element.getId());
                    }

                    return idOrShortIdOrName.equals(shortenId(element.getId())) || idOrShortIdOrName.equals(shortenId(element.getName()));
                })
                .findFirst();
    }

    public static <T extends CatalogType> Optional<T> findByIdOrShortIdOrName(String idOrShortIdOrName, Class<T> catalogType) {
        return findByIdOrShortIdOrName(idOrShortIdOrName, Sponge.getRegistry().getAllOf(catalogType));
    }

    public static Collection<String> getRegistryShortIds(Collection<? extends CatalogType> catalog) {
        Collection<String> ids = catalog
                .stream()
                .map(CatalogType::getId)
                .collect(Collectors.toList());

        Collection<String> shortIds = ids
                .stream()
                .map(FinderRegistry::shortenId)
                .collect(Collectors.toList());

        return ids
                .stream()
                .map(id -> Collections.frequency(shortIds, id) > 1 ? id : shortenId(id))
                .collect(Collectors.toList());
    }

    public static Collection<String> getRegistryShortIds(Class<? extends CatalogType> catalogType) {
        Collection<String> ids = Sponge.getRegistry().getAllOf(catalogType)
                .stream()
                .map(CatalogType::getId)
                .collect(Collectors.toList());

        Collection<String> shortIds = ids
                .stream()
                .map(FinderRegistry::shortenId)
                .collect(Collectors.toList());

        return ids
                .stream()
                .map(id -> Collections.frequency(shortIds, id) > 1 ? id : shortenId(id))
                .collect(Collectors.toList());
    }


    public static Collection<String> getIdOrShortIdOrNameIds(Class<? extends CatalogType> catalogType) {
        Collection<String> ids = Sponge.getRegistry().getAllOf(catalogType)
                .stream()
                .map(element -> Arrays.asList(element.getId(), element.getName()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return Stream.of(ids, getRegistryShortIds(catalogType))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }


    public static String shortenId(String id) {
        String[] splitId = id.split(":");

        if (splitId.length > 1) {
            return splitId[1];
        }

        return id;
    }
}
