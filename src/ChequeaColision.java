import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class ChequeaColision extends acm.program.GraphicsProgram{
	// Pantalla.
	private static int ANCHO_PANTALLA = 600;
	private static int ALTO_PANTALLA = 700;

	// Piramide
	private static final int ANCHO_LADRILLO = 60;
	private static final int ALTO_LADRILLO = 30;
	private static final int LADRILLOS_BASE = 9;

	// Cursor
	private static int ANCHO_CURSOR = 80;
	private static int ALTO_CURSOR = 10;
	// cursor
	GRect cursor;

	// pelota
	int alto_pelota = 10;
	GOval pelota;
	double xVelocidad = 3; // velocidad de la pelota en el eje x
	double yVelocidad = -3; // velocidad de la pelota en el eje y

	public void init(){
		pelota = new GOval(alto_pelota, alto_pelota);
		pelota.setFilled(true);
		pelota.setColor(Color.ORANGE);
		add(pelota, 0, ALTO_PANTALLA - 220);

		addMouseListeners();
		setSize(ANCHO_PANTALLA, ALTO_PANTALLA);
		pintaPiramide();
		cursor = new GRect(ANCHO_CURSOR, ALTO_CURSOR);
		cursor.setFilled(true);
		cursor.setFillColor(Color.RED);
		add(cursor, 0, ALTO_PANTALLA - 200);
	}

	public void run() {

		while (true) {
			pelota.move(xVelocidad, yVelocidad);
			// Comprobamos si la pelota choca con alguno de los elementos
			chequeaColision();
			// Ponemos una pausa para limitar la velocidad
			pause(20);

		}
	}

	/*
	 * pintaPiramide introduce una piramide de ladrillos en la pantalla.
	 */
	private void pintaPiramide(){
		int x= -(ANCHO_PANTALLA - LADRILLOS_BASE*ANCHO_LADRILLO) /2;
		int y= 0;

		for (int j=0; j<LADRILLOS_BASE; j++){
			for (int i=j; i<LADRILLOS_BASE; i++){
				GRect ladrillo = new GRect (ANCHO_LADRILLO,ALTO_LADRILLO);
				add (ladrillo,i*ANCHO_LADRILLO-x,y+j*ALTO_LADRILLO);
				//pause(60);
			}
			x = x+ANCHO_LADRILLO/2;
		}
	}

	private void chequeaColision(){
		if (chequeaPared()){
			//chequeo si toca con el cursor
			if(!chequeaCursor()){
				chequeaLadrillos();
			}
		}

	}

	private boolean chequeaPared(){
		boolean auxiliar = true;
		//si toca el techo
		if (pelota.getY() <= 0){
			yVelocidad = -yVelocidad;
			auxiliar = false;
		}

		//si toca la pared derecha
		if (pelota.getX() >= ANCHO_PANTALLA - alto_pelota){
			xVelocidad = -xVelocidad;
			auxiliar = false;
		}

		//si toca la pared izquierda
		if (pelota.getX() <= 0){
			xVelocidad = -xVelocidad;
			auxiliar = false;
		}
		return auxiliar;
	}


	//chequeaCursor devolverá true si ha chocado el cursor con la pelota
	// y false si no ha chocado.
	private boolean chequeaCursor(){
		if (getElementAt(pelota.getX(), pelota.getY()+alto_pelota)==cursor){
			yVelocidad = -yVelocidad;	
		}
		else if (getElementAt(pelota.getX()+alto_pelota, pelota.getY()+alto_pelota)==cursor){
			yVelocidad = -yVelocidad;	
		}
		else if (getElementAt(pelota.getX(), pelota.getY())==cursor){
			xVelocidad = -xVelocidad;	
		}
		else if (getElementAt(pelota.getX()+alto_pelota, pelota.getY())==cursor){
			xVelocidad = -xVelocidad;	
		}else {
			return false;
		} 
		return true;
	}


	//mueve el cursor siguiendo la posición del ratón
	public void mouseMoved (MouseEvent evento){
		if( ((evento.getX()+ANCHO_CURSOR) <= ANCHO_PANTALLA)&&
				(evento.getX()-10 > 0) ){
			cursor.setLocation(evento.getX(),ALTO_PANTALLA-100);
		}
	}

	/*
	 * chequeaLadrillos comprueba chequeaPosicion con las coordenadas de la
	 * pelota 
	 */
	private void chequeaLadrillos() {

		int pelotaX = (int) pelota.getX();
		int pelotaY = (int) pelota.getY();
		int lado = alto_pelota;

		// si chequea posicion devuelve false sigue mirando el resto de puntos
		//de la pelota

		if( !chequeaPosicion(pelotaX, pelotaY,'y')){
			if( !chequeaPosicion(pelotaX+lado, pelotaY-1,'y')){
				if( !chequeaPosicion(pelotaX-1, pelotaY+lado,'x')){
					if( !chequeaPosicion(pelotaX+lado, pelotaY+lado,'y')){
					}
				}
			}
		}
	}


	/**
	 * chequeaPosicion dadas unas cordenadas (posX y posY)de la pelota y una
	 * direccion, calcula el rebote teniendo en cuenta el objeto que se encuentra en esas
	 * coordenadas.
	 * 
	 */
	private boolean chequeaPosicion(int posX, int posY, char direccion) {

		GObject auxiliar;
		boolean choque = false;
		auxiliar = getElementAt(posX, posY);

		// Chequeamos los ladrillos
		if ((auxiliar != cursor) && (auxiliar != null)) {
			remove(auxiliar);
			if (direccion == 'y') {
				yVelocidad = -yVelocidad;
			} else {
				xVelocidad = -xVelocidad;
			}
			//puntuacion++;// aumentamos la puntuacion en uno
			choque = true;
		}


		// devolvemos el valor de choque
		return (choque);
	}
//	if(choque != fondo){
//		gameOver = true;
//	}	
//	if(choque != fondo){
//		gameOver!= true);
//	}

}
