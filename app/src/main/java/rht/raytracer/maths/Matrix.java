package rht.raytracer.maths;

import java.util.Arrays;

public class Matrix {
    private final double[][] values;

    public Matrix(double[][] values) {
        this.values = values;
    }

    public boolean equals(Object other) {
        if (other instanceof Matrix) {
            Matrix otherMatrix = (Matrix) other;
            return Arrays.deepEquals(values, otherMatrix.values);
        } else {
            return false;
        }
    }
}
