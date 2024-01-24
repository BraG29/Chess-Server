package com.server.player;

import java.util.UUID;

public class Game {
	
	private UUID id;
	private Player player1; //Jugador de blancas
	private Player player2; //Jugador de negras
	private Player turnOf;
	
	public Game(Player player1) {
		this.id = UUID.randomUUID();
		this.player1 = player1;
		this.turnOf = this.player1;
	}
	
	public UUID getId() {
		return id;
	}
	
	public Player getTurnOf() {
		return turnOf;
	}
	
	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public void changeTurn() {
		this.turnOf = (turnOf.getId() == player1.getId()) ? player2 : player1;
	}

}
