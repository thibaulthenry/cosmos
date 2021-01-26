package cosmos.constants;

import org.spongepowered.api.data.persistence.DataQuery;

public class Queries {

    public static final class Backup {

        public static final DataQuery DATE = DataQuery.of("Date");
        public static final DataQuery TAG = DataQuery.of("Tag");
        public static final DataQuery WORLD = DataQuery.of("World");

    }

    public static final class Advancements {

        public static final class Criterion {

            public static final DataQuery DATE = DataQuery.of("Date");
            public static final DataQuery GOAL = DataQuery.of("Goal");
            public static final DataQuery SCORE = DataQuery.of("Score");

        }

    }

    public static final class Experiences {

        public static final DataQuery EXPERIENCE = DataQuery.of("Experience");
        public static final DataQuery EXPERIENCE_FROM_START_OF_LEVEL = DataQuery.of("ExperienceFromStartOfLevel");
        public static final DataQuery EXPERIENCE_LEVEL = DataQuery.of("ExperienceLevel");
        public static final DataQuery EXPERIENCE_SINCE_LEVEL = DataQuery.of("ExperienceSinceLevel");

    }

    public static final class Healths {

        public static final DataQuery HEALTH = DataQuery.of("Health");
        public static final DataQuery MAX_HEALTH = DataQuery.of("MaxHealth");
        public static final DataQuery ABSORPTION = DataQuery.of("Absorption");

    }

    public static final class Hungers {

        public static final DataQuery EXHAUSTION = DataQuery.of("Exhaustion");
        public static final DataQuery FOOD_LEVEL = DataQuery.of("FoodLevel");
        public static final DataQuery MAX_EXHAUSTION = DataQuery.of("MaxExhaustion");
        public static final DataQuery MAX_FOOD_LEVEL = DataQuery.of("MaxFoodLevel");
        public static final DataQuery SATURATION = DataQuery.of("Saturation");

    }

    public static final class Inventories {

        public static final DataQuery INVENTORY = DataQuery.of("Inventories");
        public static final DataQuery SLOT_INDEX = DataQuery.of("SlotIndex");
        public static final DataQuery SLOT_STACK = DataQuery.of("SlotStack");

        public static final class Extended {

            public static final DataQuery CRAFTING_INVENTORY = DataQuery.of("CraftingInventory");
            public static final DataQuery ENDER_CHEST_INVENTORY = DataQuery.of("EnderChestInventory");
            public static final DataQuery PICKED_ITEM = DataQuery.of("PickedItem");
            public static final DataQuery PLAYER_INVENTORY = DataQuery.of("PlayerInventory");

        }

    }

    public static final class Scoreboards {

        public static final DataQuery DISPLAY_SLOTS = DataQuery.of("DisplaySlots");
        public static final DataQuery OBJECTIVES = DataQuery.of("Objectives");
        public static final DataQuery SCORES = DataQuery.of("PlayerScores");
        public static final DataQuery TEAMS = DataQuery.of("Teams");

        public static final class DisplaySlot {

            public static final DataQuery DISPLAY_SLOT = DataQuery.of("DisplaySlot");
            public static final DataQuery OBJECTIVE = DataQuery.of("Objective");

        }
        
        public static final class Objective {

            public static final DataQuery CRITERION = DataQuery.of("Criterion");
            public static final DataQuery DISPLAY_MODE = DataQuery.of("DisplayMode");
            public static final DataQuery DISPLAY_NAME = DataQuery.of("DisplayName");
            public static final DataQuery NAME = DataQuery.of("Name");

        }
        
        public static final class Score {

            public static final DataQuery LOCKED = DataQuery.of("Locked");
            public static final DataQuery OBJECTIVE = DataQuery.of("Objective");
            public static final DataQuery SCORE = DataQuery.of("Score");
            public static final DataQuery TARGET_NAME = DataQuery.of("Name");
            
        }
        
        public static final class Team {
            
            public static final DataQuery ALLOW_FRIENDLY_FIRE = DataQuery.of("AllowFriendlyFire");
            public static final DataQuery COLLISION_RULE = DataQuery.of("CollisionRule");
            public static final DataQuery COLOR = DataQuery.of("TeamColor");
            public static final DataQuery DEATH_MESSAGE_VISIBILITY = DataQuery.of("DeathMessageVisibility");
            public static final DataQuery DISPLAY_NAME = DataQuery.of("DisplayName");
            public static final DataQuery NAME = DataQuery.of("Name");
            public static final DataQuery NAME_TAG_VISIBILITY = DataQuery.of("NameTagVisibility");
            public static final DataQuery PLAYERS = DataQuery.of("Players");
            public static final DataQuery PREFIX = DataQuery.of("Prefix");
            public static final DataQuery SEE_FRIENDLY_INVISIBLES = DataQuery.of("SeeFriendlyInvisibles");
            public static final DataQuery SUFFIX = DataQuery.of("Suffix");
            
        }
        
    }
    
}
