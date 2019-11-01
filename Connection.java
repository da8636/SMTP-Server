import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Connection implements Runnable{
	Socket socket;
	PrintWriter printOut;
	BufferedReader readIn;
	state currentstate;




	String fromString;
	String toString;
	String dateString;
	String subjectString;
	String bodyString;
	static int emailNum = 0;
	int counterLines = 0;
	private static enum state {
		idle, from_recieved, rcpt_recieved, content
	};





	public void run(){
		try{

			printOut.println("220");
			String info = "init";
			while (true){
				//read line
				System.out.println(info);
				info = readIn.readLine();

				//test
				counterLines++;
				//printOut.println("lines of code");
				//printOut.println(counterLines);



				//HELO
				if (info.startsWith("HELO")){
					// printOut.println("Reading HELO");
					if (currentstate != state.content){
						printOut.println("250 HELO " + socket.getInetAddress().getHostName());
					}else{
						addContent(info);
					}
				}
				else if (info.startsWith("MAIL FROM:")){
					if (currentstate == state.idle){
						info = info.substring(10);
						Pattern mailPattern = Pattern.compile("<[a-zA-Z0-9-_]+@([a-zA-Z0-9-_]+\\.)*usyd.edu.au>");
						Pattern emailPattern = Pattern.compile("<[a-zA-Z0-9-_]+@([a-zA-Z0-9-_]+\\.)*[a-zA-Z0-9_-]+>");
						if(mailPattern.matcher(info).matches()){
							//TO COMPLETE
							printOut.println("250");
							currentstate = state.from_recieved;
						}else if (emailPattern.matcher(info).matches()){
							printOut.println("550");
						}
						else{
							printOut.println("501");
						}
					}else if (currentstate == state.content){
						addContent(info);
					}else{
						// //Work out which code 500/503 error
						// printOut.println("500");
						// //or
						printOut.println("503");
					}
				}
				else if (info.startsWith("RCPT TO:")){
					if ((currentstate == state.from_recieved)||(currentstate == state.rcpt_recieved)){
						Pattern reciepentPattern = Pattern.compile("<[a-zA-Z0-9-_]+@([a-zA-Z0-9-_]+\\.)*[a-zA-Z0-9_-]+>");
						info = info.substring(8);
						if (reciepentPattern.matcher(info).matches()){
							//TO COMPLETE
							printOut.println("250");
							currentstate = state.rcpt_recieved;
						}else{
							printOut.println("501");
						}
					}else if (currentstate == state.content){
						addContent(info);
					}else{
						printOut.println("503");
					}
				}
				else if (info.equals("DATA")){
					if (currentstate == state.rcpt_recieved){
						printOut.println("354");
						currentstate = state.content;
						// printOut.println("Before TEXT");
					}else if (currentstate == state.content){
						addContent(info);
					}
					else{
						printOut.println("503");
					}

				}
				else if (info.equals("QUIT")){
					if (currentstate != state.content){
						printOut.println("221");

						socket.close();
					}else{
						addContent(info);
					}
				}
				else {
					if (currentstate == state.content){
						addContent(info);
					}
					else{
						printOut.println("500");
					}
				}
				//printOut.println(info);
			}
		}catch(Exception e){
			e.printStackTrace();
			try{
				socket.close();
			}catch(IOException f){
				System.out.println("IOE");
			}
		}

	}


	public Connection(Socket s){
		socket = s;
		currentstate = state.idle;
		fromString = "From:\n";
		toString = "To:\n";
		dateString = "Date:\n";
		subjectString = "Subject:\n";
		bodyString = "";


		try{
			this.printOut = new PrintWriter(socket.getOutputStream(), true);
			this.readIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		}
		catch (IOException e){
			System.out.println("ERROR MATE");
		}
	}
	public void addContent(String newInfo){
		if (newInfo.equals(".")) {
			writeText(fromString, toString, dateString, subjectString, bodyString);
			//TO DO
			printOut.println("250");
			currentstate = state.idle;
			//printOut.println("IDLE");

		}else{
			if (newInfo.startsWith("From:")){
				fromString = newInfo + "\n";
			}else if (newInfo.startsWith("To:")){
				toString = newInfo + "\n";
			}else if (newInfo.startsWith("Date:")){
				dateString = newInfo + "\n";
			}else if (newInfo.startsWith("Subject:")){
				subjectString = newInfo + "\n";
			}else{
				// printOut.println("JOIN");
				bodyString = bodyString.concat(newInfo+"\n");
				// printOut.println("JOIN nopppppe");
			}
		}
	}

	public static void writeText(String from, String to, String date, String subject, String body){
		try {


			File file = new File("email"+emailNum+".txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			BufferedWriter data = new BufferedWriter(fileWriter);
			data.write("Message "+ emailNum + "\n");
			data.write(from);
			data.write(to);
			data.write(date);
			data.write(subject);
			data.write("Body:\n");
			data.write(body);
			data.close();
			emailNum++;


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
