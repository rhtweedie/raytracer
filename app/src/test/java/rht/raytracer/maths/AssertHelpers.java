package rht.raytracer.maths;

import static org.junit.Assert.assertEquals;

public class AssertHelpers {
    public static void assertMatrixEquals(Matrix expected, Matrix actual, double epsilon) {
        assertEquals(expected.values.length, actual.values.length);
        for (int i = 0; i < expected.values.length; ++i) {
            assertEquals(expected.values[i].length, actual.values[i].length);
            for (int j = 0; j < expected.values[i].length; ++j) {
                assertEquals(expected.values[i][j], actual.values[i][j], epsilon);
            }
        }
    }

    public static void assertVecEquals(Vec3 expected, Vec3 actual, double epsilon) {
        assertEquals(expected.x, actual.x, epsilon);
        assertEquals(expected.y, actual.y, epsilon);
        assertEquals(expected.z, actual.z, epsilon);
    }

}
