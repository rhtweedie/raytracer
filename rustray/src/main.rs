mod camera;
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

const WIDTH: u32 = 1000;
const HEIGHT: u32 = 1000;

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
    let camera = Camera {
        focal_point: Vector([0.0, 0.0, -5.0]),
        frame_centre: Vector([0.0, 0.0, -2.0]),
        x_direction: Vector([1.0, 0.0, 0.0]),
        y_direction: Vector([0.0, 1.0, 0.0]),
    };
    let image = render(&scene, &camera, WIDTH, HEIGHT);
}

fn render(scene: &Scene, camera: &Camera, width: u32, height: u32) -> Vec<u8> {
    let mut image = Vec::new();
    for y in 0..height {
        for x in 0..width {
            let ray = camera.ray_for_pixel(
                x as f64 * 2.0 / width as f64 - 1.0,
                y as f64 * 2.0 / height as f64 - 1.0,
            );
            let pixel = scene.colour_for_ray(&ray);
            image.extend(pixel.as_rgb());
        }
    }
    image
}
