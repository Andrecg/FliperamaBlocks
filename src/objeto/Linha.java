package objeto;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import android.opengl.GLES20;
import Manager.ResourcesManager;

/** Line object
*
* @author Pedro Herig
* @author Andre Garcia
* @version 1.0 Feb 4, 2015.
*/
public class Linha extends Sprite {
	
	//-----------------------------------------------
	//	CONSTANTS
	//-----------------------------------------------
	
	private static final int MAX_BLOCOS = 4;
	
	//-----------------------------------------------
	//	STATIC VARIABLES
	//-----------------------------------------------
	
	private static VertexBufferObjectManager pSpriteVertexBufferObject;
	private static int Periodo = 7;
	
	
	//-----------------------------------------------
	//	VARIABLES
	//-----------------------------------------------
	
	private int numBlocos; //Numero de blocos ativos na linha
	private int posEsq; // Index do bloco mais a esquerdo ativo
	private boolean sentido; //Tempo por bloco
	private Linha linhaAnterior;
	private boolean run;
	private int countUpdate;
	private String color;
	private Text texto;
	private TiledSprite blocos[];
	private float tempo;
	
	//-----------------------------------------------
	//	CONSTRUCTOR
	//-----------------------------------------------

	public Linha(VertexBufferObjectManager pSpriteVertexBufferObject, Camera camera, int y, Linha linhaAnterior,int numBlocos,float tempo, String color,Font fonte, VertexBufferObjectManager vbom) {
		super(960, 50+y, 1200, 100, ResourcesManager.getInstance().linha_region, pSpriteVertexBufferObject);
		this.blocos = new TiledSprite[12];
		pSpriteVertexBufferObject = vbom;
		this.linhaAnterior = linhaAnterior;
		this.setZIndex(200);
		this.tempo = tempo;
		this.numBlocos = numBlocos;
		this.posEsq = 1;		
		this.sentido = true;	//Right way
		this.setRun(false);		//It is not running
		this.setCountUpdate(0); //In the beginning, zero updates
		this.color = color;
        this.attachBlocos();
        //Initializes blocks array
        for(int i = 1;i < this.numBlocos + 1;i++){
        	this.blocos[i].setCurrentTileIndex(this.blocos[i].getCurrentTileIndex() - 1);
        }
        //Set message in the line 
        if(this.color.equals("Verd")){
        	this.texto = new Text(600, 50, fonte, "      + 1 block      ", vbom);
        	this.texto.setZIndex(600);
        	this.attachChild(this.texto);
        }
        if(this.color.equals("Verm")){
        	this.texto  = new Text(600, 50, fonte, "Current High Score", vbom);
        	this.texto.setZIndex(600);
        	this.attachChild(this.texto);
        }
	}
	
	//-----------------------------------------------
	//	GETTERS
	//-----------------------------------------------
	
	/** Getter for number of active blocks in this line
     * 
     * @return			Number of active blocks
     */
	public int getNumBlocos() {
		return this.numBlocos;
	}
	
	/** Getter for number of updates
	 * 
	 */
	public int getCountUpdate() {
		return countUpdate;
	}
	
	/** Getter for period
	 * 
	 * @return			Value of period (used in updates)
	 */
	public static int getPeriodo() {
		return Periodo;
	}

	/** Getter for line state (Running or not)
	 * 
	 * @return			Value of variable run
	 */
	public boolean isRun() {
		return run;
	}
	
	//-----------------------------------------------
	//	SETTERS
	//-----------------------------------------------
	
	/** Set this line to run
	 * 
	 */
	public void run(){
		this.setRun(true);
	}
	
	/** Setter for number of updates
	 * 
	 * @param countUpdate			Number of updates
	 */
	public void setCountUpdate(int countUpdate) {
		this.countUpdate = countUpdate;
	}


	/** Setter for line state
	 * 
	 * @param run			New line state
	 */
	public void setRun(boolean run) {
		this.run = run;
	}
	
	//-----------------------------------------------
	//	GAME
	//-----------------------------------------------
	
	/** Controls the game execution
	 * 
	 * @return			When true, the game stops
	 */
	public Boolean stop() {
		this.setRun(false);
		if(this.linhaAnterior != null) { //when it is not the very first line
			for(int i = 0;i< 12;i++){
				if(this.blocos[i].getCurrentTileIndex() < this.linhaAnterior.blocos[i].getCurrentTileIndex()){
					this.retiraBloco(i); //if a block has no block under itself, turn this block inactive
				}
			}
			if(this.numBlocos == 0) 
				return true; //if the number of active blocks reaches zero, the game stops
			this.acaoLinhaVerd();
		}
		return false; //There are still active blocks in the game, keep playing
	}
	
	/** Controls the update frequency of the game
	 *  
	 */
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mSpriteVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, Sprite.VERTICES_PER_SPRITE);
		if(this.isRun()){ //Game loop
        	this.countUpdate++; 
        	if(this.countUpdate % Linha.Periodo == Linha.Periodo - 1) { 
        		this.passo();	//Update
        		this.countUpdate = 0; //reset update variable
	        }
        }
	}
	
	//-----------------------------------------------
	//	BLOCKS' CONTROL LOGIC
	//-----------------------------------------------
	
	/** Attaches the right block color to the lines, also dim boundaries blocks
	 * 
	 */
	private void attachBlocos() {
        for(int i = 0;i< 12;i++){
        	//choose among blue, red and green blocks to be set
        	if(this.color == "Azul")
        		this.blocos[i] = new TiledSprite(51+100*i, 51, ResourcesManager.getInstance().bloco_region_azul, pSpriteVertexBufferObject);
        	else if(this.color == "Verm")
        		this.blocos[i] = new TiledSprite(51+100*i, 51, ResourcesManager.getInstance().bloco_region_verm, pSpriteVertexBufferObject);
        	else if(this.color == "Verd")
        		this.blocos[i] = new TiledSprite(51+100*i, 51, ResourcesManager.getInstance().bloco_region_verd, pSpriteVertexBufferObject);
        	this.attachChild(this.blocos[i]);
        	if(i == 0 || i == 11){
        		//if block is in the boundaries, dim the block
        		this.blocos[i].setCurrentTileIndex(2);
        	}else{
        		this.blocos[i].setCurrentTileIndex(1);
        	}
        }
    }
	
	/** Add one more block to the active blocks, if the number of blocks is not the maximum already
	 * 
	 */
	private void acaoLinhaVerd() {
		if(this.color.equals("Verd")){
			if(this.numBlocos == MAX_BLOCOS){ //No block to be added
				this.texto.setText("Max Blocks Reached");
			}else{
				TiledSprite plus = null;
				if(this.posEsq == 1){
					//if the most left block is in the first position, add next to the most right block 
					this.blocos[1+this.numBlocos].setCurrentTileIndex(this.blocos[1 + this.numBlocos].getCurrentTileIndex()-1);
					plus = new TiledSprite(51+100*(1+this.numBlocos), 51, ResourcesManager.getInstance().plus_region, pSpriteVertexBufferObject);
				}else{
					//add one block to the left of the most left block
					this.posEsq--;
					plus = new TiledSprite(51+100*(this.posEsq), 51, ResourcesManager.getInstance().plus_region, pSpriteVertexBufferObject);
					this.blocos[this.posEsq].setCurrentTileIndex(this.blocos[this.posEsq].getCurrentTileIndex()-1);
				}
				plus.setCurrentTileIndex(0);
				plus.setZIndex(500);
				this.attachChild(plus);
				this.numBlocos++; //Increment number of blocks by 1
			}
		}
	}
	
	/**
	 * 
	 * @param index			Position in the blocks array needed to be deactivated
	 */
	private void retiraBloco(int index) {
		if(this.posEsq == index) 
			posEsq++; //turn the second block the most left block
		this.blocos[index].setCurrentTileIndex(
				this.blocos[index].getCurrentTileIndex() + 1);
		this.numBlocos--; //Decrease number of active blocks by 1 
		return;
	}

	//-----------------------------------------------
	//	BLOCKS' MOVEMENT LOGIC
	//-----------------------------------------------
	
	/** Controls movement of the active blocks (Player)
	 * 
	 */
	public void passo() {
		
		//when reach the boundaries, directions is changed
		if(this.posEsq == 0 || this.posEsq + this.numBlocos == 12){
			if(sentido)
				sentido = false;
			else
				sentido = true;
		}
		if(sentido){ //moving to the right
			//Apaga bloco
			this.blocos[this.posEsq].setCurrentTileIndex(
					this.blocos[this.posEsq].getCurrentTileIndex() + 1);
			//Acende bloco
			this.blocos[this.posEsq+this.numBlocos].setCurrentTileIndex(
					this.blocos[this.posEsq+this.numBlocos].getCurrentTileIndex() - 1);
			posEsq++;
			
		}else{	//moving to the left
			posEsq--;
			//Acende bloco
			this.blocos[this.posEsq].setCurrentTileIndex(
					this.blocos[this.posEsq].getCurrentTileIndex() - 1);
			//Apaga bloco
			this.blocos[this.posEsq+this.numBlocos].setCurrentTileIndex(
					this.blocos[this.posEsq+this.numBlocos].getCurrentTileIndex() + 1);
			
		}
		
	}
}
