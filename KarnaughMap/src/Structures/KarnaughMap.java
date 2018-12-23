package Structures;

import Util.*;
import java.util.Arrays;

/**
 * @author Axell Brendow (https://github.com/axell-brendow)
 */

public class KarnaughMap
{
    char[][] graySequence1;
    char[][] graySequence2;
    char[][] mintermsMap;
    int[][] decimalMintermsMap;
    String[] variablesNames;
    MintermTable groupsTable;
    int[] usedMinterms;
    int[] statistics;
    
    public KarnaughMap(MintermTable mintermTable, String[] variablesNames)
    {
        if (mintermTable != null && mintermTable.numberOfLines > 0)
        {
            int numberOfVariables = mintermTable.table[0].mintermAsBinary.length;

            if (variablesNames != null && numberOfVariables == variablesNames.length)
            {
                this.variablesNames = variablesNames;
            }
            
            statistics = new int[numberOfVariables + 1];
            groupsTable = new MintermTable( (int) Math.pow(2, numberOfVariables) );
            
            graySequence2 = Logic.getGraySequence(numberOfVariables / 2);
            graySequence1 = Logic.getGraySequence(numberOfVariables - graySequence2[0].length);

            char[][] mintermsAsBinary = mintermTable.getAllMintermsAsBinary();
            mintermsMap = new char[graySequence1.length][graySequence2.length];
            decimalMintermsMap = new int[graySequence1.length][graySequence2.length];
            usedMinterms = new int[(int) Math.pow(getTotalNumberOfCombinationsBetweenVariables(), 2)];
            char[] currentMinterm;
            int grayIndex;

            for (int i = 0; i < graySequence1.length; i++)
            {
                for (int j = 0; j < graySequence2.length; j++)
                {
                    currentMinterm = getCorrespondingGrayNumber(i, j);
                    
                    decimalMintermsMap[i][j] = MATH.binaryToDecimal(currentMinterm);
                    
                    grayIndex = Array.indexOf(currentMinterm, mintermsAsBinary);
                    
                    if (grayIndex == -1)
                    {
                        mintermsMap[i][j] = '0';
                    }

                    else
                    {
                        mintermsMap[i][j] = (mintermTable.table[grayIndex].mintermsAsDecimal[0] >= 0 ? '1' : 'x');
                    }
                }
            }
        }
    }
    
    /**
     * Obtem o numero de gray correspondente ao mintermo da linha e coluna
     * especificada.
     * 
     * <p>Exemplo:</p>
     * <p>Considerando um mapa de Karnaugh de duas variaveis:</p>
     * 
     * <table>
     *  <tr>
     *      <td>a\b</td> <td>0</td> <td>1</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>0</td> <td>0</td> <td>0</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>1</td> <td>0</td> <td>0</td>
     *  </tr>
     * </table>
     * 
     * <p>getCorrespondingGrayNumber(0, 1) = { '1', '0' }</p>
     * <p>E' importante lembrar que o numero esta' como little endian, ou seja,
     * sua leitura e' feita da direita para a esquerda: 01</p>
     * 
     * @param mintermLine indice da linha do mintermo
     * @param mintermColumn indice da coluna do mintermo
     * 
     * @return Numero de gray do mintermo
     */
    
    private char[] getCorrespondingGrayNumber(int mintermLine, int mintermColumn)
    {
        return Array.concatArrays(graySequence2[mintermColumn], graySequence1[mintermLine]);
    }
    
    private int getNumberOfLines()
    {
        return graySequence1.length;
    }
    
    private int getNumberOfColumns()
    {
        return graySequence2.length;
    }
    
    private int getTotalNumberOfCombinationsBetweenVariables()
    {
        return getNumberOfLines() * getNumberOfColumns();
    }
    
    private int getNumberOfVariablesOfGray1()
    {
        return graySequence1[0].length;
    }
    
    private int getNumberOfVariablesOfGray2()
    {
        return graySequence2[0].length;
    }
    
    private int getNumberOfVariables()
    {
        return getNumberOfVariablesOfGray1() + getNumberOfVariablesOfGray2();
    }
    
    /**
     * Converte as coordenadas do mintermo no mapa de Karnaugh bidimensional para
     * a coordenada ou indice equivalente no mapa de Karnaugh unidimensional.
     * 
     * @param mintermLine linha do mintermo
     * @param mintermColumn coluna do mintermo
     * 
     * @return Coordenada do mintermo no Mapa de Karnaugh unidimensional.
     */
    
    private int convertTo1D(int mintermLine, int mintermColumn)
    {
        int numberOfColumns = getNumberOfColumns();
        int mintermIndex = mintermLine * numberOfColumns;
        
        if (mintermLine % 2 == 0)
        {
            mintermIndex += mintermColumn;
        }
        
        else
        {
            mintermIndex += numberOfColumns - 1 - mintermColumn;
        }
        
        return mintermIndex;
    }
    
    /**
     * Converte a coordenada ou indice do mintermo no mapa de Karnaugh
     * unidimensional para as coordenadas equivalentes no mapa de Karnaugh
     * bidimensional. O arranjo retornado tem exatamente duas posicoes onde a
     * primeira e' a linha e a segunda a coluna do mintermo.
     * 
     * @param mintermIndex coordenada ou indice do mintermo no mapa de Karnaugh
     * unidimensional
     * 
     * @return Coordenadas do mintermo no mapa de Karnaugh bidimensional.
     */
    
    private int[] convertTo2D(int mintermIndex)
    {
        int numberOfColumns = getNumberOfColumns();
        int[] coords = new int[2];
        
        // linha do mintermo
        coords[0] = mintermIndex / numberOfColumns;
        
        //coluna do mintermo
        coords[1] = mintermIndex % numberOfColumns;
        
        if (coords[0] % 2 == 1)
        {
            coords[1] = numberOfColumns - 1 - coords[1];
        }
        
        return coords;
    }
    
    /**
     * Neste metodo ja' e' implementada a ideia do mapa de Karnaugh de uma unica
     * dimensao, um arranjo. Fornecido o indice ({@code mintermIndex}) de um
     * mintermo <b>base</b> e uma referencia ({@code nthHD1Minterm}) para qual
     * dos mintermos que faz distancia hamming de 1 com o <b>base</b> deve ser
     * obtido, o metodo retorna o indice, no mapa de Karnaugh, do mintermo que
     * faz distancia hamming de 1 com o <b>base</b>.
     * 
     * <p>Ilustracao:</p>
     * <p>Mapa de Karnaugh bidimensional:</p>
     * 
     * <table>
     *  <tr>
     *      <td>a\b</td> <td>0</td> <td>1</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>0</td> <td>1</td> <td>0</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>1</td> <td>0</td> <td>1</td>
     *  </tr>
     * </table>
     * 
     * <p>Mapa de Karnaugh unidimensional equivalente: { 1, 0, 1, 0 }</p>
     * <p>Considerando que a linha 0 e' a primeira, as linhas impares do mapa
     * ficam invertidas no arranjo. Isso e' consequencia da organizacao do mapa
     * no arranjo que e' feita na mesma ordem em que os numeros da sequencia de
     * gray sao formados:</p>
     * 
     * <ul style="list-style-type: none">
     *  <li>00</li>
     *  <li>01</li>
     *  <li>11</li>
     *  <li>10</li>
     * </ul>
     * 
     * <p>Dessa forma, ao executar getMintermThatDoesHD1With(0, 1), voce esta
     * buscando o indice do segundo mintermo que faz distancia hamming de 1 com
     * o mintermo no indice 0. No contexto criado, o mintermo no indice 0 e' o
     * 00 e o primeiro mintermo que faz distancia hamming de 1 com ele e' o
     * ultimo da sequencia de gray (10), ou seja, a funcao retornaria 3, que e'
     * o indice dele. Como a sequencia de gray tem apenas 2 bits, cada mintermo
     * tem apenas 2 outros mintermos que fazem distancia hamming de 1 com ele.</p>
     * 
     * @param mintermIndex indice do mintermo <b>base</b>
     * @param nthHD1Minterm referencia para qual dos mintermos que faz distancia
     * hamming de 1 com o <b>base</b> deve ser obtido
     * 
     * @return Indice do mintermo que faz distancia hamming de 1 com o mintermo
     * <b>base</b>.
     */
    
    private int getMintermThatDoesHD1With(int mintermIndex, int nthHD1Minterm) // funcionamento semelhante ao de uma funcao hash
    {
        int numberOfVariables = getNumberOfVariables();
        int mapSize = (int) Math.pow(2, numberOfVariables - nthHD1Minterm);
        int equivalentIndexOnThisMap = mintermIndex % mapSize;
        int HD1Minterm = mapSize - 1 - equivalentIndexOnThisMap;
        
        if (mintermIndex >= mapSize)
        {
            HD1Minterm = mintermIndex + HD1Minterm - equivalentIndexOnThisMap;
        }
        
        return HD1Minterm;
    }
    
    /**
     * Transforma o indice do mintermo para as coordenadas equivalentes no mapa
     * de Karnaugh bidimensional e retorna o caractere que esta' na posicao do
     * mintermo.
     * 
     * @param mintermIndex indice do mintermo a ser analisado
     * 
     * @return O caractere que esta' na posicao do mintermo.
     */
    
    private char getLogicValue(int mintermIndex)
    {
        int[] coordsOfHD1Minterm = convertTo2D(mintermIndex);
        
        return mintermsMap[ coordsOfHD1Minterm[0] ][ coordsOfHD1Minterm[1] ];
    }
    
    /**
     * Transforma o indice do mintermo para as coordenadas equivalentes no mapa
     * de Karnaugh bidimensional e checa se no ponto exato existe o valor 'x'.
     * 
     * @param mintermIndex indice do mintermo a ser analisado
     * 
     * @return {@code true} se no ponto do mintermo existir o valor 'x'. Caso
     * contrario, {@code false}.
     */
    
    private boolean mintermIsADontCare(int mintermIndex)
    {
        return getLogicValue(mintermIndex) == 'x';
    }
    
    /**
     * Transforma o indice do mintermo para as coordenadas equivalentes no mapa
     * de Karnaugh bidimensional e checa se no ponto exato existe o valor '1' ou
     * 'x'.
     * 
     * @param mintermIndex indice do mintermo a ser analisado
     * 
     * @return {@code true} se no ponto do mintermo existir o valor '1' ou 'x'.
     * Caso contrario, {@code false}.
     */
    
    private boolean mintermMakesFuncReturnTrueOrIsADontCare(int mintermIndex)
    {
        char logicValue = getLogicValue(mintermIndex);
        
        return logicValue == '1' || logicValue == 'x';
    }
    
    /**
     * Dentre os mintermos que fazem distancia hamming de 1 com o mintermo
     * <b>base</b>, que e' o mintermo do indice {@code mintermIndex}, checa se
     * o que e' referenciado por {@code nthHD1Minterm} faz a funcao logica
     * retornar 1 ou e' um don't care.
     * 
     * @param mintermIndex indice do mintermo <b>base</b>
     * @param nthHD1Minterm referencia para qual dos mintermos que faz distancia
     * hamming de 1 com o <b>base</b> deve ser procurado
     * 
     * @return {@code true} se o mintermo referenciado por {@code nthHD1Minterm}
     * faz a funcao logica retornar 1 ou e' um don't care. Caso contrario,
     * {@code false}.
     */
    
    private boolean hasMintermThatDoesHD1(int mintermIndex, int nthHD1Minterm)
    {
        int indexOfHD1Minterm = getMintermThatDoesHD1With(mintermIndex, nthHD1Minterm);
        
        return mintermMakesFuncReturnTrueOrIsADontCare(indexOfHD1Minterm);
    }
    
    /**
     * Percorre o arranjo de indices dos mintermos participantes do grupo e ve
     * se todos esses mintermos tem o mintermo que faz distancia hamming de 1
     * com eles.
     * 
     * <p>Obs.: Caso o arranjo tenha mais espacos do que a quantidade de
     * mintermos do grupo, as posicoes depois do ultimo mintermo devem estar
     * preenchidas com valores -1.</p>
     * 
     * @param mintermsGroup arranjo de indices dos mintermos participantes do
     * grupo
     * @param nthHD1Minterm referencia para qual dos mintermos que faz distancia
     * hamming de 1 com cada mintermo do grupo deve ser procurado
     * 
     * @return {@code mintermsGroup} caso algum dos mintermos do grupo nao tenha
     * o mintermo que faz distancia hamming de 1 com ele. Caso contrario,
     * retorna um novo grupo que tem os indices dos membros do grupo antigo e
     * tambem os indices dos novos membros que sao os que fazem distancia
     * hamming de 1 com os antigos.
     */
    
    private int[] checkIfAllMintermsHasNthHD1Minterm(int[] mintermsGroup, int nthHD1Minterm)
    {
        int[] newGroup = new int[mintermsGroup.length];
        Arrays.fill(newGroup, -1);
        int numberOfElements = Array.getNumberOfElementsOf(mintermsGroup);
        int indexOfNthHD1Minterm;
        boolean allMintermsHasNthHD1Minterm = 0 < numberOfElements;
        
        for (int i = 0; allMintermsHasNthHD1Minterm && i < numberOfElements; i++)
        {
            indexOfNthHD1Minterm = getMintermThatDoesHD1With(mintermsGroup[i], nthHD1Minterm);
            allMintermsHasNthHD1Minterm = mintermMakesFuncReturnTrueOrIsADontCare(indexOfNthHD1Minterm);
            
            if (allMintermsHasNthHD1Minterm)
            {
                newGroup[numberOfElements + i] = indexOfNthHD1Minterm;
            }
        }
        
        if (!allMintermsHasNthHD1Minterm)
        {
            newGroup = mintermsGroup;
        }
        
        else
        {
            System.arraycopy(mintermsGroup, 0, newGroup, 0, numberOfElements);
        }
        
        return newGroup;
    }/*
    
    private int getNumberOfNotUsedMinterms(int[] mintermsIndexes)
    {
        int numberOfNotUsedMinterms = 0;
        
        for (int mintermIndex : mintermsIndexes)
        {
            if (!mintermIsADontCare(mintermIndex) &&
                    Array.indexOf(mintermIndex, usedMinterms) == -1)
            {
                numberOfNotUsedMinterms++;
            }
        }
        
        return numberOfNotUsedMinterms;
    }
    
    private int countDontCares(TableLine mintermsGroup)
    {
        int dontCaresCount = 0;
        int[] mintermsIndexes = mintermsGroup.mintermsAsDecimal;
        
        for (int i = 0; i < mintermsIndexes.length; i++)
        {
            if (mintermIsADontCare(mintermsIndexes[i]))
            {
                dontCaresCount++;
            }
        }
        
        return dontCaresCount;
    }*/
    
    /**
     * Tenta formar um grupo de mintermos. O primeiro mintermo, <b>base</b>, e' o
     * mintermo da linha {@code mintermLine} e coluna {@code mintermColumn}.
     * O proximo mintermo e' escolhido de acordo com {@code nthHD1Minterm} que
     * indica qual dos mintermos que faz distancia hamming de 1 com o <b>base</b>
     * deve ser escolhido. Para um mapa de Karnaugh de <b>n</b> variaveis, todos
     * mintermos tem <b>n</b> outros mintermos que fazem distancia hamming de 1
     * com ele.
     * 
     * @param mintermLine linha do primeiro mintermo do grupo
     * @param mintermColumn coluna do primeiro mintermo do grupo
     * @param nthHD1Minterm qual dos mintermos que faz distancia hamming de 1
     * com o mintermo <b>base</b> deve ser o segundo ponto de partida
     * 
     * @return O maior grupo que pode ser formado com o mintermo base, partindo
     * da referencia {@code nthHD1Minterm}.
     */
    
    private TableLine getMintermGroup(int mintermLine, int mintermColumn, int nthHD1Minterm)
    {
        int numberOfVariables = getNumberOfVariables();
        int maxGroupSize = (int) Math.pow(2, numberOfVariables - nthHD1Minterm);
        char[] mintermAsBinary = getCorrespondingGrayNumber(mintermLine, mintermColumn);
        int[] mintermsGroup = new int[maxGroupSize];
        Arrays.fill(mintermsGroup, -1);
        int currentSize = 1;
        int newSize;
        
        // adiciona o primeiro mintermo do grupo, o mintermo base.
        mintermsGroup[0] = convertTo1D(mintermLine, mintermColumn);
        
        // checa se o mintermo base tem o mintermo que faz distancia hamming de
        // 1 com ele
        if (hasMintermThatDoesHD1(mintermsGroup[0], nthHD1Minterm))
        {
            for (int i = nthHD1Minterm; i < numberOfVariables; i++)
            {
                mintermsGroup = checkIfAllMintermsHasNthHD1Minterm(mintermsGroup, i);
                newSize = Array.getNumberOfElementsOf(mintermsGroup);

                if (newSize > currentSize)
                {
                    currentSize = newSize;
                    mintermAsBinary[numberOfVariables - 1 - i] = '_';
                }
            }
        }
        
        int numberOfElements = Array.getNumberOfElementsOf(mintermsGroup);
        int[] smallerGroup = new int[numberOfElements];
        System.arraycopy(mintermsGroup, 0, smallerGroup, 0, numberOfElements);
        
        return new TableLine(smallerGroup, mintermAsBinary);
    }
    
    /**
     * Decide qual grupo de mintermos, {@code group1} ou {@code group2}, e'
     * melhor para a simplificacao final.
     * 
     * @param group1 primeiro grupo
     * @param group2 segundo grupo
     * 
     * @return O grupo que for melhor.
     */
    
    private TableLine getBestGroup(TableLine group1, TableLine group2)
    {
        TableLine bestGroup = group1;
        int numberOfSimplificationsOfGroup1 = Array.countChars('_', group1.mintermAsBinary);
        int numberOfSimplificationsOfGroup2 = Array.countChars('_', group2.mintermAsBinary);/*
        int numberOfNotUsedMintermsOfGroup1 = getNumberOfNotUsedMinterms(group1.mintermsAsDecimal);
        int numberOfNotUsedMintermsOfGroup2 = getNumberOfNotUsedMinterms(group2.mintermsAsDecimal);
        int numberOfDontCaresOfGroup1 = countDontCares(group1);
        int numberOfDontCaresOfGroup2 = countDontCares(group2);*/
        int pointsOfGroup1 = 0;
        int pointsOfGroup2 = 0;
        
        if (numberOfSimplificationsOfGroup1 > numberOfSimplificationsOfGroup2)
        {
            pointsOfGroup1 += 20;
        }
        
        else if (numberOfSimplificationsOfGroup2 > numberOfSimplificationsOfGroup1)
        {
            pointsOfGroup2 += 20;
        }/*
        
        if (numberOfNotUsedMintermsOfGroup1 > numberOfNotUsedMintermsOfGroup2)
        {
            pointsOfGroup1 += 30;
        }
        
        else if (numberOfNotUsedMintermsOfGroup2 > numberOfNotUsedMintermsOfGroup1)
        {
            pointsOfGroup2 += 30;
        }
        
        if (numberOfDontCaresOfGroup1 < numberOfDontCaresOfGroup2)
        {
            pointsOfGroup1 += 30;
        }
        
        else if (numberOfDontCaresOfGroup2 < numberOfDontCaresOfGroup1)
        {
            pointsOfGroup2 += 30;
        }*/
        
        if (pointsOfGroup2 > pointsOfGroup1)
        {
            bestGroup = group2;
        }
        
        return bestGroup;
    }
    
    /**
     * Tenta formar o melhor grupo de mintermos possivel sendo o mintermo da
     * linha {@code mintermLine} e coluna {@code mintermColumn} o primeiro
     * deles.
     * 
     * @param mintermLine linha do primeiro mintermo do grupo
     * @param mintermColumn coluna do primeiro mintermo do grupo
     * 
     * @return {@code TableLine} em que o arranjo {@code mintermsAsDecimal} tem
     * os indices, no mapa de Karnaugh unidimensional, de todos os mintermos
     * participantes do grupo. Alem disso, o arranjo {@code mintermAsBinary}
     * tera' o resultado da simplificacao na forma binaria.
     */
    
    private TableLine getMintermBestGroup(int mintermLine, int mintermColumn)
    {
        int numberOfVariables = getNumberOfVariables();
        int[] mintermsAsDecimal = { decimalMintermsMap[mintermLine][mintermColumn] };
        
        TableLine bestGroup =
                new TableLine
                (
                        mintermsAsDecimal,
                        getCorrespondingGrayNumber(mintermLine, mintermColumn)
                );
        
        for (int i = 0; i < numberOfVariables; i++)
        {
            bestGroup = getBestGroup(getMintermGroup(mintermLine, mintermColumn, i), bestGroup);
        }
        
        statistics[Array.countChars('_', bestGroup.mintermAsBinary)]++;
        
        return bestGroup;
    }
    
    /**
     * Checa se todos os mintermos do grupo {@code mintermGroup} ja' foram
     * usados em outros grupos da tabela {@code tableOfGroupsOfMinterms}. Tambem
     * e' possivel informar os indices das linhas da tabela que devem ser
     * ignoradas.
     * 
     * <p>Obs.: don't cares sao ignorados.</p>
     * 
     * @param mintermGroup grupo de mintermos
     * @param tableOfGroupsOfMinterms tabela de grupos de mintermos
     * @param groupsToIgnore indices das linhas da tabela que devem ser
     * ignoradas
     * 
     * @return {@code true} se todos os mintermos do grupo {@code mintermGroup}
     * ja' foram usados em outros grupos da tabela
     * {@code tableOfGroupsOfMinterms}.
     */
    
    private boolean allMintermsOfTheGroupWereUsed(TableLine mintermGroup, MintermTable tableOfGroupsOfMinterms, int[] groupsToIgnore)
    {
        int[] mintermsAsDecimal = mintermGroup.mintermsAsDecimal;
        boolean found = true;

        // percorre os mintermos do grupo checando se eles existem em outros grupos
        for (int i = 0; found && i < mintermsAsDecimal.length; i++)
        {
            // don't cares sao ignorados
            if (!mintermIsADontCare(mintermsAsDecimal[i]))
            {
                found = ( tableOfGroupsOfMinterms.indexesOf(mintermsAsDecimal[i], groupsToIgnore).length > 1 );
            }
        }
        
        return found;
    }
    
    /**
     * Cria uma nova {@code MintermTable} com todos os grupos em que nem todos
     * os mintermos tenham sido usados em outros grupos da tabela
     * {@code tableOfGroupsOfMinterms}.
     * 
     * @param tableOfGroupsOfMinterms tabela de grupos de mintermos
     * 
     * @return Uma nova {@code MintermTable} com todos os grupos em que nem
     * todos os mintermos tenham sido usados em outros grupos da tabela
     * {@code tableOfGroupsOfMinterms}.
     */
    
    private MintermTable removeGroupsThatAllMintermsWereUsed(MintermTable tableOfGroupsOfMinterms)
    {
        MintermTable newMintermTable = tableOfGroupsOfMinterms;
        
        if (tableOfGroupsOfMinterms != null && tableOfGroupsOfMinterms.numberOfLines > 0)
        {
            TableLine[] oldTable = tableOfGroupsOfMinterms.table;
            int oldTableLength = tableOfGroupsOfMinterms.numberOfLines;
            TableLine tableLine;
            newMintermTable = new MintermTable(oldTableLength);
            int[] groupsToIgnore = new int[oldTableLength];
            Arrays.fill(groupsToIgnore, -1);
            int counterOfGroupsToIgnore = 0;
            
            for (int i = 0; i < oldTableLength; i++)
            {
                tableLine = oldTable[i];
                
                if (!allMintermsOfTheGroupWereUsed(tableLine, tableOfGroupsOfMinterms, groupsToIgnore))
                {
                    newMintermTable.addLine(tableLine);
                }
                
                else
                {
                    groupsToIgnore[counterOfGroupsToIgnore++] = i;
                    statistics[ Array.countChars('_', tableLine.mintermAsBinary) ]--;
                }
            }
        }
        
        return newMintermTable;
    }
    
    /**
     * Percorre o mapa de Karnaugh formando os melhores grupos.
     */
    
    public void groupMinterms()
    {
        int numberOfLines = getNumberOfLines();
        int numberOfColumns = getNumberOfColumns();
        int mintermIndex;
        int[] indexesOfMintermsOfTheGroup;
        Arrays.fill(usedMinterms, -1);
        int counterOfUsedMinterms = 0;
        
        for (int i = 0; i < numberOfLines; i++)
        {
            for (int j = 0; j < numberOfColumns; j++)
            {
                if (mintermsMap[i][j] == '1')
                {
                    mintermIndex = convertTo1D(i, j);
                    
                    if (Array.indexOf(mintermIndex, usedMinterms) == -1)
                    {
                        groupsTable.addLine( getMintermBestGroup(i, j) );
                        indexesOfMintermsOfTheGroup = groupsTable.getLastLine().mintermsAsDecimal;
                        
                        System.arraycopy(
                                indexesOfMintermsOfTheGroup, 0,
                                usedMinterms, counterOfUsedMinterms,
                                indexesOfMintermsOfTheGroup.length);
                        
                        counterOfUsedMinterms += indexesOfMintermsOfTheGroup.length;
                    }
                }
            }
        }
        
        groupsTable = removeGroupsThatAllMintermsWereUsed(groupsTable);
    }
    
    /**
     * Concatena os nomes das variaveis da funcao logica. Coloca uma \ (barra invertida)
     * no meio da string. Caso a funcao logica tenha uma quantidade impar de
     * variaveis, a barra e' colocada imediatamente apos a metade.
     * 
     * <p>Ex: "abc\de"</p>
     * 
     * @return Concatenacao dos nomes das variaveis da funcao logica com uma barra
     * no meio.
     */
    
    private String getVariablesNames()
    {
        String names = "";
        int numberOfVariablesOfGray1 = getNumberOfVariablesOfGray1();
        int numberOfVariablesOfGray2 = getNumberOfVariablesOfGray2();

        for (int i = 0; i < numberOfVariablesOfGray1; i++)
        {
            names += variablesNames[i];
        }

        names += "\\";

        for (int i = 0; i < numberOfVariablesOfGray2; i++)
        {
            names += variablesNames[i + numberOfVariablesOfGray1];
        }

        return names;
    }
    
    /**
     * O cabecalho do mapa de Karnaugh e' composto pelos nomes das variaveis
     * da funcao logica e, logo a direita, de uma sequencia de gray em que a
     * quantidade de bits e' metade da quantidade de variaveis.
     * 
     * <p>Ex: "abc\de 00 01 11 10"</p>
     * 
     * @return Sequencia de gray do cabecalho.
     */
    
    private String getHeaderGraySequence()
    {
        int numberOfColumns = getNumberOfColumns();
        int numberOfVariablesOfGray2 = getNumberOfVariablesOfGray2();
        
        String headerGraySequence = "";
        
        for (int i = 0; i < numberOfColumns; i++)
        {
            headerGraySequence += " " +
                    Strings.centerStrOnABlock
                    (
                        TableLine.getBinaryRepresentation(graySequence2[i]),
                        numberOfVariablesOfGray2
                    );
        }

        return headerGraySequence;
    }
    
    /**
     *Imprime o mapa de Karnaugh no seguinte formato:
     * 
     * <p></p>
     * <p></p>
     * 
     * <table>
     *  <tr>
     *      <td>abc\de</td> <td>00</td> <td>01</td> <td>11</td> <td>10</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>000</td> <td>0</td> <td>1</td> <td>3</td> <td>2</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>001</td> <td>4</td> <td>5</td> <td>7</td> <td>6</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>011</td> <td>12</td> <td>13</td> <td>15</td> <td>14</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>010</td> <td>8</td> <td>9</td> <td>11</td> <td>10</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>110</td> <td>24</td> <td>25</td> <td>27</td> <td>26</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>111</td> <td>28</td> <td>29</td> <td>31</td> <td>30</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>101</td> <td>20</td> <td>21</td> <td>23</td> <td>22</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>100</td> <td>16</td> <td>17</td> <td>19</td> <td>18</td>
     *  </tr>
     * </table>
     * 
     * <p>Sendo "abc" e "de" os nomes das variaveis escolhidos. E os numeros
     * decimais os representantes de cada mintermo. Cada um destes sera'
     * substituido pelo valor logico 0 ou 1 de acordo com a tabela verdade.</p>
     */

    public void printMapWithLogicValues()
    {
        if (graySequence1 != null && graySequence2 != null && variablesNames != null)
        {
            String line = getVariablesNames();
            int firstColumnSize = line.length();
            int numberOfLines = getNumberOfLines();
            int numberOfColumns = getNumberOfColumns();
            int numberOfVariablesOfGray2 = getNumberOfVariablesOfGray2();
            
            line += getHeaderGraySequence();
            
            IO.println(line + "\n");
            
            for (int i = 0; i < numberOfLines; i++)
            {
                line = Strings.centerStrOnABlock
                        (
                            TableLine.getBinaryRepresentation(graySequence1[i]),
                            firstColumnSize
                        );

                for (int j = 0; j < numberOfColumns; j++)
                {
                    line += " " + Strings.centerStrOnABlock("" + mintermsMap[i][j], numberOfVariablesOfGray2);
                }

                IO.println(line);
            }
        }
    }

    /**
     *Imprime o mapa de Karnaugh no seguinte formato:
     * 
     * <p></p>
     * <p></p>
     * 
     * <table>
     *  <tr>
     *      <td>abc\de</td> <td>00</td> <td>01</td> <td>11</td> <td>10</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>000</td> <td>0</td> <td>1</td> <td>3</td> <td>2</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>001</td> <td>4</td> <td>5</td> <td>7</td> <td>6</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>011</td> <td>12</td> <td>13</td> <td>15</td> <td>14</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>010</td> <td>8</td> <td>9</td> <td>11</td> <td>10</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>110</td> <td>24</td> <td>25</td> <td>27</td> <td>26</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>111</td> <td>28</td> <td>29</td> <td>31</td> <td>30</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>101</td> <td>20</td> <td>21</td> <td>23</td> <td>22</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>100</td> <td>16</td> <td>17</td> <td>19</td> <td>18</td>
     *  </tr>
     * </table>
     * 
     * <p>Sendo "abc" e "de" os nomes das variaveis escolhidos. E os numeros
     * decimais os representantes de cada mintermo.</p>
     */

    public void printMapWithMintermsAsDecimal()
    {
        if (graySequence1 != null && graySequence2 != null && variablesNames != null)
        {
            String line = getVariablesNames();
            int firstColumnSize = line.length();
            int numberOfLines = getNumberOfLines();
            int numberOfColumns = getNumberOfColumns();
            int numberOfVariablesOfGray2 = getNumberOfVariablesOfGray2();
            
            line += getHeaderGraySequence();
            
            IO.println(line + "\n");
            
            for (int i = 0; i < numberOfLines; i++)
            {
                line = Strings.centerStrOnABlock
                        (
                            TableLine.getBinaryRepresentation(graySequence1[i]),
                            firstColumnSize
                        );

                for (int j = 0; j < numberOfColumns; j++)
                {
                    line += " " +
                            Strings.centerStrOnABlock
                            (
                                "" + decimalMintermsMap[i][j],
                                numberOfVariablesOfGray2
                            );
                }

                IO.println(line);
            }
        }
    }
    
    private int getCorrespondingDecimal(int mintermIndex)
    {
        int[] mintermCoords = convertTo2D(mintermIndex);
        
        return decimalMintermsMap[ mintermCoords[0] ][ mintermCoords[1] ];
    }
    
    private int[] getCorrespondingDecimals(int[] mintermsGroup)
    {
        int numberOfElements = Array.getNumberOfElementsOf(mintermsGroup);
        int[] decimals = new int[numberOfElements];
        
        for (int i = 0; i < numberOfElements; i++)
        {
            decimals[i] = getCorrespondingDecimal( mintermsGroup[i] );
        }
        
        return decimals;
    }
    
    private void printGroup(TableLine group)
    {
        String groupStr;
        int[] mintermsAsDecimal = getCorrespondingDecimals(group.mintermsAsDecimal);
        
        groupStr = "{ " + mintermsAsDecimal[0];
        
        for (int i = 1; i < mintermsAsDecimal.length; i++)
        {
            groupStr += ", " + mintermsAsDecimal[i];
        }
        
        groupStr += " }";
        
        IO.println(groupStr + " " + TableLine.getBinaryRepresentation(group.mintermAsBinary));
    }
    
    public void printGroups()
    {
        for (int i = 0; i < groupsTable.numberOfLines; i++)
        {
            printGroup(groupsTable.table[i]);
        }
    }
    
    /**
     * Imprime as estatisticas de grupos de mintermos formados.
     */

    public void printStatistics()
    {
        int groupCount;
        int numberOfElements;

        for (int i = 0; i < statistics.length; i++)
        {
            groupCount = statistics[i];

            if (groupCount > 0)
            {
                numberOfElements = (int) Math.pow(2, i);

                IO.println( groupCount + " grupo" + ( groupCount != 1 ? "s" : "" ) +
                        " de " + numberOfElements + " elemento" + ( numberOfElements != 1 ? "s" : "" ) );
            }
        }
    }

    /**
     * Recebe um numero binario, ja' simplificado ou nao no Mapa de Karnaugh,
     * no formato <i>little endian</i>. Retorna uma string que representa a 
     * expressao simplificada.
     * 
     * <p>Supondo que o numero binario seja { '0', '_', '1' } e os nomes das
     * variaveis sejam { "a", "b", "c" }, a string retornada sera' "ac'".</p>
     * 
     * @param mintermAsBinary numero binario, simplificado ou nao por Quine
     * McCluskey, no formato <i>little endian</i>
     * 
     * @return String que representa a expressao simplificada.
     */

    private String getExpression(char[] mintermAsBinary)
    {
        String expression = "";
        
        if (variablesNames != null)
        {
            for (int i = 0; i < variablesNames.length; i++)
            {
                if (mintermAsBinary[variablesNames.length - 1 - i] == '0')
                {
                    expression += variablesNames[i] + "'.";
                }

                else if (mintermAsBinary[variablesNames.length - 1 - i] == '1')
                {
                    expression += variablesNames[i] + ".";
                }
            }
        }
        
        int length = expression.length();
        
        if (length > 0)
        {
            expression = expression.substring(0, length - 1);
        }
        
        else
        {
            expression = "1";
        }

        return expression;
    }

    /**
     * Imprime a expressao logica simplificada.
     * 
     * <p>Obs.: e' necessario ja' ter chamado o metodo groupMinterms.</p>
     */

    public void printExpression()
    {
        String expression = getExpression(groupsTable.table[0].mintermAsBinary);

        for (int i = 1; i < groupsTable.numberOfLines; i++)
        {
            expression += " + " + getExpression(groupsTable.table[i].mintermAsBinary);
        }

        IO.println(expression);
    }
}