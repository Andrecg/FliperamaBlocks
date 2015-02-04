package Scene;

import java.io.IOException;


import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import Base.BaseScene;
import objeto.Linha;

import Manager.SceneManager;
import Manager.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener{
	
	private static int LevelStartChase = 5;//MAX = 7
	private static int IndexLinhaAtual = 3;//MAX = 7
	
	private HUD gameHUD;
	private Text levelText;
	private int level = 1;
	private static Linha linhas[] = new Linha[11];
	private float frequencia = 1;
	
	public static String cor = "";
	
	private Background background;
	
	private Text gameOverText;
	private boolean gameOverDisplayed = false;
	
	//Leitura do xml
	private static final String TAG_LINHA = "linha";
	private static final String TAG_LEVEL = "level";
	protected static final Object TAG_LINHA_ATTRIBUTE_TYPE_VALUE_VERM = "Verm";
	protected static final Object TAG_LINHA_ATTRIBUTE_TYPE_VALUE_VERD = "Verd";
	private static final String TAG_LINHA_ATTRIBUTE_LEVEL = "level";

	private static final int MAX_BLOCOS = 4;
	protected static final String TAG_LINHA_ATTRIBUTE_TYPE = "color";
	
	private void addTolevel(int i)
	{
	    level += i;
	    levelText.setText("" + level);
	}
	
	private void createGameOverText()
	{
	    gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
	}

	private void displayGameOverText()
	{
	    camera.setChaseEntity(null);
	    gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
	    attachChild(gameOverText);
	    gameOverDisplayed = true;
	}
	
	@Override
	public void createScene()
	{
	    createBackground();
	    createHUD();
	    createGameOverText();
	    inicializaJogo();
	    setOnSceneTouchListener(this); 
	}
	
	private void loadLinhaCor(final int level) {
		// TODO Auto-generated method stub
		if(level % 9 == 8)
			cor = "Verd";
		else
			cor = "Azul";
		/*
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
	    cor = "Azul";
		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
				{

					@Override
					public IEntity onLoadEntity(String pEntityName,
							IEntity pParent, Attributes pAttributes,
							SimpleLevelEntityLoaderData pEntityLoaderData)
							throws IOException {
						// TODO Auto-generated method stub
						return null;
					}
				});
		
	    levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_LINHA)
	    {
	        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
	        {
	            final String color = SAXUtils.getAttributeOrThrow(pAttributes, TAG_LINHA_ATTRIBUTE_TYPE);
	            int levelXML = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_LINHA_ATTRIBUTE_LEVEL);
	            
	            if(level == levelXML){
		            if (color.equals(TAG_LINHA_ATTRIBUTE_TYPE_VALUE_VERM))
		            {
		                cor = "Verm";
		            } 
		            else if (color.equals(TAG_LINHA_ATTRIBUTE_TYPE_VALUE_VERD))
		            {
		            	cor = "Verd";
		            }
		            else
		            {
		            	cor = "Azul";
		            }
	            }
				return null;
	        }
	    });

	    levelLoader.loadLevelFromAsset(activity.getAssets(), "scenario/infinite.lvl");
	    */
	}

	private void inicializaJogo() {
		level = 1;
		loadLinhaCor(level);
		linhas[0] = novaLinha(null,MAX_BLOCOS,0,cor);
		linhas[0].run();
		attachChild(linhas[0]);
		for(int i = 1;i<11;i++){
			loadLinhaCor(i+1);
			linhas[i] = novaLinha(linhas[i-1],0,100*i,cor);
			attachChild(linhas[i]);
		}
		
	}

	private Linha novaLinha(Linha linhaAtual2, int numBlocos1, int y, String color) {
		// TODO Auto-generated method stub
		
		return new Linha(vbom,camera,y,linhaAtual2,numBlocos1,(float)(level)/frequencia, color,resourcesManager.font,vbom);
	}

	@Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

	
	private void createBackground()
	{
		background = new Background(238f,233f,233);
	    setBackground(background);
	}
	

	private void createHUD()
	{
	    gameHUD = new HUD();
	    
	    // CREATE LEVEL TEXT
	    levelText = new Text(50, 1080 - 150, resourcesManager.font, "Level: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
	    levelText.setAnchorCenter(0, 0);
	    //levelText.setColor(Color.WHITE);
	    levelText.setScale(2f);
	    levelText.setText("1");
	    gameHUD.attachChild(levelText);
	    gameHUD.setZIndex(100);
	    camera.setHUD(gameHUD);
	}
	@Override
	public void disposeScene()
	{
	    camera.setHUD(null);
	    camera.setCenter(960, 540);
	    
	    camera.setChaseEntity(null);

	    // TODO code responsible for disposing scene
	    // removing all game scene objects.
	}
	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
		if (pSceneTouchEvent.isActionDown())
	    {
			novaLinhaAtiva();
	    }
	    return false;
	}
	
	private void novaLinhaAtiva() {
		// TODO Auto-generated method stub
		if(level <= IndexLinhaAtual){
			linhas[level - 1].stop();
			loadLinhaCor(level+1);
			linhas[level].dispose();
			linhas[level] = novaLinha(linhas[level - 1],linhas[level - 1].getNumBlocos(),100*(level),cor);
			attachChild(linhas[level]);
			linhas[level].run();
			addTolevel(1);
		}else{
			if(linhas[IndexLinhaAtual].stop()){//Se GameOver
        		GameOver();	        		
        	}else{//Se ainda tiver blocos
	        	linhas[IndexLinhaAtual - 1].dispose();
	        	for(int i = 1;i < 11;i++){
	        		linhas[i-1] = linhas[i];
	        	}
	        	loadLinhaCor(level+1);
	        	linhas[IndexLinhaAtual].dispose();
	        	linhas[IndexLinhaAtual] = novaLinha(linhas[IndexLinhaAtual - 1],linhas[IndexLinhaAtual - 1].getNumBlocos(),100*(level),cor);
	        	attachChild(linhas[IndexLinhaAtual]);
	        	loadLinhaCor(level+10-IndexLinhaAtual);
	        	linhas[10] = novaLinha(linhas[9],0,100*(level+9-IndexLinhaAtual),cor);
	        	attachChild(linhas[10]);
	        	linhas[IndexLinhaAtual].run();
	        	addTolevel(1);
        	}
		}
		/*if(level == 1){//Entre nesse if apos usuario parar linha 1
			linhas[0].stop();
			loadLinhaCor(level+1);
			linhas[1].dispose();
			linhas[1] = novaLinha(linhas[0],linhas[0].getNumBlocos(),100*(level),cor);
        	attachChild(linhas[1]);
			linhas[1].run();
		}else{
        	if(linhas[1].stop()){//Se GameOver
        		GameOver();	        		
        	}else{//Se ainda tiver blocos
	        	linhas[0].dispose();
	        	for(int i = 1;i < 11;i++){
	        		linhas[i-1] = linhas[i];
	        	}
	        	loadLinhaCor(level+1);
	        	linhas[1].dispose();
	        	linhas[1] = novaLinha(linhas[0],linhas[0].getNumBlocos(),100*(level),cor);
	        	attachChild(linhas[1]);
	        	loadLinhaCor(level+10);
	        	linhas[10] = novaLinha(linhas[9],0,100*(level+9),cor);
	        	attachChild(linhas[10]);
	        	linhas[1].run();
        	}
		}*/
    	if(level >= LevelStartChase){
    		camera.setCenter(linhas[IndexLinhaAtual+1].getX(), linhas[IndexLinhaAtual+1].getY());
    	}
	}

	private void GameOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackKeyPressed()
	{
	    SceneManager.getInstance().loadMenuScene(engine);
	}


}
