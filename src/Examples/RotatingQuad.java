package Examples;

// Bailey Jia-Tao Brown
// 2023
// Rotating quad meshes

import TGL.Color3;
import TGL.Internal.Context;
import TGL.Internal.Matrix;
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
            float cosOut = (float) Math.cos(phase);
            float sinOut = (float) Math.sin(phase);

            // create transformed vert list
            context.drawQuad(
                    Matrix.transform(vertBox,
                            new Vector3(0, 0, 5),
                            new Vector3(cosOut * 90, sinOut * 270, sinOut * 45),
                            new Vector3(1, 1, 1)), colorBox);
            context.drawLinePolygon(
                    Matrix.transform(vertBox,
                            new Vector3(0, 0, 5),
                            new Vector3(sinOut * 90, sinOut * 270, sinOut * 45),
                            new Vector3(1.5f, 1.5f, 1.5f)), colorBox);
            context.drawLinePolygon(
                    Matrix.transform(vertBox,
                            new Vector3(0, 0, 5),
                            new Vector3(cosOut * 90, cosOut * 270, sinOut * 45),
                            new Vector3(2.0f, 2.0f, 2.0f)), colorBox);

            // pause to stabilize framerate and refresh
            context.pause(1);
            context.refreshWindow();
        }
    }
}
