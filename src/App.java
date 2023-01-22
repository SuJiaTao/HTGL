import TGL.*;
import TGL.Vector.Vector2;
import TGL.Vector.Vector3;

public class App {
    public static void main(String[] args) {
        World.initialize("TestWorld", 500, 500, 0.5f);

        Cube cube = new Cube(
                new Vector3(0, 0, 6),
                1,
                new Color3(255, 128, 64)
        );

        cube.scale.set(1.0f, 1.5f, 2.0f);

        Light l = new Light(new Vector3(3, 3, 6), 7);

        while (true) {
            cube.rotate(1f, 1f, 0.0f);

            World.pause(5);
            World.update();
        }
    }
}
