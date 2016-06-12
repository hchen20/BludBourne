package com.mygdx.libgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.libgdx.Entity;
import com.mygdx.libgdx.MapManager;
import com.mygdx.libgdx.PlayerController;

/**
 * Created by Hang Chen on 6/3/2016.
 */
public class MainGameScreen extends GameScreen {
    private static final String TAG = MainGameScreen.class.getSimpleName();

    private static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    private PlayerController _controller;
    private TextureRegion _currentPlayerFrame;
    private Sprite _currentPlayerSprite;

    private OrthogonalTiledMapRenderer _mapRenderer = null;
    private OrthographicCamera _camera = null;
    private static MapManager _mapMgr;

    public MainGameScreen() {
        _mapMgr = new MapManager();
    }

    private static Entity _player;

    @Override
    public void show() {
        // camera setup
        setupViewport(10, 10);

        // get current size
        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        _mapRenderer = new OrthogonalTiledMapRenderer(_mapMgr.getCurrentMap(), MapManager.UNIT_SCALE);
        _mapRenderer.setView(_camera);

        Gdx.app.debug(TAG, "UnitScale value is: "+_mapRenderer.getUnitScale());

        _player = new Entity();
        _player.init(_mapMgr.getPlayerStartUnitScaled().x, _mapMgr.getPlayerStartUnitScaled().y);

        _currentPlayerSprite = _player.getFrameSprite();

        _controller = new PlayerController(_player);
        Gdx.input.setInputProcessor(_controller);
    }

    @Override
    public void hide() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Preferably, lock and center the camera to the player's position
        _camera.position.set(_currentPlayerSprite.getX(), _currentPlayerSprite.getY(), 0f);
        _camera.update();

        _player.update(delta);
        _currentPlayerFrame = _player.getFrame();

        updatePortalLayerActivation(_player.boundingBox);

        if(!isCollisionWithMapLayer(_player.boundingBox)) {
            _player.setNextPositionToCurrent();
        }

        _controller.update(delta);

        _mapRenderer.setView(_camera);
        _mapRenderer.render();

        _mapRenderer.getBatch().begin();

        _mapRenderer.getBatch().draw(_currentPlayerFrame,
                                     _currentPlayerSprite.getX(),
                                     _currentPlayerSprite.getY(),
                                     1,
                                     1);
        _mapRenderer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        _player.dispose();
        _controller.dispose();
        Gdx.input.setInputProcessor(null);
        _mapRenderer.dispose();
    }

    private void setupViewport(int width, int height) {
        // Make the view portal percentage of the total display area
        VIEWPORT.virtualHeight = height;
        VIEWPORT.virtualWidth = width;

        // Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

        // Pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        // Aspect ratio for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth/VIEWPORT.virtualHeight);

        // Update viewport if there could be skewing
        float skewing = VIEWPORT.physicalWidth/VIEWPORT.physicalHeight;
        if(VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio) {
            // Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else {
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
        }
        
        // Gdx.app.debug()
    }
    
    private boolean isCollisionWithMapLayer(Rectangle boundingBox) {
        MapLayer mapCollisionLayer = _mapMgr.getCollisionLayer();

        if(mapCollisionLayer == null)
            return false;

        Rectangle rectangle = null;

        for(MapObject object : mapCollisionLayer.getObjects()) {
            if(object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject)object).getRectangle();
                if(boundingBox.overlaps(rectangle))
                    return true;
            }
        }

        return false;
    }

    private boolean updatePortalLayerActivation(Rectangle boundingBox){
        MapLayer mapPortalLayer = _mapMgr.getPortalLayer();

        if(mapPortalLayer == null)
            return false;

        Rectangle rectangle = null;

        for(MapObject object : mapPortalLayer.getObjects()) {
            if(object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject)object).getRectangle();
                if(boundingBox.overlaps(rectangle)) {
                    String mapName = object.getName();
                    if(mapName == null)
                        return false;

                    _mapMgr.setClosestStartPositionFromScaledUnits(_player.getCurrentPosition());
                    _mapMgr.loadMap(mapName);
                    _player.init(_mapMgr.getPlayerStartUnitScaled().x,
                                 _mapMgr.getPlayerStartUnitScaled().y);

                    _mapRenderer.setMap(_mapMgr.getCurrentMap());

                    Gdx.app.debug(TAG, "Portal activated");
                    return true;
                }


            }
        }
        return false;
    }
}
