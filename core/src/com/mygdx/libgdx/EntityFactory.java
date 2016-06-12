package com.mygdx.libgdx;

import com.badlogic.gdx.utils.Json;

/**
 * Created by Hang Chen on 6/12/2016.
 */
public class EntityFactory {
    private static Json _json = new Json();

    public static enum EntityType {
        PLAYER,
        DEMO_PLAYER,
        NPC
    }

    public static String PLAYER_CONFIG = "scripts/player.json";

    public static Entity getEntity(EntityType entityType) {
        Entity entity = null;
        switch (entityType) {
            case PLAYER:
                entity = new Entity(new PlayerInputComponent(),
                        new PlayerPhysicsComponent(),
                        new PlayerGraphicsComponent());
                entity.setEntityConfig(Entity.getEntity(EntityFactory.PLAYER_CONFIG));
                entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS,
                        _json.toJson(entity.getEntityConfig()));
                return entity;
            case DEMO_PLAYER:
                entity = new Entity(new PlayerInputComponent(),
                                    new PlayerPhysicsComponent(),
                                    new PlayerGraphicsComponent());
                return entity;
            case NPC:
                entity = new Entity(new NPCInputComponent(),
                        new NPCPhysicsComponent(),
                        new NPCGraphicsComponent());
                return entity;
            default:
                return null;
        }
    }

}
