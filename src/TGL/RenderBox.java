package TGL;

// Bailey Jia-Tao Brown
// 2023
// Simple box class for rendering
// Holds a color

import TGL.Vector.Vector3;

public final class RenderBox {
    public Vector3 localPosition;
    public Vector3 localRotation;
    public Vector3 localScale;
    public Color3 color;

    // Ctor
    public RenderBox(Vector3 position, Vector3 rotation, Vector3 scale, Color3 color) {
        localPosition = position.copy();
        localRotation = rotation.copy();
        localScale = scale.copy();
        this.color = color.copy();
    }
}
