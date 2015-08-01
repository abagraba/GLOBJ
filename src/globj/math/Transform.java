package globj.math;



public class Transform {
	
	private final Vector3f			position;
	private final UnitQuaternion	rotation;
	
	private float scale = 1;
	
	public Transform(Vector3f position, UnitQuaternion rotation) {
		this.position = new Vector3f(position);
		this.rotation = new UnitQuaternion(rotation);
	}
	
	public Transform(Vector3f position) {
		this.position = new Vector3f(position);
		this.rotation = new UnitQuaternion();
	}
	
	public Transform(UnitQuaternion rotation) {
		this.position = new Vector3f();
		this.rotation = new UnitQuaternion(rotation);
	}
	
	public Transform() {
		this.position = new Vector3f();
		this.rotation = new UnitQuaternion();
	}
	
	public Transform(Transform t) {
		this(t.position, t.rotation);
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void setPosition(Vector3f v) {
		position.x = v.x;
		position.y = v.y;
		position.z = v.z;
	}
	
	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	
	public void setRotation(float s, float i, float j, float k) {
		rotation.s = s;
		rotation.i = i;
		rotation.j = j;
		rotation.k = k;
	}
	
	public void setRotation(UnitQuaternion q) {
		rotation.s = q.s;
		rotation.i = q.i;
		rotation.j = q.j;
		rotation.k = q.k;
	}
	
	public UnitQuaternion getRotation() {
		return new UnitQuaternion(rotation);
	}
	
	public void setScale(float f) {
		scale = f;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void translateBy(Vector3f delta) {
		position.add(delta);
	}
	
	public void rotateBy(UnitQuaternion delta) {
		rotation.set(rotation.rotate(delta));
	}
	
	public Matrix4x4f getModelMatrix() {
		float s = scale == 0 ? 0 : (1 / scale);
		Matrix4x4f mat = new Matrix4x4f();
		mat.m00 = 2 * (0.5f - rotation.j * rotation.j - rotation.k * rotation.k);
		mat.m10 = 2 * (rotation.i * rotation.j - rotation.s * rotation.k);
		mat.m20 = 2 * (rotation.i * rotation.k + rotation.s * rotation.j);
		mat.m01 = 2 * (rotation.i * rotation.j + rotation.s * rotation.k);
		mat.m11 = 2 * (0.5f - rotation.i * rotation.i - rotation.k * rotation.k);
		mat.m21 = 2 * (rotation.j * rotation.k + rotation.s * rotation.i);
		mat.m02 = 2 * (rotation.i * rotation.k - rotation.s * rotation.j);
		mat.m12 = 2 * (rotation.j * rotation.k + rotation.s * rotation.i);
		mat.m22 = 2 * (0.5f - rotation.i * rotation.i - rotation.j * rotation.j);
		mat.m30 = position.x;
		mat.m31 = position.y;
		mat.m32 = position.z;
		mat.m33 = s;
		return mat;
	}
	
	public Matrix4x4f getViewMatrix() {
		float s = scale == 0 ? 0 : (1 / scale);
		Matrix4x4f mat = new Matrix4x4f();
		mat.m00 = 2 * (0.5f - rotation.j * rotation.j - rotation.k * rotation.k);
		mat.m10 = 2 * (rotation.i * rotation.j + rotation.s * rotation.k);
		mat.m20 = 2 * (rotation.i * rotation.k - rotation.s * rotation.j);
		mat.m01 = 2 * (rotation.i * rotation.j - rotation.s * rotation.k);
		mat.m11 = 2 * (0.5f - rotation.i * rotation.i - rotation.k * rotation.k);
		mat.m20 = 2 * (rotation.j * rotation.k - rotation.s * rotation.i);
		mat.m02 = 2 * (rotation.i * rotation.k + rotation.s * rotation.j);
		mat.m12 = 2 * (rotation.j * rotation.k - rotation.s * rotation.i);
		mat.m22 = 2 * (0.5f - rotation.i * rotation.i - rotation.j * rotation.j);
		mat.m30 = -position.x;
		mat.m31 = -position.y;
		mat.m32 = -position.z;
		mat.m33 = s;
		return mat;
	}
	
}
