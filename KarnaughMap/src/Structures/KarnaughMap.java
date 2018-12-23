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
    GroupingMode groupingMode;
    MintermTable groupsTable;
    int[] usedMinterms;
    int[] statistics;
    
    public enum GroupingMode
    {
        HD1,
        HD2
    }
    
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
    
    private boolean hasGraySequences()
    {
        return graySequence1 != null && graySequence2 != null;
    }
    
    private boolean hasVariablesNames()
    {
        return variablesNames != null;
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
    
    private int getNumberOfHD2Minterms()
    {
        return MATH.combinationOf(getNumberOfVariables(), 2);
    }
    
    private int getNumberOfHDMinterms()
    {
        int numberOfHDMinterms;
        
        switch (groupingMode)
        {
            case HD1:
                numberOfHDMinterms = getNumberOfVariables();
                break;
                
            case HD2:
                numberOfHDMinterms = getNumberOfHD2Minterms();
                break;
                
            default:
                numberOfHDMinterms = 0;
                break;
        }
        
        return numberOfHDMinterms;
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
    
    private int getMintermThatDoesHD2With(int mintermIndex, int nthHD2Minterm) // funcionamento semelhante ao de uma funcao hash
    {
        return getIndexesOfHD2MintermsOf(mintermIndex)[nthHD2Minterm];
    }
    
    /**
     * Obtem o indice do mintermo que faz distancia hamming com o mintermo no
     * indice {@code mintermIndex}.
     * 
     * <p>Obs.: a decisao entre distancia hamming de 1 ou de 2 fica a cargo
     * do parametro passado para o metodo groupMinterms(GroupingMode) da classe
     * KarnaughMap.</p>
     * 
     * @param mintermIndex indice do mintermo <b>base</b>
     * @param nthHDMinterm referencia para qual dos mintermos que fazem
     * distancia hamming com o mintermo <b>base</b> deve ser procurado
     * 
     * @return Indice do mintermo que faz distancia hamming com o mintermo no
     * indice {@code mintermIndex}.
     */
    
    private int getMintermThatDoesHDWith(int mintermIndex, int nthHDMinterm)
    {
        int HDMinterm;
        
        switch (groupingMode)
        {
            case HD1:
                HDMinterm = getMintermThatDoesHD1With(mintermIndex, nthHDMinterm);
                break;
                
            case HD2:
                HDMinterm = getMintermThatDoesHD2With(mintermIndex, nthHDMinterm);
                break;
                
            default:
                HDMinterm = 0;
                break;
        }
        
        return HDMinterm;
    }
    
    /**
     * Obtem os indices de todos os mintermos que fazem distancia hamming de 1
     * com o mintermo no indice {@code mintermIndex}.
     * 
     * @param mintermIndex indice do mintermo base
     * 
     * @return indices de todos os mintermos que fazem distancia hamming de 1
     * com o mintermo no indice {@code mintermIndex}.
     */
    
    private int[] getIndexesOfHD1MintermsOf(int mintermIndex)
    {
        int numberOfVariables = getNumberOfVariables();
        int[] indexesOfHD1Minterms = new int[numberOfVariables];
        
        for (int i = 0; i < numberOfVariables; i++)
        {
            indexesOfHD1Minterms[i] = getMintermThatDoesHD1With(mintermIndex, i);
        }
        
        return indexesOfHD1Minterms;
    }
    
    /**
     * Obtem os indices de todos os mintermos que fazem distancia hamming de 2
     * com o mintermo no indice {@code mintermIndex}.
     * 
     * @param mintermIndex indice do mintermo base
     * 
     * @return indices de todos os mintermos que fazem distancia hamming de 2
     * com o mintermo no indice {@code mintermIndex}.
     */
    
    private int[] getIndexesOfHD2MintermsOf(int mintermIndex)
    {
        int[] indexesOfHD1Minterms = getIndexesOfHD1MintermsOf(mintermIndex);
        int[] indexesOfHD2Minterms = new int[getNumberOfHD2Minterms()];
        Arrays.fill(indexesOfHD2Minterms, -1);
        int[] auxiliarIndexesOfHD1Minterms;
        int[] valuesToIgnore = new int[] { mintermIndex };
        
        for (int i = 0; i < indexesOfHD1Minterms.length; i++)
        {
            auxiliarIndexesOfHD1Minterms = getIndexesOfHD1MintermsOf(indexesOfHD1Minterms[i]);
            
            Array.addEachValueIfItDoesntExistInArray(indexesOfHD2Minterms, auxiliarIndexesOfHD1Minterms, valuesToIgnore);
        }
        
        return indexesOfHD2Minterms;
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
     * Percorre o arranjo de indices dos mintermos participantes do grupo
     * ({@code mintermsGroup}) e ve se todos esses mintermos tem o mintermo que
     * faz distancia hamming com eles.
     * 
     * <p>Obs.: Caso o arranjo tenha mais espacos do que a quantidade de
     * mintermos do grupo, as posicoes depois do ultimo mintermo devem estar
     * preenchidas com valores -1.</p>
     * 
     * <p>Obs.: a decisao entre distancia hamming de 1 ou de 2 fica a cargo
     * do parametro passado para o metodo groupMinterms(GroupingMode) da classe
     * KarnaughMap.</p>
     * 
     * @param mintermsGroup arranjo de indices dos mintermos participantes do
     * grupo
     * @param nthHDMinterm referencia para qual dos mintermos que faz distancia
     * hamming com cada mintermo do grupo deve ser procurado
     * 
     * @return {@code mintermsGroup} caso algum dos mintermos do grupo nao tenha
     * o mintermo que faz distancia hamming com ele. Caso contrario, retorna um
     * novo grupo que tem os indices dos membros do grupo antigo e tambem os
     * indices dos novos membros que sao os que fazem distancia hamming com os
     * antigos.
     */
    
    private int[] checkIfAllMintermsHasNthHDMinterm(int[] mintermsGroup, int nthHDMinterm)
    {
        int numberOfElements = mintermsGroup.length;
        int[] newGroup = new int[numberOfElements * 2];
        Arrays.fill(newGroup, -1);
        int indexOfNthHDMinterm;
        boolean allMintermsHasNthHDMinterm = 0 < numberOfElements;
        
        for (int i = 0; allMintermsHasNthHDMinterm && i < numberOfElements; i++)
        {
            indexOfNthHDMinterm = getMintermThatDoesHDWith(mintermsGroup[i], nthHDMinterm);
            allMintermsHasNthHDMinterm =
                    mintermMakesFuncReturnTrueOrIsADontCare(indexOfNthHDMinterm) &&
                    !Array.contains(indexOfNthHDMinterm, mintermsGroup);
            
            if (allMintermsHasNthHDMinterm)
            {
                newGroup[numberOfElements + i] = indexOfNthHDMinterm;
            }
        }
        
        if (!allMintermsHasNthHDMinterm)
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
                    !Array.contains(mintermIndex, usedMinterms))
            {
                numberOfNotUsedMinterms++;
            }
        }
        
        return numberOfNotUsedMinterms;
    }*/
    
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
        int numberOfSimplificationsOfGroup1 = group1.getNumberOfNthHDMinterms();
        int numberOfSimplificationsOfGroup2 = group2.getNumberOfNthHDMinterms();/*
        int numberOfNotUsedMintermsOfGroup1 = getNumberOfNotUsedMinterms(group1.mintermsAsDecimal);
        int numberOfNotUsedMintermsOfGroup2 = getNumberOfNotUsedMinterms(group2.mintermsAsDecimal);*/
        int effectiveSizeOfGroup1 = group1.getNumberOfElements() - countDontCares(group1);
        int effectiveSizeOfGroup2 = group2.getNumberOfElements() - countDontCares(group2);
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
        }*//*
        
        if (effectiveSizeOfGroup1 > effectiveSizeOfGroup2)
        {
            pointsOfGroup1 += 30;
        }
        
        else if (effectiveSizeOfGroup2 > effectiveSizeOfGroup1)
        {
            pointsOfGroup2 += 30;
        }*/
        
        if (pointsOfGroup2 > pointsOfGroup1)
        {
            bestGroup = group2;
        }
        
        return bestGroup;
    }
    
    private TableLine tryToReflectGroup(TableLine baseGroup, int nthHDMinterm)
    {
        TableLine reflectedGroup = baseGroup;
        
        int[] mintermsIndexes = checkIfAllMintermsHasNthHDMinterm(baseGroup.mintermsAsDecimal, nthHDMinterm);
        
        if (mintermsIndexes != baseGroup.mintermsAsDecimal)
        {
            int[] nthHDMinterms = Array.concatArrays(baseGroup.nthHDMinterms, new int[] { nthHDMinterm });
            int numberOfReflectionsByHD1 = baseGroup.numberOfReflectionsByHD1;
            int numberOfReflectionsByHD2 = baseGroup.numberOfReflectionsByHD2;
            
            switch (groupingMode)
            {
                case HD1:
                    numberOfReflectionsByHD1++;
                    break;

                case HD2:
                    numberOfReflectionsByHD2++;
                    break;
            }
            
            reflectedGroup = new TableLine
                            (
                                mintermsIndexes,
                                baseGroup.mintermAsBinary,
                                nthHDMinterms,
                                numberOfReflectionsByHD1,
                                numberOfReflectionsByHD2
                            );
        }
        
        return reflectedGroup;
    }
    
    /**
     * Tenta formar um grupo de mintermos. O primeiro mintermo, <b>base</b>, e'
     * o mintermo do indice {@code mintermIndex}. O proximo mintermo e'
     * escolhido de acordo com {@code nthHDMinterm} que indica qual dos
     * mintermos que faz distancia hamming com o <b>base</b> deve ser escolhido.
     * Para um mapa de Karnaugh de <b>n</b> variaveis, todos mintermos tem
     * <b>n</b> outros mintermos que fazem distancia hamming de 1 com eles. Da
     * mesma forma, todos os mintermos tem C(<b>n</b>, 2) (combinacao de
     * <b>n</b> tomados 2 a 2) outros mintermos que fazem distancia hamming de
     * 2 com eles.
     * 
     * <p>Obs.: a decisao entre distancia hamming de 1 ou de 2 fica a cargo
     * do parametro passado para o metodo groupMinterms(GroupingMode) da classe
     * KarnaughMap.</p>
     * 
     * @param mintermLine linha do primeiro mintermo do grupo
     * @param mintermColumn coluna do primeiro mintermo do grupo
     * @param nthHDMinterm qual dos mintermos que faz distancia hamming com o
     * mintermo <b>base</b> deve ser o segundo ponto de partida
     * 
     * @return O maior grupo que pode ser formado com o mintermo base, partindo
     * da referencia {@code nthHDMinterm}.
     */
    
    private TableLine getGreatestGroupReflectingFrom(int nthHDMinterm, TableLine group)
    {
        boolean stop = false;
        int numberOfHDMinterms = getNumberOfHDMinterms();
        int maxGroupSize = getTotalNumberOfCombinationsBetweenVariables();
        
        for (int i = nthHDMinterm; i < numberOfHDMinterms && !stop; i++)
        {
            group = tryToReflectGroup(group, i);
            
            if (groupingMode == GroupingMode.HD2)
            {
                stop = ( group.getNumberOfElements() == maxGroupSize / 2 );
            }
        }
        
        return group;
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
     * participantes do grupo. Alem disso, o arranjo {@code nthHDMinterms} tera'
     * as referencias para todos os mintermos que fazem distancia hamming com o
     * mintermo base do grupo e que foram usados nas reflexoes do grupo pelo
     * mapa.
     */
    
    private TableLine getBestGroupReflectingAsManyAsPossible(TableLine group)
    {
        TableLine bestGroup = group;
        int numberOfHDMinterms = getNumberOfHDMinterms();
        
        for (int i = 0; i < numberOfHDMinterms; i++)
        {
            bestGroup = getBestGroup(bestGroup, getGreatestGroupReflectingFrom(i, group));
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
        TableLine bestGroup = getBestGroupReflectingAsManyAsPossible
        (
            new TableLine
            (
                new int[] { convertTo1D(mintermLine, mintermColumn) },
                getCorrespondingGrayNumber(mintermLine, mintermColumn)
            )
        );
        
        if (groupingMode == GroupingMode.HD2)
        {
            GroupingMode copy = groupingMode;
            this.groupingMode = GroupingMode.HD1;
            
            bestGroup = getBestGroupReflectingAsManyAsPossible(bestGroup);
            
            this.groupingMode = copy;
        }
        
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
                }
            }
        }
        
        return newMintermTable;
    }
    
    /**
     * Percorre o mapa de Karnaugh formando os melhores grupos.
     * 
     * @param groupingMode informa qual sera' a prioridade de agrupamento, por
     * distancia hamming de 1 ou distancia hamming de 2
     */
    
    public void groupMinterms(GroupingMode groupingMode)
    {
        if (mintermsMap != null && groupsTable != null &&
                usedMinterms != null && hasGraySequences())
        {
            this.groupingMode = groupingMode;
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

                        if (!Array.contains(mintermIndex, usedMinterms))
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
            getStatistics();
            simplify();
        }
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
        if (hasGraySequences() && hasVariablesNames())
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
        if (hasGraySequences() && hasVariablesNames())
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
    
    private void simplify()
    {
        if (groupingMode == GroupingMode.HD1)
        {
            TableLine tableLine;
            int numberOfVariables = getNumberOfVariables();
            
            for (int i = 0; i < groupsTable.numberOfLines; i++)
            {
                tableLine = groupsTable.table[i];
                
                for (int nthHDMinterm : tableLine.nthHDMinterms)
                {
                    tableLine.mintermAsBinary[numberOfVariables - 1 - nthHDMinterm] = '_';
                }
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
        
        if (groupingMode == GroupingMode.HD1)
        {
            groupStr += " " + TableLine.getBinaryRepresentation(group.mintermAsBinary);
        }
        
        IO.println(groupStr);
    }
    
    public void printGroups()
    {
        if (groupsTable != null)
        {
            for (int i = 0; i < groupsTable.numberOfLines; i++)
            {
                printGroup(groupsTable.table[i]);
            }
        }
    }
    
    private void getStatistics()
    {
        for (int i = 0; i < groupsTable.numberOfLines; i++)
        {
            statistics[groupsTable.table[i].getNumberOfNthHDMinterms()]++;
        }
    }
    
    /**
     * Imprime as estatisticas de grupos de mintermos formados.
     */

    public void printStatistics()
    {
        if (statistics != null && Array.getNumberOfElementsOf(usedMinterms) > 0)
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
        
        else
        {
            IO.println("Nenhum grupo formado");
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
        if (groupsTable != null && groupsTable.numberOfLines > 0 &&
                groupingMode == GroupingMode.HD1)
        {
            String expression = getExpression(groupsTable.table[0].mintermAsBinary);

            for (int i = 1; i < groupsTable.numberOfLines; i++)
            {
                expression += " + " + getExpression(groupsTable.table[i].mintermAsBinary);
            }

            IO.println(expression);
        }
    }
}