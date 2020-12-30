package cosmos.services.validation.impl;

import com.google.inject.Singleton;
import cosmos.services.validation.ValidationService;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;

@Singleton
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean doesOverflowMaxLength(final String sequence, final int maxLength) {
        return sequence.length() > maxLength;
    }

    @Override
    public boolean doesOverflowMaxLength(final TextComponent text, final int maxLength) {
        return this.doesOverflowMaxLength(PlainComponentSerializer.plain().serialize(text), maxLength);
    }

}
