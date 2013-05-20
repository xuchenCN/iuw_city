package org.train.easy;

public enum Operator {
    Add("+", 1) {
        int invoke(int first, int second) {
            return first + second;
        }
    },
    Minus("-", 1) {
        int invoke(int first, int second) {
            return first - second;
        }
    },
    Multiply("*", 2) {
        int invoke(int first, int second) {
            return first * second;
        }
    },
    Divide("/", 2) {
        int invoke(int first, int second) {
            if (!canDivide(first, second)) return -1;
            return first / second;
        }
    };
    
    private String opChar;
    private int precedence;
    
    Operator(String opChar, int precedence) {
        this.opChar = opChar;
        this.precedence = precedence;
    }
    
    abstract int invoke(int first, int second);
    
    @Override
    public String toString() {
        return this.opChar;
    }
    
    public int getPrecedence() {
        return this.precedence;
    }
    
    public static boolean canDivide(int first, int second) {
        return (first % second) == 0;
    }
    
    public static void main(String[] args) {
    	System.out.println(Operator.Add.invoke(3, 2));
    	System.out.println(Operator.Minus.invoke(3, 2));
    	System.out.println(Operator.Multiply.invoke(3, 2));
    }
}
