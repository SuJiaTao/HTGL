package Examples;

// Bailey Jia-Tao Brown
// 2023
// Rotating quad mesh

import TGL.Color3;
import TGL.Context;
import TGL.Matrix;
import TGL.Vector.Vector3;

public class RotatingQuad {
    public static void main(String[] args) {
        // create rendering context
        Context context = new Context("Test", 500, 500,
                250, 250,
                new Color3(0, 0, 0));

        // create vert box and colors
        Vector3 vertBox[] = new Vector3[4];
        Color3 colorBox[] = new Color3[4];
        for (int i = 0; i < vertBox.length; i++) {
            vertBox[i] = new Vector3();
            colorBox[i] = new Color3();
        }
        vertBox[0].set(-1, -1, 0);
        vertBox[1].set(-1, 1, 0);
        vertBox[2].set(1, 1, 0);
        vertBox[3].set(1, -1, 0);
        colorBox[0].set(255, 0, 0);
        colorBox[1].set(255, 0, 255);
        colorBox[2].set(255, 255, 255);
        colorBox[3].set(255, 255, 0);

        // inf draw loop
        while (true) {

            // clear window
            context.clearWindow();

            // calculate cosine output
            double phase = (double) System.currentTimeMillis() * 0.0006;
            float cosOut = (float) Math.cos(phase) * 0.5f;

            // create transformed vert list
            Vector3 tvertBox[] = new Vector3[4];
            for (int i = 0; i < vertBox.length; i++) {
                tvertBox[i] = vertBox[i].copy();
                // transform
                tvertBox[i] =
                        Matrix.rotate(tvertBox[i], new Vector3(cosOut * 180, cosOut * 45, cosOut * 360));
                tvertBox[i] =
                        Matrix.translate(tvertBox[i], new Vector3(0, 0, 3));
            }

            // draw transformed polygon
            context.drawLinePolygon(tvertBox, colorBox);

            // pause to stabilize framerate and refresh
            context.pause(1);
            context.refreshWindow();
        }
    }
}
