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

public class HelloController implements Initializable, GameObserver {
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
    public Button fcHillClimbingButton;
    @FXML
    public Button pureHillClimbingButton;
    @FXML
    public Button randomiseQueensButton;
    @FXML
    public Pane boardPane;
    Game game;
    ImageView[] queens;
    boolean manualMode = false;

    private Timeline timeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = new Game();
        game.addObserver(this);
        queens = new ImageView[8];
        randomiseQueens();
    }
    @Override
    public void update(int[] queenPositions, int capturingPairCount, int moveCount, int repeatCount) {
        for (int i = 0; i < 8; i++) {
            queens[i].setY((7-queenPositions[i])*100);
        }
        CapturingPairCount.setText("Capturing Pairs : " + capturingPairCount);
        queenMovesCount.setText("Queen Moves Count : " + moveCount);
        randomRestartCount.setText("Random Restart Count : " + repeatCount);
    }
    @FXML
    protected void randomiseQueens() {
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
        game.resetGame();
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
        }
    }
    @FXML
    protected void tryManually() {
        if(manualMode){
            manualMode = false;
            tryManuallyButton.setText("Try Manually");
            setButtons();
            game.resetGame();
        }
        else{
            manualMode = true;
            setButtons();
            CapturingPairCount.setText("Capturing Pairs : ???");
            tryManuallyButton.setText("Submit");

        }
    }
    private void setButtons(){
        fcHillClimbingButton.setDisable(manualMode);
        pureHillClimbingButton.setDisable(manualMode);
        randomiseQueensButton.setDisable(manualMode);
        timeSettings.setDisable(manualMode);
    }
    @FXML
    protected void pureHillClimbing() {
        game.resetGame();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1/timeSettings.getValue()), event -> {

            if(game.capturingPairs > 0){
                game.purehc();
            }
            else{
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();



    }



    @FXML
    protected void fcHillClimbing() {
        game.resetGame();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1/timeSettings.getValue()), event -> {

            if(game.capturingPairs > 0){
                game.fchc();
            }
            else{
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);

        timeline.playFromStart();
    }




}