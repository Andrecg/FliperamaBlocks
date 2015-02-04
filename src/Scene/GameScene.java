package Scene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import Base.BaseScene;
import objeto.Linha;

import Manager.SceneManager;
import Manager.SceneManager.SceneType;

/** Game itself
*
* @author Pedro Herig
* @author Andre Garcia
* @version 1.0 Feb 4, 2015.
*/

public class GameScene extends BaseScene implements IOnSceneTouchListener{
	
	//-----------------------------------------------
	//	CONSTANTS
	//-----------------------------------------------
	
	private static final String TAG_LINHA = "linha";
	private static final String TAG_LEVEL = "level";
	protected static final Object TAG_LINHA_ATTRIBUTE_TYPE_VALUE_VERM = "Verm";
	protected static final Object TAG_LINHA_ATTRIBUTE_TYPE_VALUE_VERD = "Verd";
	private static final String TAG_LINHA_ATTRIBUTE_LEVEL = "level";
	private static final int MAX_BLOCOS = 4;
	protected static final String TAG_LINHA_ATTRIBUTE_TYPE = "color";
	
	//-----------------------------------------------
	// STATIC VARIABLES
	//-----------------------------------------------
	
	private static int LevelStartChase = 5; //MAX = 7
	private static int IndexLinhaAtual = 3; //MAX = 7
	public static String cor = "";
	
	//-----------------------------------------------
	// VARIABLES
	//-----------------------------------------------
	
	/** HUD */
	private HUD gameHUD;
	/** Text of current level */
	private Text levelText;
	/**  Current level */
	private int level;
	/**  Array of lines shown in user's perspective */
	private static Linha linhas[];
	private float frequencia;
	/** Background */
	private Background background;
	/** Text of game over */
	private Text gameOverText;
	/** Control if the game is still active */
	private boolean gameOverDisplayed;
	
	//-----------------------------------------------
	// CONSTRUCTOR
	//-----------------------------------------------
	
	 public GameScene() {
		this.gameOverDisplayed = false; //Game running 
		this.frequencia = 1; 
		this.level = 1; //First level
	 }
	 
	//-----------------------------------------------
	// LISTENERS
	//-----------------------------------------------
	
	/**Handle touch event
	 * 
	 */
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			novaLinhaAtiva(); //Create new line
	    }
	    return false;
	}
	
	/**Handle Back key event
	 * 
	 */
	public void onBackKeyPressed() {
	    SceneManager.getInstance().loadMenuScene(engine);
	}
	
	
	//-----------------------------------------------
	// SCENE CREATION
	//-----------------------------------------------
	
	/**Generate game over text
	 * 
	 */
	private void createGameOverText() {
	    gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
	}
	
	/**Create scene to be shown to user 
	 * 
	 */
	public void createScene() {
	    createBackground();
	    createHUD();
	    createGameOverText();
	    inicializaJogo();
	    setOnSceneTouchListener(this); 
	}

	/**Generate the background
	 * 
	 */
	private void createBackground() {
		background = new Background(238f,233f,233);
	    setBackground(background);
	}
	
	/**Create the HUD
	 * 
	 */
	private void createHUD() {
	    gameHUD = new HUD();
	    
	    //Create level text
	    levelText = new Text(50, 1080 - 150, resourcesManager.font, "Level: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
	    levelText.setAnchorCenter(0, 0);
	    //levelText.setColor(Color.WHITE);
	    levelText.setScale(2f);
	    levelText.setText("1");
	    gameHUD.attachChild(levelText);
	    gameHUD.setZIndex(100);
	    camera.setHUD(gameHUD);
	}
	

	/**Getter of this scene type
	 * @return SCENE_GAME 
	 */
	public SceneType getSceneType() {
        return SceneType.SCENE_GAME;
    }
	
	/**Description of disposesScene
	 * 
	 */
	public void disposeScene() {
	    camera.setHUD(null);
	    camera.setCenter(960, 540);
	    
	    camera.setChaseEntity(null);

	    // TODO code responsible for disposing scene
	    // removing all game scene objects.
	}
	
	/**Description of GameOver()
	 * 
	 */
	private void GameOver() {
		// TODO Auto-generated method stub
		
	}
	
	/**Display game over scene
	 * 
	 */
	private void displayGameOverText() {
	    camera.setChaseEntity(null);
	    gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
	    attachChild(gameOverText);
	    gameOverDisplayed = true;
	}
	
	/**Generate lines shown to the user
	 * 
	 */
	private void inicializaJogo() {
		//set the first level
		this.level = 1;
		//load color of level
		loadLinhaCor(this.level);
		//generate the first line
		//null means that there is no line under first one
		linhas[0] = novaLinha(null,MAX_BLOCOS,0, cor);
		//run first line
		linhas[0].run();
		//attach first line to the scene
		attachChild(linhas[0]);
		
		//generate the other lines, basically the same
		for(int i = 1;i<11;i++){
			loadLinhaCor(i+1);
			//number of active blocks is zero in all lines but first 
			linhas[i] = novaLinha(linhas[i-1],0,100*i,cor);
			attachChild(linhas[i]);
		}
		
	}
	
	//-----------------------------------------------
	// LINES' TRANSITION LOGIC
	//-----------------------------------------------
	/**Increment the level and generate level text
	 * @param term			number to be added
	 */
	private void addTolevel(int term) {
		//It is added term to current level
	    this.level += term;
	    //The new current level is set in text
	    this.levelText.setText("" + this.level);
	}
	
	/**Load color of a line
	 * 
	 * @param level			Level to be loaded			
	 */
	private void loadLinhaCor(final int level) {

		//Every 8 lines add bonus line
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

	/**Generate a new line
	 * 
	 * @param linhaAnterior			Reference to previous line
	 * @param numBlocos				Number of blocks active in this line
	 * @param y						Value of line height
	 * @param color					Color of the line
	 * @return						New line generated
	 */
	private Linha novaLinha(Linha linhaAnterior, int numBlocos, int y, String color) {
		return new Linha(this.vbom,this.camera,y,linhaAnterior,numBlocos,(float)(this.level)/this.frequencia, color,this.resourcesManager.font,this.vbom);
	}
	
	/**Description of novaLinhaAtiva()
	 * 
	 */
	private void novaLinhaAtiva() {
		//stop previous
		//load color -> dispose -> new line -> Run
		if(this.level <=  IndexLinhaAtual){
			linhas[this.level - 1].stop();
			loadLinhaCor(this.level);
			linhas[this.level].dispose();
			linhas[this.level] = novaLinha(linhas[this.level - 1],linhas[this.level - 1].getNumBlocos(),100*(this.level), cor);
			attachChild(linhas[this.level]);
			linhas[this.level].run();
			addTolevel(1);
		}else {
			if(linhas[ IndexLinhaAtual].stop()){//Se GameOver
        		GameOver();	        		
        	}else{//Se ainda tiver blocos
	        	linhas[ IndexLinhaAtual - 1].dispose();
	        	//line is assigned to previous line
	        	for(int i = 1;i < 11;i++){
	        		linhas[i-1] = linhas[i];
	        	}
	        	loadLinhaCor(this.level); // IndexLinhaAtual
	        	linhas[ IndexLinhaAtual].dispose();
	        	linhas[ IndexLinhaAtual] = novaLinha(linhas[ IndexLinhaAtual - 1],linhas[ IndexLinhaAtual - 1].getNumBlocos(),100*(level),cor);
	        	attachChild(linhas[ IndexLinhaAtual]);
	        	loadLinhaCor(this.level+10- IndexLinhaAtual);
	        	//generate new last line
	        	linhas[10] = novaLinha(linhas[9],0,100*(this.level+9- IndexLinhaAtual), cor);
	        	attachChild(linhas[10]);
	        	linhas[ IndexLinhaAtual].run();
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
		
		//camera starts moving
    	if(this.level >=  LevelStartChase){
    		this.camera.setCenter(linhas[ IndexLinhaAtual+1].getX(), linhas[ IndexLinhaAtual+1].getY());
    	}
	}

}
