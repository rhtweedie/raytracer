package rht.raytracer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SphereTest {

    @Test
    public void testIntersect() {
        Sphere sphere = new Sphere(new Vec3(0, 0, 0), 1, new Colour(1, 1, 1));

        double distancePerpendicular = sphere.intersect(new Ray(new Vec3(0, -5, 0), new Vec3(0, 1, 0)));
        assertEquals(4, distancePerpendicular, 0.000001);

        Double distanceParallel = sphere.intersect((new Ray(new Vec3(0, -5, 0), new Vec3(1, 0, 0))));
        assertEquals(null, distanceParallel);

        Double distanceAway = sphere.intersect((new Ray(new Vec3(0, -5, 0), new Vec3(1, -1, -1))));
        assertEquals(null, distanceAway);

        Double distanceInside = sphere.intersect((new Ray(new Vec3(0, 0, 0), new Vec3(1, -1, -1))));
        assertEquals(null, distanceInside);

    }

    @Test
    public void testNormalAtPoint() {
        Sphere sphere = new Sphere(new Vec3(0, 0, 0), 1, new Colour(1, 1, 1));

        Vec3 normalPerpendicular = sphere.normalAtPoint(new Vec3(0, 0, 1));
        assertEquals(new Vec3(0, 0, 1), normalPerpendicular);
    }
}
