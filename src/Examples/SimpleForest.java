package Examples;

// Bailey Jia-Tao Brown
// 2023
// Simple forest scene

import TGL.*;
import TGL.Prefabs.*;
import TGL.Vector.*;

public final class SimpleForest {
    public static void main(String[] args) {
        // init world
        World.initialize("Forest", 500, 500, 0.5f);

        // generate all trees
        for (int i = 0; i < 20; i++) {
            float randX = Rand.nextFloat(-100, 100);
            float randZ = Rand.nextFloat(-100, 100);
            float treeHeight = Rand.nextFloat(5.0f, 7.5f);
            new Tree(new Vector3(randX, 0, randZ), treeHeight, 1.0f);
        }

        // create sun
        new Light(new Vector3(0, 100, 0), 500);

        // move camera up
        World.setCameraPos(new Vector3(0, 3, 0));

        // render loop
        while (true) {
            // get user input
            Vector2 lookCamera = Input.getArrowKeyDirection().multiplyCopy(3.25f);
            Vector2 moveCamera = Input.getWASDDirection().multiplyCopy(0.35f);

            // move camera based on input
            World.rotateCamera(lookCamera.y, -lookCamera.x, 0.0f);
            World.moveCameraRelativeToLooking(moveCamera.x, 0, moveCamera.y);

            World.update();
        }
    }
}
