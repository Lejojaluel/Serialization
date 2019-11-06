public enum Op {
    ADD('+'), SUBTRACT('-'), MULTIPLY('*'), DIVIDE('/');
    private final char operator;

    Op(char operator){
        this.operator = operator;
    }

    public static Op whichOperator(Character operator) {
        int i = 0;
        for (Op o : Op.values()) {
            //System.out.println(operator + " " + Op.ADD.toChar());
            //System.out.println(i++);
            //System.out.println(operator.equals(Op.ADD.toChar()));
            if(operator.equals(o.toChar())){
                return o;
            }
        }
        return null;
    }
    public char toChar() {
        //TODO make to string work
        return operator;
    }
}
