package it.polito.tdp.dizionariograph.model;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.dizionariograph.db.WordDAO;

public class Model {
	
	private class EdgeTraversedGraphListener implements TraversalListener<String, DefaultEdge> {

		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {			
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> ev) {
			String sourceVertex = grafo.getEdgeSource(ev.getEdge()) ;
			String targetVertex = grafo.getEdgeTarget(ev.getEdge()) ;
			
			if( !backVisit.containsKey(targetVertex) && backVisit.containsKey(sourceVertex)) {
				backVisit.put(targetVertex, sourceVertex) ;
			} else if(!backVisit.containsKey(sourceVertex) && backVisit.containsKey(targetVertex)) {
				backVisit.put(sourceVertex, targetVertex) ;
			}			
		}

		@Override
		public void vertexFinished(VertexTraversalEvent<String> e) {			
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<String> e) {			
		}
		
	}
	
	
	private Graph<String, DefaultEdge> grafo;
	private List<String> parole;
	private Map<String, String> paroleMap;
	Map<String, String> backVisit;
	

	public void createGraph(int numeroLettere) {

		this.grafo =new SimpleGraph<>(DefaultEdge.class);
		WordDAO dao = new WordDAO();
		this.parole = dao.getAllWordsFixedLength(numeroLettere);
		
		
		this.paroleMap = new HashMap<>();
		for (String f : this.parole)
			paroleMap.put(f, f);

		Graphs.addAllVertices(this.grafo, this.parole);
		
		
		for (String partenza : this.grafo.vertexSet()) {

			for (String arrivo : parole)
				if(isSimile(partenza, arrivo))
				this.grafo.addEdge(partenza, arrivo);
		}
		
		
	}

	private boolean isSimile(String partenza, String arrivo) {
		
		int count = 0;
		
		for(int i=0; i<partenza.length(); i++) {
			if(partenza.charAt(i) == arrivo.charAt(i))
				count ++;
		}
		
		if(count == partenza.length()-1)
			return true;
		
		return false;
	}
	
	public Graph<String, DefaultEdge> getGrafo() {
		
		return grafo;
	}

	public List<String> displayNeighbours(String parolaInserita) {

		//System.err.println("displayNeighbours -- "+parolaInserita);
		List<String> result = new ArrayList<String>();
		backVisit = new HashMap<>();
		
		
		GraphIterator<String, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, parolaInserita);
		//GraphIterator<String, DefaultEdge> it = new DepthFirstIterator<>(this.grafo, parolaInserita) ;

		it.addTraversalListener(new Model.EdgeTraversedGraphListener());
		
		backVisit.put(parolaInserita, null);

		while(it.hasNext()) {
			//if(isSimile(it.next(), result.get(result.size()-1)))
			result.add(it.next());
		}

		//System.out.println(backVisit) ;
		
		return Graphs.neighborListOf(grafo, parolaInserita);
		
	}

	public void findMaxDegree() {
		
		String sorgente = null;
		int max = 0;
		
		for(String stemp : this.grafo.vertexSet()) {
			int conteggio = Graphs.neighborListOf(this.grafo, stemp).size();
			if(conteggio>max){
				max = conteggio;
				sorgente = stemp;
			}
		
		}
		
		System.out.println(sorgente +": "+max);
	
	}
}
