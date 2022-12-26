package rht.raytracer.shapes;

import rht.raytracer.Colour;
import rht.raytracer.Ray;
import rht.raytracer.Vec3;

public interface Shape {

    /**
     * Returns the distance at which the given ray intersects the object, or null if
     * it never does.
     */
    public Double intersect(Ray ray);

    /**
     * Returns the normal vector on the surface of a sphere at a given point.
     * 
     * @param pointOnSurface The point to find the normal at
     */
    public Vec3 normalAtPoint(Vec3 pointOnSurface);

    public Colour getColour();

}
