package rht.raytracer;

import java.util.List;

public class Scene {
    private List<Sphere> objects;
    private List<Light> lights;

    public Scene(List<Sphere> objects, List<Light> lights) {
        this.objects = objects;
        this.lights = lights;
    }

    /**
     * Returns the first object in the scene which the given ray intersects, or null
     * if none.
     */
    public Colour colourForRay(Ray ray) {
        Double closestIntersection = null;
        Sphere closest = null;

        for (Sphere object : objects) {
            Double distance = object.intersect(ray);
            if (distance != null) {
                if (closestIntersection == null || distance < closestIntersection) {
                    closestIntersection = distance;
                    closest = object;
                }
            }
        }

        if (closest == null) {
            return new Colour(0, 0, 0);
        }

        Vec3 intersectionPoint = ray.distanceAlong(closestIntersection);
        Vec3 normal = closest.normalAtPoint(intersectionPoint);
        Colour totalIncidentLight = new Colour(0, 0, 0);
        for (Light light : lights) {
            Vec3 directionToLight = light.getPosition().minus(intersectionPoint);
            double lightDistance = directionToLight.length();
            Vec3 unitDirectionToLight = directionToLight.normalise();
            Colour incidentLight = light.getColour()
                    .times(normal.dot(unitDirectionToLight) / (lightDistance * lightDistance));
            totalIncidentLight = totalIncidentLight.plus(incidentLight);
        }
        return closest.getColour().times(totalIncidentLight);

    }
}
