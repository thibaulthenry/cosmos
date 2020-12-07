package cosmos.services.formatter;

import com.google.inject.ImplementedBy;
import cosmos.services.formatter.impl.FormatterServiceImpl;
import net.kyori.adventure.text.TextComponent;

@ImplementedBy(FormatterServiceImpl.class)
public interface FormatterService {

    <T> TextComponent asText(T value);

}
