package rht.raytracer;

public class Vec3 {
    public double x;
    public double y;
    public double z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 times(double scalar) {
        return new Vec3(x * scalar, y * scalar, z * scalar);
    }

    public Vec3 divide(double scalar) {
        return new Vec3(x / scalar, y / scalar, z / scalar);
    }

    public Vec3 plus(Vec3 other) {
        return new Vec3(x + other.x, y + other.y, z + other.z);
    }

    public Vec3 minus(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /** Returns a unit vector pointing in the same direction as this one. */
    public Vec3 normalise() {
        return divide(length());
    }
}
