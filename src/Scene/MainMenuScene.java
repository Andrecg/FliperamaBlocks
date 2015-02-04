package Scene;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.util.GLState;

import Base.BaseScene;
import Manager.ResourcesManager;
import Manager.SceneManager;
import Manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {
	
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_SOUND = 1;
	private final int MENU_LEADER = 2;
	private final int MENU_RATE = 3;
	private final int MENU_SHARE = 4;
	
	private TiledSprite sound;

	private void createMenuChildScene()
	{
		menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(0, 0);
	    
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.15f, 1);
	    final IMenuItem leaderMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LEADER, resourcesManager.leader_region, vbom), 0.9f, 0.9f);
	    final IMenuItem rateMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_RATE, resourcesManager.rate_region, vbom), 0.9f, 0.9f);
	    final IMenuItem shareMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SHARE, resourcesManager.share_region, vbom), 0.9f, 0.9f);
	    
	    sound = new TiledSprite(100, 100, ResourcesManager.getInstance().sound_region, vbom);
	    final IMenuItem soundMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUND, resourcesManager.botao_region, vbom), 1.2f, 0.9f);
	    
	    sound.setCurrentTileIndex(0);
	    soundMenuItem.attachChild(sound);	
	    soundMenuItem.setTag(MENU_SOUND);
	    
	    menuChildScene.addMenuItem(playMenuItem);
	    menuChildScene.addMenuItem(soundMenuItem);
	    menuChildScene.addMenuItem(leaderMenuItem);
	    menuChildScene.addMenuItem(rateMenuItem);
	    menuChildScene.addMenuItem(shareMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	    playMenuItem.setPosition(1920/2, 1080 - 248);
	    soundMenuItem.setPosition(1920*4/5, 1080*6/24+2);
	    leaderMenuItem.setPosition(1920*3/5, 1080*6/24+2);
	    shareMenuItem.setPosition(1920*2/5, 1080*6/24+2);
	    rateMenuItem.setPosition(1920/5, 1080*6/24+2);
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	}

	@Override
	public void createScene()
	{
	     createBackground();
	     createMenuChildScene();
	     startMusic();
	}

	

	private void startMusic() {
		// TODO Auto-generated method stub
		resourcesManager.menuMusic.play();
	}

	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
	        switch(pMenuItem.getID())
	        {
	        case MENU_PLAY:
	            //Load Game Scene!
	            SceneManager.getInstance().loadGameScene(engine);
	            return true;
	        case MENU_SOUND:
	        	if(resourcesManager.menuMusic.isPlaying())
	        		resourcesManager.menuMusic.pause();
	        	else
	        		resourcesManager.menuMusic.resume();
	        	sound.setCurrentTileIndex((sound.getCurrentTileIndex()+1) % 2); //Inverte o Botao
	            return true;
	        case MENU_LEADER:
	            return true;
	        case MENU_RATE:
	            return true;
	        case MENU_SHARE:
	            return true;
	        default:
	            return false;
	    }
	}
	
	@Override
	public void onBackKeyPressed()
	{
	    System.exit(0);
	}

	@Override
	public SceneType getSceneType()
	{
	    return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}
	
	private void createBackground()
	{
	    attachChild(new Sprite(960, 540, resourcesManager.menu_background_region, vbom)
	    {
	        @Override
	        protected void preDraw(GLState pGLState, Camera pCamera) 
	        {
	            super.preDraw(pGLState, pCamera);
	            pGLState.enableDither();
	        }
	    });
	}

}
