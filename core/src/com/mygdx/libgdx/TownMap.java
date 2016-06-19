package com.mygdx.libgdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Hang Chen on 6/17/2016.
 */
public class TownMap extends Map {
    private static final String TAG = PlayerPhysicsComponent.class.getSimpleName();

    private static String _mapPath = "maps/town.tmx";
    private static String _townGuardWalking = "scripts/town_guard_walking.json";
    private static String _townBlacksmith = "scripts/town_blacksmith.json";
    private static String _townMage = "scripts/town_mage.json";
    private static String _townInnKeeper = "scripts/town_innkeeper.json";
    private static String _townFolk = "scripts/town_folk.json";

    TownMap() {
        super(MapFactory.MapType.TOWN, _mapPath);

        _json = new Json();
        for (Vector2 position : _npcStartPositions) {
            Entity entity = EntityFactory.getInstance().getEntityByName(EntityFactory.EntityName.TOWN_GUARD_WALKING);
        }
    }
}
