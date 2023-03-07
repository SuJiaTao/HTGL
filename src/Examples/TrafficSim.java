package Examples;

import TGL.Vector.Vector2;
import TGL.Vector.Vector3;
import TGL.World;
import TGL.Prefabs.*;
import TGL.*;

public class TrafficSim {
    public static final float SIMBOUNDS = 100.0f;
    public static final int CARCOUNT = 100;
    public static Color3 randColor() {
        return new Color3(Rand.nextInt(0, 255),
                Rand.nextInt(128, 255),
                Rand.nextInt(128, 255));
    }
    public static void main(String[] args) {
        World.initialize("Traffic Simulator", 700, 500, 0.35f);

        // Create SUN
        Light sun = new Light(new Vector3(5, 100, 5), 500);

        Car carList[] = new Car[CARCOUNT];
        for (int i = 0; i < CARCOUNT; i++) {
            carList[i] = new Car(
                    randColor(),
                    new Vector3(
                            Rand.nextFloat(-SIMBOUNDS, SIMBOUNDS),
                            0,
                            Rand.nextFloat(-SIMBOUNDS, SIMBOUNDS)),
                    new Vector3(
                            0,
                            Rand.nextFloat(0, 360),
                            0
                    ),
                    Rand.nextFloat(0.75f, 1.25f)
            );
        }

        World.setCameraPos(0, 10, 0);

        while (true) {
            for (Car car : carList) {
                // clamp position
                car.position.x %= SIMBOUNDS;
                car.position.z %= SIMBOUNDS;

                // update random rotation
                car.rotation.y += Rand.nextFloat(-30.0f, 30.0f);

                // move forward
                car.moveRelativeToRotation(new Vector3(1, 0, 0));
            }

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
