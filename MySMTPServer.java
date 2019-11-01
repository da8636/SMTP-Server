import java.net.*;
import java.io.*;

public class MySMTPServer{
	public static void main(String[] args) {
		try {
 			// TODO: create a new socket
			ServerSocket sock = new ServerSocket(Integer.parseInt(args[0]));

			// now listen for connections
			while (true) {
 				// TODO: wait and accept a client connection
				Socket client = sock.accept();
				Connection c = new Connection(client);
				Thread t = new Thread(c);
				t.start();
 				// write the Date to the socket

 				// TODO: close the client connection
 			}
 		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}
