import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Mancala 
{
	int MARBLE_SIZE = 6;
	
	int[] player1Marbles = new int [MARBLE_SIZE];
	int[] player2Marbles = new int [MARBLE_SIZE];
	int player1Mancala;
	int player2Mancala;
	int player;
	int oppPlayer;
	
	public static void main(String[] args) {
		Mancala solution = new Mancala();
		solution.readInput();
		solution.move();
	}
	
	public void readInput() {
		Scanner scanner = new Scanner(System.in);
        
		player = scanner.nextInt();
		oppPlayer = (player == 1) ? 2 : 1;
		
		player1Mancala = scanner.nextInt();
        for (int i = 0; i < MARBLE_SIZE; i++) player1Marbles[i] = scanner.nextInt();
        
        player2Mancala = scanner.nextInt();
        for(int i = 0; i < MARBLE_SIZE; i++) player2Marbles[i] = scanner.nextInt();
        
        scanner.close();
	}
	
	public void move() {
		int[] result = minimax(13, player, Integer.MIN_VALUE, Integer.MAX_VALUE);
		System.out.println(result[1]);
    }
	
	public int[] minimax(int depth, int playerIndex, int alpha, int beta) {
		List<MancalaMove> nextMoves = generateMoves(playerIndex);
		
		// Sort moves in descending order of their scores
		Collections.sort(nextMoves, Collections.reverseOrder());

		int score;
		int bestIndex = -1;

		// Evaluate the game state when the maximum depth is reached or no more moves are available
		if (depth == 0 || nextMoves.isEmpty()) {
			score = evaluate();
			return new int[] {score, bestIndex};
		} else {
			for (MancalaMove move : nextMoves) {
				int lastDump = 0;

				int initialIndex = move.index;

				// Make a copy of the current game state
				int[] initialPlayer1Config = player1Marbles.clone();
				int initialPlayer1Mancala = player1Mancala;
				int[] initialPlayer2Config = player2Marbles.clone();
				int initialPlayer2Mancala = player2Mancala;

				if (playerIndex == 1) {
					int marbleCount = player1Marbles[move.index];

					player1Marbles[move.index++] = 0;

					for (int i = 0; i < marbleCount; i++) {
						if (move.index < 6) {
							player1Marbles[move.index++] += 1;
						} else if (move.index == 6) {
							player1Mancala += 1;
							move.index++;
						} else {
							int tempIndex = move.index - 7;
							player2Marbles[tempIndex] += 1;
							move.index++;
						}

						if (move.index == 13) {
							move.index = 0;
						}
					}

					lastDump = move.index - 1;

					if (lastDump >= 0 && lastDump < 6 && player1Marbles[move.index - 1] == 1) {
						player1Mancala += player2Marbles[5 - lastDump] + 1;
						player2Marbles[5 - lastDump] = 0;
						player1Marbles[lastDump] = 0;
					}
				} else {
					int marbleCount = player2Marbles[move.index];

					player2Marbles[move.index++] = 0;

					for (int i = 0; i < marbleCount; i++) {
						if (move.index < 6) {
							player2Marbles[move.index++] += 1;
						} else if (move.index == 6) {
							player2Mancala += 1;
							move.index += 1;
						} else {
							int tempIndex = move.index - 7;
							player1Marbles[tempIndex] += 1;
							move.index++;
						}

						if (move.index == 13) {
							move.index = 0;
						}
					}

					lastDump = move.index - 1;

					if (lastDump >= 0 && lastDump < 6 && player2Marbles[lastDump] == 1) {
						player2Mancala += player1Marbles[5 - lastDump] + 1;
						player1Marbles[5 - lastDump] = 0;
						player2Marbles[lastDump] = 0;
					}
				}
				
				// Determine the best move for the current player using the minimax algorithm with alpha-beta pruning
				if (playerIndex == player) {
					// If it's the player's turn, use their player number to determine the next move
					if (lastDump == 6) {
						// If the last dump was a 6, it's the player's turn again, so use the player number to determine the score
						score = minimax(depth -1, player, alpha, beta)[0];
					} else {
						// Otherwise, it's the opponent's turn, so use the opponent player number to determine the score
						score = minimax(depth -1, oppPlayer, alpha, beta)[0];
					}

					// Update alpha and the best index if the score is better than alpha
					if (score > alpha) {
						alpha = score;
						bestIndex = initialIndex + 1;
					}
				} else {
					// If it's the opponent's turn, use their player number to determine the next move
					if (lastDump == 6) {
						// If the last dump was a 6, it's the opponent's turn again, so use the opponent player number to determine the score
						score = minimax(depth -1, oppPlayer, alpha, beta)[0];
					} else {
						// Otherwise, it's the player's turn, so use the player number to determine the score
						score = minimax(depth -1, player, alpha, beta)[0];
					}

					// Update beta and the best index if the score is worse than beta
					if (score < beta) {
						beta = score;
						bestIndex = initialIndex + 1;
					}
				}

				// Reset the game state to its initial state
				player1Mancala = initialPlayer1Mancala;
				player2Mancala = initialPlayer2Mancala;
				player1Marbles = initialPlayer1Config;
				player2Marbles = initialPlayer2Config;

				// If alpha is greater than or equal to beta, stop searching for the best move
				if (alpha >= beta) {
					break;
				}
			}
		}
		
		return new int[] {(playerIndex == player) ? alpha : beta, bestIndex};
	}
	
	public List<MancalaMove> generateMoves(int playerIndex) {
		int[] marbles = playerIndex == 1 ? player1Marbles : player2Marbles; // accessing player's marbles array based on player index
		
		List<MancalaMove> moves = new LinkedList<MancalaMove>(); // using ArrayList instead of LinkedList for efficiency

		if ((playerIndex == 1 && isGameOver(2)) || (playerIndex == 2 && isGameOver(1))) { // checking if the game is over for either player
			return moves;
		}

		for (int i = 0; i < marbles.length; i++) { // looping through each pit
			if (marbles[i] > 0) { // checking if the pit contains marbles
				moves.add(new MancalaMove(i, marbles[i], playerIndex)); // adding the move to the list of moves
			}
		}

		return moves;
	}
	
	public boolean isGameOver(int playerIndex) {
		// set initial value of gameOver to false
		boolean gameOver = false;
		
		// Get the total count of marbles for each player
		int countMarblesPlayer1 = Arrays.stream(player1Marbles).sum();
		int countMarblesPlayer2 = Arrays.stream(player2Marbles).sum();

		// Check if the game is over for the given player index
		if (playerIndex == 1 && countMarblesPlayer2 == 0 ||
				playerIndex == 2 && countMarblesPlayer1 == 0) {
			gameOver = true;
		}

		return gameOver;
	}
	
	public int evaluate() {
		int score = 0;
		int[] countMarbles = {0, 0}; // player1's marbles count at index 0, player2's at index 1

		for (int i = 0 ; i < 6 ; i++) {
			countMarbles[0] += player1Marbles[i];
			countMarbles[1] += player2Marbles[i];
		}

		int player1Score = player1Mancala + countMarbles[0];
		int player2Score = player2Mancala + countMarbles[1];

		score = player == 1 ? player1Score - player2Score : player2Score - player1Score;

		return score;
	}
	
	class MancalaMove implements Comparable<MancalaMove> {
		
		int index;
		int marbles;
		int priority;
		
		public MancalaMove(int index, int marbles, int playerIndex) {
			this.marbles = marbles;
			this.index = index;
			int lastDump = -1;
			int[] initialPlayer1Config = player1Marbles.clone();
			int[] initialPlayer2Config = player2Marbles.clone();

			if (6 - index == marbles) {
				priority = 100;
			}
						
			if (playerIndex == 1) {
				int marbleCount = initialPlayer1Config[index];
				initialPlayer1Config[index++] = 0;
				
				while (marbleCount > 0) {
					if (index < 6) {
						initialPlayer1Config[index++] += 1;
					} else if (index == 6) {
						index++;
					} else {
						int tempIndex = index - 7;
						initialPlayer2Config[tempIndex] += 1;
						index++;
					}
					
					if (index == 13) {
						index = 0;
					}
					
					marbleCount--;
				}
				
				lastDump = index - 1;
				
				if (lastDump >= 0 && lastDump < 6 && initialPlayer1Config[lastDump] == 1 && initialPlayer2Config[5 - lastDump] > 0) {
					priority += 200;
				}
			} else {
				int marbleCount = initialPlayer2Config[index];
				initialPlayer2Config[index++] = 0;
				
				while (marbleCount > 0) {
					if (index < 6) {
						initialPlayer2Config[index++] += 1;
					} else if (index == 6) {
						index += 1;
					} else {
						int tempIndex = index - 7;
						initialPlayer1Config[tempIndex] += 1;
						index++;
					}
					
					if (index == 13) {
						index = 0;
					}
					
					marbleCount--;
				}
				
				lastDump = index - 1;
				
				if (lastDump >= 0 && lastDump < 6 && initialPlayer2Config[lastDump] == 1 && initialPlayer1Config[5 - lastDump] > 0) {
					priority += 200;
				}
			}
		}
		
		
		@Override
		public int compareTo(MancalaMove o) {
			return Integer.compare(o.index, this.index);
		}
	}
}