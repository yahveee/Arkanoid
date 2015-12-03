import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.util.RandomGenerator;

public class Arkanoid extends acm.program.GraphicsProgram {

	GImage nave;
	GImage misil;
	GRect ladrillo;
	GImage fondo;
	GImage fondoFinal;
	GRect ladrillo2;
	GLabel puntuacion;

	int alto_misil = 20;
	int ANCHO_NAVE = 100;

	int velocidadX = 1;
	int velocidadY = -1;
	int puntos =0;
	boolean gameOver = false;

	private static final int ANCHO_LADRILLO = 36;
	private static final int ALTO_LADRILLO = 19;
	private static final int ANCHO_PANTALLA = 750;
	private static final int ALTO_PANTALLA = 1000;
	private static final int LADRILLOS_BASE = 18;

	public void init(){
		setSize(ANCHO_PANTALLA,ALTO_PANTALLA);
		
	
		fondo = new GImage("universo.gif");
		add(fondo);
		
		puntuacion = new GLabel ("PUNTUACION: " + puntos);
		puntuacion.setVisible(true);
		puntuacion.setColor(Color.white);
		puntuacion.setLocation(ANCHO_PANTALLA/2-puntuacion.getWidth()/2, ALTO_PANTALLA-100);
		add(puntuacion);
		
		misil = new GImage("ball.png");
		add(misil,getWidth()/2, getHeight()-160);
		misil.setSize(20,20);

		pintaPiramide();

		nave = new GImage("barra.png");
		add(nave,getWidth()/2,getHeight()-100);
		nave.setSize(100,25);

		addMouseListeners();


	}

	private void pintaPiramide(){
		int alto = getHeight();
		int ancho = getWidth();
		int basePiramide = LADRILLOS_BASE * ANCHO_LADRILLO;
		RandomGenerator aleatorio = new RandomGenerator();
		for (int j=0; j<LADRILLOS_BASE; j++){
			//desplazamiento = desplazamiento-ANCHO_LADRILLO/2;	
			for (int i=0; i< LADRILLOS_BASE-j; i++){
				GRect casilla = new GRect(ANCHO_LADRILLO,ALTO_LADRILLO);
				add (casilla,
						ancho/2-basePiramide/2+ANCHO_LADRILLO*j/2 + ANCHO_LADRILLO*i,
						ALTO_LADRILLO*j);
				casilla.setFilled(true);
				casilla.setFillColor(aleatorio.nextColor());
			}
		}
		for (int i=0; i<LADRILLOS_BASE; i++){
			GRect casilla = new GRect(ANCHO_LADRILLO,ALTO_LADRILLO);
			add (casilla);
			casilla.setLocation(ancho/2-basePiramide/2+ANCHO_LADRILLO*i, 
								ALTO_LADRILLO*LADRILLOS_BASE);
			casilla.setFilled(true);
			casilla.setFillColor(Color.WHITE);
		}
		for (int i=0; i<LADRILLOS_BASE; i++){
			GRect casilla = new GRect(ANCHO_LADRILLO,ALTO_LADRILLO);
			add (casilla);
			casilla.setLocation(ancho/2-basePiramide/2+ANCHO_LADRILLO*i, 
								ALTO_LADRILLO*LADRILLOS_BASE);
			casilla.setFilled(true);
			casilla.setFillColor(Color.orange);
		}
	}


	public void run(){
		waitForClick();
		while(!gameOver){
			
			misil.move(velocidadX, velocidadY);
			chequeaColision();
			pause(2);

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
		if (misil.getY() <= 0){
			velocidadY = -velocidadY;
			auxiliar = false;
		}

		//si toca la pared derecha
		if (misil.getX() >= ANCHO_PANTALLA - alto_misil){
			velocidadX = -velocidadX;
			auxiliar = false;
		}

		//si toca la pared izquierda
		if (misil.getX() <= 0){
			velocidadX = -velocidadX;
			auxiliar = false;
		}
		if(misil.getY() > nave.getY()+40){
			velocidadX=0;
			velocidadY=0;
			auxiliar=false;
			fondoFinal = new GImage("gameover.png");
			fondoFinal.setSize(ANCHO_PANTALLA, ALTO_PANTALLA);
			add(fondoFinal);
			pause(2000);
		}
		return auxiliar;
	}


	//chequeaCursor devolverá true si ha chocado el cursor con la pelota
	// y false si no ha chocado.
	private boolean chequeaCursor(){
		if (getElementAt(misil.getX(), misil.getY()+alto_misil)==nave){
			velocidadY = -velocidadY;	
		}
		else if (getElementAt(misil.getX()+alto_misil, misil.getY()+alto_misil)==nave){
			velocidadY = -velocidadY;	
		}
		else if (getElementAt(misil.getX(), misil.getY())==nave){
			velocidadX = -velocidadX;	
		}
		else if (getElementAt(misil.getX()+alto_misil, misil.getY())==nave){
			velocidadX = -velocidadX;	
		}else {
			return false;
		} 
		return true;
	}


	//mueve el cursor siguiendo la posición del ratón
	public void mouseMoved (MouseEvent evento){
		if( ((evento.getX()+ANCHO_NAVE) <= getWidth()) ){
			nave.setLocation(evento.getX(),getHeight()-100);
		}
	}
	//	public void mouseMoved (MouseEvent evento){
	//		if( ((evento.getX()) <= 1000 )){
	//			nave.setLocation(evento.getX(),600);
	//		}
	//	}

	/*
	 * chequeaLadrillos comprueba chequeaPosicion con las coordenadas de la
	 * pelota 
	 */

	private void chequeaLadrillos() {

		int misilX = (int) misil.getX();
		int misilY = (int) misil.getY();
		int lado = alto_misil;

		// si chequea posicion devuelve false sigue mirando el resto de puntos
		//de la pelota

		if( !chequeaPosicion(misilX, misilY,'y')){
			if( !chequeaPosicion(misilX+lado, misilY-1,'y')){
				if( !chequeaPosicion(misilX-1, misilY+lado,'x')){
					if( !chequeaPosicion(misilX+lado, misilY+lado,'y')){
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
		if ((auxiliar != nave) && (auxiliar != fondo) && (auxiliar != misil)) {
			remove(auxiliar);
			if (direccion == 'y') {
				velocidadY = -velocidadY;
			} else {
				velocidadX = -velocidadX;
			}
			//puntuacion++;// aumentamos la puntuacion en uno
			choque = true;
			puntos+=10;
			puntuacion.setLabel("PUNTUACION: "+puntos);
		}


		// devolvemos el valor de choque
		return (choque);
	}






}
