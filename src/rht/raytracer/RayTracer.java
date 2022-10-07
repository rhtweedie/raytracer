package rht.raytracer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class RayTracer extends JPanel {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

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
        List<Sphere> objects = new ArrayList<>();
        objects.add(new Sphere(new Vec3(-1.0, -1.0, 5.0), 1.0, new Colour(1.0, 0.0, 0.0)));
        objects.add(new Sphere(new Vec3(1.0, -1.0, 5.0), 0.5, new Colour(1.0, 1.0, 0.0)));
        Scene scene = new Scene(objects);

        Camera camera = new Camera(new Vec3(0.0, 0.0, -5.0), new Vec3(0.0, 0.0, 0.0), new Vec3(1.0, 0.0, 0.0),
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
                Sphere firstSphere = scene.firstIntercept(ray);

                image.setRGB(x, y, (x + y) % 255);

            }
        }
    }
}
