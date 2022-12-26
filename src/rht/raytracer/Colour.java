package rht.raytracer;

public class Colour {
    public static final Colour BLACK = new Colour(0.0, 0.0, 0.0);
    public static final Colour WHITE = new Colour(1.0, 1.0, 1.0);

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

    public Colour times(double num) {
        return new Colour(r * num, g * num, b * num);
    }

    public Colour times(Colour other) {
        return new Colour(r * other.r, g * other.g, b * other.b);
    }

    public Colour plus(Colour other) {
        return new Colour(r + other.r, g + other.g, b + other.b);
    }

    private static double capChannel(double x) {
        return Math.max(0.0, Math.min(1.0, x));
    }

}
