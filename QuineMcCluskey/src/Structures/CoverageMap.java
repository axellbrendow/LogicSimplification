package Structures;

import java.util.Arrays;
import Util.*;

/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
 */

public class CoverageMap
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
    
    public boolean isPossibleToSimplify()
    {
        return isPossibleToSimplify;
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
                int[] mintermsAsDecimal = MintermTable.getAllMintermsInCrescentOrder(mintermTable);
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
    
    public int getNumberOfLines()
    {
        return ( mintermsMap != null ? mintermsMap.length : 0 );
    }
    
    public int getNumberOfColumns()
    {
        return ( mintermsMap != null && mintermsMap[0] != null ? mintermsMap[0].length : 0 );
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

            if (numberOfImplicantPrimes == 1 && Array.indexOf(lineOfImplicantPrime, linesOfEssentialImplicantPrimes) == -1)
            {
                linesOfEssentialImplicantPrimes[counterOfLinesOfEssentialImplicantPrimes++] = lineOfImplicantPrime;
            }
        }

        for (int i = 0; i < numberOfLines; i++)
        {
            if (Array.indexOf(i, linesOfEssentialImplicantPrimes) == -1)
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
        int numberOfColumns = getNumberOfColumns();
        MintermTable tableOfLinesOfPrimeImplicantsForEachMinterm = new MintermTable(numberOfColumns);
        int[] linesOfPrimeImplicantsForCurrentMinterm = new int[mintermsMap.length];
        int linesOfPrimeImplicantsForCurrentMintermCounter;
        int[] definitiveLinesOfPrimeImplicantsForCurrentMinterm;
        char c;

        // Percorre as colunas. Cada coluna representa um mintermo.
        for (int j = 0; j < numberOfColumns; j++)
        {
            linesOfPrimeImplicantsForCurrentMintermCounter = 0;

            // Percorre as linhas. Juntamente com o for acima, forma um movimento vertical
            for (int i = 0; i < mintermsMap.length && ( c = mintermsMap[i][j] ) != '|'; i++)
            {
                if (c == 'x') // Checa se existe um x na coluna do mintermo
                {
                    linesOfPrimeImplicantsForCurrentMinterm[linesOfPrimeImplicantsForCurrentMintermCounter++] = i;
                }
            }

            if (linesOfPrimeImplicantsForCurrentMintermCounter > 0)
            {
                // Cria o arranjo definitivo que terá os índices das linhas dos
                // primos implicantes essenciais para o mintermo na coluna atual
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
            if (Array.indexOf(linesOfPrimeImplicantsToIgnore, linesOfPrimeImplicantsForCurrentMinterm)[0] == -1)
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

                int sizeOfSmallestSetOfPrimeImplicants = Array.indexOf(-1, smallestSetOfPrimeImplicants);
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

            int numberOfPrimeImplicants = Array.indexOf(-1, smallestChoice);
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
            found = ( Array.indexOf('x', mintermsMap[i]) != -1 );
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
            statistics[ Array.countChars('_', mintermsAsBinary[lineOfImplicantPrime]) ]++;

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
    
    private int getMintermUsage(int column)
    {
        int usage = 0;
        int numberOfLines = getNumberOfLines();
        
        for (int i = 0; i < numberOfLines; i++)
        {
            if (mintermsMap[i][column] == 'x')
            {
                usage++;
            }
        }
        
        return usage;
    }
    
    private void printHeader(int lengthOfFirstColumn, int lengthOfGreatestMinterm)
    {
        String line = Strings.createClonesAndConcatThem(" ", lengthOfFirstColumn);

        for (int minterm : mintermsAsDecimal)
        {
            line += " " + Strings.centerStrOnABlock("" + minterm, lengthOfGreatestMinterm);
        }

        IO.println(line);
    }
    
    private void printMapLines(int lengthOfFirstColumn, int lengthOfGreatestMinterm)
    {
        String line;
        int numberOfLines = getNumberOfLines();
        
        for (int i = 0; i < numberOfLines; i++)
        {
            line = Strings.centerStrOnABlock(TableLine.getBinaryRepresentation(mintermsAsBinary[i]), lengthOfFirstColumn);

            for (char c : mintermsMap[i])
            {
                line += " " + Strings.centerStrOnABlock("" + c, lengthOfGreatestMinterm);
            }

            IO.println(line);
        }
    }
    
    private void printMintermsUsage(int lengthOfFirstColumn, int lengthOfGreatestMinterm)
    {
        String line = Strings.centerStrOnABlock("Usos:", lengthOfFirstColumn);
        
        int numberOfColumns = getNumberOfColumns();
        
        for (int i = 0; i < numberOfColumns; i++)
        {
            line += " " + Strings.centerStrOnABlock("" + getMintermUsage(i), lengthOfGreatestMinterm);
        }
        
        IO.println(line);
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
        int greatestMinterm = Array.getGreatest(mintermsAsDecimal);
        int lengthOfGreatestMinterm = ( greatestMinterm > 0 ? 1 + (int) Math.log10(greatestMinterm) : 1 );
        int numberOfVariables = mintermsAsBinary[0].length;
        int lengthOfFirstColumn =
                ( numberOfVariables >= "Usos:".length() ?
                numberOfVariables : "Usos:".length() );
        
        printHeader(lengthOfFirstColumn, lengthOfGreatestMinterm);
        printMapLines(lengthOfFirstColumn, lengthOfGreatestMinterm);
        printMintermsUsage(lengthOfFirstColumn, lengthOfGreatestMinterm);
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

                IO.println( groupCount + " grupo" + ( groupCount != 1 ? "s" : "" ) +
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

            IO.println(expression);
        }
    }
}