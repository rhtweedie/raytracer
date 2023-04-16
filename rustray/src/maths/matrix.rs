use std::{
    f64::consts::PI,
    fmt::{self, Display, Formatter},
};

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
}

impl<const ROWS: usize, const COLUMNS: usize> Display for Matrix<ROWS, COLUMNS> {
    fn fmt(&self, f: &mut Formatter) -> fmt::Result {
        for row in &self.0 {
            writeln!(f, "{:?}", row)?;
        }
        Ok(())
    }
}
