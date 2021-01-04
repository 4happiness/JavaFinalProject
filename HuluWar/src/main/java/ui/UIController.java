package ui;

import creature.Creature;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.ObjectExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.HuluClient;
import network.HuluServer;
import org.omg.PortableServer.THREAD_POLICY_ID;
import playback.BattleLog;
import playback.LogManager;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

;

public class UIController {
    @FXML
    private AnchorPane pane;
    @FXML
    private Canvas canvas;
    @FXML
    private Button button1;
    @FXML
    private Button button2;

    private GraphicsContext gc;
    private BatteleField batteleField;

    //游戏状态
    public static GameState gamestate;

    public static Stage stage;

    static LogManager logManager;
    HuluServer huluServer;
    HuluClient huluClient;
    private int severflag = 0;

    public void UIContrller() {
    }

    ExecutorService exec; //线程池
    Timeline timeline;

    public static int clientID;
    public static GameState.Gamestate statebefore;

    @FXML
    public void initialize() {
        if (canvas == null)
            System.out.println("canvas null");
        else
            gc = canvas.getGraphicsContext2D();

        //展示背景
        URL url = this.getClass().getClassLoader().getResource("picture/Background1.png");
        Image bg = new Image(url.toString());
        gc.drawImage(bg, 0, 0, 881, 551);

        gamestate = new GameState();
        gamestate.state = GameState.Gamestate.CHOOSE;
        //battleLog = new BattleLog();
        logManager = new LogManager(gc);
        exec = Executors.newCachedThreadPool();
    }

    private void startGame() throws ClassNotFoundException ,IOException{
        //选择一个记录的文件
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.setTitle("保存游戏记录");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("log", ".log"));
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            System.out.println("No record,No game! Please press L to choose file to record.");
            return;
        }
        //writer
        LogManager.out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file.getPath())));

        //构建作战场
        //batteleField.refreshFiled();

        gamestate.state = GameState.Gamestate.INGAME;

        //battleLog = new BattleLog(BatteleField.creatures);
        //logManager.writeLog(battleLog);

        //exec.execute(batteleField);

        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        event1 -> {
                            batteleField.refreshFiled();
                        }),
                new KeyFrame(Duration.millis(10))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        for (Creature creature : BatteleField.mycreatures) {
            exec.execute(creature);
        }
        new Thread(() -> { //创建一个线程监听有没有一方全部阵亡
            synchronized (gamestate) {
                try {
                    gamestate.wait();    //等待游戏结束
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gameOver();
            }
        }).start();

    }

    private void gameOver() {
        //System.out.println("gameover()");

        try {
            if (severflag==1) {
                huluServer.socket.shutdownInput();
                huluServer.socket.shutdownOutput();
                huluServer.socket.close();
            } else {
                huluClient.socket.shutdownInput();
                huluClient.socket.shutdownOutput();
                huluClient.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        exec.shutdownNow();  //关闭线程
        while (((ThreadPoolExecutor) exec).getActiveCount() != 0) {
        }
        ;  //等线程关闭
        timeline.stop();  //关闭动画
        batteleField.refreshFiled();   //做最后一次画面刷新
        batteleField.drawOver();
        logManager.closeOutputStream();   //关闭流
        gamestate.state = GameState.Gamestate.OVER;


    }

    private void reveiwGame() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.setTitle("选择记录进行战斗回放");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("log", "*.log"));
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            System.out.println("You choose no file to review game.");
            return;
        }
        statebefore= gamestate.state;
        gamestate.state = GameState.Gamestate.REVIEW;
        LogManager.in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file.getPath())));
        logManager.review();
    }

    private void shiftcontrol() {
        batteleField.shiftcontrl();
    }

    @FXML
    public void chooseHulu(ActionEvent actionEvent) {
        if(pane.getChildren().size()==6){
            pane.getChildren().remove(5);
        }
        pane.getChildren().remove(4);
        clientID = 1;
        batteleField = new BatteleField(gc);
        pane.getChildren().remove(3);
        pane.getChildren().remove(2);
        gamestate.state = GameState.Gamestate.NOTSTART;
        pane.addEventFilter(KeyEvent.KEY_PRESSED, new KeyEventHandler());
        Platform.runLater(() -> pane.requestFocus());
        if(severflag==1){
            huluServer = new HuluServer();
            huluServer.start();
        }else{
            huluClient = new HuluClient();
            huluClient.start();
        }

    }

    @FXML
    public void chooseSnake(ActionEvent actionEvent) {
        if(pane.getChildren().size()==6){
            pane.getChildren().remove(5);
        }
        pane.getChildren().remove(4);
        clientID = 2;
        batteleField = new BatteleField(gc);
        pane.getChildren().remove(3);
        pane.getChildren().remove(2);
        gamestate.state = GameState.Gamestate.NOTSTART;
        pane.addEventFilter(KeyEvent.KEY_PRESSED, new KeyEventHandler());
        Platform.runLater(() -> pane.requestFocus());

        if(severflag==1){
            huluServer = new HuluServer();
            huluServer.start();
        }else{
            huluClient = new HuluClient();
            huluClient.start();
        }
    }

    @FXML
    public void openSever(ActionEvent actionEvent) {
        pane.getChildren().remove(5);
        severflag = 1;
    }

    private class KeyEventHandler implements EventHandler<KeyEvent> {
        int flag = 0;

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.SPACE) {
                if (gamestate.state == GameState.Gamestate.NOTSTART) {
                    if (flag == 0) {
                        pane.getChildren().remove(1);    //将提示语label删掉
                        flag = 1;
                    }

                    try {
                        startGame();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else if (event.getCode() == KeyCode.UP && gamestate.state == GameState.Gamestate.INGAME) {
                BatteleField.mycreatures.get(batteleField.goodchoosen).move(0);
                //batteleField.refreshFiled();
            } else if (event.getCode() == KeyCode.DOWN && gamestate.state == GameState.Gamestate.INGAME) {
                BatteleField.mycreatures.get(batteleField.goodchoosen).move(1);
                //batteleField.refreshFiled();
            } else if (event.getCode() == KeyCode.LEFT && gamestate.state == GameState.Gamestate.INGAME) {
                BatteleField.mycreatures.get(batteleField.goodchoosen).move(2);
                //batteleField.refreshFiled();
            } else if (event.getCode() == KeyCode.RIGHT && gamestate.state == GameState.Gamestate.INGAME) {
                BatteleField.mycreatures.get(batteleField.goodchoosen).move(3);
                //batteleField.refreshFiled();
            } else if (event.getCode() == KeyCode.L) {
                if (gamestate.state == GameState.Gamestate.NOTSTART || gamestate.state == GameState.Gamestate.OVER) {
                    if (gamestate.state == GameState.Gamestate.NOTSTART && flag == 0) {
                        pane.getChildren().remove(1);    //将提示语label删掉
                        flag = 1;
                    }
                    try {
                        reveiwGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else if (event.getCode() == KeyCode.TAB && gamestate.state == GameState.Gamestate.INGAME) {
                shiftcontrol();
            } else {
                System.out.println("Maybe you tap a wrong key?");
            }
        }
    }
}
