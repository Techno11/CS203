package edu.kettering.zais5275.cs203;

public class Quiz2 {

    static int bo1 = 0;
    static int bo2 = 0;
    public static void main(String[] args) {
        int[] t = {0, 1, 2, 3};
        System.out.println(MysteryFunction(t));
        System.out.println(bo1);
        System.out.println(bo2);
    }

    public static int MysteryFunction(int[] A) {
        // array size is 0. 0^0 is still 0. Save computation time
        if(( A == null ) || ( A.length == 0 )) {
            return 0;
        } else if( A.length == 1 ) { // array size 1. 1^1 is 1. save computation time.
            return 1;
        } else { // array size > 1, must calculate
            // store the highest number from the array
            int tmp = A[ A.length-1 ];

            // raise tmp to the power of the current array length
            // equivalent to tmp = Math.pow(tmp, A.length-1);
            for( int i = 1; i < A.length-1; i++ ) {
                tmp = tmp * A[ A.length-1 ];
                bo1++;
            }

            // duplicate the array, excluding the value we just multiplied
            int[] A1 = new int[A.length-1];
            for(int j = 0; j < A.length-1; j++) {
                A1[j] = A[j];
                bo2++;
            }

            // do it all over again
            return tmp + MysteryFunction(A1);
        }
    }
}
