package rht.raytracer;

public class Sphere {
    private final Vec3 centre;
    private final double radius;
    private final Colour colour;

    public Sphere(Vec3 centre, double radius, Colour colour) {
        this.centre = centre;
        this.radius = radius;
        this.colour = colour;
    }
}
