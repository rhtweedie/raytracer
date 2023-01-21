package rht.raytracer.maths;

public class Vec3 {
    public double x;
    public double y;
    public double z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Object other) {
        if (other instanceof Vec3) {
            Vec3 otherVec = (Vec3) other;
            return x == otherVec.x && y == otherVec.y && z == otherVec.z;
        } else {
            return false;
        }
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

    public double squared() {
        return x * x + y * y + z * z;
    }

    public double length() {
        return Math.sqrt(squared());
    }

    /** Returns a unit vector pointing in the same direction as this one. */
    public Vec3 normalise() {
        return divide(length());
    }

    public double dot(Vec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }
}
