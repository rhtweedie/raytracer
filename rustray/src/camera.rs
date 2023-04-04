use crate::{maths::vec::Vector, ray::Ray};

#[derive(Clone, Debug, PartialEq)]
pub struct Camera {
    pub focal_point: Vector<3>,
    pub frame_centre: Vector<3>,
    pub x_direction: Vector<3>,
    pub y_direction: Vector<3>,
}

impl Camera {
    pub fn ray_for_pixel(&self, frame_x: f64, frame_y: f64) -> Ray {
        let frame_point =
            &(&self.frame_centre + &(&self.x_direction * frame_x)) + &(&self.y_direction * frame_y);
        let direction = &frame_point - &self.focal_point;
        Ray::new(&self.focal_point, &direction)
    }
}
