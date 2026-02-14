package Modul_1;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Programa {

    JFrame frame;
    private PanelDibujo panelDibujo;
    private Hoja hoja;
    private static final String FILE_PATH = "sesiones.txt";
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Programa window = new Programa();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Programa() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		String mensaje = "Sesion cancelada\n";
                registrarSesion(mensaje);
                System.out.println("Sesión cancelada.");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	}
        });
        frame.setBounds(100, 100, 972, 634);
   
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(117, 91, 67));
        panel.setBounds(0, 0, 975, 627);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JTextArea txtrCodFig = new JTextArea();
        txtrCodFig.setBackground(new Color(117, 91, 67));
        txtrCodFig.setForeground(new Color(243, 231, 209));
        txtrCodFig.setFont(new Font("Goudy Stout", Font.PLAIN, 52));
        txtrCodFig.setBounds(306, 31, 391, 76);
        txtrCodFig.setText("COD FIG");
        panel.add(txtrCodFig);

        JLabel lblLabel = new JLabel("");
        lblLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Intentar cargar la imagen
        try {
            lblLabel.setIcon(new ImageIcon(Programa.class.getResource("/Imagenes/oso2.png")));
        } catch (Exception e) {
            System.out.println("Imagen no encontrada: " + e.getMessage());
        }
        lblLabel.setBounds(254, 117, 492, 320);
        panel.add(lblLabel);

        JButton EmpButton = new JButton("EMPEZAR");
        EmpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                try {
                    new Hoja();
                    String mensaje = "Sesion abierta\n";
                    registrarSesion(mensaje);
                    System.out.println("Sesión registrada.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        EmpButton.setBackground(new Color(231, 215, 163));
        EmpButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 26));
        EmpButton.setBounds(424, 472, 158, 50);
        panel.add(EmpButton);
        
        
    }

    public static void registrarSesion(String Mensaje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(Mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}