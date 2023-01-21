package rht.raytracer.shapes;

import rht.raytracer.Ray;
import rht.raytracer.maths.Vec3;

public class Plane implements ShapeType {

    private final Vec3 centre;
    private final Vec3 normal;

    public Plane(Vec3 centre, Vec3 normal) {
        this.centre = centre;
        this.normal = normal;
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
}
