
public class Vector {
	private double x;
	private double y;
	public Vector() {
		x = 0;
		y = 0;
	}
	public Vector(double size, double angle) {
		x = size * Math.cos(angle);
		y = size * Math.sin(angle);
	}
	
	public void saccel(double xs) {
		double angle = Math.atan2(y, x);
		y += xs * Math.sin(angle);
		x += xs * Math.cos(angle);

	}
	
	public void vaccel(Vector vs) {
		x += vs.x;
		y += vs.y;
	}

	public void paccel(double precent) {
		x /= 1 +  precent;
		y /= 1 +  precent;
	}

	public void projection_accelaration(double angle, double precent1, double precent2){
		double a = Math.cos(angle);
		a *= a;
		x *= a * (1 + precent1) + (1 - a) * (1 + precent2);
		y *= (1 - a) * (1 + precent1) + a * (1 + precent2);
	}

	public void turning(double angle, double precent){
		double s = Math.sin(angle), c = Math.cos(angle);
		double temp = x * s + y * c;
		temp *= precent;
		x += temp * c;
		x -= temp * s;
		y += temp * s;
		y -= temp * c;
	}

	public double size(){
		return Math.sqrt(x*x + y*y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
