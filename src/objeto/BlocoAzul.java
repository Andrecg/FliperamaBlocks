package objeto;

import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class BlocoAzul extends Bloco {

	public BlocoAzul(float pX, float pY, VertexBufferObjectManager vbo,
			Camera camera, PhysicsWorld physicsWorld, int pos) {
		super(pX, pY, vbo, camera, physicsWorld, pos);
	}

}
