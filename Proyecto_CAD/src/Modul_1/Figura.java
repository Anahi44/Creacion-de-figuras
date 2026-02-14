package Modul_1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

public abstract class Figura {
    protected int id,x, y;
    protected int ancho, alto;
    protected Color color;
    protected List<Figura> figurasOriginales;
    protected int numeroFigura;
    protected static int contadorFiguras = 0;
    protected boolean esParteDeCompuesta = false; // Nueva variable
    public abstract String toText();
    
    public Figura(int x, int y, int ancho, int alto ,Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.numeroFigura = ++contadorFiguras;
    }

    public abstract void dibujar(Graphics g);

    public abstract boolean contienePunto(int px, int py);

    public void mover(int dx, int dy) {
        x += dx;
        y += dy;
    }
    public void cambiarTamaño(int nuevoAncho, int nuevoAlto) {
        this.ancho = nuevoAncho;
        this.alto = nuevoAlto;
    }

    public void cambiarColor(Color nuevoColor) {
        this.color = nuevoColor;
    }
    public boolean seSuperpone(Figura otra) {
        return this.x < otra.x + otra.ancho &&
               this.x + this.ancho > otra.x &&
               this.y < otra.y + otra.alto &&
               this.y + this.alto > otra.y;
    }
    public void fusionar(Figura otra) {
        int nuevoX = Math.min(this.x, otra.x);
        int nuevoY = Math.min(this.y, otra.y);
        int nuevoAncho = Math.max(this.x + this.ancho, otra.x + otra.ancho) - nuevoX;
        int nuevoAlto = Math.max(this.y + this.alto, otra.y + otra.alto) - nuevoY;
        
        this.x = nuevoX;
        this.y = nuevoY;
        this.ancho = nuevoAncho;
        this.alto = nuevoAlto;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public Color getColor() {
        return color;
    }
    public List<Figura> getFigurasOriginales() {
        return figurasOriginales;
    }
    public abstract Figura clonar();
    public abstract String getNombre();
    

    public void setParteDeCompuesta(boolean esParteDeCompuesta) {
        this.esParteDeCompuesta = esParteDeCompuesta;
    }

    public boolean esParteDeCompuesta() {
        return esParteDeCompuesta;
    }
}   