package com.mygdx.libgdx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Hang Chen on 6/5/2016.
 */
public class Entity {
    private static final String TAG = Entity.class.getSimpleName();
    private static final String _defaultSpritePath = "sprites/characters/Warrior.png";

    private Vector2 _velocity;
    private String _entityID;


    private Direction _currentDirection = Direction.LEFT;
    private Direction _previousDirection = Direction.UP;

    private Animation _walkLeftAnimation;
    private Animation _walkUpAnimation;
    private Animation _walkDownAnimation;
    private Animation _walkRightAnimation;

    private Array<TextureRegion> _walkLeftFrames;
    private Array<TextureRegion> _walkUpFrames;
    private Array<TextureRegion> _walkDownFrames;
    private Array<TextureRegion> _walkRightFrames;

    protected Vector2 _nextPlayerPosition;
    protected Vector2 _currentPlayerPosition;
    protected  State _state = State.IDLE;
    protected float _frameTime = 0f;
    protected Sprite _framSprite = null;
    protected TextureRegion _currentFrame = null;

    public final int FRAME_WIDTH = 16;
    public final int FRAME_HEIGHT = 16;
    public static Rectangle boundingBox;

    public enum State {
        IDLE, WALKING
    }

    public enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    public Enity() {
        initEntity();
    }

    public void initEntity() {

    }

    public void update(float delta) {

    }

    public void init(float startX, float startY) {

    }

    public void setBoundingBoxSize(float percentageWidthReduced, float percentageHeightReduced) {

    }

    private void loadDefaultSprite() {

    }

    public void dispose() {

    }

    public void setDirection(Direction direction, float deltaTime) {

    }

    public void setNextPositionToCurrent() {

    }

    public void calculateNextPosition(Direction currentDirection, float deltaTime) {

    }

}
