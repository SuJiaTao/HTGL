package TGL.Vector;

// Bailey Jia-Tao Brown
// 2023
// Simple 3D Vector

public final class Vector3 extends Vector {
    public float x, y, z; // 3 - component vector

    // CTOR(s)
    public Vector3() {
        this.x = 0.0f; this.y = 0.0f; this.z = 0.0f;
    }

    public Vector3(float x, float y) {
        this.x = x; this.y = y; this.z = 0.0f;
    }

    public Vector3(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }

    // Set function
    public void set(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }

    public void set(Vector3 other) {
        this.x = other.x; this.y = other.y; this.z = other.z;
    }

    // Cross product function
    public static Vector3 cross(Vector3 lhs, Vector3 rhs) {

        // terrible stolen math
        return new Vector3(
                lhs.y * rhs.z - lhs.z * rhs.y,
                lhs.z * rhs.x - lhs.x * rhs.z,
                lhs.x * rhs.y - lhs.y * rhs.x
        );
    }

    // Dot product function
    public float dot(Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    // Vector class implementations
    public float[] getComponentArray() {
        return new float[] {x, y, z};
    }

    public void setComponents(float[] arr) {
        x = arr[0]; y = arr[1]; z = arr[2];
    }

    public int getComponentCount() {
        return 3;
    }

    public Vector3 copy()
    {
        return new Vector3(this.x, this.y, this.z);
    }
}
