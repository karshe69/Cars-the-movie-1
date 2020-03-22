public class Vector { // a vector
	private double x;
	private double y;
	public Vector() { // initializes the vector with default parameters
		x = 0;
		y = 0;
	}
	public Vector(double size, double angle) { // initializes the vector with given parameters
		x = size * Math.cos(angle);
		y = size * Math.sin(angle);
	}

	public void copy(Vector vec){ // copies a given vector's parameters
		x = vec.getX();
		y = vec.getY();
	}
	
	public void vaccel(Vector vs) { // vector acceleration
		x += vs.x;
		y += vs.y;
	}

	public void paccel(double percent) { // percentage acceleration
		x /= 1 +  percent;
		y /= 1 +  percent;
	}

	public void projectionAcceleration(double angle, double percent1, double percent2){ // accelerates the vector along a given axis by percentage1
		// and by percentage2 along the perpendicular axis
		double a = Math.cos(angle);
		a *= a;
		x *= a * (1 + percent1) + (1 - a) * (1 + percent2);
		y *= (1 - a) * (1 + percent1) + a * (1 + percent2);
	}

	public void turning(double angle, double percent){ // turns the vector to angle by percent
		double s = Math.sin(angle), c = Math.cos(angle);
		double temp = x * s + y * c;
		temp *= percent;
		x += temp * c;
		x -= temp * s;
		y += temp * s;
		y -= temp * c;
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
