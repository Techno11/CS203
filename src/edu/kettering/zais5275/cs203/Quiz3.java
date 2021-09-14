package edu.kettering.zais5275.cs203;

public class Quiz3 {

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 25, 84, 421, 64, 89, 53, 24};

        System.out.println(RiddleDivideAndConquer(a, 1, a.length - 2));
        System.out.println(here);
        System.out.println((a.length - 2) - 1);
    }

    static float decoded(int[] A, int leftBound, int rightBound) {
        if( leftBound > rightBound ) { return 0.0F; }
        else if( leftBound == rightBound ) { return A[leftBound]; }
        else {
            // Find middle of array bounds
            int middle = ( leftBound + rightBound ) / 2;

            // calculate average between left and middle bound
            float leftAvg = RiddleDivideAndConquer(A, leftBound, middle);
            // calculate average between middle and right bound
            float rightAvg = RiddleDivideAndConquer(A, middle+1, rightBound);

            // Calculate Bound sizes
            int leftBoundSize = ( middle - leftBound + 1);
            int rightBoundSize = ( rightBound - middle );

            // Unaverage the results from Recursive Call
            float leftUnAvg = leftAvg * leftBoundSize;
            float rightUnAvg = rightAvg * rightBoundSize;

            // calculate total bound size from left to right
            int totalBoundSize = ( rightBound - leftBound + 1 );

            // Return average from left to right
            return ( leftUnAvg + rightUnAvg ) / totalBoundSize;
        }
    }

    static int here = 0;
    static boolean done = false;
    static float RiddleDivideAndConquer(int[] A, int L, int R) {
        if(!done) {
            System.out.println(R + " - " + L + " = " + (R-L));
            done = true;
        }
        if( L > R ) { return 0.0F; }
        else if( L == R ) { return A[L]; }
        else {
            here++;
            int S = ( L + R ) / 2;
            float res1 = RiddleDivideAndConquer(A, L, S);
            float res2 = RiddleDivideAndConquer(A, S+1, R);
            float res3 = res1 * ( S - L + 1);
            float res4 = res2 * ( R - S );
            return ( res3 + res4 ) / ( R - L + 1 );
        }
    }

    static float combo(int[] A, int L, int R) {
        if( L > R ) { return 0.0F; }
        else if( L == R ) { return A[L]; }
        else {
            here++;
            int S = ( L + R ) / 2;
            return ( (RiddleDivideAndConquer(A, L, S) * ( S - L + 1)) + (RiddleDivideAndConquer(A, S+1, R) * ( R - S )) ) / ( R - L + 1 );
        }
    }
}
