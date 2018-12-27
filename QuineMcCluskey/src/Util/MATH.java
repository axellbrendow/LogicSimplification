package Util;

/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
 */

public class MATH
{
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
}
