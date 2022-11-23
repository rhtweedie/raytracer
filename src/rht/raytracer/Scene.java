package rht.raytracer;

import java.util.List;

public class Scene {
    private List<Sphere> objects;

    public Scene(List<Sphere> objects) {
        this.objects = objects;
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
        } else {
            double brightness = Math.pow(0.9, closestIntersection);
            return closest.getColour().times(brightness);
        }
    }
}
