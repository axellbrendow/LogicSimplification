package Structures;

import java.util.Arrays;
import Util.*;

/**
 * @author Axell Brendow (https://github.com/axell-brendow)
 */

public class MintermTable
{
    TableLine[] table;
    int numberOfLines;
    boolean isPossibleToSimplify;

    private MintermTable(TableLine[] table)
    {
        this.table = table;
        this.numberOfLines = 0;
        this.isPossibleToSimplify = true;
    }

    public MintermTable(int tableSize)
    {
        this( new TableLine[tableSize] );
    }
    
    public boolean isPossibleToSimplify()
    {
        return isPossibleToSimplify;
    }
    
    protected static int encryptDontCare(int mintermAsDecimal)
    {
        return -mintermAsDecimal - 1;
    }
    
    protected static int decryptDontCare(int encryptedDontCare)
    {
        return encryptDontCare(encryptedDontCare);
    }
    
    protected static int decryptDontCareIfEncrypted(int dontCare)
    {
        return ( dontCare >= 0 ? dontCare : decryptDontCare(dontCare) );
    }
    
    /**
     * Percorre a tabela verdade coletando os numeros inteiros equivalentes a cada
     * mintermo.
     * 
     * <p>Ex: getMintermsAsDecimal("1010") = { 0, 2 }</p>
     * 
     * @param truthTable numero binario em forma de string em que cada bit
     * representa o retorno da funcao logica para o mintermo correspondente.
     * 
     * @return Arranjo com os numeros inteiros correspondentes de cada mintermo.
     */
    
    public static int[] getMintermsAsDecimal(String truthTable)
    {
        int length = truthTable.length();
        int numberOfMinterms = 0;
        char c;
        
        for (int i = 0; i < length; i++)
        {
            c = truthTable.charAt(i);
            
            numberOfMinterms += ( c == '1' || c == 'x' ? 1 : 0 );
        }
        
        int[] minterms = new int[numberOfMinterms];
        int mintermsCounter = 0;
        
        for (int i = 0; i < length; i++)
        {
            c = truthTable.charAt(i);
            
            switch (c)
            {
                case '1':
                    minterms[ mintermsCounter++ ] = i;
                    break;
                    
                case 'x': 
                    minterms[ mintermsCounter++ ] = encryptDontCare(i);
                    break;
                    
                default:
                    break;
            }
        }
        
        return minterms;
    }
    
    /**
     * Percorre a tabela verdade analisando quais combinacoes de variaveis fazem
     * a funcao logica retornar 1 e, entao, cria uma tabela de mintermos em que
     * cada linha tem a representacao decimal e a representacao binaria do
     * mintermo.
     * 
     * <p>Ex: getMintermsTable("1010") =</p>
     * <table>
     *  <tr>
     *      <td>{ 0 }</td> <td>{ '0', '0' }</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>{ 2 }</td> <td>{ '0', '1' }</td>
     *  </tr>
     * </table>
     * 
     * <p>Lembrando que os numeros binarios ficam guardados no modo little endian,
     * ou seja, a sua leitura deve ser feita da direita para a esquerda.</p>
     * 
     * @param truthTable numero binario em forma de string em que cada bit
     * representa o retorno da funcao logica para o mintermo correspondente.
     * 
     * @return {@code MintermTable} em que cada linha tem a representacao decimal
     * e binaria dos mintermos que fazem a funcao logica retornar 1.
     */
    
    public static MintermTable getMintermsTable(String truthTable)
    {
        int numberOfVariables = MATH.log2( truthTable.length() );
        
        int[] minterms = getMintermsAsDecimal(truthTable);
        MintermTable mintermsTable = new MintermTable(minterms.length);
        int minterm;
        
        for (int i = 0; i < minterms.length; i++)
        {
            minterm = minterms[i];
            
            mintermsTable.addLine
            (
                new int[] { minterm },
                MATH.decimalToBinary(decryptDontCareIfEncrypted(minterm), numberOfVariables)
            );
        }
        
        return mintermsTable;
    }
    
    /**
     * Percorre todas as linhas da tabela pegando as representacoes em
     * binario e adicionando nas linhas de uma matriz.
     * 
     * <p>Ex:</p>
     * <p>Tabela atual:</p>
     * <p>{ 3 } 110 // little endian</p>
     * <p>{ 5 } 101</p>
     * 
     * <p></p>
     * 
     * <p>Matriz_gerada[0] = { '1', '1', '0' }</p>
     * <p>Matriz_gerada[1] = { '1', '0', '1' }</p>
     * 
     * @return Matriz com as representacoes em binario de cada linha da tabela
     */

    public char[][] getAllMintermsAsBinary()
    {
        int numberOfVariables = table[0].mintermAsBinary.length;

        char[][] mintermsAsBinary = new char[numberOfLines][numberOfVariables];

        for (int i = 0; i < numberOfLines; i++)
        {
            mintermsAsBinary[i] = table[i].mintermAsBinary;
        }

        return mintermsAsBinary;
    }

    /**
     * Cria uma linha de tabela com os arranjos rebecidos e a adiciona no
     * final da tabela se ainda houver espaco.
     * 
     * @param mintermsAsDecimal arranjo de mintermos usados para chegar na
     * representacao binaria recebida
     * @param mintermAsBinary representacao binaria resultante
     */

    public void addLine(int[] mintermsAsDecimal, char[] mintermAsBinary)
    {
        if (numberOfLines < table.length)
        {
            table[numberOfLines++] = new TableLine(mintermsAsDecimal, mintermAsBinary);
        }
    }

    /**
     * Imprime a tabela no seguinte formato:
     * 
     * <p></p>
     * 
     * <p>{ 1 } 001</p>
     * <p>{ 2 } 010</p>
     * <p>{ 0, 4 } _00</p>
     * <p>{ 4, 5, 6, 7 } 1__</p>
     * 
     * <p>Onde os numeros decimais representam os mintermos usados; os numeros
     * binarios sem nenhum underline representam o seu respectivo mintermo
     * na base binaria; os numeros binarios com underline representam a
     * simplificacao pela propriedade xy + xy' = x (eliminacao de uma variavel).</p>
     */

    public void printTable()
    {
        TableLine line;

        for (int i = 0; i < table.length && (line = table[i]) != null; i++)
        {
            line.printTableLine();
        }
    }
}