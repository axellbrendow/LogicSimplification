package Structures;

import Util.Array;
import Util.IO;
import java.util.Arrays;

/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
 */

public class TableLine
{
    int[] mintermsAsDecimal;
    char[] mintermAsBinary;
    // guardara' as referencias para qual dos mintermos que fazem distancia
    // hamming com o mintermo base do grupo foram usados
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