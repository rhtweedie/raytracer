package rht.raytracer.maths;

import java.util.Arrays;

public class Matrix {
    private static double EPSILON = 1e-5;

    final double[][] values;

    public Matrix(double[][] values) {
        this.values = values;
    }

    /**
     * Constructs a transformation matrix to translate by the given offset.
     */
    public static Matrix translation(double x, double y, double z) {
        return null;
    }

    /**
     * Constructs a transformation matrix to scale about the origin by the given
     * ratios.
     */
    public static Matrix scale(double x, double y, double z) {
        return new Matrix(new double[][] {
                new double[] { x, 0, 0 },
                new double[] { 0, y, 0 },
                new double[] { 0, 0, z } });
    }

    /**
     * Constructs a transformation matrix to rotate around the X axis by the given
     * number of degrees.
     */
    public static Matrix rotateX(double degrees) {
        double radians = degrees * Math.PI / 180;
        return new Matrix(new double[][] {
                new double[] { 1, 0, 0 },
                new double[] { 0, Math.cos(radians), -Math.sin(radians) },
                new double[] { 0, Math.sin(radians), Math.cos(radians) } });

    }

    /**
     * Constructs a transformation matrix to rotate around the Y axis by the given
     * number of degrees.
     */
    public static Matrix rotateY(double degrees) {
        double radians = degrees * Math.PI / 180;
        return new Matrix(new double[][] {
                new double[] { Math.cos(radians), 0, Math.sin(radians) },
                new double[] { 0, 1, 0 },
                new double[] { -Math.sin(radians), 0, Math.cos(radians) } });
    }

    /**
     * Constructs a transformation matrix to rotate around the Z axis by the given
     * number of degrees.
     */
    public static Matrix rotateZ(double degrees) {
        double radians = degrees * Math.PI / 180;
        return new Matrix(new double[][] {
                new double[] { Math.cos(radians), -Math.sin(radians), 0 },
                new double[] { Math.sin(radians), Math.cos(radians), 0 },
                new double[] { 0, 0, 1 } });
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

    /**
     * Multiplies this matrix by the given vector, and returns the result.
     */
    public Vec3 times(Vec3 vector) {
        int firstRows = values.length;
        int firstColumns = values[0].length;
        if (firstColumns != 3 || firstRows != 3) {
            throw new IllegalArgumentException(
                    "Number of rows in second matrix and number of columns in first matrix must be three.");
        }

        double[] result = new double[firstRows];
        for (int firstRow = 0; firstRow < firstRows; ++firstRow) {
            double sum = (values[firstRow][0] * vector.x) + (values[firstRow][1] * vector.y)
                    + (values[firstRow][2] * vector.z);
            result[firstRow] = sum;
        }

        return new Vec3(result[0], result[1], result[2]);
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
