package br.com.mk;

public class TicTacToe {

	public static void main(String[] args) {
		int[][] board = new int[3][3]; // 0 for empty, 1 for X, 2 for O
		// Initialize board
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = 0; // Set all positions to empty
			}
		}
		// Simulate a game state (e.g., player X wins in first row)
		board[0][0] = 1;
		board[0][1] = 1;
		board[0][2] = 1;
		// Check rows for a winner
		for (int i = 0; i < 3; i++) {
			if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
				System.out.println("Player " + board[i][0] + " wins!");
			}
		}
	}
}
