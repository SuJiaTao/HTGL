package Examples;

// Bailey Jia-Tao Brown
// 2023
// Simple example of 2 spirals with depth-testing and color blending

import TGL.Color3;
import TGL.Internal.Context;
import TGL.Vector.Vector3;

public final class SpiralA {
    public static void main(String[] args) {
        // create new context
        Context c = new Context("--- Spiral A ---", 750, 500,
                375, 250,
                new Color3(0, 0, 0));

        // generate spiral verts + colors
        final int LEN = 100;
        Vector3 verts[] = new Vector3[LEN];
        Color3 colors[] = new Color3[LEN];
        Color3 colors2[] = new Color3[LEN];
        for (int i = 0; i < verts.length; i++){
            verts[i] = new Vector3(0.5f, 0.5f, i * 1.0f);
            if ((i & 1) == 0) {
                colors[i] = new Color3(255, 32, 128);
                colors2[i] = new Color3(32, 255, 128);
            }
            else {
                colors[i] = new Color3(255, 255, 255);
                colors2[i] = new Color3(255, 255, 255);
            }

        }

        // infinite loop
        while (true) {
            // clear window
            c.clearWindow();

            // generate spiral 1 with vert array
            // note: this is bad practice
            double phase = System.currentTimeMillis() * 0.0008;
            for (int i = 0; i < verts.length; i++){
                double vStep = (double)i;
                Vector3 vert = verts[i];

                // generate irregular spiral
                vert.x = (float)Math.cos(phase * 3.0 + vStep * 0.31) * 2.25f;
                vert.y = (float)Math.sin(phase * 3.0 + vStep * 0.31) * 2.25f;
                vert.z = (float)(vStep * 0.5);

                // translate spiral
                vert.x += (float) Math.cos(phase * 0.5) * 2.0f;
                vert.y += (float) Math.sin(phase * 0.75) * 3.0f;
            }

            // draw spiral 1
            c.drawLineArray(verts, colors);

            // generate sprial 2
            for (int i = 0; i < verts.length; i++){
                double vStep = (double)i;
                Vector3 vert = verts[i];

                // generate irregular spiral
                vert.x = (float)Math.cos(phase * 1.3 + vStep * 0.11) * 0.55f;
                vert.y = (float)Math.sin(phase * 1.2 + vStep * 0.21) * 0.55f;
                vert.z = (float)(vStep * 0.5);

                // translate spiral
                vert.x += (float) Math.cos(phase * 0.5) * 2.0f;
                vert.y += (float) Math.sin(phase * 0.75) * 3.0f;
            }

            c.drawLineArray(verts, colors2);

            // sleep to regulate refreshrate
            c.pause(1);

            // refresh window
            c.refreshWindow();
        }
    }

}
