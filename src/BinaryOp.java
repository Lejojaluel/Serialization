import java.io.Serializable;

public class BinaryOp implements Serializable {
  private static double left = 0;
  private static double right = 0;
  private static double result = 0;
  private static Op operator;

    public BinaryOp(double left, Character operator, double right){
        //TODO add validation here
        this.left = left;
        //System.out.println(left);
        this.right = right;
        //System.out.println(right);
        this.operator = this.operator.whichOperator(operator);
        //System.out.println(operator);
        this.result = 0;
        //System.out.println(this.operator.toString(this.operator));
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


    //Setters

    public void setResult(double result) {
        this.result = result;
    }

    public String toStrings() {
        return "Hello world";
        //return left + " " + operator.toChar() + " " + right + " = " + result;
    }
}
