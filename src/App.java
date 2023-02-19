import TGL.*;
import TGL.Vector.*;
import TGL.Prefabs.*;

public class App {
    public static void main(String[] args) {
        World.initialize("TestWorld", 500, 500, 0.5f);

        Cube empty = new Cube(
                new Vector3(0, 0, 6),
                new Color3(255, 255, 255)
        );
        empty.show = false;

        Car car = new Car(
                new Color3(255, 128, 64),
                new Vector3(0, -2f, 2),
                new Vector3(0, -35, 0), 0.75f);
        car.parent = empty;

        Human human = new Human(
                new Color3(64, 128, 255),
                new Color3(237, 221, 182),
                new Vector3(-1, -2f, 0),
                new Vector3(0, -35, 0), 0.75f
        );
        human.parent = empty;

        Tree tree = new Tree(
                new Vector3(0.3f, -2, 0),
                3, 0.75f
        );
        tree.parent = empty;

        Light light = new Light(new Vector3(3, 3, 3), 8);
        light.parent = empty;

        while (true) {
            // rotate human
            //human.rotate(0, 0.3f, 0);

            // rotate empty
            empty.rotate(0, 0.6f, 0);

            Vector2 lookCamera = Input.getArrowKeyDirection().multiplyCopy(3.5f);
            Vector2 moveCamera = Input.getWASDDirection().multiplyCopy(0.2f);

            World.rotateCamera(lookCamera.y, -lookCamera.x, 0.0f);
            World.moveCameraRelativeToLooking(moveCamera.x, 0, moveCamera.y);

            if (WorldObject.isColliding(human, 1, World.getCameraPos(), 1)) {
                System.out.print("touch! ");
            }

            World.pause(5);
            World.update();
        }
    }
}
