use std::ops::{Add, Div, Mul, Sub};

#[derive(Clone, Debug, PartialEq)]
pub struct Vector<const LENGTH: usize>(pub [f64; LENGTH]);

impl<const LENGTH: usize> Vector<LENGTH> {
    pub fn squared(&self) -> f64 {
        self.0.iter().map(|e| e * e).sum()
    }

    pub fn length(&self) -> f64 {
        self.squared().sqrt()
    }

    /// Returns a unit vector pointing in the same direction as this one.
    pub fn normalise(&self) -> Self {
        self / self.length()
    }
}

impl<const LENGTH: usize> Add for &Vector<LENGTH> {
    type Output = Vector<LENGTH>;

    fn add(self, rhs: Self) -> Self::Output {
        let mut result = self.0.clone();
        for i in 0..LENGTH {
            result[i] += rhs.0[i];
        }
        Vector(result)
    }
}

impl<const LENGTH: usize> Add for Vector<LENGTH> {
    type Output = Vector<LENGTH>;

    fn add(self, rhs: Self) -> Self::Output {
        &self + &rhs
    }
}

impl<const LENGTH: usize> Sub for &Vector<LENGTH> {
    type Output = Vector<LENGTH>;

    fn sub(self, rhs: Self) -> Self::Output {
        let mut result = self.0.clone();
        for i in 0..LENGTH {
            result[i] -= rhs.0[i];
        }
        Vector(result)
    }
}

impl<const LENGTH: usize> Sub for Vector<LENGTH> {
    type Output = Vector<LENGTH>;

    fn sub(self, rhs: Self) -> Self::Output {
        &self - &rhs
    }
}

impl<const LENGTH: usize> Mul for &Vector<LENGTH> {
    type Output = f64;

    fn mul(self, rhs: Self) -> Self::Output {
        let mut result = 0.0;
        for i in 0..LENGTH {
            result += self.0[i] * rhs.0[i];
        }
        result
    }
}

impl<const LENGTH: usize> Mul for Vector<LENGTH> {
    type Output = f64;

    fn mul(self, rhs: Self) -> Self::Output {
        &self * &rhs
    }
}

impl<const LENGTH: usize> Mul<f64> for &Vector<LENGTH> {
    type Output = Vector<LENGTH>;

    fn mul(self, rhs: f64) -> Self::Output {
        let mut result = self.0.clone();
        for i in 0..LENGTH {
            result[i] *= rhs;
        }
        Vector(result)
    }
}

impl<const LENGTH: usize> Mul<f64> for Vector<LENGTH> {
    type Output = Vector<LENGTH>;

    fn mul(self, rhs: f64) -> Self::Output {
        &self * rhs
    }
}

impl<const LENGTH: usize> Div<f64> for &Vector<LENGTH> {
    type Output = Vector<LENGTH>;

    fn div(self, rhs: f64) -> Self::Output {
        let mut result = self.0.clone();
        for i in 0..LENGTH {
            result[i] /= rhs;
        }
        Vector(result)
    }
}

impl<const LENGTH: usize> Div<f64> for Vector<LENGTH> {
    type Output = Vector<LENGTH>;

    fn div(self, rhs: f64) -> Self::Output {
        &self / rhs
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn length() {
        let v = Vector([3.0, 4.0, 0.0]);
        assert_eq!(v.length(), 5.0);
    }

    #[test]
    fn add() {
        let a = Vector([3.0, 4.0, 0.0]);
        let b = Vector([5.0, -4.0, -1.0]);
        assert_eq!(&a + &b, Vector([8.0, 0.0, -1.0]));
        assert_eq!(a + b, Vector([8.0, 0.0, -1.0]));
    }

    #[test]
    fn sub() {
        let a = Vector([3.0, 4.0, 0.0]);
        let b = Vector([5.0, -4.0, -1.0]);
        assert_eq!(&a - &b, Vector([-2.0, 8.0, 1.0]));
        assert_eq!(a - b, Vector([-2.0, 8.0, 1.0]));
    }

    #[test]
    fn dot() {
        let a = Vector([3.0, 4.0, 0.0]);
        let b = Vector([5.0, -4.0, -1.0]);
        assert_eq!(&a * &b, -1.0);
        assert_eq!(a * b, -1.0);
    }

    #[test]
    fn scalar_multiply() {
        let v = Vector([5.0, -4.0, -1.0]);
        assert_eq!(&v * 3.0, Vector([15.0, -12.0, -3.0]));
        assert_eq!(v * 3.0, Vector([15.0, -12.0, -3.0]));
    }

    #[test]
    fn scalar_divide() {
        let v = Vector([5.0, -4.0, -1.0]);
        assert_eq!(&v / 2.0, Vector([2.5, -2.0, -0.5]));
        assert_eq!(v / 2.0, Vector([2.5, -2.0, -0.5]));
    }

    #[test]
    fn normalise() {
        let v = Vector([3.0, -4.0, 0.0]);
        assert_eq!(v.normalise(), Vector([0.6, -0.8, 0.0]));
    }
}
