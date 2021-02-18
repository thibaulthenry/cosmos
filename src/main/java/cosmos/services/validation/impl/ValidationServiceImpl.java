package cosmos.services.validation.impl;

import com.google.inject.Singleton;
import cosmos.services.validation.ValidationService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.util.Identifiable;

@Singleton
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean doesOverflowMaxLength(final String sequence, final int maxLength) {
        return sequence.length() > maxLength;
    }

    @Override
    public boolean doesOverflowMaxLength(final Component text, final int maxLength) {
        return this.doesOverflowMaxLength(PlainComponentSerializer.plain().serialize(text), maxLength);
    }

    @Override
    public boolean isSelf(final Audience src, final Component target) {
        if (!(src instanceof Tamer)) {
            return false;
        }

        if (target != null) {
            return ((Tamer) src).name().equals(LegacyComponentSerializer.legacyAmpersand().serialize(target));
        }

        return false;
    }

    @Override
    public boolean isSelf(final Audience src, final Identifiable target) {
        if (!(src instanceof Identifiable)) {
            return false;
        }

        if (target != null) {
            return ((Identifiable) src).uniqueId().equals(target.uniqueId());
        }

        return false;
    }

    @Override
    public boolean isSelf(final Identifiable src, final Identifiable target) {
        return target != null && src.uniqueId().equals(target.uniqueId());
    }

}
