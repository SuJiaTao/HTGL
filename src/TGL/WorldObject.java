package TGL;

// Bailey Jia-Tao Brown
// 2023
// Simple abstracted world "object"
// A world object is made up of many cubes

import java.util.ArrayList;
import java.util.Arrays;

import TGL.Vector.*;

public abstract class WorldObject {

    // worldspace transformations
    public Vector3 position;
    public Vector3 rotation;
    public Vector3 scale;

    // visuals
    public boolean show;
    private ArrayList<RenderBox> boxes;

    // Ctors
    public WorldObject(Vector3 position, Vector3 rotation, Vector3 scale,
                       RenderBox[] boxes) {
        show = true;

        // init transformations
        this.position = position.copy();
        this.rotation = rotation.copy();
        this.scale = scale.copy();

        // init boxList
        this.boxes = new ArrayList<>();
        if (boxes != null)
            this.boxes.addAll(Arrays.asList(boxes));

        World.internalAddWorldObject(this);
    }

    public WorldObject(Vector3 position, Vector3 rotation, RenderBox[] boxes) {
        show = true;

        // init transformations
        this.position = position.copy();
        this.rotation = rotation.copy();
        this.scale = new Vector3(1.0f, 1.0f, 1.0f);

        // init boxList
        this.boxes = new ArrayList<>();
        if (boxes != null)
            this.boxes.addAll(Arrays.asList(boxes));

        World.internalAddWorldObject(this);
    }

    public WorldObject(Vector3 position, RenderBox[] boxes) {
        show = true;

        // init transformations
        this.position = position.copy();
        this.rotation = new Vector3(0, 0, 0);
        this.scale = new Vector3(1.0f, 1.0f, 1.0f);

        // init boxList
        this.boxes = new ArrayList<>();
        if (boxes != null)
            this.boxes.addAll(Arrays.asList(boxes));

        World.internalAddWorldObject(this);
    }

    // Add render box function
    protected void addRenderBox(RenderBox box) {
        this.boxes.add(box);
    }

    protected void removeRenderBox(RenderBox box) {
        this.boxes.remove(box);
    }

    protected void removeRenderBox(int index) {
        this.boxes.remove(index);
    }

    protected void removeAllRenderBoxes() {
        this.boxes.clear();
    }

    public ArrayList<RenderBox> getBoxList() {
        return this.boxes;
    }

    // transformations
    public void move(Vector3 vector) {
        position.add(vector);
    }

    public void move(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void rotate(Vector3 rotation) {
        this.rotation.add(rotation);
    }

    public void rotate(float x, float y, float z) {
        rotate(new Vector3(x, y, z));
    }

    public void scale(Vector3 scale) {
        this.scale.x *= scale.x;
        this.scale.y *= scale.y;
        this.scale.z *= scale.z;
    }

    public void scale(float x, float y, float z) {
        this.scale.x *= x;
        this.scale.y *= y;
        this.scale.z *= z;
    }

    // Update func
    protected abstract void update();
}
