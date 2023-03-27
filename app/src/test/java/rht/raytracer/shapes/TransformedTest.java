package rht.raytracer.shapes;

import static org.junit.Assert.assertEquals;

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
}
