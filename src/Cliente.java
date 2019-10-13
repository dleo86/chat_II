
import javax.swing.*;

import java.awt.Window;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;

public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		
		addWindowListener(new EnvioOnline());
		}	
	
}

//-----------Envio de señal online-----------------
class EnvioOnline extends WindowAdapter {
	public void windowOpened(WindowEvent e) {
		
		try {
			Socket misocket = new Socket("127.0.0.1",999);
			
			PaqueteEnvio datos = new PaqueteEnvio();
			
			datos.setMensaje(" online");
			
			ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());
			
			paquete_datos.writeObject(datos);
			
			misocket.close();
		} catch (Exception e2) {
	}
}
}
//------------Fin de envio de señal online--------------------
class LaminaMarcoCliente extends JPanel implements Runnable{
	
	public LaminaMarcoCliente(){
		
		String nick_usuario = JOptionPane.showInputDialog("Nick: ");
		
		JLabel n_nick = new JLabel("Nick: ");
		add(n_nick);
		
		nick = new JLabel();
		add(nick);
		
		nick.setText(nick_usuario);
	
		JLabel texto=new JLabel("Online: ");
		
		add(texto);
		
		ip = new JComboBox();
		
		ip.addItem("127.0.0.1");
		ip.addItem("Usuario 2");
		ip.addItem("Usuario 3");
		
		add(ip);
		
		campochat = new JTextArea(12,20);
		add(campochat);
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");
		
		EnviaTexto mievento = new EnviaTexto();
		miboton.addActionListener(mievento);
		
		add(miboton);	
		
		Thread mihilo = new Thread(this);
		
		mihilo.start();
		
	}
	
	public class EnviaTexto implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//campochat.append("\n" + campo1.getText());
			
			try {
				Socket misocket = new Socket("127.0.0.1", 9999);
				PaqueteEnvio datos = new PaqueteEnvio();
				datos.setNick(nick.getText());
				datos.setIp(ip.getSelectedItem().toString());
				datos.setMensaje(campo1.getText());
				
				ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());  
				
				paquete_datos.writeObject(datos);
				
				misocket.close();
//				DataOutputStream flujo_salida = new DataOutputStream(misocket.getOutputStream());
//				
//				flujo_salida.writeUTF(campo1.getText());
				
		//		flujo_salida.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}			
		}
		
	}
	
		
		
		
	private JTextField campo1;
	private JComboBox ip;
	private JLabel nick;
	private JTextArea campochat;
	private JButton miboton;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket servidor_cliente = new ServerSocket(9090);
			Socket cliente;
			
			PaqueteEnvio paqueteRecibido;
			
			while(true) {
				cliente= servidor_cliente.accept();
				ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());
				paqueteRecibido = (PaqueteEnvio) flujoentrada.readObject();
				
				campochat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());
			}
			
		} catch(Exception e){
			System.out.println(e.getMessage());
		
	 }
	
    }
}

class PaqueteEnvio implements Serializable {
	private String nick, ip, mensaje;
	private ArrayList <String> Ips;

	public ArrayList<String> getIps() {
		return Ips;
	}

	public void setIps(ArrayList<String> ips) {
		Ips = ips;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
}