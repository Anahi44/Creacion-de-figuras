package Modul_1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class PanelDibujo extends JPanel {

	private List<Figura> figuras;
    private Figura figuraSeleccionada;
    private int prevX, prevY;
    private Stack<Figura> figurasEliminadas;
    private FiguraCompuesta fc;
    private static final String FILE_PATH = "sesiones.txt";
    private int rec;
    private int tri;
    private int cir;
    private int recE;
    private int triE;
    private int cirE;
    private int recC;
    private int triC;
    private int cirC;
    private int comVoz;
    private int compuesta;
    private int totalFig;
    private int totalEliminada;
    
    public PanelDibujo() {
    	rec = 0;
    	tri = 0;
    	cir = 0;
    	recE= 0;
    	triE= 0;
    	cirE= 0;
    	recC= 0;
    	triC= 0;
    	cirC= 0;
    	comVoz = 0;
    	totalFig = 0;
    	compuesta = 0;
    	totalEliminada = 0;
        figuras = new ArrayList<>();
        figurasEliminadas = new Stack<>();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Figura figura : figuras) {
                    if (figura.contienePunto(e.getX(), e.getY())) {
                        figuraSeleccionada = figura;
                        prevX = e.getX();
                        prevY = e.getY();
                        if (SwingUtilities.isRightMouseButton(e)) {
                            mostrarDialogoTamano();
                        } else if (SwingUtilities.isMiddleMouseButton(e)) {
                            mostrarDialogoColor();
                        }
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (figuraSeleccionada != null) {
                    FiguraCompuesta figuraCompuesta = null;
                    for (Figura figura : figuras) {
                        if (figura != figuraSeleccionada && figura.seSuperpone(figuraSeleccionada)) {
                               compuesta(figura, figuraSeleccionada);
                            if (figura instanceof FiguraCompuesta) {
                                figuraCompuesta = (FiguraCompuesta) figura;                         
                            } else {
                                figuraCompuesta = new FiguraCompuesta(figuraSeleccionada.x, figuraSeleccionada.y, figuraSeleccionada.ancho, figuraSeleccionada.alto, figuraSeleccionada.color);
                                figuraCompuesta.addComponente(figura);
                                figuras.remove(figura);
                            }
                            figuraCompuesta.addComponente(figuraSeleccionada);                       
                            figuras.remove(figuraSeleccionada);
                            break;
                        }
                    }
                    if (figuraCompuesta != null) {
                        figuras.add(figuraCompuesta);
                    }
                }
                figuraSeleccionada = null;
                repaint();
            }


            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    for (Figura figura : figuras) {
                        if (figura.contienePunto(e.getX(), e.getY()) && figura instanceof FiguraCompuesta) {
                            separarFiguraCompuesta((FiguraCompuesta) figura);
                            break;
                        }
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (figuraSeleccionada != null && SwingUtilities.isLeftMouseButton(e)) {
                    int dx = e.getX() - prevX;
                    int dy = e.getY() - prevY;
                    figuraSeleccionada.mover(dx, dy);
                    prevX = e.getX();
                    prevY = e.getY();
                    repaint();
                }
            }
        });
    }

    private void separarFiguraCompuesta(FiguraCompuesta figuraCompuesta) {
        List<Figura> componentes = figuraCompuesta.getComponentes();
        for (Figura componente : componentes) {
            componente.setParteDeCompuesta(false); // Marcar la figura como no parte de una figura compuesta
            figuras.add(componente);
        }
        figuras.remove(figuraCompuesta);
        repaint();
    }

    public void addFigura(Figura figura) {
        figuras.add(figura);
        repaint();
        if (figura instanceof Rectangulo) {
            rec ++;
            totalFig ++;
        } else if (figura instanceof Triangulo) {
        	tri ++;
        	totalFig ++;
        } else if (figura instanceof Circulo) {
        	cir ++;
        	totalFig ++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Figura figura : figuras) {
            figura.dibujar(g);
        }
    }

    private void mostrarDialogoColor() {
        Color nuevoColor = JColorChooser.showDialog(this, "Elija un color", figuraSeleccionada.color);
        if (nuevoColor != null) {
            figuraSeleccionada.cambiarColor(nuevoColor);
            repaint();
        }
    }

    private void mostrarDialogoTamano() {
        String nuevoAncho = JOptionPane.showInputDialog(this, "Nuevo Ancho", figuraSeleccionada.ancho);
        String nuevoAlto = JOptionPane.showInputDialog(this, "Nuevo Alto", figuraSeleccionada.alto);
        if (nuevoAncho != null && nuevoAlto != null) {
            try {
                int ancho = Integer.parseInt(nuevoAncho);
                int alto = Integer.parseInt(nuevoAlto);
                figuraSeleccionada.cambiarTamaño(ancho, alto);
                repaint();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, introduzca valores válidos.");
            }
        }
    }

    public Figura getFiguraSeleccionada() {
        return figuraSeleccionada;
    }

    public void borrarFiguras() {
        Thread borrarThread = new Thread(() -> {
        	eliminacion();
            figuras.clear();
            repaint();
        });
        borrarThread.start();
    }

    public void deshacerUltimaModificacion() {
        if (!figuras.isEmpty()) {
            Figura figuraEliminada = figuras.remove(figuras.size() - 1);
            figurasEliminadas.push(figuraEliminada); // Agregar la figura eliminada a la pila
            repaint();
            if (figuraEliminada instanceof Rectangulo) {
                recE ++;
                totalEliminada ++;
            } else if (figuraEliminada instanceof Triangulo) {
        	    triE ++;
        	    totalEliminada ++;
            } else if (figuraEliminada instanceof Circulo) {
        	    cirE ++;
        	    totalEliminada ++;
            }
        }
    }

    public void rehacerUltimaModificacion() {
        if (!figurasEliminadas.isEmpty()) {
            Figura figuraRecuperada = figurasEliminadas.pop();
            figuras.add(figuraRecuperada);
            repaint();
        }
    }
    
    public List<Figura> getFiguras() {
        return figuras;
    }
    
    public void compuesta(Figura figura, Figura Seleccionada) {
    	 compuesta ++;
    	 if (figura instanceof Rectangulo) {
             recC ++;
         } else if (figura instanceof Triangulo) {
         	 triC ++;
         } else if (figura instanceof Circulo) {
         	 cirC ++;
         }
    	 if (Seleccionada instanceof Rectangulo) {
             recC ++;
         } else if (Seleccionada instanceof Triangulo) {
         	triC ++;
         } else if (Seleccionada instanceof Circulo) {
         	cirC ++;
         }
    	 System.out.println(recC+ "a" + triC +"s"+ + cirC +"d" + compuesta);
    }
    public void eliminacion() {
    	for(Figura fig: figuras) {
    		if (fig instanceof Rectangulo) {
                recE ++;
                totalEliminada ++;
            } else if (fig instanceof Triangulo) {
            	 triE ++;
            	 totalEliminada ++;
            } else if (fig instanceof Circulo) {
            	 cirE ++;
            	 totalEliminada ++;
            }
    	}
    }
    public void registrarFiguraAudio() {
    	comVoz++;
    	totalFig ++;
    }
    public int totalFiguras() {
    	int res = rec + tri +cir;
    	int figuras = res - comVoz;
    	return figuras;
    }
    public  int lectura() {
        String fraseBuscada = "sesion abierta";
        int contador = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().equalsIgnoreCase(fraseBuscada)) {
                    contador++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contador;
    }
    public void obtenerFiguras(String line) {
        String[] parts = line.split(" ");
        String figuraType = parts[0];
        switch (figuraType) {
            case "Rectangulo":
                int xRect = Integer.parseInt(parts[1]);
                int yRect = Integer.parseInt(parts[2]);
                int widthRect = Integer.parseInt(parts[3]);
                int heightRect = Integer.parseInt(parts[4]);
                Color colorRect = new Color(Integer.parseInt(parts[5]));
                addFigura(new Rectangulo(xRect, yRect, widthRect, heightRect, colorRect));
                break;
            case "Triangulo":
                int xTri = Integer.parseInt(parts[1]);
                int yTri = Integer.parseInt(parts[2]);
                int baseTri = Integer.parseInt(parts[3]);
                int heightTri = Integer.parseInt(parts[4]);
                Color colorTri = new Color(Integer.parseInt(parts[5]));
                addFigura(new Triangulo(xTri, yTri, baseTri, heightTri, colorTri));
                break;
            case "Circulo":
                int xCirc = Integer.parseInt(parts[1]);
                int yCirc = Integer.parseInt(parts[2]);
                int alto = Integer.parseInt(parts[3]);
                int ancho = Integer.parseInt(parts[4]);
                Color colorCirc = new Color(Integer.parseInt(parts[5]));
                addFigura(new Circulo(xCirc, yCirc, alto, ancho, colorCirc));
                break;
            case "FiguraCompuesta":
                String[] figurasCompuestas = parts[1].split(";");
                for (String figuraStr : figurasCompuestas) {
                    fc.obtenerFiguraCompuesta(fc, figuraStr);
                }
               addFigura(fc);
                break;
            default:
                System.out.println("Unknown figure type: " + figuraType);
                break;
        } 
    }
    public int getRec() {
        return rec;
     }
    public int getTri() {
        return tri;
    }
    public int getCir() {
        return cir;
    }
    public int getRecC() {
    	return recC;
    }
    public int getTriC() {
    	return triC;
    }
    public int getCirC() {
    	return cirC;
    }
    public int getRecE() {
    	return recE;
    }
    public int getTriE() {
    	return triE;
    }
    public int getCirE() {
    	return cirE;
    }
    public int getComando() {
    	return comVoz;
    }
    public int getFigCompuesta() {
    	return compuesta;
    }
    public String getFigTotal() {
    	String res = " " +totalFig;
    	return res;
    }
    public int getFigElimi() {
    	return totalEliminada;
    }
}