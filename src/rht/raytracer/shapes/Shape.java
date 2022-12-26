package rht.raytracer.shapes;

import rht.raytracer.Colour;

public class Shape {

    private final ShapeType shape;
    private final Colour colour;
    private final Colour reflectionColour;

    public Shape(ShapeType shape, Colour colour, Colour reflectionColour) {
        this.shape = shape;
        this.colour = colour;
        this.reflectionColour = reflectionColour;
    }

    public Shape(ShapeType shape, Colour colour) {
        this(shape, colour, Colour.BLACK);
    }

    public ShapeType getShapeType() {
        return shape;
    }

    public Colour getColour() {
        return colour;
    }

    public Colour getReflectionColour() {
        return reflectionColour;
    }
}
