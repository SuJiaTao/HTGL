import TGL.Color3;
import TGL.Context;
import TGL.Matrix;
import TGL.Vector.Vector3;

public class App {
    public static void main(String[] args) {
        // create rendering context
        Context context = new Context("Test", 500, 500,
                250, 250,
                new Color3(0, 0, 0));

        Vector3 verts[] = new Vector3[3];
        Color3 cols[] = new Color3[3];
        for (int i = 0; i < 3; i++) {
            verts[i] = new Vector3();
            cols[i] = new Color3();
        }
        verts[0].set(-1.0f, -1.0f, 0.0f);
        cols[0].set(255, 0, 0);
        verts[1].set(-1.0f, 1.0f, 0.0f);
        cols[1].set(255, 255, 255);
        verts[2].set(1.0f, -1.0f, 0.0f);
        cols[2].set(255, 255, 0);

        // inf draw loop
;        while (true) {

            context.clearWindow();

            double phase = (double)System.currentTimeMillis() * 0.0006;
            float cosOut = (float)Math.cos(phase) * 0.5f;

            Vector3 tVerts[] = new Vector3[3];
            for (int i = 0; i < verts.length; i++ ){
                tVerts[i] = verts[i].copy();
                tVerts[i] = Matrix.rotate(tVerts[i],
                        new Vector3(cosOut * 180, 0, 0));
                tVerts[i] = Matrix.translate(tVerts[i], new Vector3(0.0f, 0.0f, 2.0f));
            }

            context.drawTriangle(tVerts, cols);
            context.drawLinePolygon(tVerts, cols);

            context.pause(1);
            context.refreshWindow();
        }
    }
}
