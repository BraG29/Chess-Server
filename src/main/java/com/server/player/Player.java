package com.server.player;

import java.net.Socket;

public class Player {
	private int id;
	private Socket socket;
	private Game game;

	public Player(Socket socket) {
		this.socket = socket;
		this.game = null;
	}
	
	public int getId() {
		return id;
	}
	

	public void setId(int id) {
		this.id = id;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public Game getGame() {
		return game;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
}
