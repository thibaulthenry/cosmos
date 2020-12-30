package cosmos.services.validation;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.validation.impl.ValidationServiceImpl;
import net.kyori.adventure.text.TextComponent;

@ImplementedBy(ValidationServiceImpl.class)
public interface ValidationService extends CosmosService {

    boolean doesOverflowMaxLength(String sequence, int maxLength);

    boolean doesOverflowMaxLength(TextComponent text, int maxLength);

}
