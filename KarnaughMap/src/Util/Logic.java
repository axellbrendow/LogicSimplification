package Util;

/**
 * @author Axell Brendow (https://github.com/axell-brendow)
 */

public class Logic
{
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
