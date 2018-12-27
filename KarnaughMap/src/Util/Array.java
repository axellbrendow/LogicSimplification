package Util;

import java.util.Arrays;

/**
 * @author Axell Brendow ( https://github.com/axell-brendow )
 */

public class Array
{
    /**
     * Gera uma matriz que e' resultado do corte, a partir da linha 0, da matriz
     * {@code matrix}, incluindo {@code numberOfLines} linhas para baixo.
     * 
     * @param matrix matrix a ser ajustada
     * @param numberOfLines numero de linhas a serem pegas
     * 
     * @return Uma matriz que e' resultado do corte, a partir da linha 0, da
     * matriz {@code matrix}, incluindo {@code numberOfLines} linhas para baixo.
     */
    
    public static int[][] fit(int[][] matrix, int numberOfLines)
    {
        int[][] smallerMatrix = new int[numberOfLines][matrix[0].length];
        
        System.arraycopy(matrix, 0, smallerMatrix, 0, numberOfLines);
        
        return smallerMatrix;
    }
    
    /**
     * Gera um arranjo que e' resultado do corte, a partir da posicao 0, do
     * arranjo {@code array}, incluindo {@code numberOfElements} elementos para
     * frente.
     * 
     * @param array arranjo a ser ajustado
     * @param numberOfElements numero de elementos a serem pegos
     * 
     * @return Um arranjo que e' resultado do corte, a partir da posicao 0, do
     * arranjo {@code array}, incluindo {@code numberOfElements} elementos para
     * frente.
     */
    
    public static int[] fit(int[] array, int numberOfElements)
    {
        int[] smallerArray = new int[numberOfElements];
        
        System.arraycopy(array, 0, smallerArray, 0, numberOfElements);
        
        return smallerArray;
    }
    
    /**
     * Usando a convensao de preencher os arranjos de inteiros com -1 antes de
     * usa'-los, esse metodo gera um arranjo que contem apenas os elementos de
     * fato inseridos em {@code array}. Remove os valores -1.
     * 
     * @param array arranjo a ser ajustado
     * 
     * @return Um arranjo que contem apenas os elementos de fato inseridos em
     * {@code array}.
     */
    
    public static int[] fit(int[] array)
    {
        return fit(array, getNumberOfElementsOf(array));
    }
    
    /**
     * Adiciona o valor {@code value} ao arranjo {@code array}. Porem, caso
     * {@code value} ja' estiver em {@code array} ou em {@code valuesToIgnore},
     * ele nao e' adicionado.
     * 
     * @param array arranjo a ser preenchido
     * @param value valor a ser colocado
     * @param valuesToIgnore valores a serem ignorados
     */
    
    public static void addValueIfItDoesntExistInArray(int[] array, int value, int[] valuesToIgnore)
    {
        int numberOfElements = Array.getNumberOfElementsOf(array);
        
        if (numberOfElements < array.length &&
                !Array.contains(value, valuesToIgnore) &&
                !Array.contains(value, array))
        {
            array[numberOfElements] = value;
        }
    }
    
    /**
     * Percorre o arranjo {@code values} adicionando seus valores ao arranjo
     * {@code array}. Porem, valores que ja' estiverem em {@code array} ou que
     * estiverem em {@code valuesToIgnore} nao sao adicionados.
     * 
     * @param array arranjo a ser preenchido
     * @param values valores a serem colocados
     * @param valuesToIgnore valores a serem ignorados
     */
    
    public static void addEachValueIfItDoesntExistInArray(int[] array, int[] values, int[] valuesToIgnore)
    {
        for (int i = 0; i < values.length; i++)
        {
            addValueIfItDoesntExistInArray(array, values[i], valuesToIgnore);
        }
    }
    
    /**
     * Usando a convensao de preencher os arranjos de objetos com null antes de
     * usa'-los, esse metodo serve para saber quantos objetos foram colocados
     * no arranjo a partir da posicao 0 e em sequencia.
     * 
     * @param array arranjo a ser percorrido
     * 
     * @return Quantos objetos foram colocados no arranjo a partir da posicao
     * 0 e em sequencia.
     */
    
    public static int getNumberOfElementsOf(Object[] array)
    {
        int numberOfElements = indexOf(null, array);
        
        return ( numberOfElements != -1 ? numberOfElements : array.length );
    }
    
    /**
     * Usando a convensao de preencher os arranjos de inteiros com -1 antes de
     * usa'-los, esse metodo serve para saber quantos inteiros foram colocados
     * no arranjo a partir da posicao 0 e em sequencia.
     * 
     * @param array arranjo a ser percorrido
     * 
     * @return Quantos inteiros foram colocados no arranjo a partir da posicao
     * 0 e em sequencia.
     */
    
    public static int getNumberOfElementsOf(int[] array)
    {
        int numberOfElements = indexOf(-1, array);
        
        return ( numberOfElements != -1 ? numberOfElements : array.length );
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
    
    public static int[] concatArrays(int[] array1, int[] array2)
    {
        int[] newArray = new int[array1.length + array2.length];
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
    
    public static int indexOf(Object value, Object[] array)
    {
        int index = -1;
        
        for (int i = 0; index == -1 && i < array.length; i++)
        {
            if (array[i].equals(value))
            {
                index = i;
            }
        }
        
        return index;
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
    
    public static boolean contains(Object value, Object[] array)
    {
        return indexOf(value, array) != -1;
    }
    
    public static boolean contains(char value, char[] array)
    {
        return indexOf(value, array) != -1;
    }
    
    public static boolean contains(char[] array, char[][] matrix)
    {
        return indexOf(array, matrix) != -1;
    }
    
    public static boolean contains(int value, int[] array)
    {
        return indexOf(value, array) != -1;
    }
    
    public static boolean containsAnyOf(int[] sourceArray, int[] arrayToSearch)
    {
        return indexOf(sourceArray, arrayToSearch)[0] != -1;
    }
}
