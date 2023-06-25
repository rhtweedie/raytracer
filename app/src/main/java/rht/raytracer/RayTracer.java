package rht.raytracer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.yaml.snakeyaml.Yaml;

import rht.raytracer.maths.Matrix;
import rht.raytracer.maths.Vec3;
import rht.raytracer.shapes.Plane;
import rht.raytracer.shapes.Shape;
import rht.raytracer.shapes.ShapeType;
import rht.raytracer.shapes.Sphere;
import rht.raytracer.shapes.Transformed;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if (args.length != 1) {
            System.err.println("Usage:");
            System.err.println("  RayTracer <Scene.yaml>");
            System.exit(1);
        }

        RayTracer rayTracer;
        try {
            rayTracer = RayTracer.loadYAML(args[0]);
        } catch (FileNotFoundException e) {
            System.err.println(args[0] + " not found.");
            System.exit(2);
            return;
        }

        JFrame frame = new JFrame("Ray tracer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rayTracer.render();
        frame.add(rayTracer);
        frame.pack();
        frame.setVisible(true);
    }

    private static RayTracer loadYAML(String filename) throws FileNotFoundException {
        List<Shape> objects = new ArrayList<>();
        List<Light> lights = new ArrayList<>();

        Yaml yaml = new Yaml();
        InputStream file = new FileInputStream(filename);
        Map<String, Object> contents = yaml.load(file);
        List<Object> yamlObjects = (List<Object>) contents.get("objects");
        List<Object> yamlLights = (List<Object>) contents.get("lights");
        Map<String, Object> yamlCamera = (Map<String, Object>) contents.get("camera");
        System.out.println("Objects: " + yamlObjects);
        System.out.println("Lights: " + yamlLights);
        System.out.println("Camera: " + yamlCamera);

        for (Object o : yamlObjects) {
            Map<String, Object> object = (Map<String, Object>) o;
            String shapeName = (String) object.get("shape");
            ShapeType shapeType;
            switch (shapeName) {
                case "Sphere":
                    Vec3 centre = arrayToVec3((List<Object>) object.get("centre"));
                    double radius = toDouble(object.get("radius"));
                    shapeType = new Sphere(centre, radius);
                    break;
                case "Plane":
                    Vec3 planeCentre = arrayToVec3((List<Object>) object.get("centre"));
                    Vec3 planeNormal = arrayToVec3((List<Object>) object.get("normal"));
                    shapeType = new Plane(planeNormal, planeNormal);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected shape " + shapeName);
            }
            Colour colour = arrayToColour((List<Object>) object.get("colour"));
            objects.add(new Shape(shapeType, colour));
        }

        for (Object l : yamlLights) {
            Map<String, Object> light = (Map<String, Object>) l;
            List<Object> lightPositionVec = (List<Object>) light.get("position");
            Vec3 lightPosition = arrayToVec3(lightPositionVec);
            Colour lightColour = arrayToColour((List<Object>) light.get("colour"));
            lights.add(new Light(lightPosition, lightColour));
        }
        Scene scene = new Scene(objects, lights);

        Vec3 camPosition = arrayToVec3((List<Object>) yamlCamera.get("position"));
        Vec3 camFrameCentre = arrayToVec3((List<Object>) yamlCamera.get("frame_centre"));
        Vec3 camXDir = arrayToVec3((List<Object>) yamlCamera.get("x_direction"));
        Vec3 camYDir = arrayToVec3((List<Object>) yamlCamera.get("y_direction"));
        Camera camera = new Camera(camPosition, camFrameCentre, camXDir, camYDir);

        return new RayTracer(scene, camera);
    }

    private static double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else {
            throw new IllegalArgumentException("Input must be of type integer or a float.");
        }

    }

    private static Vec3 arrayToVec3(List<Object> arrayToConvert) {
        if (arrayToConvert.size() != 3) {
            throw new IllegalArgumentException(
                    "array has unexpected length " + arrayToConvert.size());
        }
        return new Vec3(toDouble(arrayToConvert.get(0)), toDouble(arrayToConvert.get(1)),
                toDouble(arrayToConvert.get(2)));
    }

    private static Colour arrayToColour(List<Object> arrayToConvert) {
        if (arrayToConvert.size() != 3) {
            throw new IllegalArgumentException("Colour had unexpected length " + arrayToConvert.size());
        }
        return new Colour(toDouble(arrayToConvert.get(0)), toDouble(arrayToConvert.get(1)),
                toDouble(arrayToConvert.get(2)));
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
