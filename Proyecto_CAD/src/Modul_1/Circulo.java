package Modul_1;

import java.awt.Color;
import java.awt.Graphics;

public class Circulo extends Figura {
	public Circulo( int x, int y, int ancho, int alto, Color color) {
        super(x, y, ancho, alto, color);
    }

	 @Override
	    public void dibujar(Graphics g) {
	        g.setColor(color);
	        g.fillOval(x, y, ancho, alto);
	        if (!esParteDeCompuesta) {
	            g.drawString(getNombre() + " " + numeroFigura, x, y - 5);
	        }
	    }

	    @Override
	    public boolean contienePunto(int px, int py) {
	        int radio = ancho / 2;
	        int centroX = x + radio;
	        int centroY = y + radio;
	        return Math.pow(px - centroX, 2) + Math.pow(py - centroY, 2) <= Math.pow(radio, 2);
	    }

	    @Override
	    public Figura clonar() {
	        return new Circulo(x, y, ancho, alto, Color.ORANGE);
	    }

	    @Override
	    public String getNombre() {
	        return "Círculo";
	    }
	    @Override
	    public String toText() {
	    	String rec = "Circulo " + x + " " + y + " " + alto + " " + ancho + " " + color.getRGB();
	    	System.out.println (rec);
	    	return rec;
	    }
    public int getX() {return x;}
    public int getY() {return y;}
    public int getAncho() {return ancho;}
    public int getAlto() {return alto;}
    public Color getColor(){return color;}

}
