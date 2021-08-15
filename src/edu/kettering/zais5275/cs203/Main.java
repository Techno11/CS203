package edu.kettering.zais5275.cs203;

public class Main {

    public static void main(String[] args) {
        long startTime =  System.currentTimeMillis();
        solve(16);
        System.out.println("Solution found in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static boolean checkCol(int[][] board, int col, int row) {
        for (int i = 0; i < col; i++) {
            if (board[row][i] == 1) {
                return false;
            }
        }

        return true;
    }

    private static boolean checkDiag(int[][] board, int row, int col) {
        // upper diag
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        // lower diag
        for (int i = row, j = col; j >= 0 && i < board.length; i++, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    private static boolean solveRecursive(int[][] board, int col) {
        // check if all queens are placed
        if (col >= board.length) return true;


        // try placing a queen in all rows 1 by 1
        for (int i = 0; i < board.length; i++) {

            // check if a queen in this spot will work by checking column and diagonal
            if (checkCol(board, col, i) && checkDiag(board, i, col )) {
                // queen seems like it'll work
                board[i][col] = 1;

                if (solveRecursive(board, col + 1)) {
                    return true;
                } else {
                    print(board);
                    System.out.println("Failed.");
                }

                // if a solution is not found with the queen at this position, move on
                board[i][col] = 0;
            }
        }

        return false;
    }


    private static boolean solve(int boardSize)
    {
        int board[][] = new int[boardSize][boardSize];

        if (!solveRecursive(board, 0)) {
            System.out.print("Solution does not exist");
            return false;
        }

        print(board);
        return true;
    }

    private static void print(int[][] board) {
        for (int[] row : board) {
            for (int square : row) System.out.print(" " + square);
            System.out.println();
        }
    }


}
