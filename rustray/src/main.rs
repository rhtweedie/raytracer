mod camera;
mod colour;
mod maths;
mod ray;
mod scene;
mod shapes;

use crate::{
    camera::Camera,
    colour::Colour,
    maths::{matrix::Matrix, vec::Vector},
    scene::{Light, Object, Scene},
    shapes::{plane::Plane, sphere::Sphere, transformed::Transformed},
};
use png::{BitDepth, ColorType, Encoder};
use std::{env, fs::File, io::BufWriter, process::exit};

const WIDTH: u32 = 1000;
const HEIGHT: u32 = 1000;

fn main() {
    let args = env::args().collect::<Vec<_>>();
    if args.len() != 2 {
        eprintln!("Usage:");
        eprintln!("  {} <output.png>", args[0]);
        exit(1);
    }

    let scene = Scene {
        objects: vec![
            Object {
                shape: Box::new(Sphere {
                    centre: Vector([-1.0, -1.0, 5.0]),
                    radius: 1.0,
                }),
                colour: Colour::new(1.0, 0.9, 0.9),
                reflection_colour: Colour::new(0.2, 0.2, 0.2),
            },
            Object {
                shape: Box::new(Transformed::new(
                    Sphere {
                        centre: Vector([0.0, 0.0, 0.0]),
                        radius: 0.5,
                    },
                    Matrix::translation(0.5, -0.5, 0.0) * Matrix::scale(1.0, 3.0, 1.0),
                )),
                colour: Colour::new(1.0, 0.5, 0.0),
                reflection_colour: Colour::BLACK,
            },
            Object {
                shape: Box::new(Plane {
                    centre: Vector([0.0, 1.02, 0.0]),
                    normal: Vector([0.0, -1.0, 0.0]),
                }),
                colour: Colour::new(0.8, 0.6, 0.8),
                reflection_colour: Colour::new(0.7, 0.7, 0.7),
            },
            Object {
                shape: Box::new(Plane {
                    centre: Vector([1.5, 0.0, 0.0]),
                    normal: Vector([-1.0, 0.0, 0.0]),
                }),
                colour: Colour::WHITE,
                reflection_colour: Colour::BLACK,
            },
        ],
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
    write_png(&args[1], WIDTH, HEIGHT, &image);
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

fn write_png(filename: &str, width: u32, height: u32, image: &[u8]) {
    let file = File::create(filename).expect("Failed to open output file");
    let writer = BufWriter::new(file);
    let mut encoder = Encoder::new(writer, width, height);
    encoder.set_color(ColorType::Rgb);
    encoder.set_depth(BitDepth::Eight);
    let mut writer = encoder.write_header().expect("Failed to write header");
    writer
        .write_image_data(image)
        .expect("Failed to write image data");
}
