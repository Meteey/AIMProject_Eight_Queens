package com.example.aimproject_eight_queens;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    @FXML
    public Slider timeSettings;
    @FXML
    public Text CapturingPairCount;
    @FXML
    public Text queenMovesCount;
    @FXML
    public Text randomRestartCount;
    @FXML
    public Button tryManuallyButton;
    @FXML
    public Pane boardPane;
    Game game;
    ImageView[] queens;
    boolean manualMode = false;
    int repeatCount = 0;
    int moveCount = 0;
    private Timeline timeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = new Game();
        queens = new ImageView[8];
    }
    @FXML
    protected void addQueensRandomly() {
        game.createRandomQueens();
        if(queens[0] == null) {
            for (int i = 0; i < 8; i++) {
                ImageView queen = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/aimproject_eight_queens/bqueen.png")).toExternalForm()));

                queen.setX(i*100);
                queen.setY((7-game.Queens[i])*100);
                queens[i] = queen;
                boardPane.getChildren().add(queen);
            }
        }
        else{
            for (int i = 0; i < 8; i++) {
                queens[i].setY((7-game.Queens[i]) * 100);
            }
        }
        game.analyzeNode();
        CapturingPairCount.setText("Capturing Pairs : " + game.capturingPairs);
        resetTable();
    }
    @FXML
    protected void addQueenManually(MouseEvent event) {
        if(manualMode){
            int x = (int) (event.getX()/100);
            int y = (int) (event.getY()/100);
            if(queens[x] != null){
                queens[x].setY(y * 100);
                game.Queens[x] = 7-y;
            }
            else{
                ImageView queen = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/aimproject_eight_queens/bqueen.png")).toExternalForm()));
                queens[x] = queen;
                queen.setX(x*100);
                queen.setY(y*100);
                game.Queens[x] = 7-y;
                boardPane.getChildren().add(queen);
            }
            game.analyzeNode();
            CapturingPairCount.setText("Capturing Pairs : " + game.capturingPairs);
        }
    }
    @FXML
    protected void tryManually() {
        if(manualMode){
            manualMode = false;
            tryManuallyButton.setText("Try Manually");
        }
        else{
            manualMode = true;
            tryManuallyButton.setText("Cancel");
        }
    }

    @FXML
    protected void pureHillClimbing() {
        resetTable();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1/timeSettings.getValue()), event -> {

            if(game.capturingPairs > 0){
                purehc();
            }
            else{
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();



    }
    private void purehc(){
        int bestValue = game.analyzeNode();
        int[] bestMove = new int[]{-1, -1};
        for(int column = 0; column < 8; column++){
            int originalPosition = game.Queens[column];
            for(int row = 0; row < 8; row++){
                game.Queens[column] = row;
                int currentState = game.analyzeNode();
                if(currentState < bestValue){
                    bestValue = currentState;
                    bestMove[0] = column;
                    bestMove[1] = row;
                    moveCount++;
                }
            }
            game.Queens[column] = originalPosition;
        }
        if(bestMove[0] == -1){
            randomRestart();
        }
        else{
            game.Queens[bestMove[0]] = bestMove[1];
            game.analyzeNode();
            updateBoard();
        }
    }


    @FXML
    protected void fcHillClimbing() {
        resetTable();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1/timeSettings.getValue()), event -> {

            if(game.capturingPairs > 0){
                fchc();
            }
            else{
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);


        timeline.playFromStart();


    }
    private void fchc(){
        int initialValue = game.capturingPairs;
        for(int column = 0; column < 8; column++){
            int originalPosition = game.Queens[column];

            for (int row = 0; row < 8; row++) {
                if (row == originalPosition) continue;

                game.Queens[column] = row;
                game.analyzeNode();

                if(initialValue > game.capturingPairs){
                    moveCount++;
                    updateBoard();
                    return;
                }
            }

            game.Queens[column] = originalPosition;

        }
        randomRestart();
    }
    private void randomRestart(){
        repeatCount ++;
        game.createRandomQueens();
        game.analyzeNode();
        updateBoard();
    }


    private void resetTable(){
        moveCount =0;
        repeatCount = 0;
        updateBoard();
    }

    public void updateBoard(){
        for (int i = 0; i < 8; i++) {
            queens[i].setY((7-game.Queens[i])*100);
        }
        CapturingPairCount.setText("Capturing Pairs : " + game.capturingPairs);
        queenMovesCount.setText("Queen Moves Count : " + moveCount);
        randomRestartCount.setText("Random Restart Count : " + repeatCount);
    }
}