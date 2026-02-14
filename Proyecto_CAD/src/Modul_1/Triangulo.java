package Modul_1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Triangulo extends Figura {

	public Triangulo(int x, int y, int base, int altura, Color color) {
        super(x, y, base, altura, color);
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(color);
        int[] xPoints = {x, x + ancho / 2, x + ancho};
        int[] yPoints = {y + alto, y, y + alto};
        g.fillPolygon(xPoints, yPoints, 3);
        if (!esParteDeCompuesta) {
            g.drawString(getNombre() + " " + numeroFigura, x, y - 5);
        }
    }

    @Override
    public boolean contienePunto(int px, int py) {
        Polygon poligono = new Polygon(
            new int[]{x, x + ancho / 2, x + ancho},
            new int[]{y + alto, y, y + alto},
            3
        );
        return poligono.contains(px, py);
    }

    @Override
    public Figura clonar() {
        return new Triangulo(x, y, ancho, alto, color);
    }

    @Override
    public String getNombre() {
        return "Triángulo";
    }
    @Override
    public String toText() {
        return "Triangulo " + x + " " + y + " " + ancho + " " + alto + " " + color.getRGB();
    }
    public int getX() {return x;}
    public int getY(){return y;}
    public int getAncho(){return ancho;}
    public int getAlto(){return alto;}
    public Color getColor(){return color;}
    
}



