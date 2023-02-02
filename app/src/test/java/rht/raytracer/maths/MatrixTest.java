package rht.raytracer.maths;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatrixTest {
    private static final double EPSILON = 1e-5;

    @Test
    public void identityTimes() {
        Matrix a = new Matrix(new double[][] { new double[] { 1, 0 }, new double[] { 0, 1 } });
        Matrix b = new Matrix(new double[][] { new double[] { 2, 3 }, new double[] { 4, 5 } });

        assertEquals(b, a.times(b));
        assertEquals(b, b.times(a));
    }

    @Test
    public void identityTimesTwos() {
        Matrix a = new Matrix(new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix b = new Matrix(new double[][] { new double[] { 5, 6 }, new double[] { 7, 8 } });

        Matrix expectedAB = new Matrix(new double[][] { new double[] { 19, 22 }, new double[] { 43, 50 } });
        Matrix expectedBA = new Matrix(new double[][] { new double[] { 23, 34 }, new double[] { 31, 46 } });

        assertEquals(expectedAB, a.times(b));
        assertEquals(expectedBA, b.times(a));
    }

    @Test
    public void inverse() {
        Matrix a = new Matrix(new double[][] { new double[] { 9, 6 }, new double[] { 5, 7 } });
        Matrix inverseA = a.inverse();
        Matrix identity = new Matrix(new double[][] { new double[] { 1, 0 }, new double[] { 0, 1 } });

        assertMatrixEquals(identity, inverseA.times(a));
    }

    @Test
    public void scaleVector() {
        Matrix scale = Matrix.scale(2.0, -1.0, 0.5);
        Vec3 vector = new Vec3(5.0, 6.0, -7.0);

        assertVecEquals(new Vec3(10.0, -6.0, -3.5), scale.times(vector));
    }

    @Test
    public void translateVector() {
        Matrix scale = Matrix.translation(2.0, -1.0, 0.5);
        Vec3 vector = new Vec3(5.0, 6.0, -7.0);

        assertVecEquals(new Vec3(7.0, 5.0, -6.5), scale.times(vector));
    }

    @Test
    public void rotateVector() {
        Matrix scale = Matrix.rotateX(90);
        Vec3 vector = new Vec3(5.0, 6.0, -7.0);

        assertVecEquals(new Vec3(5.0, 7.0, 6.0), scale.times(vector));

    }

    private void assertMatrixEquals(Matrix expected, Matrix actual) {
        assertEquals(expected.values.length, actual.values.length);
        for (int i = 0; i < expected.values.length; ++i) {
            assertEquals(expected.values[i].length, actual.values[i].length);
            for (int j = 0; j < expected.values[i].length; ++j) {
                assertEquals(expected.values[i][j], actual.values[i][j], EPSILON);
            }
        }
    }

    private void assertVecEquals(Vec3 expected, Vec3 actual) {
        assertEquals(expected.x, actual.x, EPSILON);
        assertEquals(expected.y, actual.y, EPSILON);
        assertEquals(expected.z, actual.z, EPSILON);
    }
}
