package objeto;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


import Manager.ResourcesManager;

public abstract class Bloco extends AnimatedSprite {
	
	private Body body;

	public Bloco(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld,int pos)
	{
	    super(pX, pY, ResourcesManager.getInstance().bloco_region_azul, vbo);
	}
	}
	

