package Examples;

// Bailey Jia-Tao Brown
// 2023
// Example of wobbly cuboid using better abstracted API

import TGL.*;
import TGL.Vector.*;

public final class WobblyCuboid {
    public static void main(String[] args) {

        // initialize world
        // window height is 500, width is 500
        // simulation is rendered at 0.5 times the window resolution
        World.initialize("Wobble World", 500, 500, 0.22f);

        // create cube at z = 10 and color orange
        Cube cube = new Cube(
                new Vector3(0, 0, 6),
                1,
                new Color3(255, 128, 64)
        );

        // create a light source at (3, 3, 6) with radius 7
        Light light = new Light(new Vector3(4, 4, 5), 7);

        // infinite loop
        while (true) {

            // generate function phase based on time
            double phase = (double)System.currentTimeMillis() * 0.001;

            // get sine and cosine output
            float sineOutput = (float)Math.sin(phase);
            float cosineOutput = (float)Math.cos(phase);

            // rotate the cube irregularly
            cube.rotation.set(sineOutput * 135,
                    cosineOutput * 45,
                    cosineOutput * 180);

            // pause simulation
            World.pause(5);

            // update simulation
            World.update();
        }
    }
}
