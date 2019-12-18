import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.util.Scanner;
import java.io.File;

/**
 *	The Client the player will use
 *
 *       Date Last Modified: 12/18/2019
 *	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
 *
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

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
				System.out.println("If you are the \"host\" of the game, and the game has not been loaded yet please  " +
						"start a " + "new game by using \"new-game\"");
				System.out.println();
				System.out.println("then doing the following, or if you are joining a game only do the following: ");
				System.out.println();
				System.out.println("Please enter either \"EXISTING (NAME OF CHARACTOR)\"" +
						" or \"NEW (NAME OF NEW CHARACTOR\")\n Otherwise you will not be able to do ANYTHING");
				String s = "";
				while (true) {
					//Gets all ready text from server
					if (fromServer.ready()) {
						while(fromServer.ready()) {
							s = fromServer.readLine();
								System.out.println(s);
						}
						System.out.print("\n> ");
						System.out.flush();
					}

					if(keyboardInput.ready()) {
						if ((s = keyboardInput.readLine()) == null) {
							break;
						}

						toServer.println(s);
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
