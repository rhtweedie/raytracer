package rht.raytracer.maths;

import java.util.Arrays;

public class Matrix {
    private static double EPSILON = 1e-5;

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
        double[][] working = deepCopy(values);

        double determinant = 1.0;
        for (int p = 0; p < working.length; ++p) {
            double pivot = working[p][p];
            if (Math.abs(pivot) < EPSILON) {
                throw new IllegalArgumentException("Matrix is not invertible.");
            }

            determinant *= pivot;

            // Update pivot column
            for (int i = 0; i < working.length; ++i) {
                working[i][p] /= -pivot;
            }
            // Update other values
            for (int i = 0; i < working.length; ++i) {
                for (int j = 0; j < working.length; ++j) {
                    if (i != p && j != p) {
                        working[i][j] += working[p][j] * working[i][p];
                    }
                }
            }
            // Update pivot row
            for (int j = 0; j < working.length; ++j) {
                working[p][j] /= pivot;
            }
            // Update pivot value
            working[p][p] = 1 / pivot;
        }

        return new Matrix(working);
    }

    private static double[][] deepCopy(double[][] values) {
        double[][] copy = new double[values.length][];
        for (int i = 0; i < values.length; ++i) {
            copy[i] = values[i].clone();
        }
        return copy;
    }
}
