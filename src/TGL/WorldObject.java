package TGL;

// Bailey Jia-Tao Brown
// 2023
// Simple abstracted world "object"
// A world object is made up of many cubes

import java.util.ArrayList;
import TGL.Internal.*;
import TGL.Vector.*;

public abstract class WorldObject {

    // worldspace transformations
    public Vector3 position;
    public Vector3 rotation;
    public Vector3 scale;

    // boxes
    public ArrayList<Box> boxes;

    // Ctors
    public WorldObject(Vector3 position, Vector3 rotation, Vector3 scale) {
        // init transformations
        this.position = position.copy();
        this.rotation = rotation.copy();
        this.scale = scale.copy();

        // init boxList
        boxes = new ArrayList<>();
    }
}
