package rht.raytracer;

import java.util.List;

import rht.raytracer.shapes.Shape;

public class Scene {
    private static final double BRIGHTNESS_CORRECTION_FACTOR = 40.0;
    private static final int RECURSION_LIMIT = 10;

    private final List<Shape> objects;
    private final List<Light> lights;

    public Scene(List<Shape> objects, List<Light> lights) {
        this.objects = objects;
        this.lights = lights;
    }

    /**
     * Returns the colour of the first object in the scene which a ray intersects,
     * or null if none.
     */
    public Colour colourForRay(Ray ray) {
        return colourForRay(ray, RECURSION_LIMIT, null);
    }

    /**
     * Returns the colour of the first object in the scene which a ray intersects,
     * or null if none.
     * 
     * @param ray            The ray along which to find the intersection.
     * @param recursionLimit The maximum number of reflections.
     * @param ignored        A shape to be ignored.
     * @return The colour of the ray, considering shape colour, lights, and
     *         reflectivity.
     */
    public Colour colourForRay(Ray ray, int recursionLimit, Shape ignored) {

        ObjectAndDistance closest = findFirstIntersectionExcept(ray, ignored);
        if (closest == null) {
            return new Colour(0, 0, 0);
        }

        Vec3 intersectionPoint = ray.distanceAlong(closest.distance);
        Vec3 normal = closest.object.getShapeType().normalAtPoint(intersectionPoint);

        // Find colour from lights.
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

        Colour reflectedColour;
        if (recursionLimit > 0 && !closest.object.getReflectionColour().equals(Colour.BLACK)) {
            // Find colour from reflection.
            Ray reflectedRay = reflectAt(ray.getDirection(), intersectionPoint, normal);
            reflectedColour = colourForRay(reflectedRay, recursionLimit - 1, closest.object);
        } else {
            reflectedColour = Colour.BLACK;
        }

        // Calculate total colour.
        return closest.object.getColour().times(totalIncidentLight)
                .plus(reflectedColour.times(closest.object.getReflectionColour()));
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

    /**
     * Find the reflected ray at a point on the surface of a shape about the normal.
     *
     * @param incidentDirection The direction of the incident ray.
     * @param intersectionPoint The point of intersection of the ray and the surface
     *                          of the shape.
     * @param surfaceNormal     The normal at the point of intersection.
     * @return The reflected ray.
     */
    private static Ray reflectAt(Vec3 incidentDirection, Vec3 intersectionPoint, Vec3 surfaceNormal) {
        Vec3 reflectedDirection = incidentDirection
                .minus(surfaceNormal.times(2 * surfaceNormal.dot(incidentDirection)));
        return new Ray(intersectionPoint, reflectedDirection);
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
