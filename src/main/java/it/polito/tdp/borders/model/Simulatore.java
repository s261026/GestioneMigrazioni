package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	
	//modello: qual è lo stato del sistema a ogni passo
	private Graph<Country, DefaultEdge> grafo;
	
	//tipi di evento: coda prioritaria -> importante quando si scatenno nuovi eventi durante la simulazione
	//cambia il numero degli eventi!
	private PriorityQueue <Evento> queue;
	
	//parametri della simulazione
	private int N_MIGRANTI = 1000;
	private Country partenza;
	
	//valori di output
	private int T = -1;
	private Map<Country, Integer>  stanziali;
	//List<CountryAndNumber> non va bene perchè il numero di persone in un paese cambia... 
	//devo quindi modificare una struttura dati -> per cambiare il valore di persone è più facile avere una mappa con una get
	//con la lista invece dovrei scorrerla ogni volta fino al number corretto
	
	//inizializzazione
	public void init(Country country, Graph<Country, DefaultEdge> grafo) {
		//impostiamo i parametri iniziali
		
		this.partenza= country;
		this.grafo=grafo;
		
		//imposto lo stato iniziale
		this.T=1;
		this.stanziali= new HashMap<>();
		for(Country c: this.grafo.vertexSet())
			stanziali.put(c, 0);
		
		//creo la coda
		this.queue= new PriorityQueue<Evento>();
		
		//inserisco il primo evento
		this.queue.add(new Evento(T,partenza, N_MIGRANTI));
		//da ora gli eventi venogno aggiunti dinamicamente alla coda durante la simulazione
	}
	
	public void run(){
		//finchè la coda non si svuota prendo un evento alla volta e lo eseguo
		Evento e;
		while((e=this.queue.poll())!=null) {
			
			//simulo l'evento e
			this.T=e.getT(); //si aggiorna automaticamente e prende il t di ogni evento
			int nPersone = e.getN();
			Country stato = e.getCountry();
			
			//ottenog i vicini di stato
			List <Country> vicini = Graphs.neighborListOf(this.grafo, stato);
			
			//la metà delle persone arrivate si spostano: nPersone/2
			//le persone che finiscono nello stato vicino sono divise in parti uguali
			//ma solo se sono abbastanza, se sono meno degli stati allora stanno ferme
			int migrantiPerStato = (nPersone/2)/vicini.size();
				
			//caso limite
			if(migrantiPerStato > 0) {
				//le persone si possono muovere
				for(Country confinante: vicini) {
					queue.add(new Evento(e.getT()+1, confinante, migrantiPerStato));
				}
			}
			
			int stanziali = nPersone - migrantiPerStato*vicini.size(); // il resto della divisione finisce qui: vedi esempio su pdf
			this.stanziali.put(stato, this.stanziali.get(stato)+stanziali); //le persone possono tornare più volte nello stesso stato	
			
		}
	}
	
	public Map<Country, Integer> getStanziali(){
		return this.stanziali;
	}
	
	public Integer getT() {
		return this.T;
	}

}
