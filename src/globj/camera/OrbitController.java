package globj.camera;


import globj.math.Vector3f;



public class OrbitController extends CameraController {
	
	private float	distance;
	private float	theta;
	private float	phi;
	
	public OrbitController(Camera camera) {
		super(camera);
	}
	
	public void lookAt(Vector3f pos) {
		float st = (float) Math.sin(theta);
		float ct = (float) Math.cos(theta);
		float sp = (float) Math.sin(phi);
		float cp = (float) Math.cos(phi);
		camera.transform().setPosition(pos.x + ct * cp * distance, pos.y + st * cp * distance, pos.y + sp * distance);
	}
	
}
