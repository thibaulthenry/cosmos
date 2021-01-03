package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.ObjectiveData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

@Singleton
public class ObjectiveDataBuilder extends AbstractDataBuilder<ObjectiveData> {

    @Inject
    public ObjectiveDataBuilder() {
        this(ObjectiveData.class, 1);
    }

    protected ObjectiveDataBuilder(final Class<ObjectiveData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<ObjectiveData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Scoreboards.Objective.CRITERION, Queries.Scoreboards.Objective.DISPLAY_NAME,
                Queries.Scoreboards.Objective.NAME, Queries.Scoreboards.Objective.RENDER_TYPE)) {
            return Optional.empty();
        }

        final String criterion = container.getString(Queries.Scoreboards.Objective.CRITERION)
                .orElseThrow(() -> new InvalidDataException("Missing criterion while building ObjectiveData"));
        final String displayName = container.getString(Queries.Scoreboards.Objective.DISPLAY_NAME)
                .orElseThrow(() -> new InvalidDataException("Missing display name while building ObjectiveData"));
        final String name = container.getString(Queries.Scoreboards.Objective.NAME)
                .orElseThrow(() -> new InvalidDataException("Missing name while building ObjectiveData"));
        final String displayMode = container.getString(Queries.Scoreboards.Objective.RENDER_TYPE)
                .orElseThrow(() -> new InvalidDataException("Missing display mode while building ObjectiveData"));

        return Optional.of(new ObjectiveData(criterion, displayName, name, displayMode));
    }

}