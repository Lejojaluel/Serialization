/**
 * The enumeration types for Operaters. They use the constructor with a char parameter to have the +
 * mean and add and so on. I am sure that this class has to be serializable aswell due to it being
 * utilized in a serializable context.
 *
 * @Author Leroy Valencia
 */

import java.io.Serializable;

public enum Op implements Serializable {
  ADD('+'),
  SUBTRACT('-'),
  MULTIPLY('*'),
  DIVIDE('/');
  private final char operator;

  /**
   * Constructor for the enumerated type.
   * This doesn't work like a normal constructor but in a sense overrides the main creation of a enumerated type.
   * @param operator the char that can be used for the operator.
   */
  Op(char operator) {
    this.operator = operator;
  }

  /**
   * A method that determines which operator to use based on an parameter character.
   *
   * @param operator the char value that needs to be determined.
   * @return The operator which the param is equal to.
   */
  public Op whichOperator(Character operator) {
    for (Op o : Op.values()) { // loop through each enumerated type
      if (operator.equals(o.toChar())) {
        return o;
      }
    }
    System.err.println("IllegalArgumentException: Impossible reach in WhichOperator in Op.java");
    return null; // return null if invalid. This should be impossible
  }
  /**
   * Method that returns the operator in char form.
   */
  public char toChar() {
    return operator;
  }
}
