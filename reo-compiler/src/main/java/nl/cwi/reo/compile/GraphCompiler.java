package nl.cwi.reo.compile;

import nl.cwi.reo.interpret.ReoProgram;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.spriteManager.*;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphCompiler.
 */
public class GraphCompiler {

	/**
	 * Displays a graph from a Reo program.
	 * 
	 * @param program
	 *            Reo program
	 */
	public static void visualize(ReoProgram program) {
		Graph graph = new SingleGraph("Tutorial 1");
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");

		SpriteManager sman = new SpriteManager(graph);

		Sprite s = sman.addSprite("S1");

		s.attachToEdge("AB");
		s.setPosition(0.5);
		s.addAttribute("ui.label", "fifo");
		s.addAttribute("ui.style",
				"sprite { shape: box; size: 15px, 20px; fill-mode: plain; fill-color: red; stroke-mode: plain; stroke-color: blue; }");

		graph.display();

	}

}
