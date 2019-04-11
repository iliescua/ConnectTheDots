/*
* CS2852
* Fall 2018
* Lab 1 - Dot Class
* Created: 9/9/2018
* Updated: 9/17/18
*/
package iliescua;

/**
 * This class is used to create a dot and provide
 * the x and y coordinates of the dots
 */
public class Dot {
    private float x = 0;
    private float y = 0;

    /**
     * Default constructor to initialize the x and y
     * values of the dots
     * @param x x-coordinate of dot
     * @param y y-coordinate of dot
     */
    public Dot(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /**
     * Calculates the critical value of a dot
     * @param previous 1st dot
     * @param next     2nd dot
     * @return the critical value of the dot inbetween
     */
    public double claculateCriticalValue(Dot previous, Dot next) {
        double dist1to2 = Math.hypot(x - previous.getX(), y - previous.getY());
        double dist2to3 = Math.hypot(next.getX() - x, next.getY() - y);
        double dist1to3 = Math.hypot(next.getX() - previous.getX(), next.getY() - previous.getY());
        return (dist1to2 + dist2to3 - dist1to3);
    }
}