package Structures;

import java.util.ArrayList;

/**
 * @author Axell Brendow (https://github.com/axell-brendow)
 */

public class Operation
{
    Operations name;
    int value;
    char auxiliarValue;
    char[] mintermAsBinary;
    String[] variablesNames;
    Operation connectedOperation;
    ArrayList<Operation> operands;
    
    public enum Operations
    {
        NONE(""),
        OR("+"),
        AND("."),
        XOR("^"),
        XNOR("~^");
        
        String symbol;
        
        Operations(String symbol)
        {
            this.symbol = symbol;
        }
        
        @Override
        public String toString()
        {
            return symbol;
        }
    }
    
    private Operation(Operations name, int value, char auxiliarValue, char[] mintermAsBinary, String[] variablesNames, Operation connectedOperation, ArrayList<Operation> operands)
    {
        this.name = name;
        this.value = value;
        this.auxiliarValue = auxiliarValue;
        this.mintermAsBinary = mintermAsBinary;
        this.variablesNames = variablesNames;
        this.connectedOperation = connectedOperation;
        this.operands = operands;
    }
    
    public Operation(Operations name, int value, char auxiliarValue, char[] mintermAsBinary, String[] variablesNames, Operation connectedOperation)
    {
        this(name, value, auxiliarValue, mintermAsBinary, variablesNames, connectedOperation, new ArrayList<Operation>());
    }
    
    public String getName()
    {
        return name.toString();
    }
    
    public int getNumberOfOperands()
    {
        return operands.size();
    }
    
    public boolean hasOperands()
    {
        return getNumberOfOperands() > 0;
    }
    
    public Operation getOperand(int index)
    {
        return operands.get(index);
    }
    
    public Operation addOperand(int index, Operation operand)
    {
        if (operand.name == name)
        {
            operand.operands.forEach( (op) -> addOperand(op) );
        }
        
        else
        {
            operands.add(index, operand);
        }
        
        return operand;
    }
    
    public Operation addOperand(Operation operand)
    {
        return addOperand(getNumberOfOperands(), operand);
    }
    
    public Operation addOperand(Operations op, int value, char auxiliarValue)
    {
        return
        addOperand
        (
            new Operation
            (
                op, value, auxiliarValue, mintermAsBinary, variablesNames, this
            )
        );
    }
    
    public Operation addOperand(Operations op, char auxiliarValue)
    {
        return addOperand(op, getNumberOfOperands(), auxiliarValue);
    }
    
    public void removeOperand(int index)
    {
        if (hasOperands())
        {
            operands.remove(index);

            if (getNumberOfOperands() == 1 && connectedOperation != null &&
                connectedOperation.hasOperands())
            {
                int indexOfMeInMyFather = connectedOperation.
                        operands.indexOf(this);

                connectedOperation.removeOperand(indexOfMeInMyFather);

                connectedOperation.addOperand(indexOfMeInMyFather, getOperand(0));
            }
        }
    }
    
    public String getCorrespondingVariable()
    {
        String variable = variablesNames[mintermAsBinary.length - 1 - value];
        
        if (mintermAsBinary[value] == '0')
        {
            variable += "'";
        }
        
        return variable;
    }
    
    public String toString(Operation op, String str)
    {
        if (op.name == Operations.NONE)
        {
            str += op.getCorrespondingVariable();
        }
        
        else if
        (
         op.name == Operations.OR && op.connectedOperation.name == Operations.AND ||
                
         ( op.name == Operations.XOR || op.name == Operations.XNOR ) &&
         !(
               op.connectedOperation.name == Operations.XOR ||
               op.connectedOperation.name == Operations.XNOR
          )
        )
        {
            str += toStringEnclosedWithParenthesis(op);
        }
        
        else
        {
            int i;
            int numberOfOperands = op.getNumberOfOperands();
            
            for (i = 0; i < numberOfOperands - 1; i++)
            {
                str = toString(op.getOperand(i), str);
                
                switch (op.name)
                {
                    case AND:
                        str += "" + op.name;
                        break;
                        
                    default:
                        str += " " + op.name + " ";
                        break;
                }
            }
            
            str = toString(op.getOperand(i), str);
        }
        
        return str;
    }
    
    private String toStringEnclosedWithParenthesis(Operation op)
    {
        return "(" + toString(op, "") + ")";
    }
    
    @Override
    public String toString()
    {
        return toString(this, "");
    }
}
