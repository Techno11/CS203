package edu.kettering.zais5275.cs203.Assignmemt2;

// Imports for Display

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.*;

// Imports for algorithm

public class TimeAnalysis {

    private static Point[] sortedX;
    private static Point[] sortedY;
    static long basicOpCount = 0;

    public static void main(String[] args) {
        for(int i = 2; i < 40; i++) {
            basicOpCount = 0;
            int n = (int)Math.pow(2, i);
            generateRandomPoints(n);                        // Setup Graph
            long startTime = System.nanoTime();             // Record Start Time
            PointPair score = divAndConq(sortedX, sortedY); // Divide and Conquer
            // Stats Outputs
            // System.out.print("Solved " + n + " points in " + (System.nanoTime() - startTime) + " nano seconds, closest points were ");
            // System.out.println(n + "	" + (System.nanoTime() - startTime));
            System.out.println(basicOpCount);
            // System.out.print("(" + score.p1.x + ", " + score.p1.y + ") and ");
            // System.out.println("(" + score.p2.x + ", " + score.p2.y + ") with a distance of " + score.distance + " units.");
            // System.out.println("A new window has launched with a visualization of this output!");
            // Open JFrame with graph visualization
            // display(n, score);
        }
    }

    /**
     * Recursive Divide-and-conquer method to solve 2d closest points
     * @param fullSortedX All coordinates, sorted by X
     * @param fullSortedY All coordinates, sorted by Y
     * @return Point Pair with the closest pair, and it's points
     */
    private static PointPair divAndConq(Point[] fullSortedX, Point[] fullSortedY) {
        if (fullSortedX.length <= 3) { // Brute-force method for finding closest pair of 3
            return bruteForce(fullSortedX);
        } else {
            // Find midpoint
            int midpoint = fullSortedX.length / 2;

            // Initialize our Arrays xLeft, xRight, yLeft, yRight
            Point[] xLeft = new Point[midpoint], xRight = new Point[fullSortedX.length - midpoint];
            Point[] yLeft = new Point[midpoint], yRight = new Point[fullSortedX.length - midpoint];

            // Split Xs in half and set storage locations
            for(int i = 0; i < fullSortedX.length; i++) {
                basicOpCount++;
                if(i < midpoint) {                                          // Place in x-left array, and set storage location to x-left
                    fullSortedX[i].storageLocation = StorageLocation.Left;
                    xLeft[i] = fullSortedX[i];
                } else {                                                    // Place in x-right array, and set storage location to x-right
                    fullSortedX[i].storageLocation = StorageLocation.Right;
                    xRight[i - midpoint] = fullSortedX[i];
                }
            }

            // Copy Ys to Y arrays based on the storage locations we just set
            int yLeftLoc = 0;
            int yRightLoc = 0;
            for(Point p : fullSortedY) {
                basicOpCount++;
                if(p.storageLocation == StorageLocation.Left) {             // if storage location is left, store in y left
                    yLeft[yLeftLoc] = p;
                    yLeftLoc++;
                } else {                                                    // otherwise store in y right
                    yRight[yRightLoc] = p;
                    yRightLoc++;
                }
            }

            // Solve each half recursively
            PointPair dL = divAndConq(xLeft, yLeft);
            PointPair dR = divAndConq(xRight, yRight);

            // Determine smallest of each half
            PointPair leastDistance = (dL.distance < dR.distance) ? dL : dR;

            // Get middlemost X from array
            double m = fullSortedX[midpoint - 1].x;

            // Copy all points in sorted Y that are less than middlemost X
            ArrayList<Point> s = new ArrayList<>();
            for(Point y : fullSortedY) {
                if (Math.abs(y.x - m) < leastDistance.distance) {
                    s.add(y);
                }
            }

            // Square Least Distance
            PointPair leastDistanceSq =  new PointPair(leastDistance.p1, leastDistance.p2, Math.pow(leastDistance.distance, 2));

            // Locate Closest Points
            for(int i = 0; i < s.size() - 2; i++) {
                for (int k = i+1; k < s.size() && Math.pow(s.get(k).y - s.get(i).y, 2) < leastDistanceSq.distance; k++) {

                    basicOpCount++;
                    // calculate distance
                    double dist = distance(s.get(k), s.get(i));
                    // if distance is smaller, update min
                    if(dist < leastDistanceSq.distance) leastDistanceSq.updateAll(s.get(k), s.get(i), dist);
                }
            }
            leastDistanceSq.distance = Math.sqrt(leastDistanceSq.distance);
            return leastDistanceSq;
        }
    }

    /**
     * Brute force algorithm for solving closest 2d point pair
     * @param points List of points to search
     * @return PointPair of closest points
     */
    private static PointPair bruteForce(Point[] points) {
        // Instantiate container for our closest points
        PointPair min = new PointPair(null, null, Double.MAX_VALUE);
        // Iterate over all Points
        for (int i = 0; i < points.length; ++i){
            // Iterate over all points
            for (int j = i+1; j < points.length; ++j){
                // compare 2 points from our 2 iterations
                double dist = distance(points[i], points[j]);
                // if distance is smaller than previous distance, update our minimum
                if (dist < min.distance) min.updateAll(points[i], points[j], dist);
            }
        }
        return min;
    }

    /**
     * Calculate distance between 2 given points
     * @param p1 Point 1
     * @param p2 Point 2
     * @return Distance between p1 and p2
     */
    private static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * Generate Random Points, sorted by X and Y
     * @param n number of points to generate
     */
    private static void generateRandomPoints(int n) {
        sortedX = new Point[n];
        sortedY = new Point[n];
        int multiplier = n * 10;
        for(int i = 0; i < sortedX.length; i++) {
            int x = (int)Math.floor(Math.random() * multiplier);
            int y = (int)Math.ceil(Math.random() * multiplier);
            Point p = new Point(x, y);
            sortedX[i] = p;
            sortedY[i] = p;
        }
        Arrays.sort(sortedX, Comparator.comparingDouble(a -> a.x));
        Arrays.sort(sortedY, Comparator.comparingDouble(a -> a.y));
    }


    /**
     * Method for setting up a jFrame to display our graph visualization
     * @param n number of points
     * @param p solved point pair to highlight in red
     */
    private static void display(int n, PointPair p) {
        Point[] criticalPoints = {p.p1, p.p2};                       // Create critical point array
        DisplayGraph dg = new DisplayGraph(sortedX, criticalPoints); // new graph
        JFrame frame = new JFrame();                                 // New Window
        frame.setTitle("zais5275 Assignment 2 | " + n + " points");  // Set Title
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        // Default Close operation
        frame.add(dg);                                               // Add graph to window
        frame.setSize(400,400);                        // Set window size
        frame.setLocation(200,200);                            // Set Window Location
        frame.setVisible(true);                                      // Set Window visible
    }
}
