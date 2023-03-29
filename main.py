import math

class Board:

    def __init__(self, board):
        if board != None:
            self.board = board[:]
        else:
            self.board = [0] * 14  # initialize board as a list of 14 zeros

            player_id = int(input())  # read player id as integer (1 or 2)
            if player_id == 1:
                player2_mancala = int(input())  # read player2's mancala count as integer
                player2_holes = list(map(int, input().split()))  # read player2's holes as list of integers
                player1_mancala = int(input())  # read player1's mancala count as integer
                player1_holes = list(map(int, input().split()))  # read player1's holes as list of integers
            elif player_id == 2:
                player1_mancala = int(input())  # read player1's mancala count as integer
                player1_holes = list(map(int, input().split()))  # read player1's holes as list of integers
                player2_mancala = int(input())  # read player2's mancala count as integer
                player2_holes = list(map(int, input().split()))  # read player2's holes as list of integers

            # fill in the board array according to the given pattern
            for i in range(6):
                self.board[i] = player1_holes[i]
            self.board[6] = player1_mancala
            for i in range(6):
                self.board[7 + i] = player2_holes[i]
            self.board[13] = player2_mancala

    def move(self, i):
        againturn = False
        stones = self.board[i]
        self.board[i] = 0

        for j in range(i + 1, i + stones + 1):
            if j % 14 == 6 and i < 6:
                self.board[6] += 1
            elif j % 14 == 13 and i > 6:
                self.board[13] += 1
            else:
                self.board[j % 14] += 1

        if i < 6 and self.board[i] == 1 and self.board[12 - i] > 0:
            self.board[6] += 1 + self.board[12 - i]
            self.board[i] = 0
            self.board[12 - i] = 0
        elif i == 6:
            againturn = True
        elif i > 6 and self.board[i] == 1 and self.board[12 - i] > 0:
            self.board[13] += 1 + self.board[12 - i]
            self.board[i] = 0
            self.board[12 - i] = 0
        elif i == 13:
            againturn = True

        return againturn

    def isEnd(self):
        if all(x == 0 for x in self.board[:6]):
            self.board[13] += sum(self.board[7:13])
            self.board[7:13] = [0] * 6
            return True
        elif all(x == 0 for x in self.board[7:13]):
            self.board[6] += sum(self.board[0:6])
            self.board[0:6] = [0] * 6
            return True
        else:
            return False

    def husVal(self):
        if self.isEnd():
            return compute_score(self.board)
        else:
            return compute_score(self.board)


def compute_score(board):
    score = board[13] - board[6]
    if board[13] > board[6]:
        score += 100
    elif board[13] < board[6]:
        score -= 100
    return score

def alphabeta(board, depth, alpha, beta, is_maximizing):
    if depth == 0 or board.isEnd():
        return (board.husVal(), -1)

    if is_maximizing:
        best_value = -math.inf
        best_move = -1
        for move in [i for i in range(7, 13) if board.board[i] != 0]:
            new_board = Board(board.board[:])
            is_maximizing = new_board.move(move)
            value, _ = alphabeta(new_board, depth - 1, alpha, beta, is_maximizing)
            if value > best_value:
                best_move = move
                best_value = value
            alpha = max(alpha, best_value)
            if alpha >= beta:
                break
        return (best_value, best_move)

    else:
        best_value = math.inf
        best_move = -1
        for move in [i for i in range(6) if board.board[i] != 0]:
            new_board = Board(board.board[:])
            is_maximizing = not new_board.move(move)
            value, _ = alphabeta(new_board, depth - 1, alpha, beta, is_maximizing)
            if value < best_value:
                best_move = move
                best_value = value
            beta = min(beta, best_value)
            if alpha >= beta:
                break
        return (best_value, best_move)

j = Board(None)
_, k = alphabeta(j, 10, -100000, 100000, True)
print(k)
