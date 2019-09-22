

/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
 */

public class Strings
{
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
}
