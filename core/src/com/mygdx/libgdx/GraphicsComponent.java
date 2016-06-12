package com.mygdx.libgdx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.Hashtable;

/**
 * Created by Hang Chen on 6/12/2016.
 */
public abstract class GraphicsComponent implements Component {
    protected TextureRegion _currentFrame = null;
    protected float _frameTime = 0f;
    protected Entity.State _currentState;
    protected Entity.Direction _currentDirection;
    protected Json _json;
    protected Vector2 _currentPosition;
    protected Hashtable<Entity.AnimationType, Animation> _animations;
    protected ShapeRenderer _shapeRenderer;

    protected GraphicsComponent() {
        _currentPosition = new Vector2(0, 0);
        _currentState = Entity.State.WALKING;
        _currentDirection = Entity.Direction.DOWN;
        _json = new Json();
        _animations = new Hashtable<Entity.AnimationType, Animation>();
        _shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(Entity entity, MapManager mapManager, Batch batch, float delta);

    protected void updateAnimations(float delta) {
        // Want to avoid overflow
        _frameTime = (_frameTime + delta)%5;

        // Look into the appropriate variable when changing position
        switch (_currentDirection) {
            case DOWN:
                if (_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_DOWN);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if (_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_DOWN);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrames()[0];
                } else if (_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_DOWN);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrame(delta);
                }
                break;
            case LEFT:
                if (_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_LEFT);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if (_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_LEFT);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrames()[0];
                } else if (_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_LEFT);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrame(delta);
                }
                break;
            case RIGHT:
                if (_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_RIGHT);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if (_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_RIGHT);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrames()[0];
                } else if (_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_RIGHT);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrame(delta);
                }
                break;
            case UP:
                if (_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_UP);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if (_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_UP);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrames()[0];
                } else if (_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_UP);
                    if (animation == null)
                        return;

                    _currentFrame = animation.getKeyFrame(delta);
                }
                break;
            default:
                break;
        }
    }

    // Specify to two frame animations where each frame is stored in a separate texture
    protected Animation loadAnimation(String firstTexture,
                                      String secondTexture,
                                      Array<GridPoint2> points,
                                      float frameDuration) {


        return new Animation(frameDuration, animationKeyFrames, Animation.PlayMode.LOOP);
    }
}
