package it.polito.tdp.dizionariograph.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		model.createGraph(2);
		
		System.out.format("Creati %d vertici e %d archi\n", model.getGrafo().vertexSet().size(),
				model.getGrafo().edgeSet().size()) ;
		
		System.out.println(model.getGrafo());
		
		System.out.println(String.format("**Grafo creato**\n"));
		
		List<String> vicini = model.displayNeighbours("ca");
		System.out.println("Neighbours di casa: " + vicini + "\n");
		
		System.out.println("Cerco il vertice con grado massimo...");
		model.findMaxDegree();
	}

}
