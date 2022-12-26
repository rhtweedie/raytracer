package rht.raytracer.shapes;

import rht.raytracer.Colour;

public class Shape {

    private final ShapeType shape;
    private final Colour colour;

    public Shape(ShapeType shape, Colour colour) {
        this.shape = shape;
        this.colour = colour;
    }

    public ShapeType getShapeType() {
        return shape;
    }

    public Colour getColour() {
        return colour;
    }
}
