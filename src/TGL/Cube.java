package TGL;

// Bailey Jia-Tao Brown
// 2023
// Simple Cube worldobject

import TGL.Vector.Vector3;

public final class Cube extends WorldObject {
    // Ctor
    public Cube(Vector3 pos, Vector3 rot, Vector3 scale, Color3 col) {
        super(pos, rot, scale, null);

        // add unit cube
        addRenderBox(new RenderBox(new Vector3(0, 0, 0),
                new Vector3(0, 0, 0),
                new Vector3(1, 1, 1), col));
    }

    public Cube(Vector3 position, float size, Color3 color) {
        super(position, new Vector3(0, 0, 0),
                new Vector3(size, size ,size), null);

        // add unit cube
        addRenderBox(new RenderBox(new Vector3(0, 0, 0),
                new Vector3(0, 0, 0),
                new Vector3(1, 1, 1), color));
    }

    public Cube(Vector3 position, Color3 color) {
        super(position, new Vector3(0, 0, 0),
                new Vector3(1, 1 ,1), null);

        // add unit cube
        addRenderBox(new RenderBox(new Vector3(0, 0, 0),
                new Vector3(0, 0, 0),
                new Vector3(1, 1, 1), color));
    }

    // Unused update func

    @Override
    protected void update() {
        // nothing
    }
}
