import TGL.*;
import TGL.Vector.*;
import Prefabs.*;

public class App {
    public static void main(String[] args) {
        World.initialize("TestWorld", 500, 500, 0.5f);

        Car car = new Car(
                new Color3(255, 128, 64),
                new Vector3(0, -2f, 6),
                new Vector3(0, -35, 0), 0.75f);

        Human human = new Human(
                new Color3(64, 128, 255),
                new Color3(237, 221, 182),
                new Vector3(-1, -2f, 4),
                new Vector3(0, -35, 0), 0.75f
        );

        Tree tree = new Tree(
                new Vector3(0.3f, -2, 4),
                3, 0.75f
        );

        Light l = new Light(new Vector3(3, 3, 6), 8);

        while (true) {
            World.pause(5);
            World.update();
        }
    }
}
