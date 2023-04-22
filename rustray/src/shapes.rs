pub mod plane;
pub mod sphere;
pub mod transformed;

use crate::{maths::vec::Vector, ray::Ray};
use std::fmt::Debug;

pub trait Shape: Debug {
    /// Returns the distance at which the given ray intersects the object, or `None` if it never
    /// does.
    fn intersect(&self, ray: &Ray) -> Option<f64>;

    /// Returns the normal vector at the given point on the surface of the shape.
    fn normal_at(&self, point: Vector<3>) -> Vector<3>;
}
