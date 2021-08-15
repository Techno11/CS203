package edu.kettering.zais5275.cs203;

import java.util.ArrayList;

public class IterativeRepair {
    /** Number of Queens Set Here **/
    private static int n = 100;

    private static int[] board = new int[n];
    private static long collisionCalculationCount = 0;

    public static void main(String[] args) {
        if(n < 4) {
            System.out.println("Please enter a board size larger than 3!");
            System.exit(1);
        } if(n > 199) {
            System.out.println("Large board... This may take a while!");
        }
        long startTime = System.currentTimeMillis();
        long totalSwaps = 0;
        long totalLoopings = 0;
        /* Initialize board with queens on diagonal
         Ex. (n = 4)
         Q X X X
         X Q X X
         X X Q X
         X X X Q
         */
        // Array to Store Available Positions
        ArrayList<Integer> positions = new ArrayList<>();
        // Fill arrays with positions
        for (int i = 0; i < n; i++) positions.add(i);
        // Randomly assign an available position to our board
        for (int i = 0; i < board.length; i++) {
            // Calculate a random avaliable position from our array
            int randomAvailablePosition = (int)Math.round(Math.random() * (positions.size() - 1));
            // Set position and remove from avaliable positions
            board[i] = positions.remove(randomAvailablePosition);
        }

        int numCollisions = calculateCollisions(board);
        boolean swapPerformed = false;
        do {
            swapPerformed = false;
            for(int qa = 0; qa < n - 1; qa++) {
                for(int qb = qa+1; qb < n; qb++) {
                    int[] swapped = board.clone();
                    swapped[qa] = board[qb];
                    swapped[qb] = board[qa];
                    int newNumCollisions = calculateCollisions(swapped);
                    if (newNumCollisions < numCollisions) {
                        swapPerformed = true;
                        totalSwaps++;
                        board = swapped;
                        numCollisions = newNumCollisions;
                        if(totalSwaps % 1000 == 0) {
                            System.out.println("Still working... Current Swap Count: " + totalSwaps);
                        }
                    }
                    totalLoopings++;
                }
            }
        } while (swapPerformed);
        printBoard();
        System.out.println("Solution found! Stats: ");
        System.out.println("Swaps: " + totalSwaps);
        System.out.println("Board Size: " + n + " x " + n);
        System.out.println("Total Time: " + ((System.currentTimeMillis() - startTime) / 1000.0) + " seconds");
        System.out.println("Innermost Main Loop Execution Count: " + totalLoopings);
        System.out.println("Collision Calculation Loop Execution Count: " + collisionCalculationCount);
    }


    public static void printBoard() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                System.out.print((board[col] == row) ? "|Q" : "|_");
            }
            System.out.println("|");
        }
    }

    private static int calculateCollisions(int[] board) {
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
            collisionCalculationCount++;
        }

        // diagonals going left
        for (int i = col - 1, j = 1; i >= 0; i--, j++) {
            if (arr[i] == queenHeight + j || arr[i] == queenHeight - j) {
                count++;
            }
            collisionCalculationCount++;
        }
        return count;
    }
}
