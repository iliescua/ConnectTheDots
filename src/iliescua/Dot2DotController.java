/*
* CS2852
* Fall 2018
* Lab 1 - Dot2DotController Class
* Created: 9/9/2018
* Updated: 9/25/18
*/
package iliescua;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * This class is the controller that tells the GUI
 * how to open, close, and draw dots or lines
 */
public class Dot2DotController {
    @FXML
    Canvas canvas;
    @FXML
    Button btnChangeTotal;
    @FXML
    TextField txtNewTotal;
    @FXML
    TextField txtCurrentTotal;
    @FXML
    TextField txtTimeTaken;
    @FXML
    RadioMenuItem rmDots;
    @FXML
    RadioMenuItem rmDotsTwo;
    @FXML
    RadioMenuItem arrayList;
    @FXML
    RadioMenuItem linkedList;

    private static FileChooser fileChooser = new FileChooser();
    private FileChooser.ExtensionFilter dot = new FileChooser.ExtensionFilter("DOT", "*.dot");
    private static final int WIDTH = 500;
    private static final int HEIGHT = -400;
    private Picture picture = new Picture(new ArrayList<>());
    private File selectedFile;
    private Logger log = Logger.getLogger("Canvas");
    private Logger bmOneLog = Logger.getLogger("Benchmark1");
    private Logger bmTwoLog = Logger.getLogger("Benchmark2");
    private static final int BENCH_DOTS = 9000;
    private static final int LAST_NAME = 3;

    @FXML
    private void openFile() {
        Logger log = Logger.getLogger("Canvas");
        Logger bmOneLog = Logger.getLogger("Benchmark1");
        Logger mbTowLog = Logger.getLogger("Benchmark2");
        log.setUseParentHandlers(false);
        try {
            FileHandler handler = new FileHandler(System.getProperty("user.dir") +
                    File.separator + "d2d.txt", true);
            log.addHandler(handler);
            FileHandler benchOneHandler = new FileHandler(System.getProperty("user.dir") +
                    File.separator + "Benchmark1.txt", true);
            bmOneLog.addHandler(benchOneHandler);
            FileHandler benchTwoHandler = new FileHandler(System.getProperty("user.dir") +
                    File.separator + "Benchmark2.txt", true);
            mbTowLog.addHandler(benchTwoHandler);
        } catch (IOException e) {
            log.severe("Logger can't be created");
        }
        fileChooser.getExtensionFilters().addAll(dot);
        picture.clear(canvas);
        try {
            selectedFile = fileChooser.showOpenDialog(null);
            picture.load(selectedFile, log);
        } catch (NullPointerException e) {
            log.severe("Error loading file");
        }
        picture.drawDots(canvas);
        picture.drawLines(canvas);
        txtCurrentTotal.setText(picture.getDotListSize() + "");
    }

    @FXML
    private void close() {
        Platform.exit();
    }

    @FXML
    private void saveFile() {
        fileChooser.setTitle("Save File");
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(dot);
        File file = fileChooser.showSaveDialog(null);
        picture.save(file);
    }

    @FXML
    private void drawDots() {
        canvas.getGraphicsContext2D().clearRect(0, 0, WIDTH, -HEIGHT);
        picture.drawDots(canvas);
    }

    @FXML
    private void drawLines() {
        canvas.getGraphicsContext2D().clearRect(0, 0, WIDTH, -HEIGHT);
        picture.drawLines(canvas);
    }

    @FXML
    private void changeTotal() {
        picture.load(selectedFile, log);
        if (arrayList.isSelected() && rmDots.isSelected()) {
            picture = new Picture(picture, new ArrayList<>());
            picture.removeDots(Integer.parseInt(txtNewTotal.getText()), log);
        } else if (linkedList.isSelected() && rmDots.isSelected()) {
            picture = new Picture(picture, new LinkedList<>());
            picture.removeDots(Integer.parseInt(txtNewTotal.getText()), log);
        } else if (arrayList.isSelected() && rmDotsTwo.isSelected()) {
            picture = new Picture(picture, new ArrayList<>());
            picture.removeDots2(Integer.parseInt(txtNewTotal.getText()), log);
        } else if (linkedList.isSelected() && rmDotsTwo.isSelected()) {
            picture = new Picture(picture, new LinkedList<>());
            picture.removeDots2(Integer.parseInt(txtNewTotal.getText()), log);
        } else {
            picture = new Picture(picture, new ArrayList<>());
            picture.removeDots(Integer.parseInt(txtNewTotal.getText()), log);
        }
        txtTimeTaken.setText(picture.getTime() + "");
        canvas.getGraphicsContext2D().clearRect(0, 0, WIDTH, -HEIGHT);
        picture.drawLines(canvas);
        picture.drawDots(canvas);
        txtCurrentTotal.setText(Integer.toString(picture.getDotListSize()) + "");
    }

    @FXML
    private void benchmarkOne() {
        Picture arrayListPic = new Picture(picture, new ArrayList<>());
        arrayListPic.removeDots(BENCH_DOTS, log);
        bmOneLog.info("Indexed ArrayList:       " + arrayListPic.getTime());
        
        Picture linkedListPic = new Picture(picture, new LinkedList<>());
        linkedListPic.removeDots(BENCH_DOTS, log);
        bmOneLog.info("Indexed LinkedList:      " + linkedListPic.getTime());

        Picture arrayListPic2 = new Picture(picture, new ArrayList<>());
        arrayListPic2.removeDots2(BENCH_DOTS, log);
        bmOneLog.info("Iterated ArrayList:      " + arrayListPic2.getTime());

        Picture linkedListPic2 = new Picture(picture, new LinkedList<>());
        linkedListPic2.removeDots2(BENCH_DOTS, log);
        bmOneLog.info("Iterated LinkedList:     " + linkedListPic2.getTime());

    }

    @FXML
    private void benchmarkTwo() {
        int n = picture.getDotListSize();
        Picture arrayListPic = new Picture(picture, new ArrayList<>());
        arrayListPic.removeDots((n - 1), log);
        bmTwoLog.info("Indexed ArrayList(n - 1):       " + arrayListPic.getTime());

        Picture arrayListPic2 = new Picture(picture, new ArrayList<>());
        arrayListPic2.removeDots(LAST_NAME, log);
        bmTwoLog.info("Indexed ArrayList(3 Dots):      " + arrayListPic2.getTime());

        Picture linkedListPic = new Picture(picture, new ArrayList<>());
        linkedListPic.removeDots((n - 1), log);
        bmTwoLog.info("Indexed LinkedList(n - 1):      " + linkedListPic.getTime());

        Picture linkedListPic2 = new Picture(picture, new LinkedList<>());
        linkedListPic2.removeDots(LAST_NAME, log);
        bmTwoLog.info("Indexed LinkedList(3 Dots):     " + linkedListPic2.getTime());
    }

    /**
     * Initialize is used to set the canvas so that the file loads in
     * and displays on the GUI
     */
    public void initialize() {
        GraphicsContext gContext = canvas.getGraphicsContext2D();
        gContext.scale(WIDTH, HEIGHT);
        gContext.translate(0, -1);
    }
}