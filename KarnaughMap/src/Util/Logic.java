package Util;

/**
 * @author Axell Brendow (https://github.com/axell-brendow)
 */

public class Logic
{
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

    public static char[][] getGraySequence(int numberOfBits)
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
}
