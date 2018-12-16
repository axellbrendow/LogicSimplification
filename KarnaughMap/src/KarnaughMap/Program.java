package KarnaughMap;

import Structures.*;
import Util.IO;

/**
 * @author Axell Brendow (https://github.com/axell-brendow)
 */

public class Program
{
    public static void main(String[] args)
    {
        //11001101000001011100110100110011 = SoP(0,1,4,5,7,13,15,16,17,20,21,23,26,27,30,31)
        //10011110001101011101011000111101 = SoP(0,3,4,5,6,10,11,13,15,16,17,19,21,22,26,27,28,29,31)
        //00010111001101000100000001100011011110110011111110111110101100111000011111110100010101111010000010110011101000110010001011101110
        //= SoP(3,5,6,7,10,11,13,17,25,26,30,31,33,34,35,36,38,39,42,43,44,45,46,47,48,50,51,52,53,54,56,58,59,62,63,64,69,70,71,72,73,74,75,77,81,83,85,86,87,88,90,96,98,99,102,103,104,106,110,111,114,118,120,121,122,124,125,126)                                                                                                                      
        
        // Le a tabela verdade
        String truthTable = IO.readLine("\nEntre com a tabela verdade: ");
        // Le os nomes das variaveis do circuito
        String variablesNames = IO.readLine("Entre com os nomes de cada variavel (ex: \"a b c d\"): ");
        String[] namesOfVariables = variablesNames.split(" ");
        
        // Extrai os mintermos e cria uma tabela para eles
        MintermTable mintermsTable = MintermTable.getMintermsTable(truthTable);
        
        IO.println("\nMintermos:");
        mintermsTable.printTable();
        
        KarnaughMap karnaughMap = new KarnaughMap(mintermsTable, namesOfVariables);
        
        IO.println("\nMapa de Karnaugh:\n");
        karnaughMap.printMapWithLogicValues();
        IO.println("");
        karnaughMap.printMapWithMintermsAsDecimal();
        
        IO.println("\nGrupos:\n");
        karnaughMap.groupMinterms();
        karnaughMap.printGroups();
        
        IO.println("\nEstatisticas:\n");
        karnaughMap.printStatistics();

        IO.println("\nExpressao final:\n");
        karnaughMap.printExpression();
    }
}
