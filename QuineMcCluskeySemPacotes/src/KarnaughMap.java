

/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
 */

public class KarnaughMap
{
    char[][] graySequence1;
    char[][] graySequence2;
    char[][] mintermsMap;
    int[][] decimalMintermsMap;
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
            
            mintermsMap = new char[graySequence1.length][graySequence2.length];
            decimalMintermsMap = new int[graySequence1.length][graySequence2.length];

            char[][] mintermsAsBinary = mintermTable.getAllMintermsAsBinary();
            char[] currentMinterm;

            for (int i = 0; i < graySequence1.length; i++)
            {
                for (int j = 0; j < graySequence2.length; j++)
                {
                    currentMinterm = getCorrespondingGrayNumber(i, j);
                    decimalMintermsMap[i][j] = MATH.binaryToDecimal(currentMinterm);
                    
                    if (Array.indexOf(currentMinterm, mintermsAsBinary) == -1)
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
}