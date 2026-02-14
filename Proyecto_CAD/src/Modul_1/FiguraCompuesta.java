package Modul_1;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FiguraCompuesta extends Figura {
	
    private List<Figura> componentes;

    public FiguraCompuesta(int x, int y, int ancho, int alto, Color color) {
        super(x, y, ancho, alto, color);
        componentes = new ArrayList<>();
        toText();
      
    }

    public void addComponente(Figura figura) {
        figura.setParteDeCompuesta(true); // Marcar la figura como parte de una figura compuesta
        componentes.add(figura);
        // Ajustar las dimensiones de la figura compuesta
        int nuevoX = Math.min(this.x, figura.getX());
        int nuevoY = Math.min(this.y, figura.getY());
        int nuevoAncho = Math.max(this.x + this.ancho, figura.getX() + figura.getAncho()) - nuevoX;
        int nuevoAlto = Math.max(this.y + this.alto, figura.getY() + figura.getAlto()) - nuevoY;

        this.x = nuevoX;
        this.y = nuevoY;
        this.ancho = nuevoAncho;
        this.alto = nuevoAlto;
    }

    @Override
    public void dibujar(Graphics g) {
        for (Figura figura : componentes) {
            figura.dibujar(g);
        }
        g.drawString(getNombre() + " " + numeroFigura, x, y - 5);
    }

    @Override
    public boolean contienePunto(int px, int py) {
        for (Figura figura : componentes) {
            if (figura.contienePunto(px, py)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void mover(int dx, int dy) {
        super.mover(dx, dy);
        for (Figura figura : componentes) {
            figura.mover(dx, dy);
        }
    }

    public List<Figura> getComponentes() {
        return componentes;
    }

    @Override
    public void cambiarColor(Color nuevoColor) {
        super.cambiarColor(nuevoColor);
        for (Figura figura : componentes) {
            figura.cambiarColor(nuevoColor);
        }
    }

    @Override
    public Figura clonar() {
        FiguraCompuesta clon = new FiguraCompuesta(this.x, this.y, this.ancho, this.alto, this.color);
        for (Figura componente : componentes) {
            clon.addComponente(componente.clonar());
        }
        return clon;
    }

    @Override
    public String getNombre() {
        return "FiguraCompuesta";
    }
    @Override
    public String toText() {
        StringBuilder sb = new StringBuilder();
        sb.append("FiguraCompuesta ");
        for (Figura figura : componentes) {
            sb.append(figura.toText()).append(";");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
    public void obtenerFiguraCompuesta(FiguraCompuesta figuraCompuesta, String figuraStr) {
        String[] parts = figuraStr.split(" ");
        String figuraType = parts[0];
        switch (figuraType) {
            case "Rectangulo":
                int xRect = Integer.parseInt(parts[1]);
                int yRect = Integer.parseInt(parts[2]);
                int altoRect = Integer.parseInt(parts[3]);
                int anchoRect = Integer.parseInt(parts[4]);
                Color colorRect = new Color(Integer.parseInt(parts[5]));
                figuraCompuesta.addComponente(new Rectangulo(xRect, yRect, altoRect, anchoRect, colorRect));
                break;
            case "Triangulo":
                int xTri = Integer.parseInt(parts[1]);
                int yTri = Integer.parseInt(parts[2]);
                int baseTri = Integer.parseInt(parts[3]);
                int anchoTri = Integer.parseInt(parts[4]);
                Color colorTri = new Color(Integer.parseInt(parts[5]));
                figuraCompuesta.addComponente(new Triangulo(xTri, yTri, baseTri, anchoTri, colorTri));
                break;
            case "Circulo":
                int xCirc = Integer.parseInt(parts[1]);
                int yCirc = Integer.parseInt(parts[2]);
                int alto = Integer.parseInt(parts[3]);
                int ancho = Integer.parseInt(parts[4]);
                Color colorCirc = new Color(Integer.parseInt(parts[5]));
                figuraCompuesta.addComponente(new Circulo(xCirc, yCirc, alto, ancho, colorCirc));
                break;
            default:
                System.out.println("Unknown figure type: " + figuraType);
                break;
        }
    }
}

