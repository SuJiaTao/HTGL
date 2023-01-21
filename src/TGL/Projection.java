package TGL;

// Bailey Jia-Tao Brown
// 2023
// Simple 3D -> 2D projection util funcs

import TGL.Vector.Vector2;
import TGL.Vector.Vector3;

public final class Projection {
    // Project 3D point to 2D
    public static Vector2 projectVertex(Vector3 vert){
        return new Vector2(vert.x / vert.z, vert.y / vert.z);
    }

    public static Vector2[] projectVertexArray(Vector3[] verts){
        Vector2 vList[] = new Vector2[verts.length];
        for (int i = 0; i < verts.length; i++){
            vList[i] = projectVertex(verts[i]);
        }
        return vList;
    }

    public static int[] projToScreenSpace(Vector2 projSpace, int sw, int sh){
        int[] screenSpace = new int[2];

        // float variants for math
        float fsw = (float)sw * 0.5f;
        float fsh = (float)sh * 0.5f;

        // account for aspect
        float aspect = fsw / fsh;

        // transform to screenspace
        float sx = ((fsw / aspect) * projSpace.x) + fsw;
        float sy = (fsh * projSpace.y) + fsh;

        screenSpace[0] = (int)sx;
        screenSpace[1] = (int)sy;

        return screenSpace;
    }

    public static float linearInterpolate(float a, float b, float f){
        // check bad cases
        if (Float.isNaN(f))
            throw new RuntimeException("lerp factor was NaN");
        if (Float.isInfinite(f))
            throw new RuntimeException("lerp factor was Inf");

        // clamp to 0, 1
        f = Math.max(0.0f, f);
        f = Math.min(f, 1.0f);

        return a + f * (b - a);
    }

    public static float fastDist(float dx, float dy) {
        // ensure abs
        dx = Math.abs(dx);
        dy = Math.abs(dy);

        return (0.96f * Math.max(dx, dy)) + (0.4f * Math.min(dx, dy));
    }

    public static float dist(float dx, float dy) {
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    public static int[] projectVertexToScreenSpace(Vector3 vert, int sw, int sh){
        return projToScreenSpace(projectVertex(vert), sw, sh);
    }
}
