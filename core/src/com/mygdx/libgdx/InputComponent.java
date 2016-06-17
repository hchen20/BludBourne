package com.mygdx.libgdx;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hang Chen on 6/12/2016.
 */
public abstract class InputComponent implements Component, InputProcessor {
    protected Entity.Direction _currentDirection = null;
    protected Entity.State _currentState = null;
    protected Json _json;

    protected enum Keys {
        LEFT, RIGHT, UP, DOWN, QUIT, PAUSE
    }

    protected enum Mouse {
        SELECT, DOACTION
    }

    protected static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
    protected  static Map<Mouse, Boolean> mouseButtons = new HashMap<Mouse, Boolean>();

    static {
        keys.put(Keys.DOWN, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.LEFT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.QUIT, false);
        keys.put(Keys.PAUSE, false);

        mouseButtons.put(Mouse.DOACTION, false);
        mouseButtons.put(Mouse.SELECT, false);
    }

    InputComponent() {
        _json = new Json();
    }

    public abstract void update(Entity entity, float delta);
}
