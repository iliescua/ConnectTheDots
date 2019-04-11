/*
* CS2852
* Fall 2018
* Lab 1 - Picture Class
* Created: 9/9/2018
* Updated: 9/25/18
*/
package iliescua;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class holds them methods that allow the file to be read
 * in as well as housing the computations so dot and line know
 * how to be drawn properly
 */
public class Picture {
    private List<Dot> dotList;
    private static final double DOT_WIDTH = 0.015;
    private static final double DOT_HEIGHT = 0.015;
    private static final double LINE_WIDTH = 0.002;
    private static final double CANVAS_HEIGHT = 400;
    private static final double CANVAS_WIDTH = 500;
    private long startTime;
    private long endTime;
    private String time;

    /**
     * Default constructor that initializes emptyList
     * @param emptyList arrayList that keeps track of the dot changes
     */
    public Picture(List<Dot> emptyList) {
        dotList = emptyList;
    }

    /**
     * Second constructor that takes in the emptyList and picture
     * @param original  The original version of the image
     * @param emptyList arrayList that keeps track of the dot changes
     */
    public Picture(Picture original, List<Dot> emptyList) {
        for(int i = 0; i < original.dotList.size(); i++) {
            emptyList.add(original.dotList.get(i));
        }
        dotList = emptyList;
    }

    /**
     * This method is used to read in the file line by line
     * and extract the information from it
     * @param file the file being passed in by the user
     * @param log  the logger to keep track of what happens
     */
    public void load(File file, Logger log) {
        dotList.clear();
        try {
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                List<String> dotLine;
                dotLine = Arrays.asList(in.nextLine().split(","));
                if (dotLine.size() != 2) {
                    throw new IllegalAccessException("Format isn't correct");
                } else {
                    Dot dot = new Dot(Float.parseFloat(dotLine.get(0)),
                            Float.parseFloat(dotLine.get(1)));
                    dotList.add(dot);
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("No File", "Please select a correct file");
            log.severe("No File");
        } catch (IllegalAccessException e) {
            showAlert("Format is Wrong", "Please correct file format");
            log.severe("Not formatted properly");
        }
    }

    /**
     * This method is used to tell the controller how to draw the dots
     * @param canvas this is what the dots are getting drawn on
     */
    public void drawDots(Canvas canvas) {
        for (int i = 0; i < dotList.size(); i++) {
            canvas.getGraphicsContext2D().fillOval(dotList.get(i).getX(), dotList.get(i).getY(),
                    DOT_WIDTH, DOT_HEIGHT);
        }
    }

    /**
     * This method is ued to tell the controller how to draw the lines
     * @param canvas this is what the lines are getting drawn on
     */
    public void drawLines(Canvas canvas) {
        GraphicsContext gContext = canvas.getGraphicsContext2D();
        gContext.setFill(Color.BLACK);
        gContext.setLineWidth(LINE_WIDTH);
        gContext.setStroke(Color.BLACK);
        for (int i = 0; i < dotList.size() - 1; i++) {
            gContext.beginPath();
            double xOne = dotList.get(i).getX();
            double xTwo = dotList.get(i + 1).getX();
            double yOne = dotList.get(i).getY();
            double yTwo = dotList.get(i + 1).getY();

            gContext.moveTo(xOne, yOne);
            gContext.lineTo(xTwo, yTwo);
            gContext.closePath();
            gContext.stroke();
        }
    }

    /**
     * This method is used to clear the canvas of any preexisting information
     * @param canvas this is what the image is getting drawn on
     */
    public void clear(Canvas canvas) {
        canvas.getGraphicsContext2D().clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    /**
     * Saves the file that was altered by the GUI
     * @param file file that is passed in
     */
    public void save(File file) {
        try {
            FileWriter fWriter = new FileWriter(file);
            for (int i = 0; i < dotList.size(); i++) {
                String xPos = Float.toString(dotList.get(i).getX());
                String yPos = Float.toString(dotList.get(i).getY());
                fWriter.write(String.format(xPos + " , " + yPos));
                fWriter.write(System.lineSeparator());
            }
            fWriter.close();
        } catch (IOException e) {
            showAlert("Error Saving", "Unable to save file");
        }
    }

    /**
     * This method is used to remove the dots from the image
     * @param numberDesired the number of dots the user wants left
     * @param log keeps track of what happens
     */
    public void removeDots(int numberDesired, Logger log) {
        startTime = System.nanoTime();
        try {
            if (numberDesired < 3) {
                throw new IllegalArgumentException("Desired number too small");
            } else if (numberDesired > dotList.size()) {
                throw new IllegalArgumentException("Desired number too large");
            } else {
                int numDots = dotList.size() - numberDesired;
                for (int i = 0; i < numDots; i++) {
                    double leastCrit = Integer.MAX_VALUE;
                    int leastCritIndex = 0;

                    for (int j = 1; j < dotList.size() - 1; j++) {
                        double crit = dotList.get(j).claculateCriticalValue(dotList.get(j - 1),
                                dotList.get(j + 1));
                        if (crit < leastCrit) {
                            leastCrit = crit;
                            leastCritIndex = j;
                        }
                    }
                    dotList.remove(leastCritIndex);
                }
            }
        } catch (IllegalArgumentException e) {
            showAlert("Number Error", "Desired number incorrect");
        }
        endTime = System.nanoTime();
        timeConvert();
    }

    public int getDotListSize() {
        return dotList.size();
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * This is the second way to remove dots
     * @param numberDesired the number the person wants left
     * @param log the logger keeps track of what happened
     */
    public void removeDots2(int numberDesired, Logger log) {
        startTime = System.nanoTime();
        Collection<Dot> list = dotList;
        int loopLength = list.size() - numberDesired;

        Iterator<Dot> iter;
        Dot previous;
        Dot current;
        Dot next;
        Dot lowCritDot = null;

        if (numberDesired <= 3) {
            throw new IllegalArgumentException("The number desired is less than 3");
        } else if (list.size() < numberDesired) {
            log.severe("Desired number invalid");
        } else {
            for (int i = 0; i < loopLength; i++) {
                double currentCritical;
                double lowCritValue = Integer.MAX_VALUE;
                iter = list.iterator();
                previous = iter.next();
                current = iter.next();
                next = iter.next();

                for (int j = 1; j < list.size() - 1; j++) {
                    currentCritical = current.claculateCriticalValue(previous, next);
                    if (currentCritical < lowCritValue) {
                        lowCritValue = currentCritical;
                        lowCritDot = current;
                    }
                    previous = current;
                    current = next;

                    if (!(iter.hasNext())) {
                        iter = list.iterator();
                        next = iter.next();
                        currentCritical = current.claculateCriticalValue(previous, next);

                        if (currentCritical < lowCritValue) {
                            lowCritValue = currentCritical;
                            lowCritDot = current;
                        }
                    } else {
                        next = iter.next();
                    }
                }
                list.remove(lowCritDot);
            }
        }
        endTime = System.nanoTime();
        timeConvert();
    }

    private void timeConvert() {
        long nanoTime = endTime - startTime;
        long hours = TimeUnit.HOURS.convert(nanoTime, TimeUnit.NANOSECONDS);
        nanoTime -= TimeUnit.NANOSECONDS.convert(hours, TimeUnit.HOURS);
        long minutes = TimeUnit.MINUTES.convert(nanoTime, TimeUnit.NANOSECONDS);
        nanoTime -= TimeUnit.NANOSECONDS.convert(minutes, TimeUnit.MINUTES);
        long seconds = TimeUnit.SECONDS.convert(nanoTime, TimeUnit.NANOSECONDS);
        nanoTime -= TimeUnit.NANOSECONDS.convert(seconds, TimeUnit.SECONDS);
        long milliseconds = TimeUnit.MILLISECONDS.convert(nanoTime, TimeUnit.NANOSECONDS);

        time = ((String.format("%02d : %02d : %02d : %03d",
                hours, minutes, seconds, milliseconds)));
    }

    public String getTime(){
        return time;
    }
}