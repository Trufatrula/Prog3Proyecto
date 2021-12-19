package prog3proyecto.juego.componentes;

import java.awt.event.KeyEvent;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.lndf.glengine.engine.DeltaTime;
import com.lndf.glengine.engine.Input;
import com.lndf.glengine.physics.CharacterCollisionData;
import com.lndf.glengine.scene.Component;
import com.lndf.glengine.scene.GameObject;
import com.lndf.glengine.scene.Transform;
import com.lndf.glengine.scene.components.physics.CharacterController;

public class MovimientoFisicas extends Component {
	
	private CharacterController controller;
	
	private float speed = 300f;
	private float jumpHeight = 5f;
	
	private Vector3f velocity = new Vector3f();
	private Vector3f posicionPrevia = new Vector3f();
	private boolean onGround = false;
	private boolean touchingWall = false;
	private boolean touchingUp = false;
	
	private int forwardKey = KeyEvent.VK_W;
	private int backWardsKey = KeyEvent.VK_S;
	private int leftKey = KeyEvent.VK_A;
	private int rightKey = KeyEvent.VK_D;
	private int upKey = KeyEvent.VK_SPACE;
	private int downKey = GLFW.GLFW_KEY_LEFT_SHIFT;
	
	public MovimientoFisicas(CharacterController controller) {
		this.controller = controller;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getForwardKey() {
		return forwardKey;
	}

	public void setForwardKey(int forwardKey) {
		this.forwardKey = forwardKey;
	}

	public int getBackWardsKey() {
		return backWardsKey;
	}

	public void setBackWardsKey(int backWardsKey) {
		this.backWardsKey = backWardsKey;
	}

	public int getLeftKey() {
		return leftKey;
	}

	public void setLeftKey(int leftKey) {
		this.leftKey = leftKey;
	}

	public int getRightKey() {
		return rightKey;
	}

	public void setRightKey(int rightKey) {
		this.rightKey = rightKey;
	}

	public int getUpKey() {
		return upKey;
	}

	public void setUpKey(int upKey) {
		this.upKey = upKey;
	}

	public int getDownKey() {
		return downKey;
	}

	public void setDownKey(int downKey) {
		this.downKey = downKey;
	}

	@Override
	public void update() {
		GameObject obj = this.getGameObject();
		Transform t = obj.getTransform();
		float step = (float) (speed * DeltaTime.get());
		Vector3f v = new Vector3f();
		Vector3f disp = new Vector3f();
		if(Input.getKey(this.forwardKey)) {
			v.add(t.getFront());
		}
		if(Input.getKey(this.backWardsKey)) {
			v.add(t.getBack());
		}
		if(Input.getKey(this.rightKey)) {
			v.add(t.getRight());
		}
		if(Input.getKey(this.leftKey)) {
			v.add(t.getLeft());
		}
		if (v.length() > 0) v.normalize().mul(step);
		disp.add(this.velocity);
		if (this.onGround) {
			if (Input.getKey(this.upKey)) {
				this.velocity.set(v);
				this.velocity.y = this.jumpHeight;
				this.onGround = false;
			} else {
				disp.add(v);
			}
		}
		disp.mul((float) DeltaTime.get());
		CharacterCollisionData data = this.controller.move(disp, 0);
		if (this.onGround) {
			this.velocity.set(0);
		} else {
			this.velocity.add(this.getScene().getGravity().mul((float) DeltaTime.get()));
		}
		Vector3f p = t.getWorldPosition();
		if (this.onGround && !data.collidesDown()) {
			this.velocity.set(v);
		}
		this.onGround = data.collidesDown();
		this.touchingWall = data.collidesSide();
		if (!this.touchingUp && data.collidesUp()) {
			this.velocity.y = 0;
		}
		if (this.touchingWall) {
			p.sub(this.posicionPrevia, this.velocity);
		}
		this.touchingUp = data.collidesUp();
		this.posicionPrevia.set(p);
	}
	
}
