/**
 * This class is the serializable class that will be transferred from client to server and vice
 * versa. It will store a binary operation inside of it and will have getters and setters for
 * information.
 *
 * @Author Leroy Valencia
 */
import java.io.Serializable;

public class BinaryOp implements Serializable {
  public double left = 0;
  public double right = 0;
  public double result = 0;
  public Op operator = Op.ADD;

  /**
   * Constructor for a Binary Operation. No need for validation because compiler will handle
   * any ridiculous inputs. Also this is an operation so we should only be limited to the
   * size of a double which is default.
   * @param left What is on the left side of the operator.
   * @param operator The char operator that will be set to the enumerated type.
   * @param right The right side of the operator
   */
  public BinaryOp(double left, Character operator, double right) {
    this.left = left;
    this.operator = this.operator.whichOperator(operator);
    this.right = validateRight(right, this.operator);
  }

  //Inspections

  /**
   * Validate the right side of a division cannot be 0.
   * @param right the double on the right side
   * @param operator the operator that is inputted
   * @return 0 if right side is 0 and operator is divide else, return the right side.
   */
  private double validateRight(double right, Op operator){
    if(operator == Op.DIVIDE && right == 0){
      System.err.println("Cannot divide by 0, Returning 0.");
      return 0;
    }else{
      return right;
    }
  }

  // Getters
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
  /**
   * The to string method that outputs a nice format of the binary operation stored in this class.
   */
  public String toString() {
    // System.out.println(left);
    return left + " " + operator.toChar() + " " + right + " = " + result;
  }
}
