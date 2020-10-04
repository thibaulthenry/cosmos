package cosmos.constants;

@SuppressWarnings("HardcodedFileSeparator")
public enum Operands {
    PLUS("+="),
    MINUS("-="),
    TIMES("*="),
    DIVIDE("/="),
    MODULUS("%="),
    ASSIGN("="),
    MIN("<"),
    MAX(">"),
    SWAPS("><");

    private final String operand;

    Operands(String operand) {
        this.operand = operand;
    }

    public String getOperand() {
        return operand;
    }
}
