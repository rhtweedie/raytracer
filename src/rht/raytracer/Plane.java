package rht.raytracer;

public class Plane implements Shape {

    private final Vec3 centre;
    private final Vec3 normal;
    private final Colour colour;

    public Plane(Vec3 centre, Vec3 normal, Colour colour) {
        this.centre = centre;
        this.normal = normal;
        this.colour = colour;
    }

    @Override
    public Double intersect(Ray ray) {
        double denominator = normal.dot(ray.getDirection());
        double numerator = normal.dot(centre.minus(ray.getOrigin()));
        if (denominator == 0) {
            return null;
        }
        double distance = numerator / denominator;

        if (distance < 0) {
            return null;
        }

        return distance;
    }

    @Override
    public Vec3 normalAtPoint(Vec3 pointOnSurface) {
        return normal;
    }

    @Override
    public Colour getColour() {
        return colour;
    }

}
