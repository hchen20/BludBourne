package com.mygdx.libgdx;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.Hashtable;

/**
 * Created by Hang Chen on 6/12/2016.
 */
public class EntityFactory {
    private static final String TAG = EntityFactory.class.getSimpleName();
    private static EntityFactory _instance = null;
    private static Json _json = new Json();
    private Hashtable<String, EntityConfig> _entities;

    public static enum EntityType {
        PLAYER,
        PLAYER_DEMO,
        NPC
    }

    public static enum EntityName {
        PLAYER_PUPPET,
        TOWN_GUARD_WALKING,
        TOWN_BLACKSMITH,
        TOWN_MAGE,
        TOWN_INNKEEPER,
        TOWN_FOLK1, TOWN_FOLK2, TOWN_FOLK3, TOWN_FOLK4, TOWN_FOLK5,
        TOWN_FOLK6, TOWN_FOLK7, TOWN_FOLK8, TOWN_FOLK9, TOWN_FOLK10,
        TOWN_FOLK11, TOWN_FOLK12, TOWN_FOLK13, TOWN_FOLK14, TOWN_FOLK15,
        FIRE
    }

    public static String PLAYER_CONFIG = "scripts/player.json";
    public static String TOWN_GUARD_WALKING_CONFIG = "scripts/town_guard_walking.json";
    public static String TOWN_BLACKSMITH_CONFIG = "scripts/town_blacksmith.json";
    public static String TOWN_MAGE_CONFIG = "scripts/town_mage.json";
    public static String TOWN_INNKEEPER_CONFIG = "scripts/town_innkeeper.json";
    public static String TOWN_FOLK_CONFIGS = "scripts/town_folk.json";
    public static String ENVIRONMENTAL_ENTITY_CONFIGS = "scripts/environmental_entities.json";

    private EntityFactory() {
        _entities = new Hashtable<String, EntityConfig>();

        Array<EntityConfig> townFolkConfigs = Entity.getEntityConfigs(TOWN_FOLK_CONFIGS);
        for (EntityConfig config : townFolkConfigs)
            _entities.put(config.getEntityID(), config);

        Array<EntityConfig> environmentalEntityConfigs = Entity.getEntityConfig(ENVIRONMENTAL_ENTITY_CONFIGS);
        for (EntityConfig config : environmentalEntityConfigs)
            _entities.put(config.getEntityID(), config);

        _entities.put(EntityName.TOWN_GUARD_WALKING.toString(), Entity.loadEntityConigPath(TOWN_GUARD_WALKING_CONFIG));
        _entities.put(EntityName.TOWN_BLACKSMITH.toString(), Entity.loadEntityConigPath(TOWN_BLACKSMITH_CONFIG));
        _entities.put(EntityName.TOWN_MAGE.toString(), Entity.loadEntityConigPath(TOWN_MAGE_CONFIG);
        _entities.put(EntityName.TOWN_INNKEEPER.toString(), Entity.loadEntityConigPath(TOWN_INNKEEPER_CONFIG));
        _entities.put(EntityName.PLAYER_PUPPET.toString(), Entity.loadEntityConigPath(PLAYER_CONFIG));
    }

    public static EntityFactory getInstance() {
        if (_instance == null) {
            _instance = new EntityFactory();
        }

        return _instance;
    }

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
            case PLAYER_DEMO:
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

    public Entity getEntityByName(Entity entityName) {
        EntityConfig config = new EntityConfig(_entities.get(entityName.toString()));
        Entity entity = Entity.initEntity(config);
        return entity;
    }

}
