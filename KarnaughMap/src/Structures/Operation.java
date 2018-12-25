package Structures;

import java.util.ArrayList;
import Util.IO;

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
    
    private void merge(Operation op)
    {
        // adiciona os operandos de op na lista de operandos desse objeto
        op.operands.forEach( (operand) -> addOperand(operand) );
    }
    
    private boolean isPossibleToMerge(Operation op)
    {
        return op.name == name;
    }
    
    public void mergeChildIfItExists(Operation child)
    {
        int childIndex = operands.indexOf(child);
        
        if (childIndex != -1)
        {
            removeOperandWithoutMergeItsOperation(childIndex);
            merge(child);
        }
    }
    
    public void mergeChildsIfIsPossible()
    {
        Operation child;
        
        for (int i = 0; i < getNumberOfOperands(); i++)
        {
            child = operands.get(i);
            
            if (isPossibleToMerge(child))
            {
                // tomar cuidado com as linhas abaixo pois a quantidade de
                // operandos vai mudando ao longo das iteracoes do for
                removeOperandWithoutMergeItsOperation(i);
                merge(child);
            }
        }
    }
    
    private void mergeTreeIfIsPossible(Operation op)
    {
        for (int i = 0; i < op.getNumberOfOperands(); i++)
        {
            mergeTreeIfIsPossible(op.getOperand(i));
        }
        
        if (op.hasOperands())
        {
            mergeChildsIfIsPossible();
        }
    }
    
    public void mergeTreeIfIsPossible()
    {
        mergeTreeIfIsPossible(this);
    }
    
    public Operation addOperandWithoutMergeIt(int index, Operation operand)
    {
        operands.add(index, operand);
        
        return operand;
    }
    
    public Operation addOperandWithoutMergeIt(Operation operand)
    {
        return addOperandWithoutMergeIt(getNumberOfOperands(), operand);
    }
    
    public Operation addOperand(int index, Operation operand)
    {
        // checa se a operacao que se deseja adicionar e' do mesmo tipo da
        // operacao desse objeto
        /*if (isPossibleToMerge(operand))
        {
            // se for, adiciona os operandos dela na operacao desse objeto
            merge(operand);
        }
        
        else
        {*/
            addOperandWithoutMergeIt(index, operand);
        //}
        
        return operand;
    }
    
    public Operation addOperand(Operation operand)
    {
        return addOperand(getNumberOfOperands(), operand);
    }
    
    public Operation addOperand(Operations op, int value, char auxiliarValue, char[] mintermAsBinary)
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
    
    public Operation addOperand(Operations op, int value, char auxiliarValue)
    {
        return addOperand(op, value, auxiliarValue, mintermAsBinary);
    }
    
    public Operation addOperand(Operations op, char auxiliarValue, char[] mintermAsBinary)
    {
        return addOperand(op, getNumberOfOperands(), auxiliarValue, mintermAsBinary);
    }
    
    public Operation addOperand(Operations op, char auxiliarValue)
    {
        return addOperand(op, auxiliarValue, mintermAsBinary);
    }
    
    public Operation addOperand(Operations op, char[] mintermAsBinary)
    {
        return addOperand(op, '?', mintermAsBinary);
    }
    
    public void removeOperandWithoutMergeItsOperation(int index)
    {
        operands.remove(index);
    }
    
    public void removeOperand(int index)
    {
        if (hasOperands())
        {
            removeOperandWithoutMergeItsOperation(index);/*

            if (getNumberOfOperands() == 1 && connectedOperation != null &&
                connectedOperation.hasOperands())
            {
                int indexOfMeInMyFather = connectedOperation.
                        operands.indexOf(this);
                
                if (indexOfMeInMyFather != -1)
                {
                    connectedOperation.removeOperand(indexOfMeInMyFather);
                    
                    connectedOperation.addOperand(indexOfMeInMyFather, getOperand(0));
                }
            }*/
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
        if (hasOperands())
        {
            if (op.name == Operations.NONE)
            {
                str += op.getCorrespondingVariable();
            }

            else if
            (
             op.connectedOperation != null &&
             ( op.name == Operations.OR && op.connectedOperation.name == Operations.AND ||

             ( op.name == Operations.XOR || op.name == Operations.XNOR ) &&
             !(
                   op.connectedOperation.name == Operations.XOR ||
                   op.connectedOperation.name == Operations.XNOR
              )
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
    
    public void printOperation()
    {
        IO.println("" + this);
    }
}
