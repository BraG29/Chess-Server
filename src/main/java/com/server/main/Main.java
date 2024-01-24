package com.server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.player.Game;
import com.server.player.Player;

public class Main {
	private static List<Player> players;
	private static Map<UUID, Game> games;
//	private static int idCount = 0;

	public static void main(String[] args) {
		players = new ArrayList<Player>();
		
		JSONObject responseJSON = new JSONObject();
		
		try {
			ServerSocket serverSocket = new ServerSocket(5000);
			
			System.out.println("Servidor levantado...");
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				
				System.out.println("Cliente conectado.");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				
				String jsonFromClient = in.readLine();
				
				JSONObject receivedJSON = new JSONObject(jsonFromClient);
				String command = receivedJSON.getString("command");
				
				switch (command) { 
					case "HS": { //HandShake
						Player player = handShake(clientSocket);
						responseJSON
						.put("Status", "OK")
						.put("playerID", player.getId());
						break;
					} 
					
					
					case "CG": { //Create Game
						Player player = createGame(receivedJSON);
						responseJSON
						.put("Status", "OK")
						.put("gameID", player.getGame().getId());
						break;
					}
					
					case "JG": { //Join Game
						Player player = joinGame(receivedJSON);
						responseJSON
						.put("Status", "OK");
						
						break;
					}
					
					case "MM":{ //Make Move
						Player player = makeMove(receivedJSON);
						out = new PrintWriter(player.getSocket().getOutputStream(), true);
						responseJSON = receivedJSON;
						break;
					}
				
				}
				
				out.println(responseJSON.toString());
			}
			
			
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
			
		
	}
	
	public static Player handShake(Socket clientSocket) {
		Player player = new Player(clientSocket);
		players.add(player);
		player.setId(players.indexOf(player));
		return player;
	}
	

	
	/**
	{
		command: "CG"
		playerID: ""
	}
	*/
	
	public static Player createGame(JSONObject receivedJSON) {
		int playerID =  receivedJSON.getInt("playerID");
		Player player = players.get(playerID);
		
		Game game = new Game(player);
		player.setGame(game);
		games.put(game.getId(), game);
		
		return player;
	}
	
	/*
	 {
	 	command: "JG"
	 	playerID: ""
	 	gameID: ""
	 }
	 */
	
	public static Player joinGame(JSONObject receivedJSON) {
		int playerID = receivedJSON.getInt("playerID");
		Player player = players.get(playerID);
		
		UUID gameID = (UUID) receivedJSON.get("gameID");
		Game game = games.get(gameID);
		game.setPlayer2(player);
		
		player.setGame(game);
		
		return player;
	}
	
	
	/*
	 {
	 	command: "MM"
	 	playerID: ""
	 	move: ""
	 	promTo: ""
	 }
	 */
	public static Player makeMove(JSONObject receivedJSON) {
		int playerID = receivedJSON.getInt("playerID");
		Player player = players.get(playerID);
		
		Game game = player.getGame();
		game.changeTurn();
		
		return game.getTurnOf();
		
	}

}
