package edu.kettering.zais5275.cs203.Assignment1;

import java.util.ArrayList;

public class IterativeRepair_nostats {

    private static int[] board;

    public static void main(String[] args) {
        /* Number of Queens Set Here */
        int numQueens = 4;

        if(numQueens < 4) {
            System.out.println("Please enter a board size larger than 3!");
            System.exit(1);
        }

        for(double i = 2; i < 10; i++) {
            numQueens = (int)Math.pow(2.0, i);
            System.out.println("Solving " + numQueens + " queens");

            board = new int[numQueens];
            setupBoard(numQueens);

            long startTime = System.nanoTime();
            repair(numQueens, false);

            System.out.println(numQueens + " queens took " + (System.nanoTime() - startTime) + " nano seconds");
            System.out.println("Board complete. Final Collision count: " + calculateCollisions(board, numQueens));
        }
    }

    public static void repair(int n, boolean printResult) {
        int numCollisions = calculateCollisions(board, n);
        boolean swapPerformed;
        do {
            swapPerformed = false;
            for(int qa = 0; qa < n - 1; qa++) {
                for(int qb = qa+1; qb < n; qb++) {
                    int[] swapped = board.clone();
                    swapped[qa] = board[qb];
                    swapped[qb] = board[qa];
                    int newNumCollisions = calculateCollisions(swapped, n);
                    if (newNumCollisions < numCollisions) {
                        swapPerformed = true;
                        board = swapped;
                        numCollisions = newNumCollisions;
                    }
                }
            }
        } while (swapPerformed);
        if(printResult) printBoard(n);
    }

    /**
     * Initialize board with queens assigned randomly, ensuring no more than 1 queen in every row and column
     * Ex. (n = 4)
     * Q X X X
     * X X Q X
     * X Q X X
     * X X X Q
    */
    public static void setupBoard(int n) {
        // Array to Store Available Positions
        ArrayList<Integer> positions = new ArrayList<>();
        // Fill arrays with positions
        for (int i = 0; i < n; i++) positions.add(i);
        // Randomly assign an available position to our board
        for (int i = 0; i < board.length; i++) {
            // Calculate a random available position from our array
            int randomAvailablePosition = (int)Math.round(Math.random() * (positions.size() - 1));
            // Set position and remove from available positions
            board[i] = positions.remove(randomAvailablePosition);
        }
    }

    public static void printBoard(int n) {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                System.out.print((board[col] == row) ? "|Q" : "|_");
            }
            System.out.println("|");
        }
    }

    private static int calculateCollisions(int[] board, int n) {
        int count = 0;
        for(int i = 0; i < n; i++) count += countInDiagonal(board, i);
        return count;
    }

    // Count # of queens in the same diagonal as the location given
    private static int countInDiagonal(int[] arr, int col) {
        /*
            To count the diagonal, we start at the location that is passed in.
            Firstly, we start by checking the right 2 diagonals.
            Ex. Column 5
            We get the queen height at our column, which is 4.
            Next, we will go to column 6, and check if there is a queen at height 3 or height 5.
            Then, we will go to column 5 and check if there is a queen at height 2 or 6... etc.
            We'll increment count if we find a queen.
            Repeat the same in reverse for the left diagonals
         */
        int count = 0;
        final int queenHeight = arr[col];
        // diagonals going right
        for (int i = col + 1, j = 1; i < arr.length; i++, j++) {
            // Diagonal Down as queen || Diagonal Up has Queen
            if (arr[i] == queenHeight + j || arr[i] == queenHeight - j) {
                count++;
            }
        }

        // diagonals going left
        for (int i = col - 1, j = 1; i >= 0; i--, j++) {
            if (arr[i] == queenHeight + j || arr[i] == queenHeight - j) {
                count++;
            }
        }
        return count;
    }
}
