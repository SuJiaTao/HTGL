package TGL.Prefabs;

// Bailey Jia-Tao Brown
// 2023
// A simple human worldobject for totally legit reasons

import TGL.*;
import TGL.Vector.*;

public class Human extends WorldObject {

    // Ctor
    public Human(Color3 clothesColor, Color3 skinColor, Vector3 pos, Vector3 rot, float size) {
        super(pos, rot, new Vector3(size, size, size), null);

        // create feet
        addRenderBox(new RenderBox(
                new Vector3(-0.3f, 0, 0.1f), // left and back
                new Vector3(),
                new Vector3(0.125f, 0.05f, 0.2f),
                new Color3(128, 128, 128) // grey shoes
        ));

        addRenderBox(new RenderBox(
                new Vector3(0.3f, 0, 0.1f), // right and back
                new Vector3(),
                new Vector3(0.125f, 0.05f, 0.2f),
                new Color3(128, 128, 128) // grey shoes
        ));

        // create legs
        addRenderBox(new RenderBox(
                new Vector3(-0.3f, 0.5f, 0.15f), // left
                new Vector3(),
                new Vector3(0.1f, 0.45f, 0.1f),
                clothesColor.scale(0.5f) // darker pants
        ));

        addRenderBox(new RenderBox(
                new Vector3(0.3f, 0.5f, 0.15f), // right
                new Vector3(),
                new Vector3(0.1f, 0.45f, 0.1f),
                clothesColor.scale(0.5f) // darker pants
        ));

        // create upper pants
        addRenderBox(new RenderBox(
                new Vector3(0, 1.15f, 0.15f),
                new Vector3(),
                new Vector3(0.45f, 0.2f, 0.2f),
                clothesColor.scale(0.5f) // darker pants
        ));

        // create shirt
        addRenderBox(new RenderBox(
                new Vector3(0, 1.85f, 0.15f),
                new Vector3(),
                new Vector3(0.45f, 0.5f, 0.2f),
                clothesColor
        ));

        // create sleeves
        addRenderBox(new RenderBox(
                new Vector3(0.60f, 2.05f, 0.15f), // left
                new Vector3(),
                new Vector3(0.15f, 0.2f, 0.2f),
                clothesColor
        ));

        addRenderBox(new RenderBox(
                new Vector3(-0.60f, 2.05f, 0.15f), // right
                new Vector3(),
                new Vector3(0.15f, 0.2f, 0.2f),
                clothesColor
        ));

        // create arms
        addRenderBox(new RenderBox(
                new Vector3(0.60f, 1.5f, 0.15f), // left
                new Vector3(),
                new Vector3(0.075f, 0.5f, 0.10f),
                skinColor
        ));

        addRenderBox(new RenderBox(
                new Vector3(-0.60f, 1.5f, 0.1f), // right
                new Vector3(),
                new Vector3(0.075f, 0.5f, 0.10f),
                skinColor
        ));

        // make neck
        addRenderBox(new RenderBox(
                new Vector3(0, 2.4f, 0.15f),
                new Vector3(),
                new Vector3(0.1f, 0.1f, 0.1f),
                skinColor
        ));

        // create head
        addRenderBox(new RenderBox(
                new Vector3(0, 2.85f, 0.15f),
                new Vector3(),
                new Vector3(0.35f, 0.35f, 0.35f),
                skinColor
        ));
    }

    // Update func (unused)
    protected void update() {
        // do nothing
    }
}
