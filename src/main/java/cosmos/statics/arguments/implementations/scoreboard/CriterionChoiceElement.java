package cosmos.statics.arguments.implementations.scoreboard;

import com.google.common.base.CaseFormat;
import cosmos.statics.arguments.implementations.selector.ExactMatchingCommandElement;
import cosmos.statics.finders.FinderRegistry;
import cosmos.statics.finders.FinderScoreboard;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.stream.Collectors;

// TODO SpongePowered/SpongeAPI Issue #2187

@SuppressWarnings("ThrowsRuntimeException")
public class CriterionChoiceElement extends ExactMatchingCommandElement {

    public CriterionChoiceElement(Text key) {
        super(key);
    }

    @Override
    protected Iterable<String> getChoices() {
        return FinderScoreboard.getAllCriteria()
                .stream()
                .map(Criterion::getName)
                .collect(Collectors.toList());
    }

    @Override
    protected Iterable<String> getOnCompleteChoices() {
        return getChoices();
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        choice = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, choice);
        Optional<Criterion> ret = FinderRegistry.findByIdOrShortIdOrName(choice, FinderScoreboard.getAllCriteria());

        if (!ret.isPresent()) {
            throw new IllegalArgumentException("Invalid input " + choice + " was found");
        }

        return ret.get();
    }

}
