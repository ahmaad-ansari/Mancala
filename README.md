# Mancala
Name: Ahmaad Ansari 
Student ID: 100785574
## Introduction
This is a Python implementation of the board game Mancala with an AI player using the alpha-beta pruning algorithm.

The `Board` class represents the state of the game and provides methods for making moves and evaluating the board. The `compute_score` function calculates the score of a given board, and the `alphabeta` function implements the alpha-beta pruning algorithm to determine the best move for the AI player.

## Usage
To use this implementation, simply import the `math` module and create a `Board` object. The `Board` object can be initialized with a list representing the starting state of the board, or with `None` to read the board state from standard input.

To determine the best move for the AI player, call the `alphabeta` function with the current `Board` object, the desired depth of the search, and the initial values for alpha and beta. The function returns a tuple containing the value of the best move and the index of the best move.

An example usage is provided in the code:
```python
import math

# create a new board object with starting state read from standard input
j = Board(None)

# determine the best move for the AI player using the alphabeta function
_, k = alphabeta(j, 10, -100000, 100000, True)

# print the index of the best move
print(k)
```
This will create a new `Board` object with the starting state read from standard input, and then use the `alphabeta` function to determine the best move for the AI player at a depth of 10. The index of the best move is then printed to the console.