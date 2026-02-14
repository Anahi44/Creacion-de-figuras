package Modul_1;

import java.awt.Color;

import javax.speech.recognition.FinalResult;
import javax.speech.recognition.FinalRuleResult;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;

public class Programas extends ResultAdapter {
	
	private PanelDibujo panelDibujo;
	private static Recognizer oreja;
	

    public Programas (PanelDibujo panelDibujo) {
        this.panelDibujo = panelDibujo;
    }

    @Override
    public void resultAccepted(ResultEvent e) {
        FinalRuleResult result = (FinalRuleResult) (e.getSource());
        ResultToken[] tokens = result.getBestTokens();

        StringBuilder comandoBuilder = new StringBuilder();
        for (ResultToken token : tokens) {
            comandoBuilder.append(token.getSpokenText()).append(" ");
        }

        String comando = comandoBuilder.toString().trim().toLowerCase();
        System.out.println("Comando recibido: " + comando);

        switch (comando) {
            case "triangulo":
                panelDibujo.addFigura(new Triangulo(200, 100, 50, 50, Color.ORANGE));
                panelDibujo.registrarFiguraAudio();
                break;
            case "circulo":
                panelDibujo.addFigura(new Circulo(300, 100, 50, 50, Color.BLUE));
                panelDibujo.registrarFiguraAudio();
                break;
            case "rectangulo":
            	panelDibujo.addFigura(new Rectangulo(100, 100, 50, 70, Color.BLACK));
            	panelDibujo.registrarFiguraAudio();
                break;
            case "deshacer":
                panelDibujo.deshacerUltimaModificacion();
                break;
            case "rehacer":
                panelDibujo.rehacerUltimaModificacion();
                break;
            case "salir":
            	detenerRec();
            	System.out.println("Salir");
            	break;
            case "borrar":
            	panelDibujo.borrarFiguras();
            	panelDibujo.eliminacion();
            default:
                System.out.println("Comando no reconocido");
                break;
        }
    }
    private void detenerRec() {
        if (oreja != null ) {
            try {
                oreja.deallocate();
                
                System.out.println("Reconocimiento de voz detenido.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


