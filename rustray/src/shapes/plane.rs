use crate::{maths::vec::Vector, ray::Ray, shapes::Shape};

#[derive(Clone, Debug, PartialEq)]
pub struct Plane {
    pub centre: Vector<3>,
    pub normal: Vector<3>,
}

impl Shape for Plane {
    fn intersect(&self, ray: &Ray) -> Option<f64> {
        let denominator = self.normal * ray.direction;
        let numerator = self.normal * (self.centre - ray.origin);
        if denominator == 0.0 {
            return None;
        }
        let distance = numerator / denominator;

        if distance < 0.0 {
            None
        } else {
            Some(distance)
        }
    }

    fn normal_at(&self, _point: Vector<3>) -> Vector<3> {
        self.normal
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn intersect() {
        let plane = Plane {
            centre: Vector([0.0, 0.0, 0.0]),
            normal: Vector([0.0, -1.0, 0.0]),
        };

        let distance_perpendicular =
            plane.intersect(&Ray::new(Vector([0.0, -5.0, 0.0]), Vector([0.0, 1.0, 0.0])));
        assert_eq!(distance_perpendicular, Some(5.0));

        let distance_parallel =
            plane.intersect(&Ray::new(Vector([1.0, -1.0, 2.0]), Vector([1.0, 0.0, 0.0])));
        assert_eq!(distance_parallel, None);

        let distance_away = plane.intersect(&Ray::new(
            Vector([1.0, -1.0, 2.0]),
            Vector([1.0, -1.0, -1.0]),
        ));
        assert_eq!(distance_away, None);
    }
}
