import TGL.*;
import TGL.Vector.Vector3;

public class App {
    public static void main(String[] args) {
        World.initialize("TestWorld", 500, 500, 1.0f);

        Cube cube = new Cube(
                new Vector3(0, 0, 5),
                new Color3(255, 128, 64)
        );

        Light l = new Light(new Vector3(3, 3, 4), 10);

        while (true) {
            cube.rotate(1f, 1f, 0.0f);
            World.update();
            World.setCameraRotation(0, 45, 0);
            World.moveCameraRelativeToLooking(0, 0, -0.2f);
        }
    }
}
