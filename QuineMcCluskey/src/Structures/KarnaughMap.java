package Structures;

import Util.*;

/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
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

            graySequence2 = getGraySequence(numberOfVariables / 2);
            graySequence1 = getGraySequence(numberOfVariables - graySequence2[0].length);

            char[][] mintermsAsBinary = mintermTable.getAllMintermsAsBinary();
            char[][] mintermsMap = new char[graySequence1.length][graySequence2.length];

            this.mintermsMap = mintermsMap;

            for (int i = 0; i < graySequence1.length; i++)
            {
                for (int j = 0; j < graySequence2.length; j++)
                {
                    if
                    (
                        Array.indexOf(
                            Array.concatArrays(graySequence2[j], graySequence1[i]),
                            mintermsAsBinary
                        ) == -1
                    )
                    {
                        mintermsMap[i][j] = '0';
                    }

                    else
                    {
                        mintermsMap[i][j] = '1';
                    }
                }
            }
        }
    }

    /**
     * Gera a sequencia de gray para a quantidade de bits especificada no
     * formato de armazenamento little endian. O bit menos significativo
     * vem primeiro.
     * <p>Exemplo para 2 bits:</p>
     * 
     * <ol style="list-style-type: none">
     *  <li>getGraySequence(2)[0] = { '0', '0' }</li>
     *  <li>getGraySequence(2)[1] = { '1', '0' }</li>
     *  <li>getGraySequence(2)[2] = { '1', '1' }</li>
     *  <li>getGraySequence(2)[3] = { '0', '1' }</li>
     * </ol>
     * 
     * <p>A leitura dos numeros deve ser feita da direita para a esquerda.</p>
     * 
     * @param numberOfBits numero de bits a serem usados
     * 
     * @return Um arranjo em que em cada uma de suas posicoes sai um arranjo
     * de caracteres sendo cada um destes um numero da sequencia.
     */

    public static final char[][] getGraySequence(int numberOfBits)
    {
        char[][] graySequence = null;

        if (numberOfBits > 0)
        {
            int numberOfLines = (int) Math.pow(2, numberOfBits);
            graySequence = new char[numberOfLines][numberOfBits];
            graySequence[0][0] = '0';
            graySequence[1][0] = '1';

            int currentNumberOfLines;
            int lastMirrorLine;

            for (int currentNumberOfBits = 1;
                    currentNumberOfBits < numberOfBits;
                    currentNumberOfBits++)
            {
                currentNumberOfLines = (int) Math.pow(2, currentNumberOfBits);
                lastMirrorLine = currentNumberOfLines * 2 - 1;

                for (int j = 0; j < currentNumberOfBits; j++)
                {
                    for (int currentLine = currentNumberOfLines - 1;
                            currentLine > -1;
                            currentLine--)
                    {
                        graySequence[lastMirrorLine - currentLine][j] = graySequence[currentLine][j];
                    }
                }

                for (int currentLine = currentNumberOfLines - 1;
                        currentLine > -1;
                        currentLine--)
                {
                    graySequence[currentLine][currentNumberOfBits] = '0';
                    graySequence[lastMirrorLine - currentLine][currentNumberOfBits] = '1';
                }
            }
        }

        return graySequence;
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

    public void printMap()
    {
        if (graySequence1 != null && graySequence2 != null && variablesNames != null)
        {
            String line = "";
            int numberOfVariablesOfGray1 = graySequence1[0].length;
            int numberOfVariablesOfGray2 = graySequence2[0].length;

            for (int i = 0; i < numberOfVariablesOfGray1; i++)
            {
                line += variablesNames[i];
            }

            line += "\\";

            for (int i = 0; i < numberOfVariablesOfGray2; i++)
            {
                line += variablesNames[i + numberOfVariablesOfGray1];
            }

            int firstColumnSize = line.length();

            if (numberOfVariablesOfGray1 > firstColumnSize)
            {
                firstColumnSize = numberOfVariablesOfGray1;
            }

            line = Strings.centerStrOnABlock(line, firstColumnSize);

            for (int i = 0; i < graySequence2.length; i++)
            {
                line += " " +
                        Strings.centerStrOnABlock
                        (
                            TableLine.getBinaryRepresentation(graySequence2[i]),
                            numberOfVariablesOfGray2
                        );
            }

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
}