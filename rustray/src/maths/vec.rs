use std::ops::{Add, Sub};

#[derive(Clone, Debug, PartialEq)]
pub struct Vector<const LENGTH: usize>([f64; LENGTH]);

impl<const LENGTH: usize> Vector<LENGTH> {
    pub fn squared(&self) -> f64 {
        self.0.iter().map(|e| e * e).sum()
    }

    pub fn length(&self) -> f64 {
        self.squared().sqrt()
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
}
