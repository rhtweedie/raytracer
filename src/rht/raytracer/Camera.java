package rht.raytracer;

public class Camera {
    private final Vec3 focalPoint;
    private final Vec3 frameCentre;
    private final Vec3 xDirection;
    private final Vec3 yDirection;

    public Camera(Vec3 focalPoint, Vec3 frameCentre, Vec3 xDirection, Vec3 yDirection) {
        this.focalPoint = focalPoint;
        this.frameCentre = frameCentre;
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    public Ray rayForPixel(double frameX, double frameY) {
        Vec3 framePoint = frameCentre.plus(xDirection.times(frameX)).plus(yDirection.times(frameY));
        Vec3 direction = framePoint.minus(focalPoint);
        return new Ray(focalPoint, direction);
    }
}
