import java.io.*;
import java.util.Arrays;

/**
 * @author Axell Brendow (https://github.com/axell-brendow)
 */

public class QuineMcCluskey
{
    static class KarnaughMap
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
                            indexOf(
                                concatArrays(graySequence2[j], graySequence1[i]),
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
         * <br></br>
         * <br></br>
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

                line = centerStrOnABlock(line, firstColumnSize);

                for (int i = 0; i < graySequence2.length; i++)
                {
                    line += " " +
                            centerStrOnABlock
                            (
                                TableLine.getBinaryRepresentation(graySequence2[i]),
                                numberOfVariablesOfGray2
                            );
                }

                println(line + "\n");

                for (int i = 0; i < graySequence1.length; i++)
                {
                    line = centerStrOnABlock
                            (
                                TableLine.getBinaryRepresentation(graySequence1[i]),
                                firstColumnSize
                            );

                    for (int j = 0; j < graySequence2.length; j++)
                    {
                        line += " " + centerStrOnABlock("" + mintermsMap[i][j], numberOfVariablesOfGray2);
                    }

                    println(line);
                }
            }
        }
    }
    
    static class TableLine
    {
        int[] mintermsAsDecimal;
        char[] mintermAsBinary;
        
        public TableLine(int[] mintermsAsDecimal, char[] mintermAsBinary)
        {
            this.mintermsAsDecimal = mintermsAsDecimal;
            this.mintermAsBinary = mintermAsBinary;
        }
        
        /**
         * Recebe um numero binario que esta' armazenado no modo little endian
         * e retorna uma string com o numero no modo big endian.
         * 
         * <p>Ex:</p>
         * 
         * <p>getBinaryRepresentation( new char[] { '1', '0' } ) = "01"</p>
         * 
         * @param binaryNumber
         * 
         * @return String com o numero no modo big endian.
         */
        
        public static String getBinaryRepresentation(char[] binaryNumber)
        {
            String binaryStr = "";

            for (int i = binaryNumber.length - 1; i > -1; i--)
            {
                binaryStr += binaryNumber[i];
            }

            return binaryStr;
        }
        
        public String getBinaryRepresentation()
        {
            return getBinaryRepresentation(mintermAsBinary);
        }
        
        /**
         * Transforma os mintermos decimais numa formatacao semelhante a de um
         * arranjo ou conjunto.
         * 
         * <p>Ex: "{ 0, 1, 2, 3 }"</p>
         * 
         * @return String com os mintermos decimais no formato de arranjo
         */
        
        public String getDecimalRepresentation()
        {
            String decimals = "{ " + mintermsAsDecimal[0];
            
            for (int i = 1; i < mintermsAsDecimal.length; i++)
            {
                decimals += ", " + mintermsAsDecimal[i];
            }
            
            decimals += " }";
            
            return decimals;
        }
        
        public void printTableLine()
        {
            println(toString());
        }
        
        @Override
        public String toString()
        {
            return getDecimalRepresentation() + " " + getBinaryRepresentation();
        }
    }
    
    static class MintermTable
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
        
        /**
         * Percorre todas as linhas da tabela pegando as representacoes em
         * binario e adicionando nas linhas de uma matriz.
         * 
         * <p>Ex:</p>
         * <p>Tabela atual:</p>
         * <p>{ 3 } 110 // little endian</p>
         * <p>{ 5 } 101</p>
         * <br></br>
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
            int column = -1;
            int[][] indexes = new int[numberOfLines][2];
            int indexesCounter = 0;
            
            for (int i = 0; i < numberOfLines; i++)
            {
                column = QuineMcCluskey.indexOf(minterm, table[i].mintermsAsDecimal);
                
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
                eachLineHasOneOrMoreElements = ( indexOf(array, table[0].mintermsAsDecimal)[0] != -1 );

                for (int i = 1; eachLineHasOneOrMoreElements && i < numberOfLines; i++)
                {
                    eachLineHasOneOrMoreElements = ( indexOf(array, table[i].mintermsAsDecimal)[0] != -1 );
                }
            }
            
            return eachLineHasOneOrMoreElements;
        }
        
        /**
         * Imprime a tabela no seguinte formato:
         * 
         * <br></br>
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
    
    static class CoverageMap
    {
        int[] mintermsAsDecimal;
        char[][] mintermsAsBinary;
        char[][] mintermsMap;
        boolean isPossibleToSimplify;
        int[] statistics;
        // guardara' os indices das linhas dos primos implicantes essencias
        int[] linesOfEssentialImplicantPrimes;
        int counterOfLinesOfEssentialImplicantPrimes;
        int cursorOfLinesOfEssentialImplicantPrimes;
        // guardara' os indices das linhas dos primos implicantes usados nas simplificacoes
        int[] usedImplicantPrimes;
        int counterOfUsedImplicantPrimes;
        // guardara' os indices das linhas dos primos implicantes nao essenciais
        int[] nonEssentialImplicantPrimes;
        int counterOfNonEssentialImplicantPrimes;
        // guardara' as linhas dos primos implicantes pertencentes ao menor conjunto
        // de primos implicantes que cobrem os mintermos ainda nao cobridos pelos essenciais
        int[] smallestSetOfNonEssentialImplicantPrimes;
        int counterOfTheSmallestSetOfNonEssentialImplicantPrimes;
        int cursorOfTheSmallestSetOfNonEssentialImplicantPrimes;
        boolean calledFindTheSmallestSetOfNonEssentialImplicantPrimes;
        
        public CoverageMap(int[] mintermsAsDecimal, char[][] mintermsAsBinary, char[][] mintermsMap)
        {
            this.mintermsAsDecimal = mintermsAsDecimal;
            this.mintermsAsBinary = mintermsAsBinary;
            this.mintermsMap = mintermsMap;
            this.statistics = new int[mintermsAsBinary[0].length + 1];
            this.linesOfEssentialImplicantPrimes = new int[mintermsAsBinary.length];
            this.counterOfLinesOfEssentialImplicantPrimes = 0;
            this.cursorOfLinesOfEssentialImplicantPrimes = 0;
            Arrays.fill(linesOfEssentialImplicantPrimes, -1);
            this.usedImplicantPrimes = new int[mintermsAsBinary.length];
            this.counterOfUsedImplicantPrimes = 0;
            Arrays.fill(usedImplicantPrimes, -1);
            this.nonEssentialImplicantPrimes = new int[mintermsAsBinary.length];
            this.counterOfNonEssentialImplicantPrimes = 0;
            Arrays.fill(nonEssentialImplicantPrimes, -1);
            this.smallestSetOfNonEssentialImplicantPrimes = new int[0];
            this.counterOfTheSmallestSetOfNonEssentialImplicantPrimes = 0;
            this.cursorOfTheSmallestSetOfNonEssentialImplicantPrimes = 0;
            this.isPossibleToSimplify = true;
            this.calledFindTheSmallestSetOfNonEssentialImplicantPrimes = false;
        }
        
        /**
         *Imprime o mapa de cobertura no seguinte formato:
         * 
         * <br></br>
         * <br></br>
         * 
         * <table>
         *  <tr>
         *      <td>   </td> <td>0</td> <td>2</td> <td>5</td> <td>7</td>
         *  </tr>
         * 
         *  <tr>
         *      <td>0_0</td> <td>x</td> <td>x</td> <td> </td> <td> </td>
         *  </tr>
         * 
         *  <tr>
         *      <td>1_1</td> <td> </td> <td> </td> <td>x</td> <td>x</td>
         *  </tr>
         * </table>
         * 
         * <p>Sendo "0_0" e "1_1" as simplificacoes encontradas; "0 2 5 7" os
         * mintermos envolvidos; "x" a marcacao que indica quais sao os
         * mintermos que levaram a cada simplificacao de cada linha.</p>
         */
        
        public void printMap()
        {
            int greatestMinterm = getGreatest(mintermsAsDecimal);
            int lengthOfGreatestMinterm = ( greatestMinterm > 0 ? 1 + (int) Math.log10(greatestMinterm) : 1 );
            int numberOfVariables = mintermsAsBinary[0].length;
            
            String line = createClonesAndConcatThem(" ", numberOfVariables);
            
            for (int minterm : mintermsAsDecimal)
            {
                line += " " + centerStrOnABlock("" + minterm, lengthOfGreatestMinterm);
            }
            
            println(line);
            
            for (int i = 0; i < mintermsMap.length; i++)
            {
                line = TableLine.getBinaryRepresentation(mintermsAsBinary[i]);
                
                for (char c : mintermsMap[i])
                {
                    line += " " + centerStrOnABlock("" + c, lengthOfGreatestMinterm);
                }
                
                println(line);
            }
        }
        
        /**
         * Este metodo deve ser chamado antes do metodo proceed. Ele e'
         * responsavel por procurar todos os primos implicantes essenciais
         * no mapa de cobertura.
         */
        
        public void findEssentialImplicantPrimes()
        {
            int numberOfImplicantPrimes;
            int lineOfImplicantPrime;
            int numberOfLines = mintermsMap.length;
            int numberOfColumns = mintermsMap[0].length;
            
            for (int j = 0; j < numberOfColumns; j++)
            {
                numberOfImplicantPrimes = 0;
                lineOfImplicantPrime = -1;
                
                for (int i = 0; i < numberOfLines; i++)
                {
                    if (mintermsMap[i][j] == 'x')
                    {
                        numberOfImplicantPrimes++;
                        lineOfImplicantPrime = i;
                    }
                }
                
                if (numberOfImplicantPrimes == 1 && indexOf(lineOfImplicantPrime, linesOfEssentialImplicantPrimes) == -1)
                {
                    linesOfEssentialImplicantPrimes[counterOfLinesOfEssentialImplicantPrimes++] = lineOfImplicantPrime;
                }
            }
            
            for (int i = 0; i < numberOfLines; i++)
            {
                if (indexOf(i, linesOfEssentialImplicantPrimes) == -1)
                {
                    nonEssentialImplicantPrimes[counterOfNonEssentialImplicantPrimes++] = i;
                }
            }
        }
        
        /**
         * Este metodo deve ser chamado apos o metodo proceed ter usado todos
         * os primos implicantes essenciais.
         * <p>Ele e' responsavel por percorrer o mapa de cobertura descobrindo
         * quais sao os primos implicantes nao essenciais restantes que cobrem
         * os mintermos restantes.</p>
         * <p>Dessa forma, gera uma {@code MintermTable} em que cada linha da
         * tabela guarda um arranjo de inteiros sendo cada inteiro o indice da
         * linha de um dos primos implicantes nao essenciais que cobrem um
         * mintermo restante.</p>
         * 
         * <p>Supondo que a tabela gerada tenha a seguinte configuracao:</p>
         * <table>
         *  <tr>
         *      <td>{3, 4, 5}</td> <td>{}</td>
         *  </tr>
         * 
         *  <tr>
         *      <td>{0, 1}   </td> <td>{}</td>
         *  </tr>
         * </table>
         * 
         * <p>Significa que o primeiro mintermo que ainda nao foi cobrido, podera'
         * ser cobrido pela escolha dos primos implicantes das linhas 3, 4 ou 5.</p>
         * <p>Da mesma forma, o segundo mintermo ainda nao coberto pelos primos
         * implicantes essenciais podera' ser cobrido pela escolha dos primos
         * implicantes das linhas 0 ou 1.</p>
         * 
         * @return {@code MintermTable} em que em cada linha se encontra um arranjo
         * de inteiros contendo as linhas de primos implicantes que podem ser
         * escolhidos para cobrir um dos mintermos ainda nao cobridos.
         */
        
        private MintermTable getTableOfLinesOfPrimeImplicantsForEachMinterm()
        {
            MintermTable tableOfLinesOfPrimeImplicantsForEachMinterm = new MintermTable(mintermsMap.length);
            int[] linesOfPrimeImplicantsForCurrentMinterm = new int[mintermsMap.length];
            int linesOfPrimeImplicantsForCurrentMintermCounter;
            int[] definitiveLinesOfPrimeImplicantsForCurrentMinterm;
            char c;
            
            for (int j = 0; j < mintermsMap[0].length; j++)
            {
                linesOfPrimeImplicantsForCurrentMintermCounter = 0;
                
                for (int i = 0; i < mintermsMap.length && ( c = mintermsMap[i][j] ) != '|'; i++)
                {
                    if (c == 'x')
                    {
                        linesOfPrimeImplicantsForCurrentMinterm[linesOfPrimeImplicantsForCurrentMintermCounter++] = i;
                    }
                }
                
                if (linesOfPrimeImplicantsForCurrentMintermCounter > 0)
                {
                    definitiveLinesOfPrimeImplicantsForCurrentMinterm = new int[linesOfPrimeImplicantsForCurrentMintermCounter];
                    System.arraycopy(linesOfPrimeImplicantsForCurrentMinterm, 0, definitiveLinesOfPrimeImplicantsForCurrentMinterm, 0, linesOfPrimeImplicantsForCurrentMintermCounter);
                    
                    tableOfLinesOfPrimeImplicantsForEachMinterm.addLine(definitiveLinesOfPrimeImplicantsForCurrentMinterm, new char[0]);
                }
            }
            
            return tableOfLinesOfPrimeImplicantsForEachMinterm;
        }
        
        /**
         * Tendo o resultado do metodo getTableOfLinesOfPrimeImplicantsForEachMinterm,
         * analisa e descobre qual e' a menor quantidade de primos implicantes nao
         * essenciais que podem ser escolhidos para que todos os mintermos restantes
         * sejam cobertos.
         * 
         * @param currentLine indice da linha atual na tabela
         * @param tableOfLinesOfPrimeImplicantsForEachMinterm tabela de linhas
         * de primos implicantes que cobrem cada mintermo
         * @param linesOfPrimeImplicantsToIgnore arranjo com as primeiras posicoes
         * preenchidas por linhas de primos implicantes que ja' foram usados e que,
         * portanto, grupos com estes devem ser ignorados. Depois dessas linhas e'
         * necessario que o resto do arranjo esteja preenchido com -1.
         * @param counterOfLinesOfPrimeImplicantsToIgnore contador de linhas de
         * primos implicantes que ja' foram usados
         * @param smallestSetOfPrimeImplicants menor conjunto, ate' o momento, de
         * primos implicantes que cobrem todos os mintermos
         * 
         * @return Arranjo com os indices das linhas dos primos implicantes que
         * participam do menor grupo de primos implicantes possivel que consegue
         * cobrir todos os mintermos restantes.
         */
        
        private int[] getSmallestSetOfLinesOfPrimeImplicantsThatCoverAllMinterms(int currentLine, MintermTable tableOfLinesOfPrimeImplicantsForEachMinterm, int[] linesOfPrimeImplicantsToIgnore, int counterOfLinesOfPrimeImplicantsToIgnore, int[] smallestSetOfPrimeImplicants)
        {
            if (currentLine < tableOfLinesOfPrimeImplicantsForEachMinterm.numberOfLines)
            {
                // obtem todas as linhas de primos implicantes que estao com um 'x' no mintermo atual
                int[] linesOfPrimeImplicantsForCurrentMinterm = tableOfLinesOfPrimeImplicantsForEachMinterm.table[currentLine].mintermsAsDecimal;

                // checa se nenhuma das linhas de primos implicantes esta' contida no conjunto das linhas a serem ignoradas
                if (indexOf(linesOfPrimeImplicantsToIgnore, linesOfPrimeImplicantsForCurrentMinterm)[0] == -1)
                {
                    for (int i = 0; i < linesOfPrimeImplicantsForCurrentMinterm.length; i++)
                    {
                        int[] newLinesOfPrimeImplicantsToIgnore = linesOfPrimeImplicantsToIgnore.clone();
                        newLinesOfPrimeImplicantsToIgnore[counterOfLinesOfPrimeImplicantsToIgnore] = linesOfPrimeImplicantsForCurrentMinterm[i];

                        smallestSetOfPrimeImplicants = getSmallestSetOfLinesOfPrimeImplicantsThatCoverAllMinterms(currentLine + 1, tableOfLinesOfPrimeImplicantsForEachMinterm, newLinesOfPrimeImplicantsToIgnore, counterOfLinesOfPrimeImplicantsToIgnore + 1, smallestSetOfPrimeImplicants);
                    }
                }

                else
                {
                    smallestSetOfPrimeImplicants = getSmallestSetOfLinesOfPrimeImplicantsThatCoverAllMinterms(currentLine + 1, tableOfLinesOfPrimeImplicantsForEachMinterm, linesOfPrimeImplicantsToIgnore, counterOfLinesOfPrimeImplicantsToIgnore, smallestSetOfPrimeImplicants);
                }
            }
            
            else
            {
                Arrays.fill(linesOfPrimeImplicantsToIgnore, counterOfLinesOfPrimeImplicantsToIgnore, linesOfPrimeImplicantsToIgnore.length, linesOfPrimeImplicantsToIgnore[0]);
                
                if (tableOfLinesOfPrimeImplicantsForEachMinterm.eachLineHasOneOrMoreElementsOfTheArray(linesOfPrimeImplicantsToIgnore))
                {
                    Arrays.fill(linesOfPrimeImplicantsToIgnore, counterOfLinesOfPrimeImplicantsToIgnore, linesOfPrimeImplicantsToIgnore.length, -1);
                    
                    int sizeOfSmallestSetOfPrimeImplicants = indexOf(-1, smallestSetOfPrimeImplicants);
                    sizeOfSmallestSetOfPrimeImplicants = ( sizeOfSmallestSetOfPrimeImplicants == -1 ? smallestSetOfPrimeImplicants.length : sizeOfSmallestSetOfPrimeImplicants );

                    if (counterOfLinesOfPrimeImplicantsToIgnore < sizeOfSmallestSetOfPrimeImplicants)
                    {
                        smallestSetOfPrimeImplicants = linesOfPrimeImplicantsToIgnore;
                    }
                }
                
                else
                {
                    Arrays.fill(linesOfPrimeImplicantsToIgnore, counterOfLinesOfPrimeImplicantsToIgnore, linesOfPrimeImplicantsToIgnore.length, -1);
                }
            }
            
            return smallestSetOfPrimeImplicants;
        }
        
        /**
         * Este metodo deve ser chamado apos o metodo proceed ter usado todos
         * os primos implicantes essenciais. Ele e' responsavel por encontrar
         * o menor grupo de primos implicantes nao essenciais que consegue
         * cobrir todos os mintermos restantes e, entao, adicionar as linhas
         * desses primos implicantes num arranjo da classe.
         */
        
        private void findTheSmallestSetOfNonEssentialImplicantPrimes()
        {
            calledFindTheSmallestSetOfNonEssentialImplicantPrimes = true;
            
            if (counterOfNonEssentialImplicantPrimes > 0)
            {
                MintermTable tableOfLinesOfPrimeImplicantsForEachMinterm = getTableOfLinesOfPrimeImplicantsForEachMinterm();
                int[] linesOfPrimeImplicantsToIgnore = new int[mintermsMap.length];
                Arrays.fill(linesOfPrimeImplicantsToIgnore, -1);
                
                int[] smallestChoice = new int[mintermsMap.length];
                Arrays.fill(smallestChoice, Integer.MIN_VALUE);
                
                for (int i = 0; i < tableOfLinesOfPrimeImplicantsForEachMinterm.numberOfLines; i++)
                {
                    // partindo da linha i, percorre todas as possibilidades de
                    // escolha das linhas a baixo na tabela para completar todos
                    // os mintermos e retorna a que for composta por menos escolhas
                    smallestChoice = getSmallestSetOfLinesOfPrimeImplicantsThatCoverAllMinterms(i, tableOfLinesOfPrimeImplicantsForEachMinterm, linesOfPrimeImplicantsToIgnore, 0, smallestChoice);
                }
                
                int numberOfPrimeImplicants = indexOf(-1, smallestChoice);
                numberOfPrimeImplicants = ( numberOfPrimeImplicants == -1 ? smallestChoice.length : numberOfPrimeImplicants );
                
                smallestSetOfNonEssentialImplicantPrimes = new int[numberOfPrimeImplicants];
                counterOfTheSmallestSetOfNonEssentialImplicantPrimes = numberOfPrimeImplicants;
                System.arraycopy(smallestChoice, 0, smallestSetOfNonEssentialImplicantPrimes, 0, numberOfPrimeImplicants);
            }
        }
        
        /**
         * Conta quantas marcacoes com "x" ha' na linha especificada.
         * 
         * @param lineIndex indice da linha
         * 
         * @return Quantas marcacoes com "x" ha' na linha especificada.
         */
        
        private int getLineKills(int lineIndex)
        {
            //int numberOfLines = mintermsMap.length;
            int numberOfColumns = mintermsMap[0].length;
            int lineKills = 0;
            
            for (int j = 0; j < numberOfColumns; j++)
            {
                if (mintermsMap[lineIndex][j] == 'x')
                {
                    lineKills++;
                    
                    /*for (int i = lineIndex - 1; i > -1; i--)
                    {
                        if (mintermsMap[i][j] == 'x') lineKills++;
                    }

                    for (int i = lineIndex + 1; i < numberOfLines; i++)
                    {
                        if (mintermsMap[i][j] == 'x') lineKills++;
                    }*/
                }
            }
            
            return lineKills;
        }
        
        /**
         * Procura por todo o mapa de cobertura a existencia de alguma marcacao
         * com "x".
         * 
         * @return {@code true} se existir algum "x", caso contrario, {@code false}.
         */
        
        private boolean findUncatchedMinterms()
        {
            boolean found = false;
            
            for (int i = 0; !found && i < mintermsMap.length; i++)
            {
                found = ( indexOf('x', mintermsMap[i]) != -1 );
            }
            
            return found;
        }
        
        /**
         * Percorre todos os primos implicantes que nao sao essencias e ve qual
         * deles tem mais marcacoes com "x" em sua linha.
         * 
         * @return Indice da linha do primo implicante com mais marcacoes.
         */
        
        private int getLineOfThePrimeImplicantWithMoreKills()
        {
            int lineOfThePrimeImplicantWithMoreKills = -1;
            int indexOfThePrimeImplicantWithMoreKills = -1;
            int currentPrimeImplicantLine;
            int greatestNumberOfKills = -1;
            int lineKills;

            for (int i = 0; i < counterOfNonEssentialImplicantPrimes; i++)
            {
                currentPrimeImplicantLine = nonEssentialImplicantPrimes[i];

                if (currentPrimeImplicantLine != -1)
                {
                    lineKills = getLineKills(currentPrimeImplicantLine);

                    if (lineKills > greatestNumberOfKills)
                    {
                        greatestNumberOfKills = lineKills;
                        indexOfThePrimeImplicantWithMoreKills = i;
                        lineOfThePrimeImplicantWithMoreKills = currentPrimeImplicantLine;
                    }
                }
            }

            if (indexOfThePrimeImplicantWithMoreKills != -1)
            {
                // apaga o primo implicante que nao era usado
                nonEssentialImplicantPrimes[indexOfThePrimeImplicantWithMoreKills] = -1;
            }
            
            return lineOfThePrimeImplicantWithMoreKills;
        }
        
        /**
         * Este metodo pode ser chamado tanto para comecar a simplificacao no
         * mapa de cobertura quanto para continuar a simplificacao. Quando o
         * metodo perceber que nao ha' mais como simplificar o campo
         * {@code isPossibleToSimplify} do objeto sera' definido como {@code false}.
         */
        
        public void proceed()
        {
            int lineOfImplicantPrime = -1;
            
            if (cursorOfLinesOfEssentialImplicantPrimes < counterOfLinesOfEssentialImplicantPrimes)
            {
                lineOfImplicantPrime = linesOfEssentialImplicantPrimes[cursorOfLinesOfEssentialImplicantPrimes++];
            }
            
            else
            {
                if (!calledFindTheSmallestSetOfNonEssentialImplicantPrimes)
                {
                    findTheSmallestSetOfNonEssentialImplicantPrimes();
                }
                
                if (cursorOfTheSmallestSetOfNonEssentialImplicantPrimes < counterOfTheSmallestSetOfNonEssentialImplicantPrimes)
                {
                    lineOfImplicantPrime = smallestSetOfNonEssentialImplicantPrimes[cursorOfTheSmallestSetOfNonEssentialImplicantPrimes++];
                }
                //lineOfImplicantPrime = getLineOfThePrimeImplicantWithMoreKills();
            }
            
            if (lineOfImplicantPrime != -1)
            {
                int numberOfLines = mintermsMap.length;
                int numberOfColumns = mintermsMap[0].length;
                
                usedImplicantPrimes[counterOfUsedImplicantPrimes++] = lineOfImplicantPrime;
                statistics[ countChars('_', mintermsAsBinary[lineOfImplicantPrime]) ]++;
                
                for (int j = 0; j < numberOfColumns; j++)
                {
                    if (mintermsMap[lineOfImplicantPrime][j] == 'x')
                    {
                        for (int i = lineOfImplicantPrime - 1; i > -1; i--)
                        {
                            if (mintermsMap[i][j] != '-')
                            {
                                mintermsMap[i][j] = '|';
                            }
                        }

                        for (int i = lineOfImplicantPrime + 1; i < numberOfLines; i++)
                        {
                            if (mintermsMap[i][j] != '-')
                            {
                                mintermsMap[i][j] = '|';
                            }
                        }
                    }
                    
                    mintermsMap[lineOfImplicantPrime][j] = '-';
                }
                
                isPossibleToSimplify =
                ( cursorOfLinesOfEssentialImplicantPrimes < counterOfLinesOfEssentialImplicantPrimes ||
                        findUncatchedMinterms()
                );
            }
            
            else
            {
                isPossibleToSimplify = false;
            }
        }
        
        /**
         * Imprime as estatisticas de grupos de mintermos escolhidos ate' o momento.
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

                    println( groupCount + " grupo" + ( groupCount != 1 ? "s" : "" ) +
                            " de " + numberOfElements + " elemento" + ( numberOfElements != 1 ? "s" : "" ) );
                }
            }
        }
        
        /**
         * Recebe um numero binario, ja' simplificado ou nao por Quine McCluskey,
         * no formato <i>little endian</i> e os nomes das variaveis da funcao
         * logica. Retorna uma string que representa que representa a expressao
         * simplificada.
         * 
         * <p>Supondo que o numero binario seja { '0', '_', '1' } e os nomes das
         * variaveis sejam { "a", "b", "c" }, a string retornada sera' "ac'".</p>
         * 
         * @param mintermAsBinary numero binario, simplificado ou nao por Quine
         * McCluskey, no formato <i>little endian</i>
         * @param variablesNames nomes das variaveis da funcao logica
         * 
         * @return String que representa a expressao simplificada.
         */
        
        private String getExpression(char[] mintermAsBinary, String[] variablesNames)
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
            
            expression = expression.substring(0, expression.length() - 1);
            
            return expression;
        }
        
        /**
         * Imprime a expressao logica de acordo com os primos implicantes ja'
         * escolhidos pelo metodo proceed.
         * 
         * @param variablesNames nomes das variaveis da funcao logica
         */
        
        public void printExpression(String[] variablesNames)
        {
            int numberOfVariables = mintermsAsBinary[0].length;
            
            if (variablesNames != null && variablesNames.length == numberOfVariables)
            {
                String expression = getExpression(mintermsAsBinary[ usedImplicantPrimes[0] ], variablesNames);
                
                for (int i = 1; i < counterOfUsedImplicantPrimes; i++)
                {
                    expression += " + " + getExpression(mintermsAsBinary[ usedImplicantPrimes[i] ], variablesNames);
                }
                
                println(expression);
            }
        }
    }
    
    static BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
    
    public static void print(String msg)
    {
        System.out.print(msg);
    }
    
    public static void println(String msg)
    {
        print(msg + System.lineSeparator());
    }
    
    public static String readLine()
    {
        String line = "";
        
        try
        {
            line = reader.readLine();
        }
        
        catch (IOException ex)
        {
            println("Nao foi possivel ler da entrada padrao");
        }
        
        return line;
    }
    
    public static String readLine(String msg)
    {
        print(msg);
        
        return readLine();
    }
    
    /**
     * Cria clones de uma string e os concatena.
     * 
     * @param str string a ser clonada
     * @param numberOfClones numero de clones a serem criados
     * 
     * @return String com todos os clones concatenados, justapostos.
     */
    
    public static String createClonesAndConcatThem(String str, int numberOfClones)
    {
        String clones = "";
        
        for (int i = 0; i < numberOfClones; i++)
        {
            clones += str;
        }
        
        return clones;
    }
    
    /**
     * Cria uma string com o tamanho do bloco de caracteres desejado e centraliza
     * a string recebida nele. Os espacos restantes sao preenchidos com espacos
     * em branco.
     * 
     * @param str string a ser centralizada
     * @param blockSize tamanho do bloco de caracteres desejado
     * 
     * @return String com o tamanho desejado e com a string recebida centralizada
     * nela.
     */
    
    public static String centerStrOnABlock(String str, int blockSize)
    {
        blockSize -= str.length();
        int paddingLeft = blockSize / 2;
        
        return createClonesAndConcatThem(" ", paddingLeft) +
                str +
                createClonesAndConcatThem(" ", blockSize - paddingLeft);
    }
    
    public static int log2(int number)
    {
        return number > 0 ? (int) ( Math.log(number) / Math.log(2) ) : 0;
    }
    
    /**
     * Converte um inteiro decimal para binario. O numero binario fica guardado
     * como little endian no arranjo de caracteres.
     * 
     * <p>Ex: decimalToBinary(12, 5) = { '0', '0', '1', '1', '0' }</p>
     * 
     * <p>A leitura do numero e' feita da direita para a esquerda: 01100</p>
     * 
     * @param number inteiro decimal a ser convertido
     * @param numberOfDigits numero de digitos desejados para o numero binario
     * 
     * @return Conversao do inteiro decimal para binario como little endian.
     */
    
    public static char[] decimalToBinary(int number, int numberOfDigits)
    {
        char[] binary = new char[numberOfDigits];
        
        for (int i = 0; i < numberOfDigits; i++)
        {
            switch (number % 2)
            {
                case 0:
                    binary[i] = '0';
                    break;
                    
                case 1:
                    binary[i] = '1';
                    break;
                    
                default:
                    binary[i] = 'x';
                    break;
            }
            
            number /= 2;
        }
        
        return binary;
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
        int numberOfVariables = log2( truthTable.length() );
        
        int[] minterms = getMintermsAsDecimal(truthTable);
        MintermTable mintermsTable = new MintermTable(minterms.length);
        
        for (int i = 0; i < minterms.length; i++)
        {
            mintermsTable.addLine
            (
                new int[] { minterms[i] },
                decimalToBinary(minterms[i], numberOfVariables)
            );
        }
        
        return mintermsTable;
    }
    
    /**
     * Percorre os dois numeros binarios contando quantos bits sao diferentes.
     * 
     * @param binary1 primeiro numero binario
     * @param binary2 segundo numero binario
     * 
     * @return Quantos bits diferentes os numeros tem.
     */
    
    public static int getHammingDistance(char[] binary1, char[] binary2)
    {
        int hammingDistance = 0;
        
        for (int i = 0; i < binary1.length; i++)
        {
            hammingDistance += ( binary1[i] != binary2[i] ? 1 : 0 );
        }
        
        return hammingDistance;
    }
    
    /**
     * Percorre dois numeros binarios verificando se os bits sao diferentes ou nao.
     * Quando um bit diferente for encontrado ele e' substituido por um
     * '_' (underline). Para todos os outros bits a funcao apenas vai copiando-os
     * para o novo numero binario.
     * 
     * <p>Ex: removeBitOfHammingDistance1({'0', '0'}, {'0', '1'}) = {'0', '_'}</p>
     * 
     * @param binary1 primeiro numero binario
     * @param binary2 segundo numero binario
     * 
     * @return Novo numero binario em que o '_' (underline) aparece onde ha'
     * diferenca entre os bits do primeiro e do segundo numero binario.
     */
    
    public static char[] removeBitOfHammingDistance1(char[] binary1, char[] binary2)
    {
        char[] newBinary = new char[binary1.length];
        
        for (int i = 0; i < binary1.length; i++)
        {
            if (binary1[i] == binary2[i]) newBinary[i] = binary1[i];
            
            else newBinary[i] = '_';
        }
        
        return newBinary;
    }
    
    /**
     * Percorre o arranjo procurando o seu maior elemento.
     * 
     * @param array arranjo a ser pesquisado
     * 
     * @return Maior elemento do arranjo.
     */
    
    public static int getGreatest(int[] array)
    {
        int greatest = 0;
        
        if (array != null && array.length > 0)
        {
            int current;
            
            greatest = array[0];
            
            for (int i = 1; i < array.length; i++)
            {
                current = array[i];
                
                if (current > greatest)
                {
                    greatest = current;
                }
            }
        }
        
        return greatest;
    }
    
    /**
     * Percorre o arranjo concatenando os seus elementos.
     * 
     * @param array arranjo a ser percorrido.
     * 
     * @return String com todos os elementos do arranjo concatenados.
     */
    
    public static String toString(char[] array)
    {
        String str = "";
        
        for (int i = 0; i < array.length; i++)
        {
            str += array[i];
        }
        
        return str;
    }
    
    /**
     * Cria um novo arranjo com todos os elementos do primeiro e, logo apos, todos
     * os elementos do segundo.
     * 
     * @param array1 primeiro arranjo
     * @param array2 segundo arranjo
     * 
     * @return Um novo arranjo com todos os elementos do primeiro e, logo apos,
     * todos os elementos do segundo.
     */
    
    public static char[] concatArrays(char[] array1, char[] array2)
    {
        char[] newArray = new char[array1.length + array2.length];
        int counterOfTheNewArray = 0;

        for (int i = 0; i < array1.length; i++)
        {
            newArray[counterOfTheNewArray++] = array1[i];
        }

        for (int i = 0; i < array2.length; i++)
        {
            newArray[counterOfTheNewArray++] = array2[i];
        }

        return newArray;
    }
    
    /**
     * Percorre um arranjo de caracteres contando quantos vezes determinado
     * caractere aparece.
     * 
     * @param c caractere a ser procurado
     * @param binary1 arranjo a ser percorrido
     * 
     * @return Quantas vezes o caractere da variavel c aparece no arranjo.
     */
    
    public static int countChars(char c, char[] binary1)
    {
        int count = 0;
        
        for (int i = 0; i < binary1.length; i++)
        {
            count += ( binary1[i] == c ? 1 : 0 );
        }
        
        return count;
    }
    
    /**
     * Percorre o arranjo procurando um elemento.
     * 
     * @param value elemento a ser procurado
     * @param array arranjo a ser percorrido
     * 
     * @return Indice do elemento no arranjo. Caso nao seja encontrado, o retorno
     * e' -1.
     */
    
    public static int indexOf(int value, int[] array)
    {
        int index = -1;
        
        for (int i = 0; index == -1 && i < array.length; i++)
        {
            if (array[i] == value)
            {
                index = i;
            }
        }
        
        return index;
    }
    
    /**
     * Percorre o arranjo fonte procurando cada um de seus elementos no arranjo
     * de pesquisa.
     * 
     * @param sourceArray arranjo fonte
     * @param arrayToSearch arranjo de pesquisa
     * 
     * @return Os indices, no arranjo fonte e de pesquisa respectivamente, do 
     * primeiro elemento que for encontrado nos dois arranjos. O arranjo retornado
     * tem exatamente dois espacos.
     */
    
    public static int[] indexOf(int[] sourceArray, int[] arrayToSearch)
    {
        int[] indexes = new int[2];
        indexes[0] = indexes[1] = -1;
        int searchIndex;
        
        for (int i = 0; indexes[0] == -1 && i < sourceArray.length; i++)
        {
            searchIndex = indexOf(sourceArray[i], arrayToSearch);
            
            if (searchIndex != -1)
            {
                indexes[0] = i;
                indexes[1] = searchIndex;
            }
        }
        
        return indexes;
    }
    
    /**
     * Percorre o arranjo procurando um elemento.
     * 
     * @param value elemento a ser procurado
     * @param array arranjo a ser percorrido
     * 
     * @return Indice do elemento no arranjo. Caso nao seja encontrado, o retorno
     * e' -1.
     */
    
    public static int indexOf(char value, char[] array)
    {
        int index = -1;
        
        for (int i = 0; index == -1 && i < array.length; i++)
        {
            if (array[i] == value)
            {
                index = i;
            }
        }
        
        return index;
    }
    
    /**
     * Percorre a matriz procurando um arranjo.
     * 
     * @param array arranjo a ser procurado
     * @param matrix matriz ou arranjo de arranjos a ser percorrido
     * 
     * @return Indice do arranjo na matriz. Em outras palavras, indice da linha
     * da matriz em que o arranjo esta'.
     */
    
    public static int indexOf(char[] array, char[][] matrix)
    {
        int index = -1;
        
        for (int i = 0; index == -1 && i < matrix.length; i++)
        {
            if (matrix[i] != null && Arrays.equals(matrix[i], array))
            {
                index = i;
            }
        }
        
        return index;
    }
    
    /**
     * Comeca a gerar o fatorial do numero porem expande apenas
     * {@code numberOfFactors} fatores.
     * 
     * <p>Ex: factorialOf(5, 2) = 5 * 4</p>
     * 
     * @param number numero a se obter o fatorial
     * @param numberOfFactors quantos fatores devem ser gerados
     * 
     * @return Fatorial de {@code number} com {@code numberOfFactors} fatores.
     */
    
    public static int factorialOf(int number, int numberOfFactors)
    {
        int factorial = 1;
        
        for (int i = 0; i < numberOfFactors; i++)
        {
            factorial *= number--;
        }
        
        return factorial;
    }
    
    /**
     * Descobre quantos grupos e' possivel formar com {@code groupsSize}
     * elementos tendo {@code numberOfElements} elementos disponiveis.
     * 
     * <p>E' importante ressaltar que a formacao dos grupos nao diferencia grupos
     * com elementos iguais em ordem diferente.</p>
     * 
     * @param numberOfElements numero de elementos disponiveis
     * @param groupsSize tamanho dos grupos a serem formados
     * 
     * @return Quantos grupos e' possivel formar com {@code groupsSize}
     * elementos tendo {@code numberOfElements} elementos disponiveis.
     */
    
    public static int combinationOf(int numberOfElements, int groupsSize)
    {
        int factorialExpansion = Math.min(numberOfElements - groupsSize, groupsSize);
        
        return factorialOf(numberOfElements, factorialExpansion) / factorialOf(factorialExpansion, factorialExpansion);
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
                currentGroupSize = combinationOf(numberOfVariables, i + 1);
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

                if (indexOf(mintermAsBinary, usedMinterms) == -1)
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

                    if (getHammingDistance(tableLine1.mintermAsBinary, tableLine2.mintermAsBinary) == 1)
                    {
                        // adiciona os valores de i e j no arranjo de mintermos usados
                        if (indexOf(i, usedMinterms) == -1 && usedMintermsCounter < usedMinterms.length)
                        {
                            usedMinterms[ usedMintermsCounter++ ] = i;
                        }

                        if (indexOf(j, usedMinterms) == -1 && usedMintermsCounter < usedMinterms.length)
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
                                removeBitOfHammingDistance1(tableLine1.mintermAsBinary, tableLine2.mintermAsBinary)
                        );
                    }
                }

                if (indexOf(i, usedMinterms) == -1)
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
                        
                        if (indexOf(minterm, tableMinterms) == -1)
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
     * Recebe uma tabela de mintermos nao simplificavel e gera um mapa de
     * cobertura com as simplificacoes finais dessa tabela, onde cada
     * simplificacao sera' um primo implicante no mapa.
     * 
     * @param mintermTable tabela de mintermos nao simplificavel
     * 
     * @return Um mapa de cobertura com as simplificacoes finais da tabela.
     */
    
    public static CoverageMap getCoverageMap(MintermTable mintermTable)
    {
        CoverageMap coverageMap = null;
        
        if (mintermTable != null)
        {
            int numberOfLines = mintermTable.numberOfLines;
            
            if (numberOfLines > 0)
            {
                int[] mintermsAsDecimal = getAllMintermsInCrescentOrder(mintermTable);
                char[][] mintermsMap = new char[numberOfLines][mintermsAsDecimal.length];
                int[] mintermsOfCurrentLine;
                
                for (int i = 0; i < numberOfLines; i++)
                {
                    Arrays.fill(mintermsMap[i], ' ');
                    
                    mintermsOfCurrentLine = mintermTable.table[i].mintermsAsDecimal;
                    
                    for (int minterm : mintermsOfCurrentLine)
                    {
                        mintermsMap[i][Arrays.binarySearch(mintermsAsDecimal, minterm)] = 'x';
                    }
                }
                
                coverageMap = new CoverageMap(mintermsAsDecimal, mintermTable.getAllMintermsAsBinary(), mintermsMap);
            }
        }
        
        return coverageMap;
    }
    
    public static void main(String[] args)
    {
        //11001101000001011100110100110011 = SoP(0,1,4,5,7,13,15,16,17,20,21,23,26,27,30,31)
        //10011110001101011101011000111101 = SoP(0,3,4,5,6,10,11,13,15,16,17,19,21,22,26,27,28,29,31)
        //00010111001101000100000001100011011110110011111110111110101100111000011111110100010101111010000010110011101000110010001011101110
        //= SoP(3,5,6,7,10,11,13,17,25,26,30,31,33,34,35,36,38,39,42,43,44,45,46,47,48,50,51,52,53,54,56,58,59,62,63,64,69,70,71,72,73,74,75,77,81,83,85,86,87,88,90,96,98,99,102,103,104,106,110,111,114,118,120,121,122,124,125,126)                                                                                                                      
        
        // Le a tabela verdade
        String truthTable = readLine("\nEntre com a tabela verdade: ");
        // Le os nomes das variaveis do circuito
        String variablesNames = readLine("Entre com os nomes de cada variavel (ex: \"a b c d\"): ");
        String[] namesOfVariables = variablesNames.split(" ");
        
        // Extrai os mintermos e cria uma tabela para eles
        MintermTable mintermsTable = getMintermsTable(truthTable);
        int groupNumber = 1;
        
        println("\nMapa de Karnaugh:\n");
        KarnaughMap karnaughMap = new KarnaughMap(mintermsTable, namesOfVariables);
        karnaughMap.printMap();
        
        println("\nMintermos:");
        mintermsTable.printTable(); // Imprime a tabela
        
        // Agrupa os mintermos com distancia hamming de 1
        mintermsTable = groupMinterms(mintermsTable);
        
        if (mintermsTable != null)
        {
            // Repete enquanto for possivel agrupar mais
            while (mintermsTable.isPossibleToSimplify)
            {
                println("\n" + groupNumber++ + "º grupo:");
                mintermsTable.printTable(); // Imprime a tabela

                // Agrupa os mintermos com distancia hamming de 1
                mintermsTable = groupMinterms(mintermsTable);
            }

            // Finalizado o agrupamento, obtem o mapa de cobertura com todos os
            // mintermos usados nas simplificacoes
            CoverageMap coverageMap = getCoverageMap(mintermsTable);

            if (coverageMap != null)
            {
                println("\nMapa de cobertura:\n");
                coverageMap.printMap(); // Imprime o mapa

                // Encontra os primos implicantes essencias e os guarda num arranjo da classe
                coverageMap.findEssentialImplicantPrimes();

                do
                {
                    coverageMap.proceed(); // Faz a simplificacao do mapa passo a passo
                    println("");
                    coverageMap.printMap(); // Imprime o mapa

                    // Repete enquanto for possivel simplificar mais
                } while (coverageMap.isPossibleToSimplify);
                
                println("\nEstatisticas:\n");
                coverageMap.printStatistics();
                
                println("\nExpressao final:\n");
                coverageMap.printExpression(namesOfVariables);
            }
        }
    }
    
}
