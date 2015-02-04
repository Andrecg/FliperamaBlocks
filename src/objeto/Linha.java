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
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.view.animation.Animation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import Manager.ResourcesManager;

import  Base.BaseScene;

public class Linha extends Sprite {
	final static int Periodo = 10;
	private boolean canRun;
	private int numBlocos; //Numero de blocos ativos na linha
	private int posEsq; // Index do bloco mais a esquerdo ativo
	private float tempo; //Tempo por bloco
	private boolean sentido; //Tempo por bloco
	private Linha linhaAnt;
	private boolean run;
	private int countUpdate;
	
	private TiledSprite blocos[] = new TiledSprite[12];
	
	public Linha(VertexBufferObjectManager pSpriteVertexBufferObject, Camera camera, int y, Linha linha1,int numBlocos1,float tempo, String color)
	{
		super(960, 50+y, 1213, 100, ResourcesManager.getInstance().linha_region, pSpriteVertexBufferObject);
		this.linhaAnt = linha1;
		this.setZIndex(200);
		this.tempo = tempo;
		this.numBlocos = numBlocos1;
		this.posEsq = 1;
		this.sentido = true;
		this.run = false;
		this.countUpdate = 0;
        this.attachBlocos(pSpriteVertexBufferObject, color);
        for(int i = 1;i<1+numBlocos1;i++){
        	this.blocos[i].setCurrentTileIndex(this.blocos[i].getCurrentTileIndex()-1);
        }
        
        
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
	        super.onManagedUpdate(pSecondsElapsed);
	        if(this.run){
	        	this.countUpdate++;
	        	if(this.countUpdate % Periodo == Periodo - 1){
		        	passo();
		        	this.countUpdate = 0;
		        }
	        }
	}
	
	public int getNumBlocos(){
		return this.numBlocos;
	}
	
	private void attachBlocos(VertexBufferObjectManager pSpriteVertexBufferObject, String color)
    {
        for(int i = 0;i< 12;i++){
        	if(color == "Azul")
        		this.blocos[i] = new TiledSprite(51+100*i, 51, ResourcesManager.getInstance().bloco_region_azul, pSpriteVertexBufferObject);
        	else if(color == "Verm")
        		this.blocos[i] = new TiledSprite(51+100*i, 51, ResourcesManager.getInstance().bloco_region_verm, pSpriteVertexBufferObject);
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
	
	public void stop(){
		this.run = false;
		if(this.linhaAnt != null)
			for(int i = 0;i< 12;i++){
				if(this.blocos[i].getCurrentTileIndex() < this.linhaAnt.blocos[i].getCurrentTileIndex()){
					this.retiraBloco(i);
				}
			}
	}
	
	public void run(){
		this.run = true;
	}
}
