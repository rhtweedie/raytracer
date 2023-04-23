use crate::{maths::vec::Vector, ray::Ray, shapes::Shape};

#[derive(Clone, Debug, PartialEq)]
pub struct Sphere {
    pub centre: Vector<3>,
    pub radius: f64,
}

impl Shape for Sphere {
    fn intersect(&self, ray: &Ray) -> Option<f64> {
        let v = ray.origin - self.centre;
        let dir_dot_v = ray.direction * v;
        let discriminant = dir_dot_v * dir_dot_v - v.squared() + self.radius * self.radius;
        if discriminant < 0.0 {
            return None;
        }
        let distance = -(ray.direction * v) - discriminant.sqrt();
        if distance >= 0.0 {
            Some(distance)
        } else {
            None
        }
    }

    fn normal_at(&self, point: Vector<3>) -> Vector<3> {
        (point - self.centre).normalise()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn intersect() {
        let sphere = Sphere {
            centre: Vector([0.0, 0.0, 0.0]),
            radius: 1.0,
        };

        let distance_perpendicular =
            sphere.intersect(&Ray::new(Vector([0.0, -5.0, 0.0]), Vector([0.0, 1.0, 0.0])));
        assert_eq!(distance_perpendicular, Some(4.0));

        let distance_parallel =
            sphere.intersect(&Ray::new(Vector([0.0, -5.0, 0.0]), Vector([1.0, 0.0, 0.0])));
        assert_eq!(distance_parallel, None);

        let distance_away = sphere.intersect(&Ray::new(
            Vector([0.0, -5.0, 0.0]),
            Vector([1.0, -1.0, -1.0]),
        ));
        assert_eq!(distance_away, None);

        let distance_inside = sphere.intersect(&Ray::new(
            Vector([0.0, 0.0, 0.0]),
            Vector([1.0, -1.0, -1.0]),
        ));
        assert_eq!(distance_inside, None);
    }

    #[test]
    fn normal_at_point() {
        let sphere = Sphere {
            centre: Vector([0.0, 0.0, 0.0]),
            radius: 2.0,
        };

        let normal = sphere.normal_at(Vector([0.0, 0.0, 2.0]));
        assert_eq!(normal, Vector([0.0, 0.0, 1.0]));
    }
}
