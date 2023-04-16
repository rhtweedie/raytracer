use crate::{colour::Colour, maths::vec::Vector, ray::Ray, shapes::Shape};
use std::{fmt::Debug, ptr};

const BRIGHTNESS_CORRECTION_FACTOR: f64 = 40.0;
const RECURSION_LIMIT: u32 = 10;

#[derive(Debug)]
pub struct Scene {
    pub objects: Vec<Object>,
    pub lights: Vec<Light>,
}

impl Scene {
    pub fn colour_for_ray(&self, ray: &Ray) -> Colour {
        self.colour_for_ray_internal(ray, RECURSION_LIMIT, None)
    }

    fn colour_for_ray_internal(
        &self,
        ray: &Ray,
        recursion_limit: u32,
        ignored: Option<&Object>,
    ) -> Colour {
        let Some((closest, distance)) = self.first_intersection(ray, ignored) else {
            return Colour::BLACK;
        };

        let intersection_point = ray.distance_along(distance);
        let normal = closest.shape.normal_at(&intersection_point);

        // Find colour from lights.
        let mut total_incident_light = Colour::BLACK;
        for light in &self.lights {
            let direction_to_light = &light.position - &intersection_point;
            let light_distance = direction_to_light.length();
            let unit_direction_to_light = direction_to_light.normalise();
            let dot_product = &normal * &unit_direction_to_light;

            // Check whether some other object is between us and the light.
            let first_object_towards_light = self.first_intersection(
                &Ray::new(&intersection_point, &unit_direction_to_light),
                Some(closest),
            );

            if dot_product > 0.0
                && first_object_towards_light
                    .map(|(_, distance)| distance)
                    .unwrap_or(f64::INFINITY)
                    > light_distance
            {
                total_incident_light = total_incident_light
                    + &light.colour
                        * (dot_product * BRIGHTNESS_CORRECTION_FACTOR
                            / (light_distance * light_distance));
            }
        }

        let reflected_colour = if recursion_limit > 0 && closest.reflection_colour != Colour::BLACK
        {
            // Find colour from reflection.
            let reflected_ray = reflect_at(&ray.direction, &intersection_point, &normal);
            self.colour_for_ray_internal(&reflected_ray, recursion_limit - 1, Some(closest))
        } else {
            Colour::BLACK
        };

        &closest.colour * &total_incident_light + &closest.reflection_colour * &reflected_colour
    }

    fn first_intersection<'a>(
        &'a self,
        ray: &Ray,
        ignored: Option<&Object>,
    ) -> Option<(&'a Object, f64)> {
        let mut closest: Option<(&Object, f64)> = None;
        for (object, distance) in self.objects.iter().filter_map(|object| {
            if ignored.is_some() && ptr::eq(object, ignored.unwrap()) {
                None
            } else {
                let distance = object.shape.intersect(ray)?;
                Some((object, distance))
            }
        }) {
            if closest.is_none() || distance < closest.unwrap().1 {
                closest = Some((object, distance));
            }
        }
        closest
    }
}

#[derive(Debug)]
pub struct Object {
    pub shape: Box<dyn Shape>,
    pub colour: Colour,
    pub reflection_colour: Colour,
}

#[derive(Clone, Debug, PartialEq)]
pub struct Light {
    pub position: Vector<3>,
    pub colour: Colour,
}

/// Find the reflected ray at a point on the surface of a shape about the normal.
fn reflect_at(
    incident_direction: &Vector<3>,
    intersection_point: &Vector<3>,
    surface_normal: &Vector<3>,
) -> Ray {
    let reflected_direction =
        incident_direction - &(surface_normal * (2.0 * (surface_normal * incident_direction)));
    Ray::new(intersection_point, &reflected_direction)
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::shapes::sphere::Sphere;

    #[test]
    fn colour_for_ray_with_sphere() {
        let scene = Scene {
            objects: vec![Object {
                shape: Box::new(Sphere {
                    centre: Vector([2.0, 0.0, 0.0]),
                    radius: 1.0,
                }),
                colour: Colour::new(1.0, 1.0, 0.0),
                reflection_colour: Colour::BLACK,
            }],
            lights: vec![Light {
                position: Vector([0.0, 0.0, 0.0]),
                colour: Colour::new(1.0, 0.0, 1.0),
            }],
        };

        let ray = Ray {
            origin: Vector([0.0, 0.0, 0.0]),
            direction: Vector([1.0, 0.0, 0.0]),
        };
        assert_eq!(scene.colour_for_ray(&ray), Colour::new(40.0, 0.0, 0.0));
    }
}
