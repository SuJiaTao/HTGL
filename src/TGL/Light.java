package TGL;

// Bailey Jia-Tao Brown
// 2023
// Point light object for the world
// Illuminates nearby worldobjects

import TGL.*;
import TGL.Vector.*;

public final class Light {
    public float radius;
    public Vector3 position;

    public Light(Vector3 position, float radius) {
        this.radius = radius;
        this.position = position.copy();

        World.internalAddLight(this);
    }
}
