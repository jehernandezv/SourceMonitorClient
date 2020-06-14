package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	private Socket clientSocket;
	private DataOutputStream output;
	private DataInputStream input;
	private boolean stop;
	
	public Client() throws UnknownHostException, IOException {
		
			this.clientSocket = new Socket("localhost", 4000);
			this.output = new DataOutputStream(this.clientSocket.getOutputStream());
			this.input = new DataInputStream(this.clientSocket.getInputStream());
			start();
			saludo();
			// Con el diseño existente cada cliente solo puede realizar un request
			//serviceTemperature();
	}
	
	public void run() {
		while (!this.stop) {
			 String response;
			   try {
				response = input.readUTF();
				if (response != null) {
					manageResponse(response);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.stop = true;
			}
		   }
		}

	private void manageResponse(String response) throws IOException {
		switch (ERequest.valueOf(response)) {
		case MESSAGE: 
			messageinput();
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + response);
		}
	}

	private void messageinput() throws IOException {
		System.out.println(this.input.readUTF());
	}
	
	private void saludo() throws IOException {
		output.writeUTF("SALUDO");
	}
	
	
	public static void main(String[] args) {
		try {
			new Client();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
