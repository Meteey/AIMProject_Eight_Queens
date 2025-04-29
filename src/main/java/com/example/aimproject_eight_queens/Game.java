package com.example.aimproject_eight_queens;

import java.util.Arrays;
import java.util.Random;

public class Game {
    public int[] Queens;
    public int capturingPairs;
    Game(){
        Queens = new int[8];
    }

    public void createRandomQueens() {
        Random rand = new Random();
        for(int h = 0; h < 8; h++) {
            Queens[h] = rand.nextInt(0,8);
        }

    }
    public int analyzeNode(){
        capturingPairs = 0;
        for (int h = 0; h < 8; h++) {
            for (int q = h+1; q < 8; q++) {
                if(isCapturing(h,Queens[h], q, Queens[q])){
                    capturingPairs++;
                }
            }
        }
        return capturingPairs;
    }
    private Boolean isCapturing(int column1, int row1, int column2, int row2) {
        return row1 == row2 || (Math.abs(column1 - column2) == Math.abs(row1 - row2));
    }
}
