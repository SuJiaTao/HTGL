package TGL.Internal;

// Bailey Jia-Tao Brown
// 2023
// Color/depth bitmap class with
// drawing capabilites

import TGL.Color3;

public final class RenderBuffer {
    // Consts
    public static final int COLOR_COMPONENTS = 3;
    public static final float CLEAR_DEPTH = Float.POSITIVE_INFINITY;

    // Size metadata
    public int buffW, buffH;

    // Color and depth arrays
    private Color3 colorBuffer[][];
    private float depthBuffer[][];

    // Ctor
    public RenderBuffer(int width, int height){

        buffW = width; buffH = height;

        // init colorbuffer
        colorBuffer = new Color3[width][height];
        for (int x = 0; x < colorBuffer.length; x++){
            for (int y = 0; y < colorBuffer[x].length; y++) {
                colorBuffer[x][y] = new Color3();
            }
        }

        // init depthbuffer
        depthBuffer = new float[width][height];
    }

    // Clear func
    public void clear(Color3 clearColor) {

        // clear colorbuffer
        for (Color3[] column : colorBuffer){
            for (Color3 pixel : column) {
                pixel.set(clearColor);
            }
        }

        // clear depthbuffer (set to +INF)
        for (int i = 0; i < buffW; i++) {
            for (int j = 0; j < buffH; j++) {
                depthBuffer[i][j] = CLEAR_DEPTH;
            }
        }
    }

    // check out of view
    public boolean outOfView(int x, int y) {
        return !((x > 0 && x < buffW) && (y > 0 && y < buffH));
    }

    // Set pixel func
    public void setPixel(int x, int y, Color3 color, float depth) {
        // check bad pos
        if (outOfView(x, y)) return;

        // check bad values
        if (Float.isNaN(depth))
            throw new RuntimeException("Bad depth value! Depth was NaN. Check for x/0 operation.");
        if (color == null)
            throw new RuntimeException("Bad color value! Color was null");

        // draw
        colorBuffer[x][y].set(color);
        depthBuffer[x][y] = depth;
    }

    // Get pixel color
    public Color3 getPixelColor(int x, int y) {
        try {
            return colorBuffer[x][y];
        } catch (Exception exp) {
            // on out of range, return err purple
            return new Color3(255, 0, 255);
        }

    }

    // Get pixel depth
    public float getPixelDepth(int x, int y){
        try {
            return depthBuffer[x][y];
        } catch (Exception exp) {
            // on out of range, discard
            return Float.NEGATIVE_INFINITY;
        }
    }

}
