package TGL.Internal;

// Bailey Jia-Tao Brown
// 2023
// Simple input handler which attaches to context instance

import TGL.Vector.Vector2;

import javax.swing.*;
import java.awt.event.*;

public final class InputHandler implements KeyListener, MouseListener {
    //defs
    private static final int MAX_KEYS_DOWN = 10;

    // key list
    private int[] currentKeysDown;
    private char[] currentCharsDown;
    private boolean mouseDown;

    // ctor
    public InputHandler() {
        currentKeysDown = new int[MAX_KEYS_DOWN];
        currentCharsDown = new char[MAX_KEYS_DOWN];
        mouseDown = false;
    }

    // check if key is down
    public boolean isKeyDown(int keyCode) {
        for (int key : currentKeysDown) {
            if (keyCode == key) return true;
        }
        return false;
    }

    // check if char is down
    public boolean isCharDown(char letter) {
        for (char c : currentCharsDown) {
            if (c == letter) return true;
        }
        return false;
    }

    public boolean isCharDownIgnoreCase(char letter) {
        for (char c : currentCharsDown) {
            if (Character.toLowerCase(c) ==
                    Character.toLowerCase(letter)) return true;
        }
        return false;
    }

    // get arrow keys vector
    public Vector2 getArrowKeyDirection() {
        Vector2 vector = new Vector2();
        if (isKeyDown(KeyEvent.VK_UP)) vector.y += 1;
        if (isKeyDown(KeyEvent.VK_DOWN)) vector.y -= 1;
        if (isKeyDown(KeyEvent.VK_LEFT)) vector.x -= 1;
        if (isKeyDown(KeyEvent.VK_RIGHT)) vector.x += 1;

        return vector;
    }

    // get WASD vector
    public Vector2 getWASDDirection() {
        Vector2 vector = new Vector2();
        if (isCharDownIgnoreCase('W'))  vector.y += 1;
        if (isCharDownIgnoreCase('A'))  vector.x -= 1;
        if (isCharDownIgnoreCase('S'))  vector.y -= 1;
        if (isCharDownIgnoreCase('D'))  vector.x += 1;

        return vector;
    }

    // event callback
    // this input handler logic is taken from my old
    // 2022 culminating project so the coding style
    // is a little different
    public void keyPressed(KeyEvent event)
    {
        int scannedKeyCode = event.getKeyCode( );
        char scannedChar = event.getKeyChar( );

        /* find free space in ckd */
        for(int i = 0; i < MAX_KEYS_DOWN; i++)
        {
            /* check if matching (key already down!) */
            if(currentKeysDown[i] == scannedKeyCode)
            {
                break;
            }

            /* free space */
            if(currentKeysDown[i] == 0)
            {
                currentKeysDown[i] = scannedKeyCode;
                break;
            }
        }

        /* find free space in cc */
        for(int i = 0; i < MAX_KEYS_DOWN; i++)
        {
            /* check if matching (key already down!) */
            if(currentCharsDown[i] == scannedChar)
            {
                break;
            }

            /* fill empty space */
            if(currentCharsDown[i] == 0)
            {
                currentCharsDown[i] = scannedChar;
                break;
            }
        }
    }

    public void keyTyped(KeyEvent event)
    {
        /* do nothing */
    }

    public void keyReleased(KeyEvent event)
    {
        /* get keys up */
        int getKeyUp  = event.getKeyCode( );
        int getCharUp = event.getKeyChar( );

        /* loop and find match */
        for(int i = 0; i < MAX_KEYS_DOWN; i++)
        {
            /* clear if matching */
            if(getKeyUp == currentKeysDown[i])
            {
                currentKeysDown[i] = 0;
            }
            if(getCharUp == currentCharsDown[i])
            {
                currentCharsDown[i] = 0;
            }
        }
    }

    public void mouseClicked(MouseEvent e) { }

    public void mousePressed(MouseEvent e)
    {
        mouseDown = true;
    }

    public void mouseReleased(MouseEvent e)
    {
        mouseDown = false;
    }

    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseDragged(MouseEvent e) { }
    public void mouseMoved(MouseEvent e)
    {

    }

}
