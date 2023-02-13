package Examples;

// Bailey Jia-Tao Brown
// 2023
// Simple terrain test

import TGL.*;
import TGL.Vector.Vector2;
import TGL.Vector.Vector3;

import java.util.Random;

public final class TerrainTest {
    public static float[][] generateHeight(int dimx, int dimz, float minH, float maxH, int smooth) {
        float hList[][] = new float[dimx][dimz];

        // create random numbers
        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimz; j++) {
                hList[i][j] = Rand.nextFloat(minH, maxH);
            }
        }

        // if no smooth, end
        if (smooth == 0) return hList;

        // smooth average (X)
        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimz; j++) {
                float accum = 0;
                for (int s = 0; s <= smooth; s++) {
                    accum += hList[(i + s) % dimx][j];
                }
                accum /= (float)smooth;
                for (int s = 0; s <= smooth; s++) {
                    hList[(i + s) % dimx][j] = accum;
                }
            }
        }

        // smooth average (Z)
        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimz; j++) {
                float accum = 0;
                for (int s = 0; s <= smooth; s++) {
                    accum += hList[i][(j + s) % dimz];
                }
                accum /= (float)smooth;
                for (int s = 0; s <= smooth; s++) {
                    hList[i][(j + s) % dimz] = accum;
                }
            }
        }

        return hList;
    }

    public static void generateTerrain(float[][] tList) {
        int moveX = 0;
        int moveZ = 0;
        for (float[] zList : tList) {
            moveZ = 0;
            for (float yPos : zList) {
                new Cube(new Vector3(moveX * 2, yPos, moveZ * 2),
                        new Color3(255, 128 ,65));
                moveZ++;
            }
            moveX++;
        }
    }

    public static void main(String[] args) {
        World.initialize("Terrain Test", 700, 500, 0.4f);

        new Light(new Vector3(0, 20, 0), 50);

        generateTerrain(generateHeight(25, 25, -4, 4, 2));

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
