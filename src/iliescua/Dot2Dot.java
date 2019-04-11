/*
* CS2852
* Fall 2018
* Lab 1 - Dot2Dot Class
* Created: 9/9/2018
*/
package iliescua;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is the driver that runs the program
 */
public class Dot2Dot extends Application {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    /**
     * This method is used to set up the GUI
     * @param stage sets stage
     * @throws Exception exception gets thrown
     */
    public void start(Stage stage) throws Exception {
        FXMLLoader firstLoader = new FXMLLoader();
        Parent root = firstLoader.load(getClass()
                .getResource("Dot2DotController.fxml").openStream());
        stage.setTitle("Dot to Dot");
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}