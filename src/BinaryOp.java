import java.io.Serializable;

public class BinaryOp implements Serializable {
  public double left = 0;
  public double right = 0;
  public double result = 0;
  public Op operator = Op.ADD;

  public BinaryOp(double left, Character operator, double right) {
    // TODO add validation here
    this.left = left;
    this.right = right;
    this.operator = this.operator.whichOperator(operator);
    this.result = 0;
  }

  public double getLeft() {
    return left;
  }

  public double getRight() {
    return right;
  }

  public Op getOperator() {
    return operator;
  }

  public double getResult() {
    return result;
  }

  // Setters

  public void setResult(double result) {
    this.result = result;
  }

  @Override
  public String toString() {
    // System.out.println(left);
    return left + " " + operator.toChar() + " " + right + " = " + result;
  }
}
