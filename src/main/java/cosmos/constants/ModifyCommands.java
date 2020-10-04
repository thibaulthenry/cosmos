package cosmos.constants;

public enum ModifyCommands {

    DISPLAY_NAME("displayname"),
    RENDER_TYPE("rendertype");

    private final String key;

    ModifyCommands(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return name().toLowerCase().replace("_", " ");
    }

}
