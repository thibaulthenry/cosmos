package cosmos.services.formatter;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.formatter.impl.FormatterServiceImpl;
import net.kyori.adventure.text.TextComponent;

import java.util.Locale;

@ImplementedBy(FormatterServiceImpl.class)
public interface FormatterService extends CosmosService {

    <T> TextComponent asText(T value);

    <T> TextComponent asText(T value, Locale locale);

    <T> TextComponent asText(T value, Locale locale, boolean keepOverflow);

    <T> TextComponent asText(T value, boolean keepOverflow);

}
