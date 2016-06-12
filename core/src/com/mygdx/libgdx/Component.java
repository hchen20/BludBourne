package com.mygdx.libgdx;

/**
 * Created by Hang Chen on 6/12/2016.
 */
public interface Component {
    public static final String MESSAGE_TOKEN = ":::::";

    public static enum MESSAGE {
        CURRENT_POSITION,
        INIT_START_POSITION,
        CURRENT_DIRECTION,
        CURRENT_STATE,
        COLLISION_WITH_MAP,
        COLLISION_WITH_ENTITY,
        LOAD_ANIMATIONS,
        INIT_DIRECTIONS,
        INIT_SELECT_ENTITY,
        INIT_STATE,
        ENTITY_SELECTED,
        ENTITY_DESELECTED
    }

    void dispose();
    void receiveMessage(String message);
}
