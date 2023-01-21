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

        World.addWorldObject(this);
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

        World.addWorldObject(this);
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

        World.addWorldObject(this);
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

    // Update func
    protected abstract void update();
}
