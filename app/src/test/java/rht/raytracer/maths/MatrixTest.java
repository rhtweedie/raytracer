package rht.raytracer.maths;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatrixTest {
    @Test
    public void identityTimes() {
        Matrix a = new Matrix(new double[][] { new double[] { 1, 0 }, new double[] { 0, 1 } });
        Matrix b = new Matrix(new double[][] { new double[] { 2, 3 }, new double[] { 4, 5 } });

        assertEquals(a.times(b), b);
        assertEquals(b.times(a), b);
    }

    @Test
    public void identityTimesTwos() {
        Matrix a = new Matrix(new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix b = new Matrix(new double[][] { new double[] { 5, 6 }, new double[] { 7, 8 } });

        Matrix expectedAB = new Matrix(new double[][] { new double[] { 19, 22 }, new double[] { 43, 50 } });
        Matrix expectedBA = new Matrix(new double[][] { new double[] { 23, 34 }, new double[] { 31, 46 } });

        assertEquals(a.times(b), expectedAB);
        assertEquals(b.times(a), expectedBA);
    }
}
