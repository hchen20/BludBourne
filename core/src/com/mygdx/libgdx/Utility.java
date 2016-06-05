package com.mygdx.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Created by Hang Chen on 6/5/2016.
 */
public class Utility {
    public static final AssetManager _assetManager = new AssetManager();

    private static final String TAG = Utility.class.getSimpleName();

    private static InternalFileHandleResolver _filePathResolver = new InternalFileHandleResolver();

    public static void unloadAsset(String assetFileNamepath) {
        // once the asset manager is done loading
        if(_assetManager.isLoaded(assetFileNamepath))
            _assetManager.unload(assetFileNamepath);
        else
            Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload:"+assetFileNamepath);
    }

    public static float loadCompleted() {
        return _assetManager.getProgress();
    }

    public static int numberAssetQueued() {
        return _assetManager.getQueuedAssets();
    }

    public static boolean updateAssetLoading() {
        return _assetManager.update();
    }

    public static boolean isAssetLoaded(String fileName) {
        return _assetManager.isLoaded(fileName);
    }

    public static void loadMapAsset(String mapFilenamePath) {
        if(mapFilenamePath == null) {
            return;
        }

        if(_filePathResolver.resolve(mapFilenamePath).exists()) {
            _assetManager.setLoader(TiledMap.class, new TmxMapLoader(_filePathResolver));
            _assetManager.load(mapFilenamePath, TiledMap.class);

            // Until we add loading screen,
            // just block until we load the map.

            _assetManager.finishLoadingAsset(mapFilenamePath);
            Gdx.app.debug(TAG, "Map loaded!: "+mapFilenamePath);
        } else {
            Gdx.app.debug(TAG, "Map does not exist!: "+mapFilenamePath);
        }
    }

    public static TiledMap getMapAsset(String mapFilenamePath) {
        TiledMap map = null;

        // Once the asset manager is done loading
        if(_assetManager.isLoaded(mapFilenamePath)) {
            map = _assetManager.get(mapFilenamePath, TiledMap.class);
        } else {
            Gdx.app.debug(TAG, "Map is not loaded: "+mapFilenamePath);
        }

        return map;
    }

    public static void loadTextureAsset(String textureFilenamePath) {
        if(textureFilenamePath == null)
            return;

        if(_filePathResolver.resolve(textureFilenamePath).exists()) {
            _assetManager.setLoader(Texture.class, new TextureLoader(_filePathResolver));
            _assetManager.load(textureFilenamePath, Texture.class);
            _assetManager.finishLoadingAsset(textureFilenamePath);
        } else {
            Gdx.app.debug(TAG, "Texture does not exist!: "+textureFilenamePath);
        }
    }

    public static Texture getTextureAsset(String textureFilenamePath) {
        Texture texture = null;

        if(_assetManager.isLoaded(textureFilenamePath)) {
            texture = _assetManager.get(textureFilenamePath, Texture.class);
        } else {
            Gdx.app.debug(TAG, "Texture is not loaded: "+textureFilenamePath);
        }

        return texture;
    }
}
