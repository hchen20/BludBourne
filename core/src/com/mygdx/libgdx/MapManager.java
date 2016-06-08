package com.mygdx.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.Hashtable;

/**
 * Created by Hang Chen on 6/5/2016.
 */
public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();

    // All maps for the game
    private Hashtable<String, String> _mapTable;
    private Hashtable<String, Vector2> _playerStartLocationTable;

    // maps
    private final static String TOP_WORLD = "TOP_WORLD";
    private final static String TOWN = "TOWN";
    private final static String CASTLE_OF_DOOM = "CASTLE_OF_DOOM";

    // Map layers
    private final static String MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER";
    private final static String MAP_SPAWNS_LAYER = "MAP_SPAWNS_LAYER";
    private final static String MAP_PORTAL_LAYER = "MAP_PORTAL_LAYER";
    private final static String PLAYER_START = "PLAYER_START";

    private Vector2 _playerStartPositionRect;
    private Vector2 _closestPlayerStartPosition;
    private Vector2 _convertedUnits;

    private Vector2 _playerStart;
    private TiledMap _currentMap;
    private String _currentMapName;
    private MapLayer _collisionLayer = null;
    private MapLayer _portalLayer = null;
    private MapLayer _spawnLayer = null;

    public final static float UNIT_SCALE = 1/16f;

    public MapManager() {
        _playerStart = new Vector2(0, 0);

        _mapTable = new Hashtable<String, String>();
        _mapTable.put(TOP_WORLD, "maps/topworld.tmx");
        _mapTable.put(TOWN, "maps/town.tmx");
        _mapTable.put(CASTLE_OF_DOOM, "map/castle_of_doom");

        _playerStartLocationTable = new Hashtable<String, Vector2>();
        _playerStartLocationTable.put(TOP_WORLD, _playerStart.cpy());
        _playerStartLocationTable.put(TOWN, _playerStart.cpy());
        _playerStartLocationTable.put(CASTLE_OF_DOOM, _playerStart.cpy());

        _playerStartPositionRect = new Vector2(0, 0);
        _closestPlayerStartPosition = new Vector2(0, 0);
        _convertedUnits = new Vector2(0, 0);
    }

    public void loadMap(String mapName) {
        _playerStart.set(0, 0);

        String mapFullPath = _mapTable.get(mapName);

        if(mapFullPath == null) {
            Gdx.app.debug(TAG, "Map is invalid");
            return;
        }

        if(_currentMap != null) {
            _currentMap.dispose();
        }

        Utility.loadMapAsset(mapFullPath);
        if(Utility.isAssetLoaded(mapFullPath)) {
            _currentMap = Utility.getMapAsset(mapFullPath);
            _currentMapName = mapName;
        } else {
            Gdx.app.debug(TAG, "Map not loaded");
            return;
        }

        _collisionLayer = _currentMap.getLayers().get(MAP_COLLISION_LAYER);
        if(_collisionLayer == null) {
            Gdx.app.debug(TAG, "No collision layer!");
        }
        _portalLayer = _currentMap.getLayers().get(MAP_PORTAL_LAYER);
        if (_portalLayer == null) {
            Gdx.app.debug(TAG, "No portal layer!");
        }

        _spawnLayer = _currentMap.getLayers().get(MAP_PORTAL_LAYER);
        if(_spawnLayer == null) {
            Gdx.app.debug(TAG, "No spawn layer!");
        } else {
            Vector2 start = _playerStartLocationTable.get(_currentMapName);
            if(start.isZero()) {
                setClosestStartPosition(_playerStart);
                start = _playerStartLocationTable.get(_currentMapName);
            }
            _playerStart.set(start.x, start.y);
        }
        Gdx.app.debug(TAG, "Player start: ("+_playerStart.x+", "+_playerStart.y+")");
    }

    public TiledMap getCurrentMap() {
        if(_currentMap == null) {
            _currentMapName = TOWN;
            loadMap(_currentMapName);
        }
        return _currentMap;
    }

    public MapLayer getCollisionLayer() {
        return _collisionLayer;
    }

    public MapLayer getPortalLayer() {
        return _portalLayer;
    }

    public Vector2 getPlayerStartUnitScaled() {
        Vector2 playerStart = _playerStart.cpy();
        playerStart.set(_playerStart.x*UNIT_SCALE, _playerStart.y*UNIT_SCALE);
        return playerStart;
    }

    private void setClosestStartPosition(final Vector2 position) {
        // Get last known position on this map
        _playerStartPositionRect.set(0, 0);
        _closestPlayerStartPosition.set(0, 0);
        float shortestDistance = 0f;

        // Go through all player start positions and choose closest to the last known position
        for(MapObject object : _spawnLayer.getObjects()) {
            if(object.getName().equalsIgnoreCase(PLAYER_START)) {
                ((RectangleMapObject)object).getRectangle().getPosition(_playerStartPositionRect);
                float distance = position.dst2(_playerStartPositionRect);
                if (distance < shortestDistance || shortestDistance == 0) {
                    _closestPlayerStartPosition.set(_playerStartPositionRect);
                    shortestDistance = distance
                }
            }
        }
        _playerStartLocationTable.put(_currentMapName, _closestPlayerStartPosition.cpy());
    }

    public void set_closestPlayerStartPositionFromScaledUnit(Vector2 position) {
        if(UNIT_SCALE <= 0)
            return;

        _convertedUnits.set(position.x/UNIT_SCALE, position.y/UNIT_SCALE);
        setClosestStartPosition((_convertedUnits));
    }
}
