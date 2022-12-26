package rht.raytracer;

import java.util.List;

import rht.raytracer.shapes.Shape;

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

        ObjectAndDistance closest = findFirstIntersectionExcept(ray, null);
        if (closest == null) {
            return new Colour(0, 0, 0);
        }

        Vec3 intersectionPoint = ray.distanceAlong(closest.distance);
        Vec3 normal = closest.object.getShapeType().normalAtPoint(intersectionPoint);
        Colour totalIncidentLight = new Colour(0, 0, 0);
        for (Light light : lights) {
            Vec3 directionToLight = light.getPosition().minus(intersectionPoint);
            double lightDistance = directionToLight.length();
            Vec3 unitDirectionToLight = directionToLight.normalise();
            double dotProduct = normal.dot(unitDirectionToLight);

            // Check whether some other object is between us and the light.
            ObjectAndDistance firstObjectTowardsLight = findFirstIntersectionExcept(
                    new Ray(intersectionPoint, unitDirectionToLight), closest.object);

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

    /**
     * Loops through all objects in the scene (except for `ignored`) to find the
     * first one that the ray hits.
     * 
     * @param ray     The ray along which to find an intersection.
     * @param ignored An object to ignore when looking for intersections, or null
     *                for none.
     * @return the object and the distance along the ray to it, or null if the ray
     *         doesn't hit any objects.
     */
    private ObjectAndDistance findFirstIntersectionExcept(Ray ray, Shape ignored) {
        Double closestIntersection = null;
        Shape closest = null;

        for (Shape object : objects) {
            if (object == ignored) {
                continue;
            }
            Double distance = object.getShapeType().intersect(ray);
            if (distance != null &&
                    (closestIntersection == null || distance < closestIntersection)) {
                closestIntersection = distance;
                closest = object;

            }
        }

        if (closest == null)

        {
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
