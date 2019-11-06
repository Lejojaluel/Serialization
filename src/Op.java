import java.io.Serializable;

public enum Op implements Serializable {
  ADD('+'),
  SUBTRACT('-'),
  MULTIPLY('*'),
  DIVIDE('/');
  private final char operator;

  Op(char operator) {
    this.operator = operator;
  }

  public Op whichOperator(Character operator) {
    int i = 0;
    for (Op o : Op.values()) {
      if (operator.equals(o.toChar())) {
        return o;
      }
    }
    return null;
  }

  public char toChar() {
    // TODO make to string work
    return operator;
  }
}
