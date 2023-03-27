package rht.raytracer.shapes;

import rht.raytracer.Ray;
import rht.raytracer.maths.Matrix;
import rht.raytracer.maths.Vec3;

public class Transformed implements ShapeType {
    private final ShapeType shape;
    private final Matrix transformation;
    private final Matrix inverseTransformation;

    public Transformed(ShapeType shape, Matrix transformation) {
        this.shape = shape;
        this.transformation = transformation;
        this.inverseTransformation = transformation.inverse();
    }

    @Override
    public Double intersect(Ray ray) {
        Ray transformedRay = ray.transform(inverseTransformation);
        Double transformedIntersection = shape.intersect(transformedRay);
        if (transformedIntersection == null) {
            return null;
        }

        // To transform the distance back to the outer co-ordinate system, we find the
        // point of intersection, transform that back, and then find the distance from
        // the original ray's origin.
        Vec3 transformedIntersectionPoint = transformedRay.distanceAlong(transformedIntersection);
        Vec3 intersectionPoint = transformation.times(transformedIntersectionPoint);
        return intersectionPoint.minus(ray.getOrigin()).length();
    }

    @Override
    public Vec3 normalAtPoint(Vec3 pointOnSurface) {
        Vec3 transformedPoint = inverseTransformation.times(pointOnSurface);
        Vec3 transformedNormal = shape.normalAtPoint(transformedPoint);
        return transformation.linearTimes(transformedNormal).normalise();
    }
}
