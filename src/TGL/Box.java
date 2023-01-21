package TGL;

// Bailey Jia-Tao Brown
// 2023
// Simple box class for rendering
// Holds a color

import TGL.Vector.Vector3;

public final class Box extends WorldObject {
    public Color3 color;

    // Ctor
    public Box(Vector3 position, Vector3 rotation, Vector3 scale, Color3 color) {
        super(position, rotation, scale);
        this.color = color.copy();
    }
}
