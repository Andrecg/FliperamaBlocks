package objeto;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;
import android.view.animation.Animation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import Manager.ResourcesManager;

import  Base.BaseScene;

public class Linha extends Sprite {
	private static int Periodo = 7;
	private static final int MAX_BLOCOS = 4;
	private int numBlocos; //Numero de blocos ativos na linha
	private int posEsq; // Index do bloco mais a esquerdo ativo
	private boolean sentido; //Tempo por bloco
	private Linha linhaAnt;
	private boolean run;
	private int countUpdate;
	private String color;
	private Text texto;
	private static VertexBufferObjectManager pSpriteVertexBufferObject;
	
	private TiledSprite blocos[] = new TiledSprite[12];
	private float tempo;
	
	public Linha(VertexBufferObjectManager pSpriteVertexBufferObject, Camera camera, int y, Linha linha1,int numBlocos1,float tempo, String color,Font fonte, VertexBufferObjectManager vbom)
	{
		super(960, 50+y, 1200, 100, ResourcesManager.getInstance().linha_region, pSpriteVertexBufferObject);
		pSpriteVertexBufferObject = vbom;
		this.linhaAnt = linha1;
		this.setZIndex(200);
		this.tempo = tempo;
		this.numBlocos = numBlocos1;
		this.posEsq = 1;
		this.sentido = true;
		this.setRun(false);
		this.setCountUpdate(0);
		this.color = color;
        this.attachBlocos();
        for(int i = 1;i<1+numBlocos1;i++){
        	this.blocos[i].setCurrentTileIndex(this.blocos[i].getCurrentTileIndex()-1);
        }
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
	
	
	private void acaoLinhaVerd() {
		// TODO Auto-generated method stub
		if(this.color.equals("Verd")){
			if(this.numBlocos == MAX_BLOCOS){
				this.texto.setText("Max Blocks Reached");
			}else{
				TiledSprite plus = null;
				if(this.posEsq == 1){
					this.blocos[1+this.numBlocos].setCurrentTileIndex(this.blocos[1 + this.numBlocos].getCurrentTileIndex()-1);
					plus = new TiledSprite(51+100*(1+this.numBlocos), 51, ResourcesManager.getInstance().plus_region, pSpriteVertexBufferObject);
				}else{
					this.posEsq--;
					plus = new TiledSprite(51+100*(this.posEsq), 51, ResourcesManager.getInstance().plus_region, pSpriteVertexBufferObject);
					this.blocos[this.posEsq].setCurrentTileIndex(this.blocos[this.posEsq].getCurrentTileIndex()-1);
				}
				plus.setCurrentTileIndex(0);
				plus.setZIndex(500);
				this.attachChild(plus);
				this.numBlocos++;
			}
		}
	}


	public int getNumBlocos(){
		return this.numBlocos;
	}
	
	private void attachBlocos()
    {
        for(int i = 0;i< 12;i++){
        	if(this.color == "Azul")
        		this.blocos[i] = new TiledSprite(51+100*i, 51, ResourcesManager.getInstance().bloco_region_azul, pSpriteVertexBufferObject);
        	else if(this.color == "Verm")
        		this.blocos[i] = new TiledSprite(51+100*i, 51, ResourcesManager.getInstance().bloco_region_verm, pSpriteVertexBufferObject);
        	else if(this.color == "Verd")
        		this.blocos[i] = new TiledSprite(51+100*i, 51, ResourcesManager.getInstance().bloco_region_verd, pSpriteVertexBufferObject);
        	this.attachChild(this.blocos[i]);
        	if(i == 0 || i == 11){
        		this.blocos[i].setCurrentTileIndex(2);
        	}else{
        		this.blocos[i].setCurrentTileIndex(1);
        	}
        }
    }
	
	
	
	public void passo()
	{
		
		if(this.posEsq == 0 || this.posEsq + this.numBlocos == 12){
			if(sentido)
				sentido = false;
			else
				sentido = true;
		}
		if(sentido){
			//Apaga bloco
			this.blocos[this.posEsq].setCurrentTileIndex(
					this.blocos[this.posEsq].getCurrentTileIndex() + 1);
			//Acende bloco
			this.blocos[this.posEsq+this.numBlocos].setCurrentTileIndex(
					this.blocos[this.posEsq+this.numBlocos].getCurrentTileIndex() - 1);
			posEsq++;
			
		}else{
			posEsq--;
			//Acende bloco
			this.blocos[this.posEsq].setCurrentTileIndex(
					this.blocos[this.posEsq].getCurrentTileIndex() - 1);
			//Apaga bloco
			this.blocos[this.posEsq+this.numBlocos].setCurrentTileIndex(
					this.blocos[this.posEsq+this.numBlocos].getCurrentTileIndex() + 1);
			
		}
		
	}
	
	private void retiraBloco(int i){
		if(this.posEsq == i)
			posEsq++;
		this.blocos[i].setCurrentTileIndex(
				this.blocos[i].getCurrentTileIndex() + 1);
		this.numBlocos--;
		return;
	}
	
	public Boolean stop(){ //Retorna GameOver(True) ou nao
		this.setRun(false);
		if(this.linhaAnt != null){
			for(int i = 0;i< 12;i++){
				if(this.blocos[i].getCurrentTileIndex() < this.linhaAnt.blocos[i].getCurrentTileIndex()){
					this.retiraBloco(i);
				}
			}
			if(this.numBlocos == 0)
				return true;
			this.acaoLinhaVerd();
		}
		return false;
	}
	
	public void run(){
		this.setRun(true);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mSpriteVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, Sprite.VERTICES_PER_SPRITE);
		if(this.isRun()){
        	this.countUpdate++;
        	if(this.countUpdate % Linha.Periodo == Linha.Periodo - 1){
        		this.passo();
        		this.countUpdate = 0;
	        }
        }
	}

	public int getCountUpdate() {
		return countUpdate;
	}


	public void setCountUpdate(int countUpdate) {
		this.countUpdate = countUpdate;
	}


	public static int getPeriodo() {
		return Periodo;
	}


	public boolean isRun() {
		return run;
	}


	public void setRun(boolean run) {
		this.run = run;
	}
}
