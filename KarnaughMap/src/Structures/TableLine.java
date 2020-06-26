package Structures;

import Util.Array;
import Util.IO;

/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
 * 
 * Esta classe tem dois usos principais no projeto.
 * 
 * O primeiro uso é para guardar a representação binária e decimal de um mintermo
 * nos campos internos {@code mintermAsBinary} e {@code mintermsAsDecimal}.
 * 
 * O segundo uso é para guardar as informações de um grupo de mintermos usando
 * todos os campos internos.
 */

public class TableLine
{
    /**
     * O primeiro uso desse arranjo é para guardar o número decimal de um mintermo.
     * O segundo uso é para guardar os índices de mintermos percentes a um grupo no
     * mapa de Karnaugh.
     */
    int[] mintermsAsDecimal;

    /**
     * O primeiro uso desse arranjo é para guardar a representação binária de um
     * mintermo. Ex.: o mintermo 3 num mapa de Karnaugh com 4 variáveis seria
     * representado por 1100 (invertido por estar no formato little endian).
     * 
     * O segundo uso é para guardar uma representação visual da simplificação de
     * um grupo. Por exemplo, o mintermo 00 simplifica com 01 gerando 0_.
     */
    char[] mintermAsBinary;

    /**
     * Guardará as referencias para qual dos mintermos que fazem distancia hamming
     * com o mintermo base do grupo foram usados.
     * 
     * <p></p>
     * 
     * Explicando um pouco melhor:
     * 
     * <p></p>
     * 
     * Imagine o seguinte mapa:
     * 
     * <table>
     *  <tr>
     *      <td>ab\de</td> <td>00</td> <td>01</td> <td>11</td> <td>10</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>00</td> <td>1</td> <td>1</td> <td>0</td> <td>1</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>01</td> <td>1</td> <td>0</td> <td>0</td> <td>0</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>11</td> <td>0</td> <td>0</td> <td>0</td> <td>0</td>
     *  </tr>
     * 
     *  <tr>
     *      <td>10</td> <td>1</td> <td>0</td> <td>0</td> <td>0</td>
     *  </tr>
     * </table>
     * 
     * Imagine que o mintermo 0000, ou seja, a primeira célula, é o mintermo base.
     * Nessa caso, todos os outros mintermos que marquei como '1' podem fazer uma
     * simplificação com ele. 0000 simplifica com 0001, 0010, 0100 e 1000. Quando
     * eu falo sobre "uma referência para qual dos mintermos que fazem distancia
     * hamming com o mintermo base do grupo foram usados.", estou querendo dizer,
     * qual deles, 0001, 0010, 0100 ou 1000 está sendo usado na simplificação ?
     * Uso os números de 0 a 3 para identificá-los.
     */
    int[] nthHDMinterms;
    int numberOfReflectionsByHD1;
    int numberOfReflectionsByHD2;
    
    public TableLine(int[] mintermsAsDecimal, char[] mintermAsBinary, int[] nthHDMinterms, int numberOfReflectionsByHD1, int numberOfReflectionsByHD2)
    {
        this.mintermsAsDecimal = mintermsAsDecimal;
        this.mintermAsBinary = mintermAsBinary;
        this.nthHDMinterms = nthHDMinterms;
        this.numberOfReflectionsByHD1 = numberOfReflectionsByHD1;
        this.numberOfReflectionsByHD2 = numberOfReflectionsByHD2;
    }
    
    public TableLine(int[] mintermsAsDecimal, char[] mintermAsBinary, int[] nthHDMinterms)
    {
        this(mintermsAsDecimal, mintermAsBinary, nthHDMinterms, 0, 0);
    }

    public TableLine(int[] mintermsAsDecimal, char[] mintermAsBinary)
    {
        this(mintermsAsDecimal, mintermAsBinary, new int[0]);
    }
    
    /**
     * Obtem o numero de mintermos do grupo que fazem distancia hamming com o
     * mintermo base do grupo.
     * 
     * <p>Obs.: a decisao entre distancia hamming de 1 ou de 2 fica a cargo
     * do parametro passado para o metodo groupMinterms(GroupingMode) da classe
     * KarnaughMap.</p>
     * 
     * @return Numero de mintermos do grupo que fazem distancia hamming com o
     * mintermo base do grupo.
     */
    
    public int getNumberOfNthHDMinterms()
    {
        return Array.getNumberOfElementsOf(nthHDMinterms);
    }
    
    public int getNumberOfElements()
    {
        return Array.getNumberOfElementsOf(mintermsAsDecimal);
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
        String decimals = "{ " + MintermTable.decryptDontCareIfEncrypted(mintermsAsDecimal[0]);

        for (int i = 1; i < mintermsAsDecimal.length; i++)
        {
            decimals += ", " + MintermTable.decryptDontCareIfEncrypted(mintermsAsDecimal[i]);
        }

        decimals += " }";

        return decimals;
    }

    public void printTableLine()
    {
        IO.println(toString());
    }

    @Override
    public String toString()
    {
        return getDecimalRepresentation() + " " + getBinaryRepresentation();
    }
}