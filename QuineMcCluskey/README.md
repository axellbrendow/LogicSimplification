### Observações sobre ambiente

Este projeto foi feito usando a IDE Eclipse com Java 8+, basta abrir a pasta QuineMcCluskey como projeto na IDE e tudo estará OK.
Caso queira usar o VSCode, é possível também, basta abrir a pasta QuineMcCluskey nele. É importante que seja ela e não a pasta do repositório inteiro (LogicSimplification).

## Como executar o projeto ?

Fique a vontade para modificar o código e fazer os seus testes a partir do arquivo `src/QuineMcCluskey/QuineMcCluskey.java`. Adicionei scripts nesta pasta que te ajudarão a compilar, executar e construir o projeto. São eles `compile.sh`, `run.sh` e `build.sh`.
Caso esteja no Windows, a sintaxe desses scripts é perfeitamente compatível com o PowerShell, então basta usá-lo para executar os arquivos .sh. Lembre-se de ativar a execução de scripts com o comando `Set-ExecutionPolicy Bypass Process`.

### Entrada esperada pelo programa

Considerando uma tabela verdade onde a e b são variáveis e s é a saída:

| a   | b   | s   |
| --- | --- | --- |
| 0   | 0   | 0   |
| 0   | 1   | 1   |
| 1   | 0   | 1   |
| 1   | 1   | 0   |

A entrada que o programa espera como tabela verdade será 0110 que é a coluna s.
