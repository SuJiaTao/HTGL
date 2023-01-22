package TGL;

// Bailey Jia-Tao Brown
// 2023
// Simple abstracted input class that wraps
// InputHandler within Context within World

import TGL.Internal.*;
import TGL.Vector.Vector2;

public final class Input {

    // check for keycode
    public static boolean isKeyPressed(int keyCode) {
        if (!World.isInitialized())
            throw new RuntimeException("Cannot check inputs until world is initialized.");

        return World.getContext().inputHandler.isKeyDown(keyCode);
    }

    // check for char down
    public static boolean isCharPressed(char character) {
        if (!World.isInitialized())
            throw new RuntimeException("Cannot check inputs until world is initialized.");

        return World.getContext().inputHandler.isCharDown(character);
    }

    public static boolean isCharPressedIgnoreCase(char character) {
        if (!World.isInitialized())
            throw new RuntimeException("Cannot check inputs until world is initialized.");

        return World.getContext().inputHandler.isCharDownIgnoreCase(character);
    }

    // arrow keys / WASD inputs
    public static Vector2 getArrowKeyDirection() {
        if (!World.isInitialized())
            throw new RuntimeException("Cannot check inputs until world is initialized.");

        return World.getContext().inputHandler.getArrowKeyDirection();
    }

    public static Vector2 getWASDDirection() {
        if (!World.isInitialized())
            throw new RuntimeException("Cannot check inputs until world is initialized.");

        return World.getContext().inputHandler.getWASDDirection();
    }
}
