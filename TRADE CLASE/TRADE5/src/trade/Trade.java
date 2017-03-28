package trade;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.pretty_tools.dde.DDEException;
import com.pretty_tools.dde.DDEMLException;
import com.pretty_tools.dde.client.DDEClientConversation;

public class Trade extends JFrame {
	
	private static final long serialVersionUID = 5126459686105080505L;
	
	private static final String SERVICE = "TRADE";
	private static final String TOPIC = "io";
	private static final String ITEM = "INPUT";
	private static final String REQUEST = "OUTPUT";
    private static final String COMMAND = "@GI;23;";
    
    private JLabel lblFecha_e;
    private JLabel lblHora_e;
    private JLabel lblPesoBruto_e;
    private JLabel lblTara_e;
    private JLabel lblPesoNeto_e;
    
    private JLabel lblFecha_t;
    private JLabel lblHora_t;
    private JLabel lblPesoBruto_t;
    private JLabel lblTara_t;
    private JLabel lblPesoNeto_t;
    
    private JPanel pnlCentro;
    
    Timer timer;
    
    private enum enumValor {FECHA, HORA, PESO_BRUTO, TARA, PESO_NETO};
    
    public static void main(String[] args) {
    	Trade trade = new Trade();
    }
    
    public Trade() {
    	init();
    	monitoreo();
    }
    
    private void init() {
    	
    	lblFecha_e = new JLabel("Fecha:");
    	lblFecha_t = new JLabel(" ");
    	lblFormat(lblFecha_t, false);
    	
    	lblHora_e = new JLabel("Hora:");
    	lblHora_t = new JLabel(" ");
    	lblFormat(lblHora_t, false);
    	    	
    	lblPesoBruto_e = new JLabel("Peso Bruto:");
    	lblPesoBruto_t = new JLabel("0");
    	lblFormat(lblPesoBruto_t, true);
    	
    	lblTara_e = new JLabel("Tara:");
    	lblTara_t = new JLabel("0");
    	lblFormat(lblTara_t, true);
    	
    	lblPesoNeto_e = new JLabel("Peso Neto:");
    	lblPesoNeto_t = new JLabel("0");
    	lblFormat(lblPesoNeto_t, true);
    	
    	pnlCentro = new JPanel(new GridBagLayout());
    	
    	pnlCentro.add(lblFecha_e, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, 
				GridBagConstraints.NONE, new Insets( 5, 5, 5, 5), 0, 0));
    	
    	pnlCentro.add(lblFecha_t, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, 
				GridBagConstraints.HORIZONTAL, new Insets( 5, 0, 5, 5), 0, 0));
    	
    	pnlCentro.add(lblHora_e, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, 
				GridBagConstraints.NONE, new Insets( 5, 5, 5, 5), 0, 0));
    	
    	pnlCentro.add(lblHora_t, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, 
				GridBagConstraints.HORIZONTAL, new Insets( 5, 0, 5, 5), 0, 0));
    	
    	pnlCentro.add(lblPesoBruto_e, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, 
				GridBagConstraints.NONE, new Insets( 5, 5, 5, 5), 0, 0));
    	
    	pnlCentro.add(lblPesoBruto_t, new GridBagConstraints(1,2, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, 
				GridBagConstraints.HORIZONTAL, new Insets( 5, 0, 5, 5), 0, 0));
    	
    	pnlCentro.add(lblTara_e, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, 
				GridBagConstraints.NONE, new Insets( 5, 5, 5, 5), 0, 0));
    	
    	pnlCentro.add(lblTara_t, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, 
				GridBagConstraints.HORIZONTAL, new Insets( 5, 0, 5, 5), 0, 0));
    	
    	pnlCentro.add(lblPesoNeto_e, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, 
				GridBagConstraints.NONE, new Insets( 5, 5, 5, 5), 0, 0));
    	
    	pnlCentro.add(lblPesoNeto_t, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, 
				GridBagConstraints.HORIZONTAL, new Insets( 5, 0, 5, 5), 0, 0));
    	
    	this.setLayout(new BorderLayout());
		this.setSize(new Dimension(170, 200));
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("Compiere - Proceso de actualizacion");
		this.getContentPane().add(pnlCentro, BorderLayout.CENTER);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        timer.cancel();
		    }
		});
		
    }
    
	private void lblFormat(JLabel lbl, boolean rightAlign) {
    	
    	lbl.setBorder(BorderFactory.createTitledBorder(""));
    	lbl.setBackground(Color.WHITE);
    	lbl.setOpaque(true);
    	
    	if (rightAlign)
    		lbl.setHorizontalAlignment(SwingConstants.RIGHT);
    	else
    		lbl.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void monitoreo() {
    	
    	TimerTask timerTask = new TimerTask() {
            public void run() {
            	muestraDatos();
            }
        };
        
        timer = new Timer(); 
        timer.scheduleAtFixedRate(timerTask, 0, 500);
    }
    
    private void muestraDatos() {
    	
    	Map<Object, String> mapDatos = capturaDatos();
    	
    	if (mapDatos != null && mapDatos.size() > 0) {
    	
	    	lblFecha_t.setText(mapDatos.get(enumValor.FECHA));
	    	lblHora_t.setText(mapDatos.get(enumValor.HORA));
	    	lblPesoBruto_t.setText(mapDatos.get(enumValor.PESO_BRUTO));
	    	lblTara_t.setText(mapDatos.get(enumValor.TARA));
	    	lblPesoNeto_t.setText(mapDatos.get(enumValor.PESO_NETO));
    	} else {
    		lblFecha_t.setText("--");
	    	lblHora_t.setText("--");
	    	lblPesoBruto_t.setText("-1");
	    	lblTara_t.setText("-1");
	    	lblPesoNeto_t.setText("-1");
    	}
    }
    
    private Map<Object, String> capturaDatos() {
    	
    	final int INDICE_FECHA_HORA = 4;
        
        final int INDICE_DIA = 0;
        final int INDICE_HORA = 1;
        
        final int INDICE_PESOS = 7;
        
        final int INDICE_PESO_BRUTO = 1;
        final int INDICE_PESO_NETO = 2;
        final int INDICE_TARA = 3;
    	
        Map<Object , String> mapValores = null;
        
    	try {
    		
            final DDEClientConversation conversation = new DDEClientConversation();
            // We can use UNICODE format if server prefers it
            //conversation.setTextFormat(ClipboardFormat.CF_UNICODETEXT);
            
            //System.out.println("Connecting...");
            conversation.connect(SERVICE, TOPIC);
            
            try {
            	conversation.poke(ITEM, COMMAND);
            	
                String data = conversation.request(REQUEST);
                
                /*
                 * El request devuelve una cadena de valores similar a la muestra (Sin saltos de linea):
                 * 
                 * "@GI","","","","29.12.16  11:11:31","[????-????-ce15-1.1-01.02.02]",
                 *		"Simulation-1:Simulation Min=400kg Max=60000kg e=d=20kg","Stat M|33000|33000|0",
                 *		"33000kg","33000kg","0kg","",""
                 * */
                
                /* Creamos un arreglo a partir de los valores de la cadena */
                String[] valores = data.split(",");
                
                if (valores != null && valores.length > 0) {
                	
                	mapValores = new HashMap<Object , String>();
                	
                	String arrfechaHora = valores[INDICE_FECHA_HORA];
                	
                	String[] fechaHora = arrfechaHora.substring(1, arrfechaHora.length() -1)
                				.replace("  ", "|").split("\\|");
                	
                	mapValores.put(enumValor.FECHA, fechaHora[INDICE_DIA]);
                	mapValores.put(enumValor.HORA, fechaHora[INDICE_HORA]);
                	
                	String arrPesos = valores[INDICE_PESOS];
                	
                	String[] pesos = arrPesos.substring(1, arrPesos.length() -1)
            					.split("\\|");
                	
                	mapValores.put(enumValor.PESO_BRUTO, pesos[INDICE_PESO_BRUTO]);
                	mapValores.put(enumValor.TARA, pesos[INDICE_TARA]);
                	mapValores.put(enumValor.PESO_NETO, pesos[INDICE_PESO_NETO]);
                	
                }
                
                //System.out.println("Rpta=" + data);
                
            } finally {
                conversation.disconnect();
            }
        } catch (DDEMLException e) {
            System.out.println("DDEMLException: 0x" + Integer.toHexString(e.getErrorCode()) + " " + e.getMessage());
        } catch (DDEException e) {
            System.out.println("DDEClientException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    	
    	return mapValores;
    }
    
}
