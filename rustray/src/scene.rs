use crate::{colour::Colour, maths::vec::Vector, shapes::Shape};
use std::fmt::Debug;

#[derive(Debug)]
pub struct Object {
    pub shape: Box<dyn Shape>,
    pub colour: Colour,
    pub relection_colour: Colour,
}

#[derive(Clone, Debug, PartialEq)]
pub struct Light {
    pub position: Vector<3>,
    pub colour: Colour,
}
