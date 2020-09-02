package myPackage;

import java.util.ArrayList;
import java.util.List;

public class WordNode {
	
	
		public class wordNode{
			String chinWord;
			String pinyin;
			int diffLvl;
			String known;
			String engDef;
			//boolean printed(?)//check if node was printed to file
			//String[] exampleWords; if individual char, else for all chars in word
			
			public wordNode(String[] args) {
				this.chinWord = args[0];
				this.pinyin = args[1];
				this.engDef = args[2];
				this.diffLvl = Integer.parseInt(args[3]);
				this.known = args[4];
				//System.out.print("Making wordNode " + args[0] + "\n");
			}
			
			public wordNode() {
				
			}				
			
		}
		
		
		public class charNode{

			String chinChar;
			//array of wordNodes
			List<wordNode> words = new ArrayList<wordNode>(); 	
			
			public charNode(String[] args) {
				String first = args[0].substring(0,1);
				//System.out.print("making charNode " + first + "\n");
				this.chinChar = first;
			}				
		}
}
