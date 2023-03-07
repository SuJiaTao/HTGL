package TGL;

// Bailey Jia-Tao Brown
// 2023
// Simple abstracted world "object"
// A world object is made up of many cubes

import java.util.ArrayList;
import java.util.Arrays;

import TGL.Internal.Matrix;
import TGL.Vector.*;

public abstract class WorldObject {

    // parent object (inherits transforms)
    public WorldObject parent;

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

    public void moveRelativeToRotation(Vector3 vect) {
        Vector3 moveDir = Matrix.rotate(vect,
                new Vector3(this.rotation.x, 0, 0));
        moveDir = Matrix.rotate(moveDir,
                new Vector3(0, this.rotation.y, 0));
        this.position.add(moveDir);
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

    public static boolean isColliding(Vector3 p1, float p1Rad,
                                      Vector3 p2, float p2Rad) {
        // generate and compare radii
        float dist = p1.addCopy(p2.multiplyCopy(-1.0f)).magnitude();
        return dist <= (p1Rad + p2Rad);
    }

    public static boolean isColliding(WorldObject o, float oRad,
                                      Vector3 p, float pRad) {
        Vector3 op = o.position;
        while (o.parent != null) {
            op = Matrix.transform(op,
                    o.parent.position,
                    o.parent.rotation,
                    o.parent.scale);
            o = o.parent;
        }

        return isColliding(op, oRad, p, pRad);
    }

    public static boolean isColliding(WorldObject o1, float o1Rad,
                                      WorldObject o2, float o2Rad) {
        Vector3 p1 = o1.position;
        while (o1.parent != null) {
            p1 = Matrix.transform(p1,
                    o1.parent.position,
                    o1.parent.rotation,
                    o1.parent.scale);
            o1 = o1.parent;
        }
        Vector3 p2 = o2.position;
        while (o2.parent != null) {
            p2 = Matrix.transform(p2,
                    o2.parent.position,
                    o2.parent.rotation,
                    o2.parent.scale);
            o2 = o2.parent;
        }
        return isColliding(p1, o1Rad, p2, o2Rad);
    }
    public boolean isColliding(float thisRadius, float otherRadius, WorldObject other) {
        // on not exist, false
        if (other == null)
            return false;

        return isColliding(this, thisRadius, other, otherRadius);
    }

    // Update func
    protected abstract void update();
}
