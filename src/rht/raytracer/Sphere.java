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

    /**
     * Returns the distance at which the given ray intersects the object, or null if
     * it never does.
     */
    public Double intersect(Ray ray) {
        Vec3 v = ray.getOrigin().minus(centre);
        double dirDotV = ray.getDirection().dot(v);
        double discriminant = dirDotV * dirDotV - v.squared() + radius * radius;
        if (discriminant < 0) {
            return null;
        }
        return -ray.getDirection().dot(v) - Math.sqrt(discriminant);
    }
}
