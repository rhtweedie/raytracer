use crate::{
    maths::{matrix::Matrix, vec::Vector},
    ray::Ray,
};

use super::Shape;

#[derive(Debug)]
pub struct Transformed {
    shape: Box<dyn Shape>,
    transformation: Matrix<4, 4>,
    inverse_transformation: Matrix<4, 4>,
}

impl Transformed {
    pub fn new(shape: impl Shape + 'static, transformation: Matrix<4, 4>) -> Self {
        let inverse_transformation = transformation.inverse().unwrap();
        Self {
            shape: Box::new(shape),
            transformation,
            inverse_transformation,
        }
    }
}

impl Shape for Transformed {
    fn intersect(&self, ray: &Ray) -> Option<f64> {
        let transformed_ray = ray.transform(&self.inverse_transformation);
        let tranformed_intersection = self.shape.intersect(&transformed_ray)?;

        // To transform the distance back to the outer co-ordinate system, we find the point of
        // intersection, transform that back, and then find the distance from the original ray's
        // origin.
        let transformed_intersection_point =
            transformed_ray.distance_along(tranformed_intersection);
        let intersection_point = &self.transformation * transformed_intersection_point;
        Some((intersection_point - ray.origin).length())
    }

    fn normal_at(&self, point: Vector<3>) -> Vector<3> {
        let transformed_point = &self.inverse_transformation * point;
        let transformed_normal = self.shape.normal_at(transformed_point);
        self.transformation
            .linear_times(transformed_normal)
            .normalise()
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::shapes::{plane::Plane, sphere::Sphere};

    const EPSILON: f64 = 1e-5;

    fn assert_vector_eq<const SIZE: usize>(a: Vector<SIZE>, b: Vector<SIZE>) {
        println!("{:?}={:?}", a, b);
        for i in 0..SIZE {
            assert!((a.0[i] - b.0[i]).abs() < EPSILON);
        }
    }

    #[test]
    fn translated_sphere() {
        let sphere = Sphere {
            centre: Vector([0.0, 0.0, 0.0]),
            radius: 1.0,
        };
        let transformation = Matrix::translation(1.0, 2.0, 0.0);
        let transformed_sphere = Transformed::new(sphere, transformation);

        let ray = Ray::new(Vector([1.0, -1.0, 0.0]), Vector([0.0, 1.0, 0.0]));
        let intersection_distance = transformed_sphere.intersect(&ray);
        assert_eq!(intersection_distance, Some(2.0));

        let intersection_point = ray.distance_along(intersection_distance.unwrap());
        let normal = transformed_sphere.normal_at(intersection_point);
        assert_eq!(normal, Vector([0.0, -1.0, 0.0]));
    }

    #[test]
    fn rotated_plane() {
        let plane = Plane {
            centre: Vector([0.0, 0.0, 0.0]),
            normal: Vector([0.0, 1.0, 0.0]),
        };
        let rotated_plane = Transformed::new(plane, Matrix::rotateZ(45.0));

        let ray = Ray::new(Vector([-1.0, 1.0, 0.0]), Vector([0.0, -1.0, 0.0]));
        let intersection_distance = rotated_plane.intersect(&ray).unwrap();
        assert!((intersection_distance - 2.0).abs() < EPSILON);

        let intersection_point = ray.distance_along(intersection_distance);
        let normal = rotated_plane.normal_at(intersection_point);
        assert_vector_eq(normal, Vector([-1.0, 1.0, 0.0]).normalise());
    }
}
