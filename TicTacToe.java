package tttmain;

import java.util.*;

public class TicTacToe {

	/*
	 * naming style of variables in 'snake case' -> lower case and concatenated with
	 * '_'
	 * example: is_player_move
	 */

	/// class members constants
	private final int board_size_x = 3;
	private final int board_size_y = 3;
	private final char player_mark = 'X';
	private final char npc_mark = 'O';
	private final char neutral_mark = '_';

	/// class members
	private boolean is_player_move = false;
	private char[][] board = new char[board_size_x][board_size_y];

	private void start() {

		while (true) {
			String answer = askQuestion("Do you want to play game? (Yes/No)");

			if (isAnswerInvalid(answer)) {
				continue;
			}

			if (!isPositiveAnswer(answer)) {
				break;
			}

			resetData();

			answer = askQuestion("Would you like to make the first move? (type: Yes/No)");

			if (isAnswerInvalid(answer)) {
				continue;
			}

			if (isPositiveAnswer(answer)) {
				is_player_move = true;
			}

			move();
		}

		System.out.println("Game Over");
	}

	private void move() {

		printBoard();

		while (isMoveLeft()) {

			printLine(50);

			if (is_player_move) {
				movePlayer();
			} else {
				moveNpc();
			}

			printBoard();

			is_player_move = !is_player_move;

			if (checkWin()) {
				return;
			}
		}

		System.out.println("Game is a tie, nobody won!");
	}

	private void movePlayer() {
		boolean is_valid_input = false;
		int number = 0;

		do {
			String answer = askQuestion("It's your turn. Enter a number between 1 and 9");

			if (!isDigit(answer)) {
				System.out.printf("Answer is no digit '%s'%n", answer);
				continue;
			}

			number = Integer.parseInt(answer);
			is_valid_input = validateMove(number, player_mark);

		} while (!is_valid_input);

		System.out.printf("You selected number '%d'%n", number);
	}

	private void moveNpc() {

		System.out.println("It's computers turn!");

		/// add all free fields to free_position
		Vector<Integer> free_position = new Vector<>();
		for (int i = 0; i < board_size_x * board_size_y; ++i) {
			if (board[i / 3][i % 3] == neutral_mark) {
				// a player would select 1 - 9 -> the for loop iterates from 0 - 8 -> we have to
				// add +1 to i
				free_position.add(i + 1);
			}
		}
		/*
		 * 
		 * y
		 * " _ X 2 "
		 * x " 3 x 5 "
		 * " 6 x 8 "
		 * 
		 * 
		 * i = 5
		 * pos_x = 5/size_x -> 1
		 * pos_y = 5%size_y -> 2
		 * 
		 * vector -> {4, 7}
		 * (Math.random() * 1) -> {0, 1} -> 1
		 * 
		 * vector.elementAt(1) -> 7
		 * 
		 */

		boolean is_valid_input = false;
		int number = 0;

		do {
			int position = (int) (Math.random() * (free_position.size() - 1));
			number = free_position.elementAt(position);
			is_valid_input = validateMove(number, npc_mark);

		} while (!is_valid_input);

		System.out.printf("Computer selected number '%d'%n", number);
	}

	private boolean checkWin() {

		boolean win = false;
		char character = neutral_mark;

		for (int pos = 0; pos < board_size_x * board_size_y; ++pos) {
			character = getBoardValue(pos);

			if (character == neutral_mark) {
				continue;
			}

			int pos_x = pos / board_size_x;
			int pos_y = pos % board_size_y;

			/// check horizontal
			if (pos_y == 0) {
				if (isAllEqual(
						pos_x * board_size_x,
						pos_x * board_size_x + 1,
						pos_x * board_size_x + 2, character)) {
					win = true;
					break;
				}
			}

			/// check vertical
			if (pos_x == 0) {
				if (isAllEqual(
						pos_y,
						board_size_x + pos_y,
						2 * board_size_x + pos_y, character)) {
					win = true;
					break;
				}
			}

			// diagonal right
			if (pos_x == 0 && pos_y == 0) {
				if (isAllEqual(0, 4, 8, character)) {
					win = true;
					break;
				}
			}

			// diagonal left
			if (pos_x == 0 && pos_y == 2) {
				if (isAllEqual(2, 4, 6, character)) {
					win = true;
					break;
				}
			}

		}

		if (win) {
			if (character == player_mark) {
				System.out.println("Player has won the game");
			} else if (character == npc_mark) {
				System.out.println("Computer has won the game");
			} else {
				System.out.println("Error: I don't know who won!");
			}
		}

		return win;
	}

	private boolean isAllEqual(int pos_1, int pos_2, int pos_3, char character) {

		char[] selection = {
				getBoardValue(pos_1),
				getBoardValue(pos_2),
				getBoardValue(pos_3)
		};

		for (char c : selection) {
			if (character != c) {
				return false;
			}
		}
		return true;
	}

	private char getBoardValue(int position) {

		return board[position / board_size_x][position % board_size_y];
	}

	private boolean validateMove(int number, char character) {

		if (number <= 0 || number > (board_size_x * board_size_y)) {
			System.out.printf("Number out of range '%d'%n", number);
			return false;
		}

		/// increment number so we can compute the board index
		number--;
		int pos_x = number / board_size_x;
		int pox_y = number % board_size_y;

		if (board[pos_x][pox_y] != neutral_mark) {
			System.out.printf("Field '%d' already used %n", number + 1);
			return false;
		}

		board[pos_x][pox_y] = character;

		return true;
	}

	private boolean isMoveLeft() {
		for (int i = 0; i < board_size_x; ++i) {
			for (int j = 0; j < board_size_y; ++j) {
				if (board[i][j] == neutral_mark) {
					return true;
				}
			}
		}
		return false;
	}

	private void resetData() {
		is_player_move = false;

		for (int i = 0; i < board_size_x; ++i) {
			for (int j = 0; j < board_size_y; ++j) {
				board[i][j] = neutral_mark;
			}
		}
	}

	private void printBoard() {

		for (char[] row : board) {
			for (char column : row) {
				System.out.print(column);
			}
			System.out.println();
		}
	}

	private static boolean isDigit(String digit_string) {
		if (digit_string.length() == 0) {
			return false;
		}

		for (char digit : digit_string.toCharArray()) {
			if (!Character.isDigit(digit)) {
				return false;
			}
		}

		return true;
	}

	private static boolean isPositiveAnswer(String answer) {
		return answer.equalsIgnoreCase("yes");
	}

	private static boolean isAnswerInvalid(String answer) {
		boolean is_invalid = !(isPositiveAnswer(answer) || answer.equalsIgnoreCase("no"));
		if (is_invalid) {
			System.out.printf("Unknown answer '%s'%n", answer);
		}
		return is_invalid;
	}

	private static String askQuestion(String question) {
		System.out.println(question);
		return readInput();
	}

	private static String readInput() {
		Scanner input_scanner = new Scanner(System.in);
		return input_scanner.nextLine();
	}

	private static void printLine(int length) {
		System.out.println("-".repeat(length));
	}

	/// entry point of the program
	public static void main(String[] args) {
		TicTacToe game = new TicTacToe();
		game.start();
	}

}
