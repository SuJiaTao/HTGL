package TGL.Internal;

// Bailey Jia-Tao Brown
// 2023
// Rendering context, holds all info for a
// rendering instance

import TGL.*;
import TGL.Vector.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public final class Context {
    // Bufferstrategy count
    public static final int NUMBUFFERS = 2;

    // Resolution data
    private int windowW, windowH;
    private int resW, resH;

    // Window/render data
    private RenderBuffer frameBuffer;
    private JFrame window;
    private Graphics gfxObject;
    private Color3 clearCol;

    // input system
    public InputHandler inputHandler;

    // Ctor
    public Context(String title, int windowWidth, int windowHeight,
                   int resolutionWidth, int resolutionHeight,
                   Color3 clearColor) {

        // init metadata
        windowW = windowWidth;
        windowH = windowHeight;

        resW = resolutionWidth;
        resH = resolutionHeight;

        // setup framebuffer
        frameBuffer = new RenderBuffer(resW, resH);
        clearCol = clearColor.copy();

        // init window
        window = new JFrame();
        window.setSize(windowW, windowH);
        window.setTitle(title);
        window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // enable DOUBLE BUFFERING
        window.createBufferStrategy(NUMBUFFERS);

        // grab rendering object
        gfxObject = window.getBufferStrategy().getDrawGraphics();

        // init input system
        inputHandler = new InputHandler();
        window.addMouseListener(inputHandler);
        window.addKeyListener(inputHandler);
    }

    // get window
    public JFrame getWindow() {
        return window;
    }

    // get render resolution
    public int[] getResolution() {
        return new int[] {resW, resH};
    }

    // get window dimensions
    public int[] getDimensions() {
        return new int[] {windowW, windowH};
    }

    // Clearing related funcs
    public void setClearColor(Color3 color) {
        if (color == null)
            throw new RuntimeException("Color was null!");
        clearCol = color.copy();
    }
    public void clearWindow() {
        frameBuffer.clear(clearCol);
    }

    // framebuffer swap function
    public void refreshWindow() {
        // calculate pixel size
        float fpxlW = ((float)windowW / (float)resW);
        float fpxlH = ((float)windowH / (float)resH);

        // draw pixels
        for (int walkX = 0; walkX < resW; walkX++){
            for (int walkY = 0; walkY < resH; walkY++) {

                // invert height (if you are a graphics guy you'd understand this)
                Color3 col = frameBuffer.getPixelColor(walkX, resH - walkY - 1);

                // calculate pixel dims
                float fpxlX = fpxlW * (float)walkX;
                float fpxlY = fpxlH * (float)walkY;

                // draw "pixel"
                gfxObject.setColor(new Color(col.r, col.g, col.b, 255));
                gfxObject.fillRect((int)Math.floor(fpxlX), (int)Math.floor(fpxlY),
                        (int)Math.ceil(fpxlW), (int)Math.ceil(fpxlH));
            }
        }

        // swap buffer
        window.getBufferStrategy().show();
    }

    // Set pixel func
    public void setPixel(int x, int y, Color3 color, float depth) {
        // depth test
        if (depth < 0.0f || depth > frameBuffer.getPixelDepth(x, y))
            return;

        frameBuffer.setPixel(x, y, color, depth);
    }

    // Sleep func
    public void pause(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        }
        catch (Exception exc) {
            // ignore
        }
    }

    // Draw vertex func
    public void drawVertex(Vector3 vert, Color3 color) {
        // do 3D to 2D projection
        int[] screenPos = Projection.projectVertexToScreenSpace(vert,
                resW, resH);

        // set framebuffer
        setPixel(screenPos[0], screenPos[1],
                color, vert.z);
    }

    public void drawVertexArray(Vector3 vArr[], Color3 cArr[]){
        if (vArr.length > cArr.length)
            throw new RuntimeException("Mismatching color and vertex array lengths.");

        for (int i = 0; i < vArr.length; i++) {
            drawVertex(vArr[i], cArr[i]);
        }
    }

    public void drawVertexArray(Vector3 vArr[], Color3 color){
        for (int i = 0; i < vArr.length; i++) {
            drawVertex(vArr[i], color);
        }
    }

    public void drawLine(Vector3 verts[], Color3 cols[]){
        // check bad cases
        if (verts == null)
            throw new RuntimeException("Line vertex array was null.");
        if (verts.length < 2)
            throw new RuntimeException("Line vertex array length was too small");
        if (cols == null)
            throw new RuntimeException("Color array was null");
        if (cols.length < 2)
            throw new RuntimeException("Color array length was too small");

        // if any past clipping plane, cull
        for (Vector3 vert : verts) {
            if (vert.z <= 0.0f) return;
        }

        // project each point
        int[] spv1 = Projection.projectVertexToScreenSpace(verts[0], resW, resH);
        int[] spv2 = Projection.projectVertexToScreenSpace(verts[1], resW, resH);

        // calculate dist between the two
        float spRise = (float)(spv2[0] - spv1[0]);
        float spRun  = (float)(spv2[1] - spv1[1]);
        float spDist = Projection.fastDist(spRise, spRun);

        // "normalize" rise/run
        spRise /= spDist;
        spRun  /= spDist;

        // walk all
        int stepCount = (int)Math.ceil(spDist);
        for (float step = 0; step < stepCount; step += 1.0f) {

            // calculate drawX and drawY
            int drawX = spv1[0] + (int)(spRise * step);
            int drawY = spv1[1] + (int)(spRun  * step);

            // calculate lerpfactor (0-1)
            float lerpFactor = step / (float)stepCount;

            // lerp color and depth
            float depth = Projection.linearInterpolate(verts[0].z, verts[1].z,
                    lerpFactor);
            Color3 drawColor = Color3.blend(cols[0], cols[1], lerpFactor);

            setPixel(drawX, drawY, drawColor, depth);
        }
    }

    public void drawLine(Vector3 verts[], Color3 col) {
        drawLine(verts, new Color3[] {col, col});
    }

    public void drawLineArray(Vector3[] lines, Color3[] colors) {
        // ensure good color length
        if (lines.length > colors.length)
            throw new RuntimeException("Bad color array length.");

        // draw all line segments
        for (int i = 1; i < lines.length; i++) {
            drawLine(new Vector3[] { lines[i - 1], lines[i] },
                    new Color3[] { colors[i - 1], colors[i]});
        }
    }

    public void drawLineArray(Vector3[] lines, Color3 color) {
        // create array of all same colors
        Color3[] colors = new Color3[lines.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = color;
        }

        drawLineArray(lines, colors);
    }

    public void drawLinePolygon(Vector3[] lines, Color3[] colors){
        // ensure good color length
        if (lines.length > colors.length)
            throw new RuntimeException("Bad color array length.");

        // draw all line segments
        for (int i = 0; i < lines.length; i++) {
            drawLine(new Vector3[] { lines[i], lines[(i + 1) % lines.length] },
                    new Color3[] { colors[i], colors[(i + 1) % lines.length]});
        }
    }

    public void drawLinePolygon(Vector3[] lines, Color3 color) {
        // create array of all same colors
        Color3[] colors = new Color3[lines.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = color;
        }

        drawLinePolygon(lines, colors);
    }

    // fragment class for better organization
    private final class Fragment {
        int x, y; // screenspace position
        public float depth;
        public Color3 color;

        public Fragment(int[] sp, float d, Color3 col) {
            x = sp[0]; y = sp[1];
            depth = d;
            color = col;
        }
    }

    private void sortFragmentsByY(Fragment[] frags) {
        Arrays.sort(frags, new Comparator<Fragment>() {
                    @Override
                    public int compare(Fragment f1, Fragment f2) {
                        // compare Y position
                        // push higher values down
                        return f2.y - f1.y;
                    } } );
    }

    private Fragment[] generateFragments(Vector3[] verts, Color3[] colors) {

        // project all into projection space
        Vector2 projVectArray[] = Projection.projectVertexArray(verts);

        // if all are out of view
        int outCounter = 0;
        float aspect = (float)windowW / (float)windowH;
        for (Vector2 projVert : projVectArray) {
            if (projVert.y < -1f || projVert.y > 1f ||
                projVert.x < -aspect || projVert.x > aspect) outCounter++;
        }

        if (outCounter >= 3) return null;

        // generate fragment list
        Fragment frags[] = new Fragment[3];
        for (int i = 0; i < 3; i++) {
            frags[i] = new Fragment(Projection.projToScreenSpace(projVectArray[i], resW, resH),
                    verts[i].z, colors[i]);
        }

        // check for out of view
        float apsect = (float)windowW / (float)windowH;

        // sort by height
        sortFragmentsByY(frags);
        return frags;
    }

    // depth and color interpolating based on distance to 3 point
    private float interpolateFragDepth(int x, int y, Fragment f1, Fragment f2, Fragment f3) {
        float invDist1 = 1.0f /
                Projection.fastDist(f1.x - x,
                        f1.y - y);
        float invDist2 = 1.0f /
                Projection.fastDist(f2.x - x,
                        f2.y - y);
        float invDist3 = 1.0f /
                Projection.fastDist(f3.x - x,
                        f3.y - y);

        // check for winners
        if (Float.isInfinite(invDist1)) return f1.depth;
        if (Float.isInfinite(invDist2)) return f2.depth;
        if (Float.isInfinite(invDist3)) return f3.depth;

        // interpolate
        return (f1.depth * invDist1 + f2.depth * invDist2 + f3.depth * invDist3) /
                (invDist1 + invDist2 + invDist3);
    }

    private float interpolateFragDepthQuad(int x, int y, Fragment f1, Fragment f2, Fragment f3,
                                           Fragment fQ) {
        float invDist1 = 1.0f /
                Projection.fastDist(f1.x - x,
                        f1.y - y);
        float invDist2 = 1.0f /
                Projection.fastDist(f2.x - x,
                        f2.y - y);
        float invDist3 = 1.0f /
                Projection.fastDist(f3.x - x,
                        f3.y - y);
        float invDistQ = 1.0f /
                Projection.fastDist(fQ.y - x,
                        fQ.y - y);

        // check for winners
        if (Float.isInfinite(invDist1)) return f1.depth;
        if (Float.isInfinite(invDist2)) return f2.depth;
        if (Float.isInfinite(invDist3)) return f3.depth;
        if (Float.isInfinite(invDistQ)) return fQ.depth;

        // interpolate
        return (f1.depth * invDist1 + f2.depth * invDist2 + f3.depth * invDist3 + fQ.depth * invDistQ) /
                (invDist1 + invDist2 + invDist3 + invDistQ);
    }

    // inverse slope calculator
    private float invslope(Fragment f1, Fragment f2) {
        return (float)(f2.x - f1.x) / (float)(f2.y - f1.y);
    }

    private Color3 interpolateFragColor(int x, int y, Fragment f1, Fragment f2, Fragment f3) {
        float invDist1 = 1.0f / Projection.fastDist(f1.x - x, f1.y - y);
        float invDist2 = 1.0f / Projection.fastDist(f2.x - x, f2.y - y);
        float invDist3 = 1.0f / Projection.fastDist(f3.x - x, f3.y - y);

        // check for winners
        if (Float.isInfinite(invDist1)) return f1.color;
        if (Float.isInfinite(invDist2)) return f2.color;
        if (Float.isInfinite(invDist3)) return f3.color;

        // interpolate colors
        Color3 col = new Color3();
        col.r = (int)((f1.color.r * invDist1 + f2.color.r * invDist2 + f3.color.r * invDist3) /
                (invDist1 + invDist2 + invDist3));
        col.g = (int)((f1.color.g * invDist1 + f2.color.g * invDist2 + f3.color.g * invDist3) /
                (invDist1 + invDist2 + invDist3));
        col.b = (int)((f1.color.b * invDist1 + f2.color.b * invDist2 + f3.color.b * invDist3) /
                (invDist1 + invDist2 + invDist3));

        return col;
    }

    private Color3 interpolateFragColorQuad(int x, int y, Fragment f1, Fragment f2, Fragment f3,
                                            Fragment fq) {
        float invDist1 = 1.0f / Projection.fastDist(f1.x - x, f1.y - y);
        float invDist2 = 1.0f / Projection.fastDist(f2.x - x, f2.y - y);
        float invDist3 = 1.0f / Projection.fastDist(f3.x - x, f3.y - y);
        float invDistQ = 1.0f / Projection.fastDist(fq.x - x, fq.y - y);

        // check for winners
        if (Float.isInfinite(invDist1)) return f1.color;
        if (Float.isInfinite(invDist2)) return f2.color;
        if (Float.isInfinite(invDist3)) return f3.color;

        // interpolate colors
        Color3 col = new Color3();
        col.r = (int)((f1.color.r * invDist1 + f2.color.r * invDist2 + f3.color.r * invDist3
            + fq.color.r * invDistQ) /
                (invDist1 + invDist2 + invDist3 + invDistQ));
        col.g = (int)((f1.color.g * invDist1 + f2.color.g * invDist2 + f3.color.g * invDist3
                + fq.color.g * invDistQ) /
                (invDist1 + invDist2 + invDist3 + invDistQ));
        col.b = (int)((f1.color.b * invDist1 + f2.color.b * invDist2 + f3.color.b * invDist3
                + fq.color.b * invDistQ) /
                (invDist1 + invDist2 + invDist3 + invDistQ));

        return col;
    }

    // triangle filling func (1 color)
    public void drawTriangle(Vector3[] verts, Color3 color) {
        drawTriangle(verts, new Color3[] { color, color, color} );
    }

    // triangle filling func (no quad)
    public void drawTriangle(Vector3[] verts, Color3[] colors) {
        drawTriangle(verts, colors, null, null);
    }

    // triangle filling func (with quad face support)
    public void drawTriangle(Vector3[] verts, Color3[] colors,
                             Vector3 quadVert, Color3 quadColor) {
        // check for bad values
        if (verts == null)
            throw new RuntimeException("Vertex array was null.");
        if (colors == null)
            throw new RuntimeException("Color array was null");
        if (verts.length < 3)
            throw new RuntimeException("Vertex array too small.");
        if (colors.length < 3)
            throw new RuntimeException("Color array to small.");

        // if any past clipping plane, cull
        for (Vector3 vert : verts) {
            if (vert.z <= 0.5f) return;
        }

        // get normal vector
        Vector3 normal = Vector3.cross(
                verts[1].addCopy(verts[0].multiplyCopy(-1.0f)),
                verts[1].addCopy(verts[2].multiplyCopy(-1.0f))
        );
        normal.normalize();

        // cull if facing away
        if (normal.dot(new Vector3(0, 0, -1)) < 0) return;

        // generate fragments
        // frags are sorted by height from high to low (v1, v2, v3)
        Fragment frags[] = generateFragments(verts, colors);

        // if is null, cull
        if (frags == null) return;

        // take invslope from frag 3 to 1
        // multiply by vertical distance from frag 3 to 2
        float invslope31 = invslope(frags[2], frags[0]);
        float vdist32 = (float)(frags[1].y - frags[2].y);

        // cut tri into 2 by slitting it at the y
        // of frag 2
        // generate 4th fragment
        float frag4x = frags[2].x + invslope31 * vdist32;
        Fragment frag4 = new Fragment(new int[] { (int)frag4x, frags[1].y },
                interpolateFragDepth((int)frag4x, frags[1].y, frags[0], frags[1], frags[2]),
                interpolateFragColor((int)frag4x, frags[1].y, frags[0], frags[1], frags[2]));

        // account for 4th vertex color blending (if exists)
        Fragment qFrag = null;
        if (quadVert != null) {
            qFrag = new Fragment(
                    Projection.projectVertexToScreenSpace(quadVert, resW, resH),
                    quadVert.z, quadColor
            );
        }

        // fill flat bottom
        fillTriangleFlatBottom(frags[0], frags[1], frag4, frags, qFrag);

        // fill flat top
        fillTriangleFlatTop(frags[2], frags[1], frag4, frags, qFrag);
     }

    // flat bottom triangle filling
    // p -> upper point
    // bl -> left base
    // br -> right base
    private void fillTriangleFlatBottom(Fragment p, Fragment bl, Fragment br,
                                        Fragment[] realFrags, Fragment qFrag) {
        // ensure left and right
        if (bl.x > br.x) {
            Fragment temp = bl;
            bl = br;
            br = temp;
        }

        // take invslope from l, r to p
        float LinvSlope = invslope(bl, p);
        float RinvSlope = invslope(br, p);

        // if invslopes are inf, don't draw
        if (Float.isInfinite(LinvSlope) ||
                Float.isInfinite(RinvSlope)) return;

        // walk from bottom to top, calculate points of each side, draw
        int verticalDistance = p.y - bl.y;
        for (int yWalk = 0; yWalk <= verticalDistance; yWalk++) {

            // calculate left and right points
            int lx = bl.x + (int)((float)yWalk * LinvSlope);
            int rx = br.x + (int)((float)yWalk * RinvSlope);

            // draw horizontal line from left to right
            for (int xWalk = lx; xWalk <= rx; xWalk++) {
                int drawX = xWalk;
                int drawY = bl.y + yWalk;

                // if not in view, skip
                if (frameBuffer.outOfView(drawX, drawY)) continue;

                float depth;
                Color3 color;
                // interpolate color
                if (qFrag == null) {
                    color = interpolateFragColor(drawX, drawY,
                            realFrags[0],realFrags[1], realFrags[2]);
                    depth = interpolateFragDepth(drawX, drawY,
                            realFrags[0],realFrags[1], realFrags[2]);
                }
                else {
                    color = interpolateFragColorQuad(drawX, drawY,
                            realFrags[0],realFrags[1], realFrags[2], qFrag);
                    depth = interpolateFragDepthQuad(drawX, drawY,
                            realFrags[0],realFrags[1], realFrags[2], qFrag);
                }
                setPixel(drawX, drawY, color, depth);
            }
        }
    }

    // flat top triangle filling
    // p -> upper point
    // bl -> left base
    // br -> right base
    private void fillTriangleFlatTop(Fragment p, Fragment tl, Fragment tr,
                                     Fragment[] realFrags, Fragment qFrag) {
        // ensure left and right
        if (tl.x > tr.x) {
            Fragment temp = tl;
            tl = tr;
            tr = temp;
        }

        // take invslope from p to l, r
        float LinvSlope = invslope(p, tl);
        float RinvSlope = invslope(p, tr);

        // if invslopes are inf, don't draw
        if (Float.isInfinite(LinvSlope) ||
        Float.isInfinite(RinvSlope)) return;

        // walk from bottom to top, calculate points of each side, draw
        int verticalDistance = tl.y - p.y;
        for (int yWalk = 0; yWalk <= verticalDistance; yWalk++) {

            // calculate left and right points
            int lx = p.x + (int)((float)yWalk * LinvSlope);
            int rx = p.x + (int)((float)yWalk * RinvSlope);

            // draw horizontal line from left to right
            for (int xWalk = lx; xWalk <= rx; xWalk++) {
                int drawX = xWalk;
                int drawY = p.y + yWalk;

                // if not in view, skip
                if (frameBuffer.outOfView(drawX, drawY)) continue;

                float depth;
                Color3 color;
                // interpolate color
                if (qFrag == null) {
                    color = interpolateFragColor(drawX, drawY,
                            realFrags[0],realFrags[1], realFrags[2]);
                    depth = interpolateFragDepth(drawX, drawY,
                            realFrags[0],realFrags[1], realFrags[2]);
                }
                else {
                    color = interpolateFragColorQuad(drawX, drawY,
                            realFrags[0],realFrags[1], realFrags[2], qFrag);
                    depth = interpolateFragDepthQuad(drawX, drawY,
                            realFrags[0],realFrags[1], realFrags[2], qFrag);
                }
                setPixel(drawX, drawY, color, depth);
            }
        }
    }

    // drawquad (1 color version)
    public void drawQuad(Vector3[] verts, Color3 color) {
        drawQuad(verts, new Color3[] {color, color, color, color});
    }

    // draws two triangles using different verts of the quad
    public void drawQuad(Vector3[] verts, Color3[] colors) {
        // check for bad values
        if (verts == null)
            throw new RuntimeException("Vertex array was null.");
        if (colors == null)
            throw new RuntimeException("Color array was null");
        if (verts.length < 4)
            throw new RuntimeException("Vertex array too small.");
        if (colors.length < 4)
            throw new RuntimeException("Color array to small.");

        // draw 2 triangles
        drawTriangle(new Vector3[] { verts[0], verts[1], verts[3]},
                new Color3[] { colors[0], colors[1], colors[3]},
                verts[2], colors[2]);
        drawTriangle(new Vector3[] { verts[1], verts[2], verts[3]},
                new Color3[] { colors[1], colors[2], colors[3]},
                verts[0], colors[0]);
    }

}
