package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class ExperienceData implements ShareableSerializable<ServerPlayer> {

    private final int experience;
    private final int experienceFromStartOfLevel;
    private final int experienceLevel;
    private final int experienceSinceLevel;

    public ExperienceData(final ServerPlayer player) {
        this.experience = player.get(Keys.EXPERIENCE).orElse(0);
        this.experienceFromStartOfLevel = player.get(Keys.EXPERIENCE_FROM_START_OF_LEVEL).orElse(0);
        this.experienceLevel = player.get(Keys.EXPERIENCE_LEVEL).orElse(0);
        this.experienceSinceLevel = player.get(Keys.EXPERIENCE_SINCE_LEVEL).orElse(0);
    }

    public ExperienceData(final int experience, final int experienceFromStartOfLevel, final int experienceLevel, final int experienceSinceLevel) {
        this.experience = experience;
        this.experienceFromStartOfLevel = experienceFromStartOfLevel;
        this.experienceLevel = experienceLevel;
        this.experienceSinceLevel = experienceSinceLevel;
    }

    public ExperienceData() {
        this.experience = 0;
        this.experienceFromStartOfLevel = 0;
        this.experienceLevel = 0;
        this.experienceSinceLevel = 7;
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public void share(final ServerPlayer data) {
        data.offer(Keys.EXPERIENCE, this.experience);
        data.offer(Keys.EXPERIENCE_FROM_START_OF_LEVEL, this.experienceFromStartOfLevel);
        data.offer(Keys.EXPERIENCE_LEVEL, this.experienceLevel);
        data.offer(Keys.EXPERIENCE_SINCE_LEVEL, this.experienceSinceLevel);
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Experiences.EXPERIENCE, this.experience)
                .set(Queries.Experiences.EXPERIENCE_FROM_START_OF_LEVEL, this.experienceFromStartOfLevel)
                .set(Queries.Experiences.EXPERIENCE_LEVEL, this.experienceLevel)
                .set(Queries.Experiences.EXPERIENCE_SINCE_LEVEL, this.experienceSinceLevel);
    }

}
