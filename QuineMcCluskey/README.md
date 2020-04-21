### Observações sobre ambiente

Este projeto foi feito usando a IDE Eclipse com Java 8+, basta abrir a pasta QuineMcCluskey como projeto na IDE e tudo estará OK.
Caso queira usar o VSCode, é possível também, basta abrir a pasta QuineMcCluskey nele. É importante que seja ela e não a pasta do repositório inteiro (LogicSimplification).

### Como usar ?

```
git clone https://github.com/axell-brendow/LogicSimplification
pushd LogicSimplification
pushd QuineMcCluskey
java -jar QuineMcCluskey.jar
```

### Entrada esperada pelo programa

Considerando uma tabela verdade onde a e b são variáveis e s é a saída:

|a|b|s|
|-|-|-|
|0|0|0|
|0|1|1|
|1|0|1|
|1|1|0|

A entrada que o programa espera como tabela verdade será 0110 que é a coluna s.

### Compilando e executando

```
git clone https://github.com/axell-brendow/LogicSimplification
pushd LogicSimplification
pushd KarnaughMap
pushd src
javac KarnaughMap/Program.java
java KarnaughMap.Program
```
