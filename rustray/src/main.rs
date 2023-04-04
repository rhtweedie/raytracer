mod colour;
mod maths;
mod ray;
mod scene;
mod shapes;

use crate::{
    camera::Camera,
    colour::Colour,
    maths::vec::Vector,
    scene::{Light, Object, Scene},
    shapes::sphere::Sphere,
};

fn main() {
    let scene = Scene {
        objects: vec![Object {
            shape: Box::new(Sphere {
                centre: Vector([-1.0, -1.0, 5.0]),
                radius: 1.0,
            }),
            colour: Colour::new(1.0, 0.9, 0.9),
            reflection_colour: Colour::new(0.2, 0.2, 0.2),
        }],
        lights: vec![
            Light {
                position: Vector([0.0, -5.0, -5.0]),
                colour: Colour::WHITE,
            },
            Light {
                position: Vector([-1.0, 0.7, 1.0]),
                colour: Colour::new(0.2, 0.15, 0.2),
            },
        ],
    };
}
