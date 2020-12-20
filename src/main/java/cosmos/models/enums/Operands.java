package cosmos.models.enums;

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

    Operands(final String operand) {
        this.operand = operand;
    }

    public String getOperand() {
        return this.operand;
    }
}
