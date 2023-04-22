use super::vec::Vector;
use std::{
    f64::consts::PI,
    fmt::{self, Display, Formatter},
    ops::Mul,
};

const EPSILON: f64 = 1e-5;

#[derive(Clone, Debug, PartialEq)]
pub struct Matrix<const ROWS: usize, const COLUMNS: usize>(pub [[f64; COLUMNS]; ROWS]);

impl Matrix<4, 4> {
    /// Constructs a transformation matrix to translate by the given offset.
    pub fn translation(x: f64, y: f64, z: f64) -> Self {
        Self([
            [1.0, 0.0, 0.0, x],
            [0.0, 1.0, 0.0, y],
            [0.0, 0.0, 1.0, z],
            [0.0, 0.0, 0.0, 1.0],
        ])
    }

    /// Constructs a transformation matrix to scale about the origin by the given ratios.
    pub fn scale(x: f64, y: f64, z: f64) -> Self {
        Self([
            [x, 0.0, 0.0, 0.0],
            [0.0, y, 0.0, 0.0],
            [0.0, 0.0, z, 0.0],
            [0.0, 0.0, 0.0, 1.0],
        ])
    }

    /// Constructs a transformation matrix to rotate around the X axis by the given number of
    /// degrees.
    pub fn rotateX(degrees: f64) -> Self {
        let radians = degrees * PI / 180.0;
        Self([
            [1.0, 0.0, 0.0, 0.0],
            [0.0, radians.cos(), -radians.sin(), 0.0],
            [0.0, radians.sin(), radians.cos(), 0.0],
            [0.0, 0.0, 0.0, 1.0],
        ])
    }

    /// Constructs a transformation matrix to rotate around the Y axis by the given number of
    /// degrees.
    pub fn rotateY(degrees: f64) -> Self {
        let radians = degrees * PI / 180.0;
        Self([
            [radians.cos(), 0.0, radians.sin(), 0.0],
            [0.0, 1.0, 0.0, 0.0],
            [-radians.sin(), 0.0, radians.cos(), 0.0],
            [0.0, 0.0, 0.0, 1.0],
        ])
    }

    /// Constructs a transformation matrix to rotate around the Z axis by the given number of
    /// degrees.
    pub fn rotateZ(degrees: f64) -> Self {
        let radians = degrees * PI / 180.0;
        Self([
            [radians.cos(), -radians.sin(), 0.0, 0.0],
            [radians.sin(), radians.cos(), 0.0, 0.0],
            [0.0, 0.0, 1.0, 0.0],
            [0.0, 0.0, 0.0, 1.0],
        ])
    }

    /// Multiplies the first 3 rows and columns of this matrix by the given vector, and returns the
    /// result.
    ///
    /// This performs only the linear part of the affine transformation.
    pub fn linear_times(&self, vector: Vector<3>) -> Vector<3> {
        Vector([
            self.0[0][0] * vector.0[0] + self.0[0][1] * vector.0[1] + self.0[0][2] * vector.0[2],
            self.0[1][0] * vector.0[0] + self.0[1][1] * vector.0[1] + self.0[1][2] * vector.0[2],
            self.0[2][0] * vector.0[0] + self.0[2][1] * vector.0[1] + self.0[2][2] * vector.0[2],
        ])
    }
}

impl<const ROWS_A: usize, const COMMON: usize, const COLS_B: usize> Mul<&Matrix<COMMON, COLS_B>>
    for &Matrix<ROWS_A, COMMON>
{
    type Output = Matrix<ROWS_A, COLS_B>;

    fn mul(self, rhs: &Matrix<COMMON, COLS_B>) -> Self::Output {
        let mut result = [[0.0; COLS_B]; ROWS_A];
        for first_row in 0..ROWS_A {
            for second_column in 0..COLS_B {
                let mut sum = 0.0;
                for i in 0..COMMON {
                    sum += self.0[first_row][i] * rhs.0[i][second_column];
                }
                result[first_row][second_column] = sum;
            }
        }
        Matrix(result)
    }
}

impl<const ROWS_A: usize, const COMMON: usize, const COLS_B: usize> Mul<Matrix<COMMON, COLS_B>>
    for Matrix<ROWS_A, COMMON>
{
    type Output = Matrix<ROWS_A, COLS_B>;

    fn mul(self, rhs: Matrix<COMMON, COLS_B>) -> Self::Output {
        &self * &rhs
    }
}

impl Mul<Vector<3>> for &Matrix<4, 4> {
    type Output = Vector<3>;

    fn mul(self, rhs: Vector<3>) -> Self::Output {
        let mut result = [0.0; 3];
        for row in 0..3 {
            result[row] = self.0[row][0] * rhs.0[0]
                + self.0[row][1] * rhs.0[1]
                + self.0[row][2] * rhs.0[2]
                + self.0[row][3];
        }
        Vector(result)
    }
}

impl Mul<Vector<3>> for Matrix<4, 4> {
    type Output = Vector<3>;

    fn mul(self, rhs: Vector<3>) -> Self::Output {
        &self * rhs
    }
}

impl<const ROWS: usize, const COLUMNS: usize> Display for Matrix<ROWS, COLUMNS> {
    fn fmt(&self, f: &mut Formatter) -> fmt::Result {
        for row in &self.0 {
            writeln!(f, "{:?}", row)?;
        }
        Ok(())
    }
}

impl<const SIZE: usize> Matrix<SIZE, SIZE> {
    /// Constructs an identity matrix.
    pub fn identity() -> Self {
        let mut values = [[0.0; SIZE]; SIZE];
        for i in 0..SIZE {
            values[i][i] = 1.0;
        }
        Self(values)
    }

    /// Returns the inverse of the matrix, or `None` if it is not invertible.
    pub fn inverse(&self) -> Option<Self> {
        let mut working = self.0.clone();

        for p in 0..SIZE {
            let pivot = working[p][p];
            if pivot.abs() < EPSILON {
                return None;
            }

            // Update pivot column.
            for i in 0..SIZE {
                working[i][p] /= -pivot;
            }
            // Update other values.
            for i in 0..SIZE {
                for j in 0..SIZE {
                    if i != p && j != p {
                        working[i][j] += working[p][j] * working[i][p];
                    }
                }
            }
            // Update pivot row.
            for j in 0..SIZE {
                working[p][j] /= pivot;
            }
            // Update pivot value.
            working[p][p] = 1.0 / pivot;
        }

        Some(Self(working))
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn assert_matrix_eq<const ROWS: usize, const COLS: usize>(
        a: Matrix<ROWS, COLS>,
        b: Matrix<ROWS, COLS>,
    ) {
        for row in 0..ROWS {
            for col in 0..COLS {
                assert!((a.0[row][col] - b.0[row][col]).abs() < EPSILON);
            }
        }
    }

    #[test]
    fn identity_times() {
        let a = Matrix::identity();
        let b = Matrix([[2.0, 3.0], [4.0, 5.0]]);

        assert_eq!(&a * &b, b);
        assert_eq!(&b * &a, b);
    }

    #[test]
    fn times_twos() {
        let a = Matrix([[1.0, 2.0], [3.0, 4.0]]);
        let b = Matrix([[5.0, 6.0], [7.0, 8.0]]);

        assert_eq!(&a * &b, Matrix([[19.0, 22.0], [43.0, 50.0]]));
        assert_eq!(&b * &a, Matrix([[23.0, 34.0], [31.0, 46.0]]));
    }

    #[test]
    fn invert_two() {
        let a = Matrix([[9.0, 6.0], [5.0, 7.0]]);
        let inverse_a = a.inverse().unwrap();

        assert_matrix_eq(inverse_a * a, Matrix::identity());
    }

    #[test]
    fn invert_four() {
        let a = Matrix([
            [9.0, 6.0, 3.0, 5.0],
            [5.0, 7.0, 3.0, 4.0],
            [2.0, 6.0, 7.0, 1.0],
            [5.0, 8.0, 9.0, 53.0],
        ]);
        let inverse_a = a.inverse().unwrap();

        assert_matrix_eq(inverse_a * a, Matrix::identity());
    }

    #[test]
    fn scale_vector() {
        let scale = Matrix::scale(2.0, -1.0, 0.5);
        let vector = Vector([5.0, 6.0, -7.0]);

        assert_eq!(&scale * vector, Vector([10.0, -6.0, -3.5]));
        assert_eq!(scale * vector, Vector([10.0, -6.0, -3.5]));
    }

    #[test]
    fn translate_vector() {
        let translate = Matrix::translation(2.0, -1.0, 0.5);
        let vector = Vector([5.0, 6.0, -7.0]);

        assert_eq!(&translate * vector, Vector([7.0, 5.0, -6.5]));
        assert_eq!(translate * vector, Vector([7.0, 5.0, -6.5]));
    }

    #[test]
    fn rotate_vector() {
        let rotate = Matrix::rotateX(90.0);
        let vector = Vector([5.0, 6.0, -7.0]);

        assert_eq!(&rotate * vector, Vector([5.0, 7.0, 6.0]));
        assert_eq!(rotate * vector, Vector([5.0, 7.0, 6.0]));
    }
}
