package rht.raytracer.shapes;

import rht.raytracer.Ray;
import rht.raytracer.Vec3;

public class Sphere implements ShapeType {

    private final Vec3 centre;
    private final double radius;

    public Sphere(Vec3 centre, double radius) {
        this.centre = centre;
        this.radius = radius;
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
        double distance = -ray.getDirection().dot(v) - Math.sqrt(discriminant);
        if (distance >= 0) {
            return distance;
        } else {
            return null;
        }
    }

    /**
     * Returns the normal vector on the surface of a sphere at a given point.
     * 
     * @param pointOnSurface The point to find the normal at
     */
    public Vec3 normalAtPoint(Vec3 pointOnSurface) {
        return pointOnSurface.minus(centre).normalise();
    }
}
