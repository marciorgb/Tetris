# Tetris

Esse projeto foi feito baseado no
[Tetris](https://gist.github.com/DataWraith/5236083).

Consiste em um tetris simples de fácil jogabilidade.

# Comandos básicos

- <kbd>&uarr;</kbd> rotaciona a peça.
- <kbd>&darr;</kbd> desloca a peça mais rapidamente.
- <kbd>&larr;</kbd> e <kbd>&rarr;</kbd> deslocam a peça para os lados.

# Dificuldade

A dificuldade não pode ser controlada. Ela é incrementada baseada no score
atual do jogador. 

A dificuldade apenas aumenta a velodcidade em que a peça cai.

# Score

O score é baseado nos seguintes critérios:

- Deslocamento forçado da peça para baixo, a cada unidade são acrescidos 1
  ponto ao score.
- Quantidade de linhas destruídas de uma só vez, seguindo 100 para uma linha
  200 para duas 300 para tres e 400 para 4.

# Game Over

O Game Over se dá por uma das seguintes condições:

- A peça atingir a origem, ou seja, ser impossível adicionar uma nova peça.
- O score atingir 1000 pontos.

# Pause e reset

O pause e o reset ainda não foram implementados, meus conhecimentos em
`java`não me permitiram ainda descobrir o não funcionamento.
