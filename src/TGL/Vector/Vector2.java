package TGL.Vector;

// Bailey Jia-Tao Brown
// 2023
// Simple 2D-Vector

public final class Vector2 extends Vector {
    public float x, y; // 2- component vector

    // CTOR(s)
    public Vector2() {
        this.x = 0.0f; this.y = 0.0f;
    }

    public Vector2(float x, float y) {
        this.x = x; this.y = y;
    }

    // Vector class implementations
    public float[] getComponentArray() {
        return new float[] {x, y};
    }

    public void setComponents(float[] arr) {
        x = arr[0]; y = arr[1];
    }

    public int getComponentCount() {
        return 2;
    }

    public Vector2 copy()
    {
        return new Vector2(this.x, this.y);
    }
}
