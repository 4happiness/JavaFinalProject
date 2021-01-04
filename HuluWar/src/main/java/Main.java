import creature.Hulu;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.HuluServer;
import playback.LogManager;
import ui.BatteleField;
import ui.UIController;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GameUI.fxml"));
        Scene scene = new Scene(root, 881, 551);
        primaryStage.setTitle("葫芦娃大战妖精");
        primaryStage.setScene(scene);
        primaryStage.show();
        UIController.stage = primaryStage;

        //处理窗口关闭
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
