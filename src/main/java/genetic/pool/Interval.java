package genetic.pool;

public class Interval {

    private double min;
    private double max;

    public Interval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(Object o) {
        // Check if a number is in the interval
        if (o instanceof Double) {
            double n = (double) o;
            return this.min <= n && n < this.max;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + this.min + ", " + this.max + "[";
    }
}
