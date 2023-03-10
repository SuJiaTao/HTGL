package TGL.Prefabs;

// Bailey Jia-Tao Brown
// 2023
// "Procedural" tree worldobejct

import TGL.Vector.*;
import TGL.*;
import java.util.Random;

public class Tree extends WorldObject{
    // Ctor
    public Tree(Vector3 position, float height, float size) {
        super(position, new Vector3(), new Vector3(size, size, size), null);

        // create tree trunk
        addRenderBox(new RenderBox(
                new Vector3(0, height - 0.15f, 0),
                new Vector3(0, Rand.nextFloat() * 360, 0),
                new Vector3(0.25f, height, 0.25f),
                new Color3(155, 130, 100) // brown
        ));

        // create leaves
        int leafCount = Rand.nextInt(15, 20);

        for (int i = 0; i < leafCount; i++) {
            float halfHeight = height * 0.5f;
            // generate random position
            float leafX = Rand.nextFloat(-halfHeight, halfHeight);
            float leafY = Rand.nextFloat(height);
            float leafZ = Rand.nextFloat(-halfHeight, halfHeight);

            // generate random rotation
            float leafRotX = Rand.nextFloat() * 360;
            float leafRotY = Rand.nextFloat() * 360;
            float leafRotZ = Rand.nextFloat() * 360;

            // generate random scales
            float leafScaleX = Rand.nextFloat(height * 0.15f, height * 0.35f);
            float leafScaleY = Rand.nextFloat(height * 0.15f, height * 0.35f);
            float leafScaleZ = Rand.nextFloat(height * 0.15f, height * 0.35f);

            // generate random leaf brightness
            float leafBrightness = Rand.nextFloat(0.65f, 1.0f);

            // create leaf
            addRenderBox(new RenderBox(
                    new Vector3(leafX, height * 1.5f + leafY, leafZ),
                    new Vector3(leafRotX, leafRotY, leafRotZ),
                    new Vector3(leafScaleX, leafScaleY, leafScaleZ),
                    new Color3(91, 204, 79).scale(leafBrightness) // greenish
            ));
        }
    }

    // Update func (unused)
    protected void update() {
        //unused
    }
}
