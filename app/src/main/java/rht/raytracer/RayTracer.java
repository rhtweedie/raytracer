package rht.raytracer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rht.raytracer.maths.Vec3;
import rht.raytracer.shapes.Plane;
import rht.raytracer.shapes.Shape;
import rht.raytracer.shapes.Sphere;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class RayTracer extends JPanel {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final Scene scene;
    private final Camera camera;

    public RayTracer(Scene scene, Camera camera) {
        this.scene = scene;
        this.camera = camera;
        ImageIcon icon = new ImageIcon(image);
        add(new JLabel(icon));
    }

    public static void main(String[] args) {
        List<Shape> objects = new ArrayList<>();
        List<Light> lights = new ArrayList<>();
        // Medium spheres
        objects.add(new Shape(new Sphere(new Vec3(-1.0, -1.0, 5.0), 1.0), new Colour(1.0, 0.9, 0.9),
                new Colour(0.2, 0.2, 0.2)));
        objects.add(new Shape(new Sphere(new Vec3(0.5, 0.5, 0.0), 0.5), new Colour(1.0, 0.5, 0.0)));
        // Little spheres
        for (double x = -1.6; x <= -0.5; x += 0.3) {
            for (double z = 0.0; z <= 2.0; z += 0.3) {
                objects.add(new Shape(new Sphere(new Vec3(x, 0.5, z), 0.04), new Colour(0.5, 0.5, 1.0)));
            }
        }
        // Bottom plane
        objects.add(
                new Shape(new Plane(new Vec3(0.0, 1.02, 0.0), new Vec3(0.0, -1.0, 0.0)), new Colour(0.8, 0.6, 0.8),
                        new Colour(0.7, 0.7, 0.7)));
        // Right plane
        objects.add(
                new Shape(new Plane(new Vec3(1.5, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0)),
                        Colour.WHITE));
        // Back plane
        objects.add(new Shape(new Plane(new Vec3(0.0, 0.0, 10.0), new Vec3(0.0, 0.0, -1.0)), Colour.WHITE));
        // Lights
        lights.add(new Light(new Vec3(0.0, -5.0, -5.0), Colour.WHITE));
        lights.add(new Light(new Vec3(-1.0, 0.7, 1.0), new Colour(0.2, 0.15, 0.2)));
        Scene scene = new Scene(objects, lights);

        Camera camera = new Camera(new Vec3(0.0, 0.0, -5.0), new Vec3(0.0, 0.0, -2.0), new Vec3(1.0, 0.0, 0.0),
                new Vec3(0.0, 1.0, 0.0));

        JFrame frame = new JFrame("Ray tracer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RayTracer rayTracer = new RayTracer(scene, camera);
        rayTracer.render();
        frame.add(rayTracer);
        frame.pack();
        frame.setVisible(true);

    }

    private void render() {
        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                Ray ray = camera.rayForPixel(x * 2.0 / WIDTH - 1.0, y * 2.0 / HEIGHT - 1.0);
                Colour rayColour = scene.colourForRay(ray);
                image.setRGB(x, y, rayColour.toRGBInt());
            }
        }
    }
}
