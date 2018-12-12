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
        
        for (int i = 0; i < length; i++)
        {
            numberOfMinterms += ( truthTable.charAt(i) == '1' ? 1 : 0 );
        }
        
        int[] minterms = new int[numberOfMinterms];
        int mintermsCounter = 0;
        
        for (int i = 0; i < length; i++)
        {
            if (truthTable.charAt(i) == '1')
            {
                minterms[ mintermsCounter++ ] = i;
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
        
        for (int i = 0; i < minterms.length; i++)
        {
            mintermsTable.addLine
            (
                new int[] { minterms[i] },
                MATH.decimalToBinary(minterms[i], numberOfVariables)
            );
        }
        
        return mintermsTable;
    }
    
    /**
     * De acordo com a quantidade de variaveis da funcao logica, descobre qual
     * e' a maior quantidade de grupos que podem ser formados entre mintermos
     * com i e i + 1 bits ligados em 1 com i indo de 0 a quantidade de variaveis - 1.
     * 
     * @param oldMintermTable antiga tabela de mintermos
     * 
     * @return A maior quantidade de grupos que podem ser formados entre mintermos
     * com i e i + 1 bits ligados em 1 com i indo de 0 a quantidade de variaveis - 1.
     */
    
    private static int getMaxSizeOfNewMintermTable(MintermTable oldMintermTable)
    {
        int maxSize = 1;
        
        if (oldMintermTable != null && oldMintermTable.numberOfLines > 0)
        {
            int previousGroupSize = 1; // tamanho do grupo de numeros com 0 "ums"
            int currentGroupSize;
            int numberOfVariables = oldMintermTable.table[0].mintermAsBinary.length;

            for (int i = 0; i < numberOfVariables; i++)
            {
                // tamanho do grupo de numeros com i + 1 "ums"
                currentGroupSize = MATH.combinationOf(numberOfVariables, i + 1);
                maxSize += previousGroupSize * currentGroupSize;
                previousGroupSize = currentGroupSize;
            }
        }
        
        return maxSize;
    }/*
    
    public static MintermTable removeGroupsThatAllMintermsWereUsed(MintermTable oldMintermTable)
    {
        MintermTable definitiveTable = oldMintermTable;
        
        if (oldMintermTable != null && oldMintermTable.numberOfLines > 0)
        {
            TableLine[] oldTable = oldMintermTable.table;
            int oldTableLength = oldMintermTable.numberOfLines;
            TableLine tableLine;
            MintermTable newMintermTable = new MintermTable(oldTableLength);
            
            boolean found;
            int[] mintermsAsDecimal;
            
            for (int i = oldTableLength - 1; i > -1; i--)
            {
                tableLine = oldTable[i];
                mintermsAsDecimal = tableLine.mintermsAsDecimal;
                
                if (mintermsAsDecimal[0] != -1)
                {
                    found = ( oldMintermTable.indexesOf(mintermsAsDecimal[0]).length > 1 );

                    // percorre os mintermos do grupo checando se eles existem em outros grupos
                    for (int j = 1; found && j < mintermsAsDecimal.length; j++)
                    {
                        found = ( oldMintermTable.indexesOf(mintermsAsDecimal[j]).length > 1 );
                    }

                    // checa se algum mintermo do grupo nao foi encontrado em outro lugar
                    if (!found)
                    {
                        newMintermTable.addLine(mintermsAsDecimal, tableLine.mintermAsBinary);
                    }

                    else // se todos foram encontrados,
                    {
                        // apaga o grupo na tabela antiga
                        Arrays.fill(mintermsAsDecimal, -1);
                    }
                }
            }
            
            int numberOfLines = newMintermTable.numberOfLines;
            definitiveTable = new MintermTable(numberOfLines);
            definitiveTable.isPossibleToSimplify = oldMintermTable.isPossibleToSimplify;
            
            for (int i = 0; i < numberOfLines; i++)
            {
                tableLine = newMintermTable.table[numberOfLines - 1 - i];
                
                definitiveTable.addLine(tableLine.mintermsAsDecimal, tableLine.mintermAsBinary);
            }
        }
        
        return definitiveTable;
    }*/
    
    /**
     * Percorre a tabela procurando simplificacoes iguais e deixa apenas uma copia.
     * 
     * @param oldMintermTable tabela de mintermos gerada pelo metodo groupMinterms
     * 
     * @return Nova {@code MintermTable} sem simplificacoes duplicadas.
     */
    
    public static MintermTable removeDuplicatedGroups(MintermTable oldMintermTable)
    {
        MintermTable newMintermTable = oldMintermTable;
        
        if (oldMintermTable != null && oldMintermTable.numberOfLines > 0)
        {
            TableLine[] oldTable = oldMintermTable.table;
            int oldTableLength = oldMintermTable.numberOfLines;
            newMintermTable = new MintermTable(oldTableLength);
            
            char[][] usedMinterms = new char[oldTableLength][oldTable[0].mintermAsBinary.length];
            int usedMintermsCounter = 0;
            char[] mintermAsBinary;

            newMintermTable.isPossibleToSimplify = oldMintermTable.isPossibleToSimplify;

            for (int i = 0; i < oldTableLength; i++)
            {
                mintermAsBinary = oldTable[i].mintermAsBinary;

                if (Array.indexOf(mintermAsBinary, usedMinterms) == -1)
                {
                    usedMinterms[ usedMintermsCounter++ ] = mintermAsBinary;
                    newMintermTable.addLine(oldTable[i].mintermsAsDecimal, mintermAsBinary);
                }
            }
        }
        
        return newMintermTable;
    }
    
    /**
     * Agrupa os mintermos com distancia hamming de 1. Obs.: E' necessario chamar
     * este metodo varias vezes ate' que o campo isPossibleToSimplify da
     * {@code MintermTable} esteja {@code false}.
     * 
     * @param oldMintermTable tabela de mintermos anterior que tenha sido gerada
     * ou pelo metodo groupMinterms ou pelo metodo getMintermsTable.
     * 
     * @return Nova {@code MintermTable} com os mintermos agrupados e a representacao
     * binaria simplificada.
     */
    
    public static MintermTable groupMinterms(MintermTable oldMintermTable)
    {
        MintermTable newMintermTable = null;
        
        if (oldMintermTable != null && oldMintermTable.numberOfLines > 0)
        {
            TableLine[] oldTable = oldMintermTable.table;
            int oldTableLength = oldMintermTable.numberOfLines;
            int sizeOfMintermsGroup = oldTable[0].mintermsAsDecimal.length * 2;
            int[] usedMinterms = new int[oldTableLength];
            int[] notUsedMinterms = new int[oldTableLength];
            int usedMintermsCounter = 0;
            int notUsedMintermsCounter = 0;
            Arrays.fill(usedMinterms, -1);

            newMintermTable = new MintermTable( getMaxSizeOfNewMintermTable(oldMintermTable) );
            int[] mintermsGroup;
            TableLine tableLine1;
            TableLine tableLine2;

            for (int i = 0; i < oldTableLength; i++)
            {
                for (int j = i + 1; j < oldTableLength; j++)
                {
                    tableLine1 = oldTable[i];
                    tableLine2 = oldTable[j];

                    if (Logic.getHammingDistance(tableLine1.mintermAsBinary, tableLine2.mintermAsBinary) == 1)
                    {
                        // adiciona os valores de i e j no arranjo de mintermos usados
                        if (Array.indexOf(i, usedMinterms) == -1 && usedMintermsCounter < usedMinterms.length)
                        {
                            usedMinterms[ usedMintermsCounter++ ] = i;
                        }

                        if (Array.indexOf(j, usedMinterms) == -1 && usedMintermsCounter < usedMinterms.length)
                        {
                            usedMinterms[ usedMintermsCounter++ ] = j;
                        }

                        // cria espaco para guardar os mintermos participantes dessa
                        // simplificacao por distancia hamming de 1
                        mintermsGroup = new int[sizeOfMintermsGroup];

                        // pega os mintermos da linha de tabela 1 e coloca no grupo de participantes
                        System.arraycopy(
                                tableLine1.mintermsAsDecimal, 0,
                                mintermsGroup, 0,
                                tableLine1.mintermsAsDecimal.length
                        );

                        // pega os mintermos da linha de tabela 2 e coloca no grupo de participantes
                        System.arraycopy(
                                tableLine2.mintermsAsDecimal, 0,
                                mintermsGroup, tableLine1.mintermsAsDecimal.length,
                                tableLine2.mintermsAsDecimal.length
                        );

                        // cria uma linha na nova tabela com os mintermos usados e
                        // com a simplificacao por QuineMcCluskey em binario
                        newMintermTable.addLine(
                                mintermsGroup,
                                Logic.removeBitOfHammingDistance1(tableLine1.mintermAsBinary, tableLine2.mintermAsBinary)
                        );
                    }
                }

                if (Array.indexOf(i, usedMinterms) == -1)
                {
                    notUsedMinterms[ notUsedMintermsCounter++ ] = i;
                }
            }

            for (int i = 0; i < notUsedMintermsCounter; i++)
            {
                newMintermTable.addLine(
                        oldTable[ notUsedMinterms[i] ].mintermsAsDecimal,
                        oldTable[ notUsedMinterms[i] ].mintermAsBinary
                );
            }

            if (usedMintermsCounter == 0)
            {
                newMintermTable = oldMintermTable;
                newMintermTable.isPossibleToSimplify = false;/*
                newMintermTable = removeDuplicatedGroups(newMintermTable);
                newMintermTable = removeGroupsThatAllMintermsWereUsed(newMintermTable);*/
            }

            else
            {
                newMintermTable = removeDuplicatedGroups(newMintermTable);
            }
        }
        
        return newMintermTable;
    }
    
    /**
     * Pega todos os mintermos da tabela e retorna um arranjo com eles em ordem
     * crescente. Sem nenhum mintermo duplicado.
     * 
     * @param mintermTable tabela de mintermos a ser percorrida
     * 
     * @return Arranjo com os mintermos da tabela em ordem crescente.
     */
    
    public static int[] getAllMintermsInCrescentOrder(MintermTable mintermTable)
    {
        int[] minterms = null;
        
        if (mintermTable != null)
        {
            int numberOfLines = mintermTable.numberOfLines;
            
            if (numberOfLines > 0)
            {
                int[] tableMinterms = new int[ numberOfLines * mintermTable.table[0].mintermsAsDecimal.length ];
                Arrays.fill(tableMinterms, -1);
                int mintermsCounter = 0;
                int[] mintermsAsDecimal;
                int minterm;
                
                for (int i = 0; i < numberOfLines; i++)
                {
                    mintermsAsDecimal = mintermTable.table[i].mintermsAsDecimal;
                    
                    for (int j = 0; j < mintermsAsDecimal.length; j++)
                    {
                        minterm = mintermsAsDecimal[j];
                        
                        if (Array.indexOf(minterm, tableMinterms) == -1)
                        {
                            tableMinterms[ mintermsCounter++ ] = minterm;
                        }
                    }
                }
                
                minterms = new int[mintermsCounter];
                
                System.arraycopy(tableMinterms, 0, minterms, 0, mintermsCounter);
                
                Arrays.sort(minterms);
            }
        }
        
        return minterms;
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
     * Procura um mintermo por toda a tabela e vai guardando em pares os
     * indices das linhas e colunas, respectivamente, onde ele for
     * encontrado. Caso nao seja encontrado, a funcao retorna uma matriz
     * com 0 linhas.
     * 
     * @param minterm mintermo a ser procurado
     * 
     * @return Matriz em que cada linha tem um par de indices que representa
     * a linha e a coluna onde o mintermo foi encontrado. A matriz tera' <i>n</i>
     * linhas e 2 colunas, sendo <i>n</i> a quantidade de vezes que o mintermo foi
     * encontrado.
     */

    public int[][] indexesOf(int minterm)
    {
        int column;
        int[][] indexes = new int[numberOfLines][2];
        int indexesCounter = 0;

        for (int i = 0; i < numberOfLines; i++)
        {
            column = Array.indexOf(minterm, table[i].mintermsAsDecimal);

            if (column != -1)
            {
                indexes[indexesCounter][0] = i;
                indexes[indexesCounter++][1] = column;
            }
        }

        int[][] definitiveIndexes = new int[indexesCounter][2];

        for (int i = 0; i < indexesCounter; i++)
        {
            System.arraycopy(indexes[i], 0, definitiveIndexes[i], 0, 2);
        }

        return definitiveIndexes;
    }

    /**
     * Verifica se todas as linhas da tabela tem pelo menos algum elemento
     * do arranjo.
     * 
     * @param array arranjo a ser usado como base nas pesquisas
     * 
     * @return {@code true} se todas as linhas da tabela tiverem pelo menos um
     * elemento do arranjo, caso contrario, {@code false}.
     */

    public boolean eachLineHasOneOrMoreElementsOfTheArray(int[] array)
    {
        boolean eachLineHasOneOrMoreElements = false;

        if (numberOfLines > 0)
        {
            eachLineHasOneOrMoreElements = ( Array.indexOf(array, table[0].mintermsAsDecimal)[0] != -1 );

            for (int i = 1; eachLineHasOneOrMoreElements && i < numberOfLines; i++)
            {
                eachLineHasOneOrMoreElements = ( Array.indexOf(array, table[i].mintermsAsDecimal)[0] != -1 );
            }
        }

        return eachLineHasOneOrMoreElements;
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