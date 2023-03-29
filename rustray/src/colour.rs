use std::ops::{Add, Mul};

#[derive(Clone, Debug, PartialEq)]
pub struct Colour {
    pub r: f64,
    pub g: f64,
    pub b: f64,
}

impl Colour {
    pub fn new(r: f64, g: f64, b: f64) -> Self {
        Self { r, g, b }
    }
}

impl Add for &Colour {
    type Output = Colour;

    fn add(self, rhs: Self) -> Self::Output {
        Colour {
            r: self.r + rhs.r,
            g: self.g + rhs.g,
            b: self.b + rhs.b,
        }
    }
}

impl Add for Colour {
    type Output = Colour;

    fn add(self, rhs: Self) -> Self::Output {
        &self + &rhs
    }
}

impl Mul for &Colour {
    type Output = Colour;

    fn mul(self, rhs: Self) -> Self::Output {
        Colour {
            r: self.r * rhs.r,
            g: self.g * rhs.g,
            b: self.b * rhs.b,
        }
    }
}

impl Mul for Colour {
    type Output = Colour;

    fn mul(self, rhs: Self) -> Self::Output {
        &self * &rhs
    }
}

impl Mul<f64> for &Colour {
    type Output = Colour;

    fn mul(self, rhs: f64) -> Self::Output {
        Colour {
            r: self.r * rhs,
            g: self.g * rhs,
            b: self.b * rhs,
        }
    }
}

impl Mul<f64> for Colour {
    type Output = Colour;

    fn mul(self, rhs: f64) -> Self::Output {
        &self * rhs
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn add() {
        let a = Colour::new(1.0, 0.5, 0.0);
        let b = Colour::new(0.1, 0.3, 0.6);
        assert_eq!(&a + &b, Colour::new(1.1, 0.8, 0.6));
        assert_eq!(a + b, Colour::new(1.1, 0.8, 0.6));
    }

    #[test]
    fn mul() {
        let a = Colour::new(1.0, 0.5, 0.0);
        let b = Colour::new(0.1, 0.3, 0.6);
        assert_eq!(&a * &b, Colour::new(0.1, 0.15, 0.0));
        assert_eq!(a * b, Colour::new(0.1, 0.15, 0.0));
    }

    #[test]
    fn scalar_multiply() {
        let a = Colour::new(1.0, 0.5, 0.0);
        assert_eq!(&a * 0.5, Colour::new(0.5, 0.25, 0.0));
        assert_eq!(a * 0.5, Colour::new(0.5, 0.25, 0.0));
    }
}
