import TGL.*;
import TGL.Vector.Vector3;

public class App {
    public static void main(String[] args) {
        World.initialize("TestWorld", 500, 500, 1.0f);

        Cube cube = new Cube(
                new Vector3(0, 2, 5),
                new Vector3(45, 45, 0),
                new Vector3(1, 1, 1),
                new Color3(255, 255, 255)
        );

        while (true) {
            World.update();
        }
    }
}
