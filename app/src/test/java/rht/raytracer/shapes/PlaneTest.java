package rht.raytracer.shapes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import rht.raytracer.Ray;
import rht.raytracer.Vec3;

public class PlaneTest {

    @Test
    public void testIntersect() {
        Plane plane = new Plane(new Vec3(0, 0, 0), new Vec3(0, -1, 0));

        double distancePerpendicular = plane.intersect(new Ray(new Vec3(0, -5, 0), new Vec3(0, 1, 0)));
        assertEquals(5, distancePerpendicular, 0.000001);

        Double distanceParallel = plane.intersect((new Ray(new Vec3(1, -1, 2), new Vec3(1, 0, 0))));
        assertEquals(null, distanceParallel);

        Double distanceAway = plane.intersect((new Ray(new Vec3(1, -1, 2), new Vec3(1, -1, -1))));
        assertEquals(null, distanceAway);

    }
}
