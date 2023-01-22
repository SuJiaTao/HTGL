package TGL.Vector;

// Bailey Jia-Tao Brown
// 2023
// General-use vector class to avoid re-writing vector math funcs for
// multi-dim vectors e.g. vec2, vec3, vec4

public abstract class Vector {
    // Retrieves all components of vectors in form of array
    public abstract float[] getComponentArray();

    // Sets all components of vector based on array
    protected abstract void setComponents(float[] cArr);

    // Gets number of components in vector
    public abstract int getComponentCount();

    // Creates copy of vector
    public abstract Vector copy();

    // Check if vectors are compatible (have same component count)
    public static boolean sameComponentCount(Vector v1, Vector v2) {
        return v1.getComponentCount() == v2.getComponentCount();
    }

    // Vector add operation
    public void add(Vector v2) {
        // check for faulty cases
        if (v2 == null)
            throw new RuntimeException("Vector was null!");

        if (sameComponentCount(this, v2) == false)
            throw new RuntimeException("Vector component mismatch!");

        if (!v2.isValid())
            throw new RuntimeException("Vector had NaN/inf values. Check for x/0 operation.");

        // get components
        float[] v1Comps = this.getComponentArray();
        float[] v2Comps = v2.getComponentArray();

        // add all components
        for (int i = 0; i < v1Comps.length; i++) {
            v1Comps[i] += v2Comps[i];
        }

        // set this new components
        this.setComponents(v1Comps);
    }

    // Vector multiply operation
    public void multiply(float f) {
        // check for bad float values
        if (Float.isNaN(f) || Float.isInfinite(f))
            throw new RuntimeException("Multiply factor was NaN/inf. Check for x/0 operation.");

        // get components and multiply all
        float[] comps = this.getComponentArray();
        for (int i = 0; i < comps.length; i++) {
            comps[i] *= f;
        }

        // update components
        this.setComponents(comps);
    }

    public Vector multiplyCopy(float f) {
        Vector v = this.copy();
        v.multiply(f);
        return v;
    }

    // Get Vector magnitude
    public float magnitude() {
        // get all components, sum up the squares of each
        float accum = 0.0f;
        float[] comps = this.getComponentArray();
        for (float comp : comps){
            accum += comp * comp;
        }

        // get sqrt of all
        return (float)Math.sqrt(accum);
    }

    // Get distance between other vector
    public float distance(Vector v2){
        // check for faulty cases
        if (v2 == null)
            throw new RuntimeException("Vector was null!");

        if (sameComponentCount(this, v2) == false)
            throw new RuntimeException("Vector component mismatch!");

        if (!v2.isValid())
            throw new RuntimeException("Vector had NaN/inf values. Check for x/0 operation.");

        // copy v2
        Vector vf = v2.copy();
        vf.multiply(-1.0f);

        // copy self
        Vector v1 = this.copy();

        // subtract and return
        v1.add(vf);
        return v1.magnitude();
    }

    // Get manhattan distance
    public float manhattanDistance(Vector3 v2) {
        // check for faulty cases
        if (v2 == null)
            throw new RuntimeException("Vector was null!");

        if (sameComponentCount(this, v2) == false)
            throw new RuntimeException("Vector component mismatch!");

        if (!v2.isValid())
            throw new RuntimeException("Vector had NaN/inf values. Check for x/0 operation.");

        float accum = 0.0f;
        float[] thisComps = getComponentArray();
        float[] thatComps = v2.getComponentArray();
        for (int i = 0; i < thisComps.length; i++) {
            accum += Math.abs(thisComps[i] - thatComps[i]);
        }

        return accum;
    }

    // Vector normalize operation
    public void normalize() {
        multiply(1.0f / magnitude());
    }

    // Check if vector is valid
    public boolean isValid() {
        float[] comps = this.getComponentArray();
        for (float comp : comps) {
            if (Float.isNaN(comp) || Float.isInfinite(comp)) return false;
        }
        return true;
    }

    // Tostring override
    public String toString() {
        float[] comps = this.getComponentArray();
        String s = "V" + this.getComponentCount() + " { ";
        for (float comp : comps) {
            s += comp + ", ";
        }
        s += "}";
        return s;
    }
}
