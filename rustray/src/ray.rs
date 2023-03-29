use crate::maths::vec::Vector;

#[derive(Clone, Debug, PartialEq)]
pub struct Ray {
    pub origin: Vector<3>,
    /// Unit vector.
    pub direction: Vector<3>,
}

impl Ray {
    pub fn distance_along(&self, distance: f64) -> Vector<3> {
        &self.origin + &(&self.direction * distance)
    }
}
