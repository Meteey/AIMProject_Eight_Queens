package com.example.aimproject_eight_queens;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class AlgorithmPerformanceRecorder {
    private BufferedWriter writer;

    public void run(){
        try {
            writer = new BufferedWriter(new FileWriter("AlgorithmPerformanceRecorder.log", true));

            Game game = new Game();
            game.createRandomQueens();
            int[] queens = game.Queens.clone();
            game.analyzeNode();
            writer.write("Initial Table : " + Arrays.toString(game.Queens) + "\n");
            writer.write("Initial H value: " + game.capturingPairs + "\n");
            long startTime = System.nanoTime();

            while (game.capturingPairs != 0) {
                game.HillClimbing();

            }

            long endTime = System.nanoTime();
            double durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
            writer.write("pure hc\n");
            writer.write("Toplam Süre: " + durationInMilliseconds + " ms\n");
            writer.write("Toplam Restart :" + game.repeatCount + "\n");
            writer.write("Toplam Adım: " + game.moveCount + "\n\n");
            game.Queens = queens.clone();
            game.resetGame();
            startTime = System.nanoTime();

            while (game.capturingPairs != 0 ) {
             game.FirstChoiceHillClimbing();
            }

            endTime = System.nanoTime();
            durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
            writer.write("fchc");
            writer.write("Toplam Süre: " + durationInMilliseconds + " ms\n");
            writer.write("Toplam Restart :" + game.repeatCount + "\n");
            writer.write("Toplam Adım: " + game.moveCount + "\n\n");
            writer.close();
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    AlgorithmPerformanceRecorder() {

    }

    public static void main(String[] args) {
        AlgorithmPerformanceRecorder recorder = new AlgorithmPerformanceRecorder();
        for(int i = 0; i< 20; i++){
            recorder.run();
        }

    }
}
