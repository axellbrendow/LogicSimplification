package Structures;

import java.util.ArrayList;
import Util.IO;
import java.util.function.*;

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
    String textRepresentation;
    
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
    
    public boolean hasAllVariablesNames()
    {
        return (variablesNames != null && variablesNames.length == mintermAsBinary.length);
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
    
    public int getIndexOfMeInMyFather()
    {
        int indexOfMeInMyFather = -1;
        
        if (connectedOperation != null)
        {
            indexOfMeInMyFather = connectedOperation.operands.indexOf(this);
        }
        
        return indexOfMeInMyFather;
    }
    
    public void runTreeExecuting(
            BiFunction<Operation, Integer, Boolean> toExecuteBefore,
            BiConsumer<Operation, Integer> toExecuteBetween,
            BiConsumer<Operation, Integer> toExecuteAfter,
            Operation operation,
            int operationIndex
    )
    {
        boolean stopRecursion =
                operation == null ||
                toExecuteBefore.apply(operation, operationIndex);
        
        if (!stopRecursion)
        {
            int numberOfOperands = operation.getNumberOfOperands();

            if (numberOfOperands > 0)
            {
                if (numberOfOperands > 1)
                {
                    int i;

                    for (i = 0; i < operation.getNumberOfOperands() - 1; i++)
                    {
                        runTreeExecuting(
                            toExecuteBefore,
                            toExecuteBetween,
                            toExecuteAfter,
                            operation.getOperand(i), i
                        );
                        toExecuteBetween.accept(operation, operationIndex);
                    }

                    runTreeExecuting(
                        toExecuteBefore,
                        toExecuteBetween,
                        toExecuteAfter,
                        operation.getOperand(i), i
                    );
                }

                else
                {
                    runTreeExecuting(
                        toExecuteBefore,
                        toExecuteBetween,
                        toExecuteAfter,
                        operation.getOperand(0), 0
                    );

                    toExecuteBetween.accept(operation, operationIndex);
                }
            }

            else
            {
                toExecuteBetween.accept(operation, operationIndex);
            }

            toExecuteAfter.accept(operation, operationIndex);
        }
    }
    
    public void runTreeExecuting(
            BiFunction<Operation, Integer, Boolean> toExecuteBefore,
            BiConsumer<Operation, Integer> toExecuteBetween,
            BiConsumer<Operation, Integer> toExecuteAfter,
            Function<Operation, Boolean> toExecuteInRootBefore,
            Consumer<Operation> toExecuteInRootBetween,
            Consumer<Operation> toExecuteInRootAfter
    )
    {
        boolean stopRecursion = toExecuteInRootBefore.apply(this);
        
        if (!stopRecursion)
        {
            int numberOfOperands = getNumberOfOperands();

            if (numberOfOperands > 0)
            {
                if (numberOfOperands > 1)
                {
                    int i;

                    for (i = 0; i < getNumberOfOperands() - 1; i++)
                    {
                        runTreeExecuting(
                            toExecuteBefore,
                            toExecuteBetween,
                            toExecuteAfter,
                            getOperand(i), i
                        );
                        toExecuteInRootBetween.accept(this);
                    }

                    runTreeExecuting(
                        toExecuteBefore,
                        toExecuteBetween,
                        toExecuteAfter,
                        getOperand(i), i
                    );
                }

                else
                {
                    runTreeExecuting(
                        toExecuteBefore,
                        toExecuteBetween,
                        toExecuteAfter,
                        getOperand(0), 0
                    );

                    toExecuteInRootBetween.accept(this);
                }
            }

            else
            {
                toExecuteInRootBetween.accept(this);
            }

            toExecuteInRootAfter.accept(this);
        }
    }
    
    public Operation addOperand(int index, Operation operand)
    {
        operands.add(index, operand);
        
        if (operand != null)
        {
            operand.connectedOperation = this;
        }
        
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
    
    public void disconnect(int operandIndex)
    {
        Operation operand = getOperand(operandIndex);
        
        if (operand != null)
        {
            // desconecta o ponteiro de subida do operando
            operand.connectedOperation = null;
        }
    }
    
    public void removeOperandFromList(int index)
    {
        disconnect(index);
        operands.remove(index);
    }
    
    public void removeOperand(int index)
    {
        disconnect(index);
        
        // remove o operando
        operands.set(index, null);
    }
    
    public void removeOperand(Operation operand)
    {
        int index = operands.indexOf(operand);
        
        if (index != -1)
        {
            removeOperand(index);
        }
    }
    
    public void removeOperationsWithOnlyOneOperand()
    {
        runTreeExecuting
        (
            (operation, operationIndex) -> { return false; },
            (operation, operationIndex) -> {  },
            (operation, operationIndex) ->
            {
                if (operation.getNumberOfOperands() == 1)
                {
                    operation.connectedOperation.mergeChild(operationIndex);
                }
            },
            
            (root) -> { return false; },
            (root) -> {  },
            (root) -> {  }
        );
        
        cleanTree();
    }
    
    public void cleanOperands()
    {
        for (int i = 0; i < getNumberOfOperands(); i++)
        {
            if (getOperand(i) == null)
            {
                removeOperandFromList(i);
                i = -1;
            }
        }
    }
    
    public void cleanTree()
    {
        runTreeExecuting
        (
            (operation, operationIndex) ->
            {
                operation.cleanOperands();
                return false;
            },
            (operation, operationIndex) -> {  },
            (operation, operationIndex) -> {  },
            
            (root) ->
            {
                root.cleanOperands();
                return false;
            },
            (root) -> {  },
            (root) -> {  }
        );
    }
    
    private void merge(Operation op)
    {
        // adiciona os operandos de op na lista de operandos desse objeto
        op.operands.forEach(
            (operand) ->
            {
                addOperand(operand);
            }
        );
    }
    
    private boolean isPossibleToMerge(Operation op)
    {
        return op.name == name;
    }
    
    private void mergeChild(int childIndex)
    {
        Operation child = getOperand(childIndex);
        
        removeOperand(childIndex);
        merge(child);
    }
    
    public void mergeChildIfIsPossible(int childIndex)
    {
        Operation child = getOperand(childIndex);

        if (isPossibleToMerge(child))
        {
            mergeChild(childIndex);
        }
    }
    
    public void mergeChildsIfIsPossible()
    {
        for (int i = 0; i < getNumberOfOperands(); i++)
        {
            mergeChildIfIsPossible(i);
        }
    }
    
    public void mergeTreeIfIsPossible()
    {
        runTreeExecuting
        (
            (operation, operationIndex) -> { return false; },
            (operation, operationIndex) -> {  },
            (operation, operationIndex) ->
            {
                Operation father = operation.connectedOperation;

                if (operationIndex < father.getNumberOfOperands())
                {
                    father.mergeChildIfIsPossible(operationIndex);
                }
            },
            
            (root) -> { return false; },
            (root) -> {  },
            (root) -> {  }
        );
        
        cleanTree();
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
    
    public boolean isXOR_or_XNOR()
    {
        return ( name == Operations.XOR || name == Operations.XNOR );
    }
    
    public boolean hasMorePriorityThan(Operation operation)
    {
        return operation != null &&
        ( name == Operations.OR && operation.name == Operations.AND ||
          ( isXOR_or_XNOR() && !operation.isXOR_or_XNOR() )
        );
    }
    
    public boolean needsParenthesis()
    {
        return connectedOperation != null &&
                connectedOperation.getNumberOfOperands() > 1 &&
                hasMorePriorityThan(connectedOperation);
    }
    
    private boolean toStringBefore(Operation operation)
    {
        boolean stopRecursion = false;

        if (operation.name == Operations.NONE)
        {
            textRepresentation += operation.getCorrespondingVariable();
            stopRecursion = true;
        }

        else
        {
            if (operation.needsParenthesis())
            {
                textRepresentation += "(";
            }
        }

        return stopRecursion;
    }
    
    private void toStringBetween(Operation operation)
    {
        if (operation.getNumberOfOperands() > 1)
        {
            switch (operation.name)
            {
                case AND:
                    textRepresentation += "" + operation.name;
                    break;

                default:
                    textRepresentation += " " + operation.name + " ";
                    break;
            }
        }
    }
    
    private void toStringAfter(Operation operation)
    {
        if (operation.needsParenthesis())
        {
            textRepresentation += ")";
        }
    }
    
    @Override
    public String toString()
    {
        textRepresentation = "";
        
        if (hasAllVariablesNames())
        {
            runTreeExecuting
            (
                (operation, operationIndex) -> { return toStringBefore(operation); },
                (operation, operationIndex) -> { toStringBetween(operation); },
                (operation, operationIndex) -> { toStringAfter(operation); },

                (root) -> { return toStringBefore(this); },
                (root) -> { toStringBetween(this); },
                (root) -> { toStringAfter(this); }
            );
            
            if (textRepresentation.isEmpty())
            {
                textRepresentation = "1";
            }
        }
        
        return textRepresentation;
    }
    
    public void printOperation()
    {
        IO.println("" + this);
    }
}
