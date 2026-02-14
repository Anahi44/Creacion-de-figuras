package Modul_1;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.sound.sampled.TargetDataLine;
import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineModeDesc;
import javax.speech.EngineStateError;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;
import javax.speech.recognition.RuleGrammar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDesktopPane;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.DropMode;


public class Hoja {

	private JFrame frame;
	private JTable table;
	private PanelDibujo panelDibujo;
    private JLabel cameraFeedLabel;
    private volatile boolean cameraRunning = false;
    private boolean escucha = false;
    private static Recognizer oreja;
    private Movimiento_Camara mv ;
    private LocalDateTime tiempo; // verificar tiempo de sesion
    private int sumaFig;
    private int sumElim;
    private int com;
    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hoja window = new Hoja();
					window.frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Hoja() {
		initialize();
		sumaFig = 0;
		sumElim = 0;
		com = 0;
		mv = new Movimiento_Camara(panelDibujo);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1264, 745);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//NO SE VA CERRAR LA VENTANA 
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//cerrar();
			}
		});frame.getContentPane().setLayout(null);
		
		panelDibujo = new PanelDibujo();
	    panelDibujo.setBackground(new Color(229, 245, 244));
	    panelDibujo.setBounds(0, 98, 802, 600);
	    frame.getContentPane().add(panelDibujo);
	    panelDibujo.setLayout(null);
	    panelDibujo.setVisible(true);
	    JPanel Panel1 = new JPanel();
	    Panel1.setBackground(new Color(217, 221, 242));
		Panel1.setBounds(49, 33, 701, 296);
		panelDibujo.add(Panel1);
		Panel1.setLayout(null);
		Panel1.setVisible(false);
		JPanel panelFiguras = new JPanel();
		panelFiguras.setBackground(new Color(247, 244, 219));
		panelFiguras.setBounds(56, 63, 574, 164);
		Panel1.add(panelFiguras);
		panelFiguras.setLayout(null);
		
		JLabel lblFigura = new JLabel("FIGURA                 AÑADIDAS              ELIMINADAS            COMPUESTAS");
		lblFigura.setFont(new Font("SansSerif", Font.ITALIC, 15));
		lblFigura.setBounds(38, 26, 529, 27);
		panelFiguras.add(lblFigura);
		
		JLabel lblARec = new JLabel("");
		lblARec.setFont(new Font("Segoe Print", Font.PLAIN, 14));
		lblARec.setBounds(187, 64, 380, 13);
		panelFiguras.add(lblARec);
		
		JLabel lblATri = new JLabel("");
		lblATri.setFont(new Font("Segoe Print", Font.PLAIN, 14));
		lblATri.setBounds(187, 99, 380, 13);
		panelFiguras.add(lblATri);
		
		JLabel lblACir = new JLabel("");
		lblACir.setFont(new Font("Segoe Print", Font.PLAIN, 14));
		lblACir.setBounds(187, 122, 377, 13);
		panelFiguras.add(lblACir);
		
		JTextPane txtpnRectangulotriangulo = new JTextPane();
		txtpnRectangulotriangulo.setBackground(new Color(247, 244, 219));
		txtpnRectangulotriangulo.setFont(new Font("Segoe Print", Font.PLAIN, 16));
		txtpnRectangulotriangulo.setBounds(26, 51, 94, 91);
		panelFiguras.add(txtpnRectangulotriangulo);
		txtpnRectangulotriangulo.setText("Rectangulo\r\nTriangulo\r\nCirculo");
		panelFiguras.setVisible(false);
		
		
		JLabel dibujoLabel = new JLabel("");
		dibujoLabel.setVerticalAlignment(SwingConstants.TOP);
		dibujoLabel.setBackground(Color.WHITE);
		dibujoLabel.setIcon(new ImageIcon(Hoja.class.getResource("/Imagenes/est.png")));
		dibujoLabel.setBounds(282, 63, 148, 145);
		Panel1.add(dibujoLabel);
		
		JLabel lblTitulo = new JLabel("Estadistica ");
		lblTitulo.setFont(new Font("Segoe Script", Font.ITALIC, 27));
		lblTitulo.setBounds(261, 10, 179, 42);
		Panel1.add(lblTitulo);
		
		JButton btnFiguras = new JButton("Figuras");
		btnFiguras.setVerticalAlignment(SwingConstants.TOP);
		btnFiguras.setHorizontalAlignment(SwingConstants.LEFT);
		btnFiguras.setFont(new Font("Yu Gothic", Font.ITALIC, 19));
		btnFiguras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblARec.setText("" + panelDibujo.getRec()+"                      "+panelDibujo.getRecE()+"                       "+panelDibujo.getRecC());
				lblATri.setText("" + panelDibujo.getTri()+"                      "+panelDibujo.getTriE()+"                       "+panelDibujo.getTriC());
				lblACir.setText("" + panelDibujo.getCir()+"                      "+panelDibujo.getCirE()+"                       "+panelDibujo.getCirC());
				panelFiguras.setVisible(true);
			}});
		btnFiguras.setBounds(90, 247, 101, 31);
		Panel1.add(btnFiguras);
		
		JPanel panelSumas = new JPanel();
		panelSumas.setBackground(new Color(223, 242, 217 ));
		panelSumas.setBounds(56, 63, 601, 164);
		Panel1.add(panelSumas);
		panelSumas.setLayout(null);
		JLabel lblInsercion = new JLabel("FORMAS DE AÑADIDO DE LAS FIGURAS");
		lblInsercion.setFont(new Font("SansSerif", Font.ITALIC, 15));
		lblInsercion.setBounds(130, 26, 310, 27);
		panelSumas.add(lblInsercion);
		JLabel lblFigBoton = new JLabel("");
		lblFigBoton.setFont(new Font("Segoe Print", Font.PLAIN, 14));
		lblFigBoton.setBounds(10, 64, 581, 32);
		panelSumas.add(lblFigBoton);
		JLabel lblFigVoz = new JLabel("");
		lblFigVoz.setFont(new Font("Segoe Print", Font.PLAIN, 14));
		lblFigVoz.setBounds(10, 100, 581, 38);
		panelSumas.add(lblFigVoz);
		panelSumas.setVisible(false);
		
		JButton btnSumas = new JButton("Forma Añadido");
		btnSumas.setVerticalAlignment(SwingConstants.TOP);
		btnSumas.setFont(new Font("Yu Gothic", Font.ITALIC, 19));
		btnSumas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelFiguras.setVisible(false);
				dibujoLabel.setVisible(false);
				int res = panelDibujo.totalFiguras();
				lblFigBoton.setText("El numero de figuras añadidas al panel por\r\n medio de los botones es de "+ res);
				lblFigVoz.setText("El numero de figuras añadidas al panel por\r\n medio del comando de voz es de "+ panelDibujo.getComando());
				panelSumas.setVisible(true);
		    }
		});
		btnSumas.setBounds(260, 250, 170, 28);
		Panel1.add(btnSumas);
		
		JPanel panelRespuestas = new JPanel();
		panelRespuestas.setBackground(new Color(242, 217, 217 ));
		panelRespuestas.setBounds(56, 55, 601, 182);
		Panel1.add(panelRespuestas);
		panelRespuestas.setLayout(null);
		JLabel lbllabel = new JLabel("TABLA DE RESULTADOS");
		lbllabel.setFont(new Font("SansSerif", Font.PLAIN, 17));
		lbllabel.setBounds(181, 10, 195, 13);
		panelRespuestas.add(lbllabel);
		
		JTextPane txtpnList = new JTextPane();
		txtpnList.setBackground(new Color(242, 217, 217 ));
		txtpnList.setText("LISTA\r\n  Total de figuras añadidas\r\n  Total de figuras eleminadas\r\n  Total de figuras compuestas \r\n  Numero seciones abiertas");
		txtpnList.setFont(new Font("Segoe Print", Font.PLAIN, 14));
		txtpnList.setBounds(22, 33, 332, 139);
		panelRespuestas.add(txtpnList);
		
		JTextPane txtpnRes = new JTextPane();
		txtpnRes.setBackground(new Color(242, 217, 217 ));
		txtpnRes.setFont(new Font("Segoe Print", Font.PLAIN, 15));
		txtpnRes.setBounds(332, 33, 222, 139);
		panelRespuestas.add(txtpnRes);
		panelRespuestas.setVisible(false);
		
		JButton btnRes= new JButton("Resultado");
		btnRes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelFiguras.setVisible(false);
				dibujoLabel.setVisible(false);
				panelSumas.setVisible(false);
				txtpnRes.setText("Resultados\r\n    " + panelDibujo.getFigTotal()+ "\r\n     " +panelDibujo.getFigElimi()+" \r\n     "+ panelDibujo.getFigCompuesta() + " \r\n     "+ panelDibujo.lectura());
				panelRespuestas.setVisible(true);
			}
		});
		btnRes.setVerticalAlignment(SwingConstants.TOP);
		btnRes.setHorizontalAlignment(SwingConstants.LEFT);
		btnRes.setFont(new Font("Yu Gothic", Font.ITALIC, 19));
		btnRes.setBounds(499, 250, 131, 28);
		Panel1.add(btnRes);
		
		
		JButton btnSalir = new JButton("");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelFiguras.setVisible(false);
				panelSumas.setVisible(false);
				panelRespuestas.setVisible(false);
				Panel1.setVisible(false);
			}
		});
		btnSalir.setIcon(new ImageIcon(Hoja.class.getResource("/Imagenes/salida.png")));
		btnSalir.setBounds(650, 21, 25, 20);
		Panel1.add(btnSalir);
	    
	    JPanel panelMenu = new JPanel();
		panelMenu.setBackground(new Color(234, 228, 214));
		panelMenu.setBounds(0, 0, 802, 102);
		frame.getContentPane().add(panelMenu);
		panelMenu.setVisible(true);
		panelMenu.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setBounds(10, 10, 149, 82);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(Programa.class.getResource("/Imagenes/oso.png")));
		panelMenu.add(lblNewLabel);
		
		JButton btnAbrir = new JButton("ABRIR");
		btnAbrir.setBounds(177, 50, 90, 36);
		btnAbrir.setFont(new Font("MS Gothic", Font.PLAIN, 15));
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		panelMenu.add(btnAbrir);
		
		JPanel panelFig = new JPanel();
		panelFig.setBackground(new Color(227, 213, 181));
		frame.getContentPane().add(panelFig);
		panelFig.setBounds(10, 0, 1250, 698);
		 panelFig.setLayout(null);
		 
		 JTextArea txtrFiguras = new JTextArea();
		 txtrFiguras.setFont(new Font("MV Boli", Font.BOLD | Font.ITALIC, 20));
		 txtrFiguras.setBackground(new Color(227, 213, 181));
		 txtrFiguras.setText("FIGURAS");
		 txtrFiguras.setBounds(972, 10, 111, 31);
		 panelFig.add(txtrFiguras);
		 
		 JButton btnRectangulo = new JButton("");
		 btnRectangulo.setBackground(new Color(227, 213, 181));
		 btnRectangulo.setBackground(SystemColor.info);
		 btnRectangulo.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		panelDibujo.addFigura(new Rectangulo(100, 100,50,70,Color.BLACK));
		 	}
		 });
		 btnRectangulo.setIcon(new ImageIcon(Hoja.class.getResource("/Imagenes/rect2.png")));
		 btnRectangulo.setBounds(861, 53, 90, 81);
		 panelFig.add(btnRectangulo);
		 
		 JButton btnTriangulo = new JButton("");
		 btnTriangulo.setBackground(new Color(227, 213, 181));
		 btnTriangulo.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		panelDibujo.addFigura(new Triangulo(200,100,50,50,Color.ORANGE));
		 	}
		 });
		 btnTriangulo.setIcon(new ImageIcon(Hoja.class.getResource("/Imagenes/Triangulo.png")));
		 btnTriangulo.setBounds(1084, 51, 102, 74);
		 panelFig.add(btnTriangulo);
		 
		 JButton btnCirculo = new JButton("");
		 btnCirculo.setBackground(new Color(227, 213, 181));
		 btnCirculo.addActionListener(new ActionListener() {
			 	public void actionPerformed(ActionEvent e) {
			 		panelDibujo.addFigura(new Circulo(300,100, 50, 50, Color.BLUE));
			 	}
			 });
		 btnCirculo.setIcon(new ImageIcon(Hoja.class.getResource("/Imagenes/circ1.png")));
		 btnCirculo.setBounds(972, 51, 90, 81);
		 panelFig.add(btnCirculo);
		 
		 JTextArea txtInf = new JTextArea();
	     txtInf.setBounds(1020, 179, 115, 31);
	     txtInf.setBackground(new Color(227, 213, 181));
	     txtInf.setFont(new Font("MV Boli", Font.ITALIC, 12));
	     panelFig.add(txtInf);
	     
		 JButton btnMic = new JButton("");
		 btnMic.setBounds(1030, 220, 53, 52);
		 panelFig.add(btnMic);
		 btnMic.setIcon(new ImageIcon(Hoja.class.getResource("/Imagenes/mic1.png")));
		 btnMic.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                txtInf.setText("Microfono encendido");
	                iniciarRec();
	            }
	        });
		 JButton btnStopMic = new JButton("Detener Grabacion");
	        btnStopMic.setBounds(1092, 220, 135, 41); 
	        btnStopMic.setFont(new Font("OCR A Extended", Font.ITALIC, 10));
	        panelFig.add(btnStopMic);
	        btnStopMic.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                detenerRec();
	                txtInf.setText("Microfono apagado");
	            }
	        });
		 
	     JTextArea txtrInteracciones = new JTextArea();
		 txtrInteracciones.setFont(new Font("MV Boli", Font.BOLD | Font.ITALIC, 20));
		 txtrInteracciones.setText("INTERACCIONES");
		 txtrInteracciones.setBounds(938, 144, 187, 31);
		 panelFig.add(txtrInteracciones);
		 
		 JButton btnCam= new JButton("");
	        btnCam.setBounds(817, 214, 61, 52);
	        panelFig.add(btnCam);
	        btnCam.setIcon(new ImageIcon(Hoja.class.getResource("/Imagenes/cam.png")));
	        btnCam.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                if (!cameraRunning) {
	                    cameraRunning = true;
	                    new Thread(() -> openCamera()).start();
	                    
	                }
	            }
	        });
		 
		 JButton btnBorrar = new JButton("BORRAR");
		 btnBorrar.setFont(new Font("MS Gothic", Font.PLAIN, 15));
	        btnBorrar.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                panelDibujo.borrarFiguras();
	            }
	        });
	        btnBorrar.setBounds(423, 50, 100, 30);
	        panelMenu.add(btnBorrar);
	        
	        JButton btnDeshacer = new JButton("DESHACER");
	        btnDeshacer.setFont(new Font("MS Gothic", Font.PLAIN, 15));
	        btnDeshacer.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                panelDibujo.deshacerUltimaModificacion();
	            }
	        });
	        btnDeshacer.setBounds(533, 50, 100, 30);
	        panelMenu.add(btnDeshacer);
	        
	        JButton btnRehacer = new JButton("REHACER");
	        btnRehacer.setFont(new Font("MS Gothic", Font.PLAIN, 15));
	        btnRehacer.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                panelDibujo.rehacerUltimaModificacion();
	            }
	        });
	        btnRehacer.setBounds(643, 50, 100, 30);
	        panelMenu.add(btnRehacer);
	        
	        JButton btnSave = new JButton("GUARDAR");
	        btnSave.setBounds(294, 50, 100, 33);
	        btnSave.setFont(new Font("MS Gothic", Font.PLAIN, 15));
	        btnSave.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                saveFile();
	            }
	        });
	        panelMenu.add(btnSave);
	        
	        JButton btnFiguraFrecuente = new JButton("ESTADISTICA");
	        btnFiguraFrecuente.setFont(new Font("MS Gothic", Font.PLAIN, 15));
	        btnFiguraFrecuente.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	dibujoLabel.setVisible(true);
	            	Panel1.setVisible(true);
	            }
	        });
	            
	        btnFiguraFrecuente.setBounds(169, 5 , 132, 35);
	        panelMenu.add(btnFiguraFrecuente);
	        
	        cameraFeedLabel = new JLabel();
	        cameraFeedLabel.setBounds(802, 289, 425, 399);
	        panelFig.add(cameraFeedLabel);
	        JButton btnStopCamera = new JButton("DETENER CÁMARA");
	        btnStopCamera.setFont(new Font("OCR A Extended", Font.ITALIC, 10));
	        btnStopCamera.setBounds(883, 214, 129, 41);
	        panelFig.add(btnStopCamera);
	        btnStopCamera.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                cameraRunning = false;}});
	}
	
	private void saveFile() {
	    JFileChooser fileChooser = new JFileChooser();
	    int option = fileChooser.showSaveDialog(frame);
	    if (option == JFileChooser.APPROVE_OPTION) {
	        File file = fileChooser.getSelectedFile();
	        try (FileWriter writer = new FileWriter(file)) {
	            List<Figura> figuras = panelDibujo.getFiguras();
	            for (Figura figura : figuras) {
	                writer.write(figura.toText() + "\n");
	            }
	            System.out.println("Saved file: " + file.getAbsolutePath());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	private void openFile() {
	    JFileChooser fileChooser = new JFileChooser();
	    int option = fileChooser.showOpenDialog(frame);
	    if (option == JFileChooser.APPROVE_OPTION) {
	        File file = fileChooser.getSelectedFile();
	        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	panelDibujo.obtenerFiguras(line);
	            }
	            System.out.println("Loaded file: " + file.getAbsolutePath());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

	private void openCamera() {
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Error: Camera not available.");
            return;
        }

        Mat frame = new Mat();
        while (cameraRunning && camera.read(frame)) {
            Mat handDetectedFrame = mv.detectHandGestures(frame);
            BufferedImage image = matToBufferedImage(handDetectedFrame);
            ImageIcon icon = new ImageIcon(image);
            cameraFeedLabel.setIcon(icon);
            cameraFeedLabel.repaint();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        camera.release();
        cameraFeedLabel.setIcon(null); // Limpiar la imagen cuando se apague la cámara
    }
	
	private BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, data);
        return image;
    }
	 
    
    public void iniciarRec() {
        try {
            // Inicializa el reconocedor (oreja)
            oreja = Central.createRecognizer(new EngineModeDesc(Locale.ROOT));
            oreja.allocate();
            // Cargar la gramática
            FileReader grammar1 = new FileReader("Programas.txt");
            RuleGrammar rg = oreja.loadJSGF(grammar1);
            rg.setEnabled(true);
            // Añadir el listener para los resultados
            oreja.addResultListener(new Programas(panelDibujo));
            System.out.println("Pronuncia un programa");
            // Configurar y activar el reconocedor
            oreja.commitChanges();
            oreja.requestFocus();
            oreja.resume();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    private void detenerRec() {
        if (oreja != null && escucha) {
            try {
                oreja.deallocate();
                escucha = false;
                System.out.println("Reconocimiento de voz detenido.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean getCamara() {
    	return cameraRunning;
    }
    
    
    
}
