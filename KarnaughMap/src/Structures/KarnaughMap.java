package Structures;

import Util.*;

/**
 * @author Axell Brendow (https://github.com/axell-brendow)
 */

public class KarnaughMap
{
    char[][] graySequence1;
    char[][] graySequence2;
    char[][] mintermsMap;
    String[] variablesNames;

    public KarnaughMap(MintermTable mintermTable, String[] variablesNames)
    {
        if (mintermTable != null && mintermTable.numberOfLines > 0)
        {
            int numberOfVariables = mintermTable.table[0].mintermAsBinary.length;

            if (variablesNames != null && numberOfVariables == variablesNames.length)
            {
                this.variablesNames = variablesNames;
            }

            graySequence2 = Logic.getGraySequence(numberOfVariables / 2);
            graySequence1 = Logic.getGraySequence(numberOfVariables - graySequence2[0].length);

            char[][] mintermsAsBinary = mintermTable.getAllMintermsAsBinary();
            char[][] mintermsMap = new char[graySequence1.length][graySequence2.length];
            int grayIndex;
            
            this.mintermsMap = mintermsMap;

            for (int i = 0; i < graySequence1.length; i++)
            {
                for (int j = 0; j < graySequence2.length; j++)
                {
                    grayIndex =
                            Array.indexOf(
                                Array.concatArrays(graySequence2[j], graySequence1[i]),
                                mintermsAsBinary
                            );
                    
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
        int numberOfVariablesOfGray1 = graySequence1[0].length;
        int numberOfVariablesOfGray2 = graySequence2[0].length;

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
        int numberOfVariablesOfGray2 = graySequence2[0].length;
        
        String headerGraySequence = "";
        
        for (int i = 0; i < graySequence2.length; i++)
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
            int numberOfVariablesOfGray2 = graySequence2[0].length;
            
            line += getHeaderGraySequence();
            
            IO.println(line + "\n");
            
            for (int i = 0; i < graySequence1.length; i++)
            {
                line = Strings.centerStrOnABlock
                        (
                            TableLine.getBinaryRepresentation(graySequence1[i]),
                            firstColumnSize
                        );

                for (int j = 0; j < graySequence2.length; j++)
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
            int numberOfVariablesOfGray2 = graySequence2[0].length;
            
            line += getHeaderGraySequence();
            
            IO.println(line + "\n");
            
            for (int i = 0; i < graySequence1.length; i++)
            {
                line = Strings.centerStrOnABlock
                        (
                            TableLine.getBinaryRepresentation(graySequence1[i]),
                            firstColumnSize
                        );

                for (int j = 0; j < graySequence2.length; j++)
                {
                    line += " " +
                            Strings.centerStrOnABlock
                            (
                                "" +
                                MATH.binaryToDecimal
                                (
                                    Array.concatArrays(graySequence2[j], graySequence1[i])
                                ),
                                numberOfVariablesOfGray2
                            );
                }

                IO.println(line);
            }
        }
    }
}