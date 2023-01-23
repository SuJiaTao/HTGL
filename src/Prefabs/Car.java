package Prefabs;

// Bailey Jia-Tao Brown
// 2023
// Simple colorable car worldobject for your rendering pleasures

import TGL.*;
import TGL.Vector.*;

public class Car extends WorldObject {
    // Ctor
    public Car(Color3 color, Vector3 pos, Vector3 rot, float size) {
        super(pos, rot, new Vector3(size, size ,size), null);

        // make main body of car
        addRenderBox(new RenderBox(
                new Vector3(0, 0.5f, 0),
                new Vector3(0, 0, 0),
                new Vector3(2.75f, 0.5f, 1.50f),
                color
        ));

        // make upper frame of car
        addRenderBox(new RenderBox(
                new Vector3(-0.2f, 1.0f, 0),
                new Vector3(0, 0, 0),
                new Vector3(2.25f, 1.0f, 1.25f),
                color));

        // make wheels of car
        addRenderBox(new RenderBox(
                new Vector3(-2.15f, 0, -1.5f), // back left
                new Vector3(0, 0, 45), // tilted
                new Vector3(0.35f, 0.35f, 0.2f), // thin along Z axis
                new Color3(128, 128, 128)));

        addRenderBox(new RenderBox(
                new Vector3(-2.15f, 0, 1.5f), // back right
                new Vector3(0, 0, 45), // tilted
                new Vector3(0.35f, 0.35f, 0.2f), // thin along Z axis
                new Color3(128, 128, 128)));

        addRenderBox(new RenderBox(
                new Vector3(2.15f, 0, -1.5f), // front left
                new Vector3(0, 0, 45), // tilted
                new Vector3(0.35f, 0.35f, 0.2f), // thin along Z axis
                new Color3(128, 128, 128)));

        addRenderBox(new RenderBox(
                new Vector3(2.15f, 0, 1.5f), // front right
                new Vector3(0, 0, 45), // tilted
                new Vector3(0.35f, 0.35f, 0.2f), // thin along Z axis
                new Color3(128, 128, 128)));
    }

    // Update function
    protected void update() {
        // do nothing
    }
}
