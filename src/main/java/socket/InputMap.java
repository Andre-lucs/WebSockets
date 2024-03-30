package socket;

public enum InputMap {
    OTHER("="),
    STRING("§");


    private String input;

    InputMap(String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }
}
