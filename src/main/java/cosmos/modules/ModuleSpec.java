package cosmos.modules;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.args.ChildCommandElementExecutor;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ModuleSpec {

    private final CommandElement args;
    private final CommandExecutor executor;

    private ModuleSpec(CommandElement args, CommandExecutor executor) {
        this.args = args;
        this.executor = executor;
    }

    static Builder builder() {
        return new Builder();
    }

    CommandExecutor getExecutor() {
        return executor;
    }

    CommandElement getArgs() {
        return args;
    }

    public static final class Builder {

        private static final CommandElement DEFAULT_ARG = GenericArguments.none();
        private CommandElement args = DEFAULT_ARG;
        private Map<List<String>, CommandCallable> childCommandMap;
        private CommandExecutor fallbackExecutor;
        private CommandExecutor executor;

        Builder() {
        }

        public Builder fallbackExecutor(CommandExecutor fallbackExecutor) {
            this.fallbackExecutor = fallbackExecutor;
            return this;
        }

        public void child(CommandCallable child, String... aliases) {
            if (childCommandMap == null) {
                childCommandMap = new HashMap<>();
            }
            childCommandMap.put(ImmutableList.copyOf(aliases), child);
        }

        public void arguments(CommandElement... args) {
            this.args = GenericArguments.seq(args);
        }

        public ModuleSpec build() {
            ChildCommandElementExecutor childCommandElementExecutor =
                    registerInDispatcher(new ChildCommandElementExecutor(fallbackExecutor, null, false));

            if (args == DEFAULT_ARG) {
                arguments(childCommandElementExecutor);
            } else {
                arguments(args, childCommandElementExecutor);
            }

            return new ModuleSpec(args, executor);
        }

        private ChildCommandElementExecutor registerInDispatcher(ChildCommandElementExecutor childDispatcher) {
            for (Map.Entry<List<String>, ? extends CommandCallable> spec : childCommandMap.entrySet()) {
                childDispatcher.register(spec.getValue(), spec.getKey());
            }

            executor = childDispatcher;
            return childDispatcher;
        }
    }
}
