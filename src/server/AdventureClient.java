import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.SocketException;


public class AdventureClient {
	public static void main ( String[] args ) {
		if ( args.length != 2 ) {
			System.out.println( "Command line arguments: server_address port" );
		} else {
			try ( Socket server = new Socket ( args[0], Integer.valueOf ( args[1] ) ) ) {
				System.out.println("Connected to AdventureServer host " + server.getInetAddress());
				BufferedReader fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
				PrintWriter toServer = new PrintWriter(server.getOutputStream(), true);
				BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Please enter either \"EXISTING (NAME OF CHARACTOR)\"" +
						" or \"NEW (NAME OF NEW CHARACTOR\")\n Otherwise you will not be able to do ANYTHING");
				String s = "";
				while (true) {
					while (fromServer.ready()) {
						s = fromServer.readLine();
						System.out.println(s);
					}
					System.out.print("> ");
					System.out.flush();
					if ((s = keyboardInput.readLine()) == null) {
						break;
					}
					toServer.println(s);
					while (fromServer.ready()) {
						s = fromServer.readLine();
						System.out.println(s);
					}
					if (s == null) {
						break;
					}
				}
				fromServer.close();
				toServer.close();
				keyboardInput.close();
			} catch (SocketException e){
				System.out.println("Uknown Disconnect: \nPossible reasons:  HOST QUIT, UNKOWN HOST, or SERVER CLOSED");
			} catch ( UnknownHostException e ) {
				System.out.println("Uknown Host");
			} catch ( IOException e ) {
				e.printStackTrace ( );
			}
		}
	}
}