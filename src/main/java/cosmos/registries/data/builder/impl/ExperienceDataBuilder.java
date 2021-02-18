package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.ExperienceData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

@Singleton
public class ExperienceDataBuilder extends AbstractDataBuilder<ExperienceData> {

    @Inject
    public ExperienceDataBuilder() {
        this(ExperienceData.class, 1);
    }

    protected ExperienceDataBuilder(final Class<ExperienceData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<ExperienceData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Experiences.EXPERIENCE, Queries.Experiences.EXPERIENCE_FROM_START_OF_LEVEL,
                Queries.Experiences.EXPERIENCE_LEVEL, Queries.Experiences.EXPERIENCE_SINCE_LEVEL)) {
            return Optional.empty();
        }

        final int experience = container.getInt(Queries.Experiences.EXPERIENCE)
                .orElseThrow(() -> new InvalidDataException("Missing experience while building ExperienceData"));

        final int experienceFromStartOfLevel = container.getInt(Queries.Experiences.EXPERIENCE_FROM_START_OF_LEVEL)
                .orElseThrow(() -> new InvalidDataException("Missing experience from start of level while building ExperienceData"));

        final int experienceLevel = container.getInt(Queries.Experiences.EXPERIENCE_LEVEL)
                .orElseThrow(() -> new InvalidDataException("Missing experience level while building ExperienceData"));

        final int experienceSinceLevel = container.getInt(Queries.Experiences.EXPERIENCE_SINCE_LEVEL)
                .orElseThrow(() -> new InvalidDataException("Missing experience since level while building ExperienceData"));

        return Optional.of(new ExperienceData(experience, experienceFromStartOfLevel, experienceLevel, experienceSinceLevel));
    }

}
