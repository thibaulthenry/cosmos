package cosmos.constants;

import cosmos.Cosmos;
import org.spongepowered.api.ResourceKey;

public class DataKeys {

    public static final class Portal {

        public static final class Delay {

            public static final class Format {

                public static final ResourceKey DIGITAL_CLOCK = ResourceKey.of(Cosmos.NAMESPACE, "digital-clock");
                public static final ResourceKey PERCENTAGE = ResourceKey.of(Cosmos.NAMESPACE, "percentage");
                public static final ResourceKey SECONDS = ResourceKey.of(Cosmos.NAMESPACE, "seconds");
                public static final ResourceKey TICKS = ResourceKey.of(Cosmos.NAMESPACE, "ticks");

            }

        }

        public static final class Type {

            public static final ResourceKey BUTTON = ResourceKey.of(Cosmos.NAMESPACE, "button");
            public static final ResourceKey FRAME = ResourceKey.of(Cosmos.NAMESPACE, "frame");
            public static final ResourceKey PRESSURE_PLATE = ResourceKey.of(Cosmos.NAMESPACE, "pressure-plate");
            public static final ResourceKey SIGN = ResourceKey.of(Cosmos.NAMESPACE, "sign");

        }

    }

    public static final class Selector {

        public static final ResourceKey SELECTOR_WORLD = ResourceKey.of(Cosmos.NAMESPACE, "selector-worlds");

    }

}
