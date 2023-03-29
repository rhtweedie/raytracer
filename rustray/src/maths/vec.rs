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

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn length() {
        let v = Vector([3.0, 4.0, 0.0]);
        assert_eq!(v.length(), 5.0);
    }
}
