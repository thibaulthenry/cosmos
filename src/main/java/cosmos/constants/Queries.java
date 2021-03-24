package cosmos.constants;

import org.spongepowered.api.data.persistence.DataQuery;

public class Queries {

    public static final class Advancement {

        public static final class Criterion {

            public static final DataQuery DATE = DataQuery.of("Date");
            public static final DataQuery GOAL = DataQuery.of("Goal");
            public static final DataQuery SCORE = DataQuery.of("Score");

        }

    }

    public static final class Backup {

        public static final DataQuery DATE = DataQuery.of("Date");
        public static final DataQuery TAG = DataQuery.of("Tag");
        public static final DataQuery WORLD = DataQuery.of("World");

    }

    public static final class Experience {

        public static final DataQuery EXPERIENCE = DataQuery.of("Experience");
        public static final DataQuery EXPERIENCE_FROM_START_OF_LEVEL = DataQuery.of("ExperienceFromStartOfLevel");
        public static final DataQuery EXPERIENCE_LEVEL = DataQuery.of("ExperienceLevel");
        public static final DataQuery EXPERIENCE_SINCE_LEVEL = DataQuery.of("ExperienceSinceLevel");

    }

    public static final class Health {

        public static final DataQuery HEALTH = DataQuery.of("Health");
        public static final DataQuery MAX_HEALTH = DataQuery.of("MaxHealth");
        public static final DataQuery ABSORPTION = DataQuery.of("Absorption");

    }

    public static final class Hunger {

        public static final DataQuery EXHAUSTION = DataQuery.of("Exhaustion");
        public static final DataQuery FOOD_LEVEL = DataQuery.of("FoodLevel");
        public static final DataQuery MAX_EXHAUSTION = DataQuery.of("MaxExhaustion");
        public static final DataQuery MAX_FOOD_LEVEL = DataQuery.of("MaxFoodLevel");
        public static final DataQuery SATURATION = DataQuery.of("Saturation");

    }

    public static final class Inventory {

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

    public static final class Portal {

        public static final DataQuery DELAY = DataQuery.of("Delay");
        public static final DataQuery DELAY_FORMAT = DataQuery.of("DelayFormat");
        public static final DataQuery DELAY_GRADIENT_COLORS = DataQuery.of("DelayGradients");
        public static final DataQuery DELAY_SHOWN = DataQuery.of("DelayShown");
        public static final DataQuery DESTINATION = DataQuery.of("Destination");
        public static final DataQuery KEY = DataQuery.of("Key");
        public static final DataQuery NAUSEA = DataQuery.of("Nausea");
        public static final DataQuery ORIGINS = DataQuery.of("Origins");
        public static final DataQuery PARTICLES = DataQuery.of("Particles");
        public static final DataQuery PARTICLES_FLUCTUATION = DataQuery.of("ParticlesFluctuation");
        public static final DataQuery PARTICLES_SPAWN_INTERVAL = DataQuery.of("ParticlesSpawnInterval");
        public static final DataQuery PARTICLES_VIEW_DISTANCE = DataQuery.of("ParticlesViewDistance");
        public static final DataQuery SOUND_AMBIENT = DataQuery.of("SoundAmbient");
        public static final DataQuery SOUND_DELAY = DataQuery.of("SoundDelay");
        public static final DataQuery SOUND_DELAY_INTERVAL = DataQuery.of("SoundDelayInterval");
        public static final DataQuery SOUND_TRAVEL = DataQuery.of("SoundTravel");
        public static final DataQuery SOUND_TRIGGER = DataQuery.of("SoundTrigger");
        public static final DataQuery TRIGGER = DataQuery.of("Trigger");
        public static final DataQuery TYPE = DataQuery.of("Type");

        public static final class Location {

            public static final DataQuery POSITION = DataQuery.of("Position");
            public static final DataQuery WORLD = DataQuery.of("World");

        }

        public static final class Sound {

            public static final DataQuery PITCH = DataQuery.of("Pitch");
            public static final DataQuery TYPE = DataQuery.of("Type");
            public static final DataQuery VOLUME = DataQuery.of("Volume");

        }

    }

    public static final class Scoreboard {

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
