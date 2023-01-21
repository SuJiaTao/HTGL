package TGL;

// Bailey Jia-Tao Brown
// 2023
// Primitive 3-component color class

import TGL.Internal.Projection;

public final class Color3 {
    public int r, g, b;

    // Ctors
    public Color3() {
        r = 0; g = 0; b = 0;
    }

    public Color3(int _r, int _g, int _b) {
        if (!validComponent(_r) ||
                !validComponent(_g) ||
                !validComponent(_b))
            throw new RuntimeException("Color component array had bad color value. Ensure (0-255).");
        r = _r;
        g = _g;
        b = _b;
    }

    public Color3(int[] comps) {
        if (comps == null)
            throw new RuntimeException("Color component array was null!");
        if (comps.length < 3)
            throw new RuntimeException("Color component array was too small!");

        if (!validComponent(comps[0]) ||
                !validComponent(comps[1]) ||
                !validComponent(comps[2]))
            throw new RuntimeException("Color component array had bad color value. Ensure (0-255).");

        r = comps[0];
        g = comps[1];
        b = comps[2];
    }

    public void set(int _r, int _g, int _b){
        if (!validComponent(_r) ||
            !validComponent(_g) ||
            !validComponent(_b))
            throw new RuntimeException("Color component array had bad color value. Ensure (0-255).");
        r = _r;
        g = _g;
        b = _b;
    }

    public void set(int[] comps)
    {
        if (comps == null)
            throw new RuntimeException("Color component array was null!");
        if (comps.length < 3)
            throw new RuntimeException("Color component array was too small!");

        if (!validComponent(comps[0]) ||
                !validComponent(comps[1]) ||
                !validComponent(comps[2]))
            throw new RuntimeException("Color component array had bad color value. Ensure (0-255).");

        r = comps[0];
        g = comps[1];
        b = comps[2];
    }

    public void set(Color3 color) {
        r = color.r;
        g = color.g;
        b = color.b;
    }

    public int[] toArray(){
        return new int[] {r, g, b};
    }

    public Color3 copy(){
        return new Color3(r, g, b);
    }

    // Ensure color range is good
    public boolean validComponent(int component) {
        return (component >> 8) == 0;
    }

    // Blend colors
    public static Color3 blend(Color3 c1, Color3 c2, float factor) {
        int br = (int) Projection.linearInterpolate(c1.r, c2.r, factor);
        int bg = (int)Projection.linearInterpolate(c1.g, c2.g, factor);
        int bb = (int)Projection.linearInterpolate(c1.b, c2.b, factor);
        return new Color3(br, bg, bb);
    }

}
