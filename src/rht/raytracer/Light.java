package rht.raytracer;

public class Light {

    private final Colour colour;
    private final Vec3 position;

    public Light(Vec3 position, Colour colour) {
        this.position = position;
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public Vec3 getPosition() {
        return position;
    }

}
