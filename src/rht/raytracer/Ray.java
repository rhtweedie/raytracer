package rht.raytracer;

public class Ray {
    private Vec3 origin;
    /** Unit vector. */
    private Vec3 direction;

    public Ray(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction.normalise();
    }

    public Vec3 getOrigin() {
        return this.origin;
    }

    public Vec3 getDirection() {
        return this.direction;
    }

    public Vec3 distanceAlong(double distance) {
        return this.origin.plus(this.direction.times(distance));
    }
}
