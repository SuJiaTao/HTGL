package Examples;

// Bailey Jia-Tao Brown
// 2023
// Simple scene showcasing prefabs with moveable camera

import TGL.*;
import TGL.Vector.*;
import TGL.Prefabs.*;

public final class SimpleScene {
    public static void main(String[] args) {

        // initialize world
        World.initialize("Simple Scene", 500, 500, 0.5f);

        // create empty parent for all objects
        Cube emptyParent = new Cube(
                new Vector3(0, 0, 6),
                new Color3(255, 255, 255)
        );
        emptyParent.show = false;

        // create car
        Car car = new Car(
                new Color3(255, 128, 64),
                new Vector3(0, -2f, 2),
                new Vector3(0, -35, 0), 0.75f);
        car.parent = emptyParent;

        // make human with blue-ish clothes
        Human human = new Human(
                new Color3(64, 128, 255),
                new Color3(237, 221, 182),
                new Vector3(-1, -2f, 0),
                new Vector3(0, -35, 0), 0.75f
        );
        human.parent = emptyParent;

        // create tree
        Tree tree = new Tree(
                new Vector3(0.3f, -2, 0),
                3, 0.75f
        );
        tree.parent = emptyParent;

        // create light source
        Light light = new Light(new Vector3(3, 3, 3), 8);
        light.parent = emptyParent;

        while (true) {
            // get user input
            Vector2 lookCamera = Input.getArrowKeyDirection().multiplyCopy(3.25f);
            Vector2 moveCamera = Input.getWASDDirection().multiplyCopy(0.35f);

            // move camera based on input
            World.rotateCamera(lookCamera.y, -lookCamera.x, 0.0f);
            World.moveCameraRelativeToLooking(moveCamera.x, 0, moveCamera.y);

            // update world
            World.pause(5);
            World.update();
        }
    }
}
