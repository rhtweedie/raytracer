package rht.raytracer;

import java.util.List;

public class Scene {
    private static final double BRIGHTNESS_CORRECTION_FACTOR = 40.0;

    private final List<Shape> objects;
    private final List<Light> lights;

    public Scene(List<Shape> objects, List<Light> lights) {
        this.objects = objects;
        this.lights = lights;
    }

    /**
     * Returns the first object in the scene which the given ray intersects, or null
     * if none.
     */
    public Colour colourForRay(Ray ray) {

        ObjectAndDistance closest = findFirstIntersection(ray);
        if (closest == null) {
            return new Colour(0, 0, 0);
        }

        Vec3 intersectionPoint = ray.distanceAlong(closest.distance);
        Vec3 normal = closest.object.normalAtPoint(intersectionPoint);
        Colour totalIncidentLight = new Colour(0, 0, 0);
        for (Light light : lights) {
            Vec3 directionToLight = light.getPosition().minus(intersectionPoint);
            double lightDistance = directionToLight.length();
            Vec3 unitDirectionToLight = directionToLight.normalise();
            double dotProduct = normal.dot(unitDirectionToLight);

            // Check whether some other object is between us and the light.
            ObjectAndDistance firstObjectTowardsLight = findFirstIntersection(
                    new Ray(intersectionPoint, unitDirectionToLight));

            if (dotProduct > 0.0
                    && (firstObjectTowardsLight == null || firstObjectTowardsLight.distance > lightDistance)) {
                Colour incidentLight = light.getColour()
                        .times(dotProduct * BRIGHTNESS_CORRECTION_FACTOR
                                / (lightDistance * lightDistance));
                totalIncidentLight = totalIncidentLight.plus(incidentLight);
            }
        }
        return closest.object.getColour().times(totalIncidentLight);

    }

    private ObjectAndDistance findFirstIntersection(Ray ray) {

        Double closestIntersection = null;
        Shape closest = null;

        for (Shape object : objects) {
            Double distance = object.intersect(ray);
            if (distance != null) {
                if (closestIntersection == null || distance < closestIntersection) {
                    closestIntersection = distance;
                    closest = object;
                }
            }
        }

        if (closest == null) {
            return null;
        } else {
            return new ObjectAndDistance(closest, closestIntersection);
        }
    }
}

class ObjectAndDistance {
    final Shape object;
    final double distance;

    public ObjectAndDistance(Shape object, double distance) {
        this.object = object;
        this.distance = distance;
    }
}
