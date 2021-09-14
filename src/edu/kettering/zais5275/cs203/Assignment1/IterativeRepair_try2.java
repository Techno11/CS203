package edu.kettering.zais5275.cs203.Assignment1;

import java.util.ArrayList;
import java.util.Arrays;

public class IterativeRepair_try2 {

    private static int[] board;
    private static int basicOpCnt = 0;

    public static void main(String[] args) {
        /* Number of Queens Set Here */
        int numQueens = 8;

        if(numQueens < 4) {
            System.out.println("Please enter a board size larger than 3!");
            System.exit(1);
        }

        runTimingTest();

        /*
        Create while loop to run the board calculation until a solution with 0 collision on the board is found.
        Sometimes, rarely, the algorithm will exit before a proper solution is found.  The lower the n count,
        the more likely the algorithm is to exit without having found a proper solution. Thus, we scrap the board,
        generate a new board, and retry.
        As the paper states:
        "For an initial permutation, if no solution is found after the completion of the repeat loop, a new
            permutation is generated and a new search process is started."
         */
/*
        int c = -1;
        long start = System.nanoTime();
        while (c != 0) {
            // Create a new board of the specified size
            board = new int[numQueens];
            // Randomly assign queens
            setupBoard(numQueens);
            // Repair algorithm
            repair(numQueens);
            // Run through every queen anc check collisions on each
            c =  calculateCollisionsWholeBoard(board);
            if (c != 0) { // Collisions Exist
                System.out.println("Board complete with collisions. Scrapping...");
            } else { //
                System.out.println(numQueens + "-Queens Board completed in " + ((System.nanoTime() - start)/1000000000.0) + " seconds");
            }
        }

 */
    }

    public static void runTimingTest() {
        int numQueens, attemptCount = 0;
        for(double i = 2; i < 20; i++) {
            numQueens = (int)Math.pow(2.0, i);
            // System.out.println("Solving " + numQueens + " queens");

            board = new int[numQueens];
            basicOpCnt = 0;
            setupBoard(numQueens);

            long startTime = System.nanoTime();


            repair(numQueens);
            long endTime = System.nanoTime();

            int c =  calculateCollisionsWholeBoard(board);
            if(c > 0) {
                i--;
                attemptCount++;
            } else {
                System.out.println(numQueens + " queens took " + (endTime - startTime) + " nano seconds in " + attemptCount + " times, Basic Op Count: " + basicOpCnt);
                attemptCount = 0;
            }
        }
    }

    public static void repair(int n) {
        int numCollisions;
        boolean swapPerformed;
        do {
            swapPerformed = false;
            for(int qa = 0; qa < n - 1; qa++) {
                for(int qb = qa + 1; qb < n; qb++) {
                    basicOpCnt++;
                    numCollisions = calculateCollisionsQaQb(board, qa, qb);
                    // Check if Queens are being attacked
                    if(numCollisions > 0) {
                        int[] swapped = board.clone();
                        swapped[qa] = board[qb];
                        swapped[qb] = board[qa];
                        int newNumCollisions = calculateCollisionsQaQb(swapped, qa, qb);
                        if (newNumCollisions < numCollisions) {
                            swapPerformed = true;
                            board = swapped;
                            // basicOpCnt++;
                        }
                    }
                }
            }
        } while (swapPerformed);
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
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                System.out.print((board[col] == row) ? "|Q" : "|_");
            }
            System.out.println("|");
        }
        System.out.println(Arrays.toString(board));
    }

    /**
     * Count the collisions that are in the diagonals of a queen in column a and column b
     * @param board Board to search
     * @param colA Column A Queen to check diagonals of
     * @param colB Column B Queen to check diagonals of
     * @return Count of total collisions of a queen in column a or b
     */
    private static int calculateCollisionsQaQb(int[] board, int colA, int colB) {
        return countInDiagonal(board, colA) + countInDiagonal(board, colB);
    }

    /**
     * Count all collisions of all queens on the board
     * @param board board to search
     * @return Number of collisions on the whole board
     */
    private static int calculateCollisionsWholeBoard(int[] board) {
        int count = 0;
        for(int i = 0; i < board.length; i++) {
            int c = countInDiagonal(board, i);
            count += c;
        }
        return count;
    }

    /**
     * Count # of collisions in the diagonals of the location given
     * @param board Board to search
     * @param col Queen column to check collisions of
     * @return number of collisions for queen in column
     */
    private static int countInDiagonal(int[] board, int col) {
        /*
            This method checks the X of diagonals around the queen.
            For example, If we are looking at a queen in column 3, these cells are checked:
            x  _  _  _  _  _
            _  x  _  _  _  x
            _  _  x  _  x  _
            _  _  _  Q  _  _
            _  _  x  _  x  _
            _  x  _  _  _  x
            To count the diagonal, we start at the location that is passed in.
            Ex. Column 5
            Firstly, we start by checking the diagonals going right and up and going right and down.
            We get the queen height at our column, which is 4.
            Next, we will go to column 6, and check if there is a queen at height 3 or height 5.
            Then, we will go to column 5 and check if there is a queen at height 2 or 6... etc.
            We'll increment count if we find a queen.
            Repeat the same in reverse for the left diagonals
         */
        int count = 0;
        final int queenHeight = board[col];
        // diagonals going right
        for (int i = col + 1, j = 1; i < board.length; i++, j++) {
            // Diagonal Down is within board constraints and has queen OR Diagonal Up is within board constraints and has queen
            if ((queenHeight + j < board.length && board[i] == queenHeight + j) || (queenHeight - j >= 0 && board[i] == queenHeight - j)) {
                count++;
            }
        }

        // diagonals going left
        for (int i = col - 1, j = 1; i >= 0; i--, j++) {
            // Diagonal Down is within board constraints and has queen OR Diagonal Up is within board constraints and has queen
            if ((queenHeight + j < board.length && board[i] == queenHeight + j) || ( queenHeight - j >= 0 && board[i] == queenHeight - j)) {
                count++;
            }
        }
        return count;
    }
}
