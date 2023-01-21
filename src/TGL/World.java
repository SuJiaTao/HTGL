package TGL;

// Bailey Jia-Tao Brown
// 2023
// Abstracted "context" class
// Holds the rendering context as well as all the
// objects rendered within it

import TGL.Internal.*;
import TGL.Vector.*;
import java.util.*;

public final class World {

    // definitions
    private static final int INITIAL_WORLDOBJECT_CAPACITY = 512;
    // init flag
    private static boolean initialized = false;

    // context
    private static Context context;

    // list of all objects
    private static ArrayList<WorldObject> worldObjects;

    // internal meshes
    private static Vector3[] quadMesh;
    private static Vector3[][] cubeMesh;

    // Initialize world function
    public static void initialize(String title, int windowWidth, int windowHeight,
                                  float renderScale) {
        // check already init
        if (initialized)
            throw new RuntimeException("World already initialized!");

        // check for bad inputs
        if (windowWidth <= 0 || windowHeight <= 0)
            throw new RuntimeException("Window size too small!");

        // check for bad renderscale
        if (renderScale < 0.1f || renderScale > 1.0f)
            throw new RuntimeException("Invalid renderscale. Renderscale can be 0.1 to 1.0");

        // init context
        context = new Context(title, windowWidth, windowHeight,
                (int)(windowWidth * renderScale),
                (int)(windowHeight * renderScale), new Color3(0, 0, 0));

        // init world object list
        worldObjects = new ArrayList<>(INITIAL_WORLDOBJECT_CAPACITY);

        // set init flag to true
        initialized = true;

        // initialize quadmesh
        quadMesh = new Vector3[4];
        quadMesh[0] = new Vector3(-1, -1);
        quadMesh[1] = new Vector3(-1, 1);
        quadMesh[2] = new Vector3(1, 1);
        quadMesh[3] = new Vector3(1, -1);

        // generate cubemesh
        generateCubeMesh();
    }

    // Get context object
    public static Context getContext() {
        return context;
    }

    // Add world object
    public static void addWorldObject(WorldObject obj) {
        // check for not init
        if (!initialized)
            throw new RuntimeException("World must be initialized before creating world objects!");

        if (worldObjects.contains(obj))
            throw new RuntimeException("Cannot add the same world object twice!");

        worldObjects.add(obj);
    }

    private static void generateCubeMesh() {
        // make cube
        cubeMesh = new Vector3[6][];

        // generate 1 face of the cube
        // the face sticks out in the positive Z direction
        Vector3[] face = Matrix.transform(quadMesh,
                new Vector3(0, 0, 1),
                new Vector3(0, 0, 0),
                new Vector3(1, 1, 1));

        // make 4 sides of the box
        // this forms a "ring" around the positive X
        // rotational axis
        for (int side = 0; side < 4; side++) {
            cubeMesh[side] = Matrix.transform(face,
                    new Vector3(0, 0, 0),
                    new Vector3(90 * side, 0, 0),
                    new Vector3(1, 1, 1));
        }

        // make last 2 sides of the box
        cubeMesh[4] = Matrix.transform(face,
                new Vector3(0, 0, 0),
                new Vector3(0, 90, 0),
                new Vector3(1, 1, 1));
        cubeMesh[5] = Matrix.transform(face,
                new Vector3(0, 0, 0),
                new Vector3(0, -90, 0),
                new Vector3(1, 1, 1));
    }

    // transform cube function
    private static Vector3[][] transformCube(Vector3[][] cube,
                                             Vector3 trl,
                                             Vector3 rot,
                                             Vector3 scl) {
        Vector3 tCube[][] = new Vector3[6][];
        for (int i = 0; i < 6; i++) {
            tCube[i] = Matrix.transform(cube[i],
                    trl, rot, scl);
        }
        return tCube;
    }

    // internal draw box function
    private static void drawRenderBox(WorldObject parent, RenderBox box) {
        // transform by box
        Vector3[][] mesh = transformCube(cubeMesh,
                box.localPosition, box.localRotation, box.localScale);

        // transform by parent
        mesh = transformCube(mesh,
                parent.position, parent.rotation, parent.scale);

        // draw all quad faces
        for (int i = 0; i < 6; i++) {
            context.drawQuad(mesh[i], box.color);
        }
    }

    // Update world
    public static void update() {
        context.clearWindow();

        for (WorldObject obj : worldObjects) {
            for (RenderBox box : obj.getBoxList()) {
                drawRenderBox(obj, box);
            }
        }

        context.refreshWindow();
    }
}
