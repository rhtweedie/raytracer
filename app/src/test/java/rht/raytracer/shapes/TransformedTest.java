package rht.raytracer.shapes;

import static org.junit.Assert.assertEquals;
import static rht.raytracer.maths.AssertHelpers.assertVecEquals;

import org.junit.Test;

import rht.raytracer.Ray;
import rht.raytracer.maths.Matrix;
import rht.raytracer.maths.Vec3;

public class TransformedTest {
    private static final double EPSILON = 1e-5;

    @Test
    public void testTranslatedSphere() {
        Sphere sphere = new Sphere(new Vec3(0, 0, 0), 1);
        Matrix transformation = Matrix.translation(1, 2, 0);
        Transformed transformedSphere = new Transformed(sphere, transformation);

        Ray ray = new Ray(new Vec3(1, -1, 0), new Vec3(0, 1, 0));
        double intersectionDistance = transformedSphere.intersect(ray);
        assertEquals(intersectionDistance, 2.0, EPSILON);
    }

    @Test
    public void testRotatedPlane() {
        Plane plane = new Plane(new Vec3(0, 0, 0), new Vec3(0, 1, 0));
        Transformed rotatedPlane = new Transformed(plane, Matrix.rotateZ(45));

        Ray ray = new Ray(new Vec3(-1, 1, 0), new Vec3(0, -1, 0));
        double intersectionDistance = rotatedPlane.intersect(ray);
        assertEquals(intersectionDistance, 2.0, EPSILON);

        Vec3 intersectionPoint = ray.distanceAlong(intersectionDistance);
        Vec3 normal = rotatedPlane.normalAtPoint(intersectionPoint);
        assertVecEquals(normal, new Vec3(-1, 1, 0).normalise(), EPSILON);
    }
}
