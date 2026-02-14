package Modul_1;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangulo extends Figura {
	
	public Rectangulo(int x, int y, int ancho, int alto, Color color) {
        super(x, y, ancho, alto, color);
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, ancho, alto);
        if (!esParteDeCompuesta) {
            g.drawString(getNombre() + " " + numeroFigura, x, y - 5);
        }
    }

    @Override
    public boolean contienePunto(int px, int py) {
        return px >= x && px <= x + ancho && py >= y && py <= y + alto;
    }

    @Override
    public Figura clonar() {
        return new Rectangulo(x, y, ancho, alto, color);
    }

    @Override
    public String getNombre() {
        return "Rectángulo";
    }
    @Override
    public String toText() {
        return "Rectangulo " + x + " " + y + " " + alto + " " + ancho + " " + color.getRGB();
    }
}

