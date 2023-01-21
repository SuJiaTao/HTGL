package TGL;

// Bailey Jia-Tao Brown
// 2023
// 4x4 affine transform matrix for transforming verts

import TGL.Vector.Vector3;

public final class Matrix {
    public static final int ROWS = 4;
    public static final int COLS = 4;
    public static final int TRANSFORM_TRANSLATE = 0;
    public static final int TRANSFORM_ROTATE = 1;
    public static final int TRANSFORM_SCALE = 2;
    private float[][] matrix;

    // float trig
    private float fsin(float val) {
        return (float)Math.sin(Math.toRadians(val));
    }

    private float fcos(float val) {
        return (float)Math.cos(Math.toRadians(val));
    }

    // set matrix value
    public void setValue(int x, int y, float val) {
        try {
            matrix[x][y] = val;
        } catch (Exception exp) {
            throw new RuntimeException("Failed to set matrix value: ", exp);
        }
    }

    private void init() {
        matrix = new float[ROWS][COLS];

        // init identity matrix
        setValue(0, 0, 1.0f);
        setValue(1, 1, 1.0f);
        setValue(2, 2, 1.0f);
        setValue(3, 3, 1.0f);
    }

    public Matrix() {
        init();
    }

    public void set(Vector3 input, int type) {
        init();

        // switch init based on type
        switch (type) {
            case TRANSFORM_TRANSLATE:
                // setup translation params
                setValue(3, 0, input.x);
                setValue(3, 1, input.y);
                setValue(3, 2, input.z);
                break;

            case TRANSFORM_SCALE:
                // setup scale params
                setValue(0, 0, input.x);
                setValue(1, 1, input.y);
                setValue(2, 2, input.z);
                break;

            case TRANSFORM_ROTATE:
                //setup rotation params
                // this one is extra bad
                // refer to this:
                // https://math.stackexchange.com/questions/1882276/combining-all-three-rotation-matrices

                // row 1
                setValue(0, 0, fcos(input.y) * fcos(input.z));
                setValue(1, 0, fcos(input.y) * fsin(input.z));
                setValue(2, 0, -fsin(input.y));

                // row 2
                setValue(0, 1, fsin(input.x) * fsin(input.y) * fcos(input.z) -
                        fcos(input.x) * fsin(input.z));
                setValue(1, 1, fsin(input.x) * fsin(input.y) * fsin(input.z) +
                        fcos(input.x) * fcos(input.z));
                setValue(2, 1, fsin(input.x) * fcos(input.y));

                // row 3
                setValue(0, 2, fcos(input.x) * fsin(input.y) * fcos(input.z) +
                        fsin(input.x) * fsin(input.z));
                setValue(1, 2, fcos(input.x) * fsin(input.y) * fsin(input.z) -
                        fsin(input.x) * fcos(input.z));
                setValue(2, 2, fcos(input.x) * fcos(input.y));
                break;

            default:
                throw new RuntimeException("Invalid transform operation!");
        }
    }

    public Matrix(Vector3 input, int type) {
        set(input, type);
    }

    // get matrix array
    public float[][] getMatrix() {
        return matrix;
    }

    // return transformed vector3
    public Vector3 transformVertex(Vector3 vert) {
        // get components
        float temp[] = vert.getComponentArray();
        float vector[] = new float[] { temp[0], temp[1], temp[2], 1.0f};

        // multiply vector by matrix
        float tVector[] = new float[4];

        // loop each component of transformed vector
        for (int tcomp = 0; tcomp < 4; tcomp++) {
            // for each "x" of the matrix
            for (int mx = 0; mx < ROWS; mx++) {
                tVector[tcomp] += vector[mx] * matrix[mx][tcomp];
            }
        }

        // return transformed vector
        return new Vector3(tVector[0], tVector[1], tVector[2]);
    }

    // static transformations
    public static Vector3 translate(Vector3 vert, Vector3 translation) {
        Matrix tform = new Matrix(translation,
                TRANSFORM_TRANSLATE);
        return tform.transformVertex(vert);
    }

    public static Vector3 rotate(Vector3 vert, Vector3 rotation) {
        Matrix tform = new Matrix(rotation,
                TRANSFORM_ROTATE);
        return tform.transformVertex(vert);
    }

    public static Vector3 scale(Vector3 vert, Vector3 scale) {
        Matrix tform = new Matrix(scale,
                TRANSFORM_SCALE);
        return tform.transformVertex(vert);
    }
}
