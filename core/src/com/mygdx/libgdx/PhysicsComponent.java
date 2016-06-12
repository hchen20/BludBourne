package com.mygdx.libgdx;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Hang Chen on 6/12/2016.
 */
public abstract class PhysicsComponent implements Component {
    public abstract void update(Entity entity, MapManager mpaMgr, float delta);

    public Rectangle _boundingBox;
    protected BoundingBoxLocation _boundingBoxLocation;

    public static enum BoundingBoxLocation {
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        CENTER,
    }

    PhysicsComponent() {

    }

    protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr) {
        Array<Entity> entities = mapMgr.getCurrentEntities();
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
}
