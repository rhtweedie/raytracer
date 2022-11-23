package rht.raytracer;

public class Colour {
    public double r;
    public double g;
    public double b;

    public Colour(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int toRGBInt() {
        int r = (int) (capChannel(this.r) * 255);
        int g = (int) (capChannel(this.g) * 255);
        int b = (int) (capChannel(this.b) * 255);
        return r << 16 | g << 8 | b;
    }

    private static double capChannel(double x) {
        return Math.max(0.0, Math.min(1.0, x));
    }
}
