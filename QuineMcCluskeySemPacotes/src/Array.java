import java.util.Arrays;

/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
 */

public class Array
{
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
}
