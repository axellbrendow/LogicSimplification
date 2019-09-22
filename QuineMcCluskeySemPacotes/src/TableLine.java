/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
 */

public class TableLine
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
        IO.println(toString());
    }

    @Override
    public String toString()
    {
        return getDecimalRepresentation() + " " + getBinaryRepresentation();
    }
}