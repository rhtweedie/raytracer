use crate::{maths::vec::Vector, ray::Ray, shapes::Shape};

#[derive(Clone, Debug, PartialEq)]
pub struct Sphere {
    pub centre: Vector<3>,
    pub radius: f64,
}

impl Shape for Sphere {
    fn intersect(&self, ray: &Ray) -> Option<f64> {
        let v = &ray.origin - &self.centre;
        let dir_dot_v = &ray.direction * &v;
        let discriminant = dir_dot_v * dir_dot_v - v.squared() + self.radius * self.radius;
        if discriminant < 0.0 {
            return None;
        }
        let distance = -(&ray.direction * &v) - discriminant.sqrt();
        if distance >= 0.0 {
            Some(distance)
        } else {
            None
        }
    }

    fn normal_at(&self, point: &Vector<3>) -> Vector<3> {
        (point - &self.centre).normalise()
    }
}
