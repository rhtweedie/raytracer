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

    public String toString() {
        String s = "";
        for (double[] row : values) {
            s += Arrays.toString(row) + "\n";
        }
        return s;
    }

    public Matrix times(Matrix other) {
        int firstRows = values.length;
        int firstColumns = values[0].length;
        int secondRows = other.values.length;
        int secondColumns = other.values[0].length;
        if (firstColumns != secondRows) {
            throw new IllegalArgumentException(
                    "Number of rows in second matrix must equal number of columns in first matrix.");
        }

        double[][] result = new double[firstRows][secondColumns];
        for (int firstRow = 0; firstRow < firstRows; ++firstRow) {
            for (int secondColumn = 0; secondColumn < secondColumns; ++secondColumn) {
                double sum = 0.0;
                for (int i = 0; i < firstColumns; ++i) {
                    sum += values[firstRow][i] * other.values[i][secondColumn];
                }
                result[firstRow][secondColumn] = sum;
            }
        }

        return new Matrix(result);
    }

    public Matrix inverse() {
        return null;
    }
}
