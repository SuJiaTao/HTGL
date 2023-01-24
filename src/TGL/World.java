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
    private static final int INITIAL_LIGHT_CAPACITY = 64;

    // init flag
    private static boolean initialized = false;

    // context
    private static Context context;

    // lists of all lights and objects
    private static ArrayList<WorldObject> worldObjects;
    private static ArrayList<Light> lights;
    private static float ambientLight = 0.25f;

    // internal meshes
    private static Vector3[] quadMesh;
    private static Vector3[][] cubeMesh;

    // camera transform
    private static Vector3 cameraPos = new Vector3();
    private static Vector3 cameraRot = new Vector3();

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

        // init world and light object list
        worldObjects = new ArrayList<>(INITIAL_WORLDOBJECT_CAPACITY);
        lights = new ArrayList<>(INITIAL_LIGHT_CAPACITY);

        // set init flag to true
        initialized = true;

        // initialize quadmesh
        // 5th vertex is the average vertex position
        quadMesh = new Vector3[5];
        quadMesh[0] = new Vector3(-1, -1);
        quadMesh[1] = new Vector3(-1, 1);
        quadMesh[2] = new Vector3(1, 1);
        quadMesh[3] = new Vector3(1, -1);
        quadMesh[4] = new Vector3(0.5f, 0.5f);

        // generate cubemesh
        generateCubeMesh();
    }

    // Get context object
    public static Context getContext() {
        return context;
    }

    // Set ambient light
    public static void setAmbientLight(float val) {
        ambientLight = val;
    }

    // Check if initialized
    public static boolean isInitialized() {
        return initialized;
    }

    // Add world object
    public static void internalAddWorldObject(WorldObject obj) {
        // check for not init
        if (!initialized)
            throw new RuntimeException("World must be initialized before creating world objects!");

        if (worldObjects.contains(obj))
            throw new RuntimeException("Cannot add the same world object twice!");

        worldObjects.add(obj);
    }

    // Add light object
    public static void internalAddLight(Light light) {
        // check for not init
        if (!initialized)
            throw new RuntimeException("World must be initialized before creating lights!");

        if (lights.contains(light))
            throw new RuntimeException("Cannot add the same light twice!");

        lights.add(light);
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
                new Vector3(0, 270, 0),
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

        // traverse parent heigharchy and transform
        WorldObject wParent = parent.parent;
        while (wParent != null) {
            mesh = transformCube(mesh,
                    wParent.position, wParent.rotation, wParent.scale);
            wParent = wParent.parent;
        }

        // transform by camera
        Vector3 camMove = cameraPos.multiplyCopy(-1.0f);
        Vector3 camRotate = cameraRot.multiplyCopy(-1.0f);
        mesh = transformCube(mesh,
                camMove, new Vector3(0, 0, 0), new Vector3(1.0f, 1.0f, 1.0f));
        mesh = transformCube(mesh,
                new Vector3(0, 0, 0), camRotate, new Vector3(1.0f, 1.0f, 1.0f));

        // draw all quad faces
        for (int i = 0; i < 6; i++) {
            // get quad verts
            Vector3[] qVerts = mesh[i];

            // accumulate light factor
            float lightFactor = 0.0f;

            // loop all lights that exist
            for (Light light : lights) {

                // transform by parent heigharchy
                WorldObject lParent = light.parent;
                Vector3 lightPosActual = light.position;

                while (lParent != null) {
                    lightPosActual =
                            Matrix.rotate(lightPosActual, lParent.rotation);
                    lightPosActual =
                            Matrix.translate(lightPosActual, lParent.position);
                    lParent = lParent.parent;
                }

                // get actual light position
                lightPosActual =
                        Matrix.translate(lightPosActual, camMove);
                lightPosActual =
                        Matrix.rotate(lightPosActual, camRotate);

                // get actual distance (cannot be > radius)
                float dist = lightPosActual.distance(qVerts[4]);

                // clamp distance and generate light factor
                // pre-normal mult
                dist = Math.min(light.radius, dist);
                // do generate face normal
                Vector3 normal = Vector3.cross(
                        qVerts[1].addCopy(qVerts[0].multiplyCopy(-1.0f)),
                        qVerts[1].addCopy(qVerts[2].multiplyCopy(-1.0f))
                );
                normal.normalize();

                // generate vector pointing towards face
                Vector3 lightDir = lightPosActual.addCopy(qVerts[4].multiplyCopy(-1.0f));
                lightDir.normalize();

                // multiply by dot product
                lightFactor += (1.0f - (dist / light.radius)) * Math.max(0, lightDir.dot(normal));

                // if lightfactor > 1, done
                if (lightFactor > 1.0f) break;
            }

            // darken by lightfactor
            context.drawQuad(mesh[i], box.color.scale(ambientLight + lightFactor));
        }
    }

    // Camera functions
    public static void setCameraPos(Vector3 pos) {
        cameraPos = pos.copy();
    }

    public static void setCameraPos(float x, float y, float z) {
        setCameraPos(new Vector3(x, y, z));
    }

    public static void setCameraRotation(Vector3 rot) {
        cameraRot = rot.copy();
    }

    public static void setCameraRotation(float x, float y, float z) {
        setCameraRotation(new Vector3(x, y, z));
    }

    public static void translateCamera(Vector3 vect) {
        cameraPos.add(vect);
    }

    public static void translateCamera(float x, float y, float z) {
        translateCamera(new Vector3(x, y, z));
    }

    public static void rotateCamera(Vector3 rotation) {
        cameraRot.add(rotation);
    }

    public static void rotateCamera(float x, float y, float z) {
        rotateCamera(new Vector3(x, y, z));
    }

    public static void moveCameraRelativeToLooking(Vector3 vect) {
        Vector3 moveDir = Matrix.rotate(vect,
                new Vector3(cameraRot.x, 0, 0));
        moveDir = Matrix.rotate(moveDir,
                new Vector3(0, cameraRot.y, 0));
        cameraPos.add(moveDir);
    }

    public static void moveCameraRelativeToLooking(float x, float y, float z) {
        moveCameraRelativeToLooking(new Vector3(x, y, z));
    }

    // Pause
    public static void pause(long miliseconds) {
        context.pause(miliseconds);
    }

    // Update world
    public static void update() {
        context.clearWindow();

        for (WorldObject obj : worldObjects) {
            // check for no draw
            if (obj.show == false) continue;

            // draw all boxes
            for (RenderBox box : obj.getBoxList()) {
                drawRenderBox(obj, box);
            }
        }

        context.pause(1); // pause 1 msec to regulate
        context.refreshWindow();
    }
}
