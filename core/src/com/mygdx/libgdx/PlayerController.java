package com.mygdx.libgdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hang Chen on 6/5/2016.
 */
public class PlayerController implements InputProcessor {
    private static final String TAG = PlayerController.class.getSimpleName();

    enum Keys {
        LEFT, RIGHT, UP, DOWN, QUIT
    }

    enum Mouse {
        SELECT, DOACTION
    }

    private static Map<Keys, Boolean> keys = new HashMap<PlayerController.Keys, Boolean>();
    private static Map<Mouse, Boolean> mouseButtons = new HashMap<PlayerController.Mouse, Boolean>();
    private Vector3 lastMouseCooredinates;

    // initialize the hashmap for inputs
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.QUIT, false);
    }

    private Entity _player;

    public PlayerController(Entity player) {
        this.lastMouseCooredinates = new Vector3();
        this._player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
            this.leftPressed();

        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
            this.rightPressed();

        if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
            this.upPressed();

        if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
            this.downPressed();

        if(keycode == Input.Keys.Q)
            this.quitPressed();

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
            this.leftRelased();

        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
            this.rightReleased();

        if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
            this.upReleased();

        if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
            this.downReleased();

        if(keycode == Input.Keys.Q)
            this.quitReleased();

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }
}
