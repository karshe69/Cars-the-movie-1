import java.awt.Graphics2D;

public class Car extends Point{ // the car~TM

	private Point[] edges = new Point[8]; // spots specified as edges which can be hit be the track
	private Point[] points = new Point[56]; // the individual vertices that make up the car

	// arrays of lines which are combined from 2 points to make up the car
	private Line[] spoiler1 = new Line[3];
	private Line[] cockpit = new Line[4];
	private Line[] spoiler34 = new Line[5];
	private Line[] wheel_axis = new Line[6];
	private Line[] body = new Line[6];
	private Line[] spoiler2 = new Line[7];
	private Line[][] wheels = new Line[4][4];

	// 2 constants that make up the size and shape of the wheels
	private double wheelRadius;
	private double wheel_angle;

	// 2 constants that make up the size and shape of the car
	private double innerAngle;
	private double innerRadius;

	private double angle; // current angle of the car
	private Vector speed; // current speed of the car
	private double wheelAngle = 0; //angle of the wheels of the car compared to the car

	//to returns the car to its starting position when it crashes
	private Point startPoint;
	private double startAngle;
	private Vector startSpeed;
	
	public Car(double x, double y, int width, int height, double angle) { // initializes the car
		super(x, y); // position of the center of the car on the screen
		speed = new Vector(); // speed of the car
		this.angle = angle; // angle of the car compared to the width of the screen

		// sets up the starting parameters
		startPoint = new Point(x, y);
		startAngle = angle;
		startSpeed = new Vector();

		// creates the constants that make up the shape and size of both the car and the wheels
		innerRadius = Math.sqrt(height * height / 4.0 + width * width / 4.0);
		double temp1 = height / 16.0, temp2 = width / 16.0;
		wheelRadius = Math.sqrt(temp1 * temp1 + temp2 * temp2);
		wheel_angle = Math.atan(temp2 / temp1);
		innerAngle = Math.atan((double)width / height);

		// initializes the points
		for (int i = 0; i < 56; i++)
			points[i] = new Point();

		// initializes the lines that make up the car
		for (int i = 0; i < 3; i++) {
			spoiler1[i] = new Line(points[i], points[i + 1]);
			cockpit[i] = new Line(points[i + 4], points[i + 5]);
		}
		cockpit[3] = new Line(points[7], points[4]);
		for (int i = 0; i < 2; i++){
			spoiler34[i] = new Line(points[i + 8], points[i + 9]);
			spoiler34[i + 2] = new Line(points[i + 11], points[i + 12]);
			wheel_axis[i] = new Line(points[i + 16], points[i + 17]);
			wheel_axis[i + 3] = new Line(points[i + 21], points[i + 22]);
		}
		spoiler34[4] = new Line(points[14], points[15]);
		wheel_axis[2] = new Line(points[19], points[20]);
		wheel_axis[5] = new Line(points[24], points[25]);
		body[0] = new Line(points[26], points[8]);
		body[1] = new Line(points[8], points[27]);
		body[2] = new Line(points[27], points[28]);
		body[3] = new Line(points[29], points[11]);
		body[4] = new Line(points[11], points[30]);
		body[5] = new Line(points[30], points[31]);
		for (int i = 0; i < 7; i++){
			spoiler2[i] = new Line(points[32 + i], points[33 + i]);
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++)
				wheels[i][j] = new Line(points[40 + 4 * i + j], points[41 + 4 * i + j]);
			wheels[i][3] = new Line(points[40 + 4 * i], points[43 + 4 * i]);
		}

		// makes it so the edges of the car sync with the appointed points that make up those edges
		edges[0] = points[1];
		edges[1] = points[2];
		edges[2] = points[35];
		edges[3] = points[36];
		edges[4] = points[50];
		edges[5] = points[54];
		edges[6] = points[28];
		edges[7] = points[31];

		updatePoints(); // updates the point's position relative to the car's position and orientation.
	}

	public void wheelTurn(double alpha) { // turns the wheels of the car
		double limit = Math.PI / 6;
		if (wheelAngle + alpha < limit) // limits how much the wheels can turn compared to the car
			if (wheelAngle + alpha > -limit)
				wheelAngle += alpha;
			else wheelAngle = -limit;
		else wheelAngle = limit;
	}

	public void turn(){ // turns the car and updates everything accordingly
		double turnn = wheelAngle * 0.5;
		angle += turnn; // updates the car's angle
		wheelAngle -= turnn; // updates the wheels's angle
		speed.turning(-angle, 0.05); // updates the car's speed
	}

	public void  move() { // moves the car
		turn(); // turns the car
		x += speed.getX(); // moves it according to it's speed in the x axis
		y += speed.getY(); // moves it according to it's speed in the y axis
		updatePoints(); // updates the points to the new position
	}

	public void draw(Graphics2D g){ // draws all of the lines that make up the car
		for (int i = 0; i < 3; i++)
			spoiler1[i].draw(g);

		for (int i = 0; i < 4; i++){
			cockpit[i].draw(g);
		}
		for (int i = 0; i < 5; i++)
			spoiler34[i].draw(g);

		for (int i = 0; i < 6; i++){
			wheel_axis[i].draw(g);
			body[i].draw(g);
		}
		for (int i = 0; i < 7; i++)
			spoiler2[i].draw(g);
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				wheels[i][j].draw(g);
	}

	public void shine(){ // updates the car to its starting position
		x = startPoint.getX();
		y = startPoint.getY();
		angle = startAngle;
		speed.copy(startSpeed);
		wheelAngle = 0;
	}

	public void updatePoints(){ // updates the points of the car relative to the corners of the car
		double x1, y1, x2, y2;
		Point[] corners = new Point[4];
		for (int i = -1; i <= 1; i += 2){
			corners[i + 2] = new Point(x + i * (innerRadius * Math.cos(innerAngle + i * angle)), y + innerRadius * Math.sin(innerAngle + i * angle));
			corners[i + 1] = new Point(x - i * (innerRadius * Math.cos(innerAngle + i * angle)),y - innerRadius * Math.sin(innerAngle + i * angle));
		}

		//spoiler1
		x1 = (corners[2].getX() + 3 * corners[1].getX()) / 4;
		y1 = (corners[2].getY() + 3 * corners[1].getY()) / 4;
		x2 = (corners[0].getX() + 3 * corners[3].getX()) / 4;
		y2 = (corners[0].getY() + 3 * corners[3].getY()) / 4;
		points[0].update((x1 * 29 + x2 * 3) / 32, (y1 * 29 + y2 * 3) / 32);
		points[1].update(x1, y1);
		points[8].update((x1 + x2) / 2, (y1 + y2) / 2); // part of spoiler3 for better efficeincy

		x1 = (corners[1].getX() + 3 * corners[2].getX()) / 4;
		y1 = (corners[1].getY() + 3 * corners[2].getY()) / 4;
		x2 = (corners[3].getX() + 3 * corners[0].getX()) / 4;
		y2 = (corners[3].getY() + 3 * corners[0].getY()) / 4;
		points[2].update(x1, y1);
		points[3].update((x1 * 29 + x2 * 3) / 32, (y1 * 29 + y2 * 3) / 32);
		points[11].update((x1 + x2) / 2, (y1 + y2) / 2); // part of spoiler4 for better efficeincy

		//cockpit
		x1 = (13 * corners[1].getX() + 19 * corners[2].getX()) / 32;
		y1 = (13 * corners[1].getY() + 19 * corners[2].getY()) / 32;
		x2 = (13 * corners[3].getX() + 19 * corners[0].getX()) / 32;
		y2 = (13 * corners[3].getY() + 19 * corners[0].getY()) / 32;
		points[4].update((x1 * 19 + x2 * 13) / 32, (y1 * 19 + y2 * 13) / 32);
		points[5].update((x1 * 57 + x2 * 71) / 128, (y1 * 57 + y2 * 71) / 128);

		x1 = (13 * corners[2].getX() + 19 * corners[1].getX()) / 32;
		y1 = (13 * corners[2].getY() + 19 * corners[1].getY()) / 32;
		x2 = (13 * corners[0].getX() + 19 * corners[3].getX()) / 32;
		y2 = (13 * corners[0].getY() + 19 * corners[3].getY()) / 32;
		points[6].update((x1 * 57 + x2 * 71) / 128, (y1 * 57 + y2 * 71) / 128);
		points[7].update((x1 * 19 + x2 * 13) / 32, (y1 * 19 + y2 * 13) / 32);

		//spoiler3

		x1 = (61 * corners[2].getX() + 67 * corners[0].getX()) / 128;
		y1 = (61 * corners[2].getY() + 67 * corners[0].getY()) / 128;
		x2 = (61 * corners[1].getX() + 67 * corners[3].getX()) / 128;
		y2 = (61 * corners[1].getY() + 67 * corners[3].getY()) / 128;
		points[9].update((x1 * 3 + x2 * 29) / 32, (y1 * 3 + y2 * 29) / 32);

		x1 = (25 * corners[2].getX() + 7 * corners[0].getX()) / 32;
		y1 = (25 * corners[2].getY() + 7 * corners[0].getY()) / 32;
		x2 = (25 * corners[1].getX() + 7 * corners[3].getX()) / 32;
		y2 = (25 * corners[1].getY() + 7 * corners[3].getY()) / 32;
		points[10].update((x1 * 3 + x2 * 29) / 32, (y1 * 3 + y2 * 29) / 32);

		//spoiler4

		x1 = (15 * corners[1].getX() + 17 * corners[3].getX()) / 32;
		y1 = (15 * corners[1].getY() + 17 * corners[3].getY()) / 32;
		x2 = (15 * corners[2].getX() + 17 * corners[0].getX()) / 32;
		y2 = (15 * corners[2].getY() + 17 * corners[0].getY()) / 32;
		points[12].update((x1 * 3 + x2 * 29) / 32, (y1 * 3 + y2 * 29) / 32);

		x1 = (25 * corners[1].getX() + 7 * corners[3].getX()) / 32;
		y1 = (25 * corners[1].getY() + 7 * corners[3].getY()) / 32;
		x2 = (25 * corners[2].getX() + 7 * corners[0].getX()) / 32;
		y2 = (25 * corners[2].getY() + 7 * corners[0].getY()) / 32;
		points[13].update((x1 * 3 + x2 * 29) / 32, (y1 * 3 + y2 * 29) / 32);

		//back
		x1 = (27 * corners[1].getX() + 5 * corners[2].getX()) / 32;
		y1 = (27 * corners[1].getY() + 5 * corners[2].getY()) / 32;
		x2 = (27 * corners[3].getX() + 5 * corners[0].getX()) / 32;
		y2 = (27 * corners[3].getY() + 5 * corners[0].getY()) / 32;
		points[14].update((x1 * 29 + x2 * 3) / 32, (y1 * 29 + y2 * 3) / 32);


		x1 = (27 * corners[2].getX() + 5 * corners[1].getX()) / 32;
		y1 = (27 * corners[2].getY() + 5 * corners[1].getY()) / 32;
		x2 = (27 * corners[0].getX() + 5 * corners[3].getX()) / 32;
		y2 = (27 * corners[0].getY() + 5 * corners[3].getY()) / 32;
		points[15].update((x1 * 29 + x2 * 3) / 32, (y1 * 29 + y2 * 3) / 32);

		//wheel axis
		x1 = (10 * corners[2].getX() + 22 * corners[1].getX()) / 32;
		y1 = (10 * corners[2].getY() + 22 * corners[1].getY()) / 32;
		x2 = (10 * corners[0].getX() + 22 * corners[3].getX()) / 32;
		y2 = (10 * corners[0].getY() + 22 * corners[3].getY()) / 32;
		points[17].update((x1 * 7 + x2 * 25) / 32, (y1 * 7 + y2 * 25) / 32);

		x1 = (9 * corners[1].getX() + 23 * corners[2].getX()) / 32;
		y1 = (9 * corners[1].getY() + 23 * corners[2].getY()) / 32;
		x2 = (9 * corners[3].getX() + 23 * corners[0].getX()) / 32;
		y2 = (9 * corners[3].getY() + 23 * corners[0].getY()) / 32;
		points[22].update((x1 * 7 + x2 * 25) / 32, (y1 * 7 + y2 * 25) / 32);

		//body
		x1 = (3 * corners[2].getX() + 5 * corners[1].getX()) / 8;
		y1 = (3 * corners[2].getY() + 5 * corners[1].getY()) / 8;
		x2 = (3 * corners[0].getX() + 5 * corners[3].getX()) / 8;
		y2 = (3 * corners[0].getY() + 5 * corners[3].getY()) / 8;
		points[26].update((x1 * 29 + x2 * 3) / 32, (y1 * 29 + y2 * 3) / 32);

		x1 = (5 * corners[2].getX() + 11 * corners[1].getX()) / 16;
		y1 = (5 * corners[2].getY() + 11 * corners[1].getY()) / 16;
		x2 = (5 * corners[0].getX() + 11 * corners[3].getX()) / 16;
		y2 = (5 * corners[0].getY() + 11 * corners[3].getY()) / 16;
		points[27].update((x1 * 29 + x2 * 35) / 64, (y1 * 29 + y2 * 35) / 64);

		x1 = (7 * corners[0].getX() + 9 * corners[3].getX()) / 16;
		y1 = (7 * corners[0].getY() + 9 * corners[3].getY()) / 16;
		points[28].update(x1, y1);


		x1 = (3 * corners[1].getX() + 5 * corners[2].getX()) / 8;
		y1 = (3 * corners[1].getY() + 5 * corners[2].getY()) / 8;
		x2 = (3 * corners[3].getX() + 5 * corners[0].getX()) / 8;
		y2 = (3 * corners[3].getY() + 5 * corners[0].getY()) / 8;
		points[29].update((x1 * 29 + x2 * 3) / 32, (y1 * 29 + y2 * 3) / 32);

		x1 = (5 * corners[1].getX() + 11 * corners[2].getX()) / 16;
		y1 = (5 * corners[1].getY() + 11 * corners[2].getY()) / 16;
		x2 = (5 * corners[3].getX() + 11 * corners[0].getX()) / 16;
		y2 = (5 * corners[3].getY() + 11 * corners[0].getY()) / 16;
		points[30].update((x1 * 29 + x2 * 35) / 64, (y1 * 29 + y2 * 35) / 64);

		x1 = (7 * corners[3].getX() + 9 * corners[0].getX()) / 16;
		y1 = (7 * corners[3].getY() + 9 * corners[0].getY()) / 16;
		points[31].update(x1, y1);

		//rest of wheel axis
		x1 = points[27].getX();
		y1 = points[27].getY();
		x2 = points[28].getX();
		y2 = points[28].getY();
		points[16].update((17 * x1 + 15 * x2 ) / 32, (17 * y1 + 15 * y2 ) / 32);
		points[20].update((x1 * 11 + x2 * 21) / 32, (y1 * 11 + y2 * 21) / 32);

		points[32].update((3 * x1 + 29 * x2 ) / 32, (3 * y1 + 29 * y2 ) / 32);//part of spoiler2


		x1 = points[30].getX();
		y1 = points[30].getY();
		x2 = points[31].getX();
		y2 = points[31].getY();
		points[21].update((17 * x1 + 15 * x2 ) / 32, (17 * y1 + 15 * y2 ) / 32);
		points[25].update((x1 * 11 + x2 * 21) / 32, (y1 * 11 + y2 * 21) / 32);


		points[39].update((3 * x1 + 29 * x2 ) / 32, (3 * y1 + 29 * y2 ) / 32);//part of spoiler2


		//spoiler2
		x1 = (7 * corners[2].getX() + 57 * corners[1].getX()) / 64;
		y1 = (7 * corners[2].getY() + 57 * corners[1].getY()) / 64;
		x2 = (7 * corners[0].getX() + 57 * corners[3].getX()) / 64;
		y2 = (7 * corners[0].getY() + 57 * corners[3].getY()) / 64;
		points[33].update((3 * x1 + 29 * x2 ) / 32, (3 * y1 + 29 * y2 ) / 32);

		x1 = corners[1].getX();
		y1 = corners[1].getY();
		x2 = corners[3].getX();
		y2 = corners[3].getY();
		points[34].update((3 * x1 + 29 * x2 ) / 32, (3 * y1 + 29 * y2 ) / 32);
		points[35].update(x2, y2);


		points[50].update((x1 * 15 + x2 * 1) / 16, (y1 * 15 + y2 * 1) / 16);//part of wheel3
		points[51].update((x1 * 25 + x2 * 7) / 32, (y1 * 25 + y2 * 7) / 32);//part of wheel3


		x1 = corners[2].getX();
		y1 = corners[2].getY();
		x2 = corners[0].getX();
		y2 = corners[0].getY();
		points[36].update(x2, y2);
		points[37].update((3 * x1 + 29 * x2 ) / 32, (3 * y1 + 29 * y2 ) / 32);

		points[54].update((x1 * 15 + x2 * 1) / 16, (y1 * 15 + y2 * 1) / 16);//part of wheel4
		points[55].update((x1 * 25 + x2 * 7) / 32, (y1 * 25 + y2 * 7) / 32);//part of wheel4

		x1 = (7 * corners[1].getX() + 57 * corners[2].getX()) / 64;
		y1 = (7 * corners[1].getY() + 57 * corners[2].getY()) / 64;
		x2 = (7 * corners[3].getX() + 57 * corners[0].getX()) / 64;
		y2 = (7 * corners[3].getY() + 57 * corners[0].getY()) / 64;
		points[38].update((3 * x1 + 29 * x2 ) / 32, (3 * y1 + 29 * y2 ) / 32);

		//wheel3
		x1 = (5 * corners[2].getX() + 27 * corners[1].getX()) / 32;
		y1 = (5 * corners[2].getY() + 27 * corners[1].getY()) / 32;
		x2 = (5 * corners[0].getX() + 27 * corners[3].getX()) / 32;
		y2 = (5 * corners[0].getY() + 27 * corners[3].getY()) / 32;
		points[48].update((x1 * 25 + x2 * 7) / 32, (y1 * 25 + y2 * 7) / 32);
		points[49].update((x1 * 15 + x2 * 1) / 16, (y1 * 15 + y2 * 1) / 16);

		//wheel4
		x1 = (5 * corners[1].getX() + 27 * corners[2].getX()) / 32;
		y1 = (5 * corners[1].getY() + 27 * corners[2].getY()) / 32;
		x2 = (5 * corners[3].getX() + 27 * corners[0].getX()) / 32;
		y2 = (5 * corners[3].getY() + 27 * corners[0].getY()) / 32;
		points[52].update((x1 * 25 + x2 * 7) / 32, (y1 * 25 + y2 * 7) / 32);
		points[53].update((x1 * 15 + x2 * 1) / 16, (y1 * 15 + y2 * 1) / 16);




		//wheel 1
		x1 = (17 * corners[2].getX() + 239 * corners[1].getX()) / 256;
		y1 = (17 * corners[2].getY() + 239 * corners[1].getY()) / 256;
		x2 = (17 * corners[0].getX() + 239 * corners[3].getX()) / 256;
		y2 = (17 * corners[0].getY() + 239 * corners[3].getY()) / 256;
		Point middle = new Point((x1 * 51 + x2 * 205) / 256, (y1 * 51 + y2 * 205) / 256);

		Point[] tcorners = new Point[4];
		for (int i = -1; i <= 1; i += 2){
			tcorners[i + 2] = new Point(middle.getX() + i * (wheelRadius * Math.cos(wheel_angle +(i * ( wheelAngle + angle)))), middle.getY() + wheelRadius * Math.sin(wheel_angle + i * ( wheelAngle + angle)));
			tcorners[i + 1] = new Point(middle.getX() - i * (wheelRadius * Math.cos(wheel_angle + i * ( wheelAngle + angle))),middle.getY() - wheelRadius * Math.sin(wheel_angle+ i *  ( wheelAngle + angle)));
		}

		points[40].update(tcorners[0].getX(), tcorners[0].getY());//part of wheel1
		points[41].update(tcorners[2].getX(), tcorners[2].getY());//part of wheel1
		points[42].update(tcorners[1].getX(), tcorners[1].getY());//part of wheel1
		points[43].update(tcorners[3].getX(), tcorners[3].getY());//part of wheel1

		points[18].update((tcorners[2].getX() * 9 + tcorners[0].getX() * 7) / 16, (tcorners[2].getY() * 9 + tcorners[0].getY() * 7) / 16);
		points[19].update((tcorners[2].getX() * 7 + tcorners[0].getX() * 9) / 16, (tcorners[2].getY() * 7 + tcorners[0].getY() * 9) / 16);

		//wheel 2

		x1 = (239 * corners[2].getX() + 17 * corners[1].getX()) / 256;
		y1 = (239 * corners[2].getY() + 17 * corners[1].getY()) / 256;
		x2 = (239 * corners[0].getX() + 17 * corners[3].getX()) / 256;
		y2 = (239 * corners[0].getY() + 17 * corners[3].getY()) / 256;
		middle = new Point((x1 * 51 + x2 * 205) / 256, (y1 * 51 + y2 * 205) / 256);

		for (int i = -1; i <= 1; i += 2){
			tcorners[i + 2].update(middle.getX() + i * (wheelRadius * Math.cos(wheel_angle + i * ( wheelAngle + angle))), middle.getY() + wheelRadius * Math.sin(wheel_angle + i * ( wheelAngle + angle)));
			tcorners[i + 1].update(middle.getX() - i * (wheelRadius * Math.cos(wheel_angle + i * ( wheelAngle + angle))),middle.getY() - wheelRadius * Math.sin(wheel_angle+ i *  ( wheelAngle + angle)));
		}

		points[44].update(tcorners[0].getX(), tcorners[0].getY());//part of wheel1
		points[45].update(tcorners[2].getX(), tcorners[2].getY());//part of wheel1
		points[46].update(tcorners[1].getX(), tcorners[1].getY());//part of wheel1
		points[47].update(tcorners[3].getX(), tcorners[3].getY());//part of wheel1

		points[23].update((tcorners[1].getX() * 9 + tcorners[3].getX() * 7) / 16, (tcorners[1].getY() * 9 + tcorners[3].getY() * 7) / 16);
		points[24].update((tcorners[1].getX() * 7 + tcorners[3].getX() * 9) / 16, (tcorners[1].getY() * 7 + tcorners[3].getY() * 9) / 16);

	}
	
	public void vaccel(Vector vs) { // vector acceleration
		speed.vaccel(vs);
	}

	public void accel(double size){ // speeds up by size
		vaccel(new Vector(size, angle));
	}

	public double getAngle() {
		return angle;
	}

	public Vector getSpeed() {
		return speed;
	}

	public synchronized Point[] getEdges() {
		final Point[] pnts = edges;
		return pnts;
	}
}
