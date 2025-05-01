package com.example.aimproject_eight_queens;
import java.util.Random;

public class Game {
    public int[] Queens;
    public int capturingPairs;
    private GameObserver observer;
    public int moveCount =0;
    public int repeatCount =0;

    public void addObserver(GameObserver observer) {
        this.observer = observer;
    }
    public void notifyObserver(){
        if(observer != null){
            observer.update(Queens.clone(), capturingPairs, moveCount, repeatCount);
        }
    }

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

    public void FirstChoiceHillClimbing(){
        int initialValue = capturingPairs;
        for(int column = 0; column < 8; column++){
            int originalPosition = Queens[column];

            for (int row = 0; row < 8; row++) {
                if (row == originalPosition) continue;

                Queens[column] = row;
                analyzeNode();

                if(initialValue > capturingPairs){
                    moveCount++;
                    notifyObserver();
                    return;
                }
            }

            Queens[column] = originalPosition;

        }
        randomRestart();
    }

    public void HillClimbing(){
        int bestValue = analyzeNode();
        int[] bestMove = new int[]{-1, -1};
        for(int column = 0; column < 8; column++){
            int originalPosition = Queens[column];
            for(int row = 0; row < 8; row++){
                Queens[column] = row;
                int currentState = analyzeNode();
                if(currentState < bestValue){
                    bestValue = currentState;
                    bestMove[0] = column;
                    bestMove[1] = row;
                }
            }
            Queens[column] = originalPosition;
        }
        if(bestMove[0] == -1){
            randomRestart();
        }
        else{
            Queens[bestMove[0]] = bestMove[1];
            moveCount++;
            analyzeNode();
            notifyObserver();
        }
    }


    private void randomRestart(){
        repeatCount ++;
        createRandomQueens();
        analyzeNode();
        notifyObserver();
    }

    public void resetGame(){
        analyzeNode();
        moveCount = 0;
        repeatCount = 0;
        notifyObserver();
    }


}
