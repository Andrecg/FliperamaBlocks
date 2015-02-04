package jogo;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.Display;
import android.view.KeyEvent;

import Manager.ResourcesManager;
import Manager.SceneManager;

public class JogoActivity extends BaseGameActivity
{
	private Camera camera;
	public static final int CAMERA_WIDTH = 1920;
	public static final int CAMERA_HEIGHT = 1080;
	
	private ResourcesManager resourcesManager;
	
	public EngineOptions onCreateEngineOptions()
    {
		//final Display display = getWindowManager().getDefaultDisplay();
		//CAMERA_WIDTH = display.getWidth();
		//CAMERA_HEIGHT = display.getHeight();
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		//camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		//camera.setBounds(0, 0, 0, 0);
		//camera.setBoundsEnabled(true);
        //EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.camera);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), this.camera);
        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }

    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
    {
    	ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
        resourcesManager = ResourcesManager.getInstance();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
    {
    	SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
    {
        mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
        }));
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) 
    {
        return new LimitedFPSEngine(pEngineOptions, 60);
    }
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false; 
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
            System.exit(0);	
    }
    
}