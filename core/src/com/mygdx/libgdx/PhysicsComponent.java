package com.mygdx.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Hang Chen on 6/12/2016.
 */
public abstract class PhysicsComponent implements Component {
    private static final String TAG = PhysicsComponent.class.getSimpleName();

    public abstract void update(Entity entity, MapManager mpaMgr, float delta);

    protected Vector2 _nextEntityPosition;
    protected Vector2 _currentEntityPosition;
    protected Entity.Direction _currentDirection;
    protected Json _json;
    protected Vector2 _velocity;

    protected Array<Entity> _tempEntities;

    public Rectangle _boundingBox;
    protected BoundingBoxLocation _boundingBoxLocation;
    protected Ray _selectionRay;
    protected final float _selectedRayMaximumDistance = 32.0f;

    public static enum BoundingBoxLocation {
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        CENTER,
    }

    PhysicsComponent() {
        this._nextEntityPosition = new Vector2(0,0);
        this._currentEntityPosition new Vector2(0, 0);
        this._velocity = new Vector2(2f, 2f);
        this._boundingBox = new Rectangle();
        this._json = new Json();
        this._tempEntities = new Array<Entity>();
        _boundingBoxLocation = BoundingBoxLocation.BOTTOM_LEFT;
        _selectionRay = new Ray(new Vector3(), new Vector3());

    }

    protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr) {
        Array<Entity> entities = mapMgr.getCurrentMapEntities();
        boolean isCollisionWithMapEntities = false;

        for (Entity mapEntity : entities) {
            // Check for testing against itself
            if (mapEntity.equals(entity))
                continue;

            Rectangle targetRec = mapEntity.getCurrentBoundingBox();
            if (_boundingBox.overlaps(targetRec)) {
                // Collision
                entity.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
                isCollisionWithMapEntities = true;
                break;
            }
        }

        return isCollisionWithMapEntities;
    }

    protected boolean isCollision(Entity source, Entity target) {
        boolean isCollision = false;

        if (source.equals(target))
            return false;

        if (source.getCurrentBoundingBox().overlaps(target.getCurrentBoundingBox())) {
            source.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
            isCollision = true;
        }

        return isCollision;
    }

    protected boolean isCollisionWithMapLayer(Entity entity, MapManager mapMgr) {
        MapLayer mapCollisionLayer = mapMgr.getCollisionLayer();

        if (mapCollisionLayer == null)
            return false;

        Rectangle rectangle = null;

        for (MapObject object : mapCollisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject)object).getRectangle();
                if (_boundingBox.overlaps(rectangle)) {
                    entity.sendMessage(MESSAGE.COLLISION_WITH_MAP);
                    return true;
                }
            }
        }

        return false;
    }

    protected  void setNextPositionToCurrent(Entity entity) {
        this._currentEntityPosition.x = _nextEntityPosition.x;
        this._currentEntityPosition.y = _nextEntityPosition.y;

        entity.sendMessage(MESSAGE.CURRENT_POSITION, _json.toJson(_currentEntityPosition));
    }

    protected void calculateNextPosition(float deltaTime) {
        if(_currentDirection == null)
            return;

        if (deltaTime > 0.7) return;

        float testX = _currentEntityPosition.x;
        float testY = _currentEntityPosition.y;

        _velocity.scl(deltaTime);

        switch (_currentDirection) {
            case LEFT:
                testX -= _velocity.x;
                break;
            case RIGHT:
                testX += _velocity.x;
                break;
            case UP:
                testY += _velocity.y;
                break;
            case DOWN:
                testY -= _velocity.y;
                break;
            default:
                break;
        }

        _nextEntityPosition.x = testX;
        _nextEntityPosition.y = testY;

        // velocity
        _velocity.scl(1/deltaTime);
    }

    protected void initBoundingBox(float percentageWidthReduced, float percentageHeightReduced) {
        // Update the current bounding box
        float width;
        float height;

        float origWidth = Entity.FRAME_WIDTH;
        float origHeight = Entity.FRAME_HEIGHT

        float widthReductionAmount = 1.0f - percentageWidthReduced;
        float heightReductionAmount = 1.0f - percentageHeightReduced;

        if(widthReductionAmount > 0 && widthReductionAmount < 1) {
            width = Entity.FRAME_WIDTH*widthReductionAmount;
        } else {
            width = Entity.FRAME_WIDTH;
        }

        if(heightReductionAmount > 0 && heightReductionAmount < 1) {
            height = Entity.FRAME_HEIGHT*heightReductionAmount;
        } else {
            height = Entity.FRAME_HEIGHT;
        }
        if(width == 0 || height == 0) {
            Gdx.app.debug(TAG, "Width or height is 0!! "+width+":"+height);
        }

        // Need to account for the unit scale since the map coordinates will be in pixels
        float minX;
        float minY;
        if(MapManager.UNIT_SCALE > 0) {
            minX = _nextEntityPosition.x/MapManager.UNIT_SCALE;
            minY = _nextEntityPosition.y/MapManager.UNIT_SCALE;
        } else {
            minX = _nextEntityPosition.x;
            minY = _nextEntityPosition.y;
        }

        _boundingBox.setWidth(width);
        _boundingBox.setHeight(height);

        switch (_boundingBoxLocation) {
            case BOTTOM_LEFT:
                _boundingBox.set(minX, minY, width, height);
                break;
            case BOTTOM_CENTER:
                _boundingBox.setCenter(minX+origWidth/2, minY+origHeight/4);
                break;
            case CENTER:
                _boundingBox.setCenter(minX+origWidth/2, minY+origHeight/2);
                break;
        }
    }
}
