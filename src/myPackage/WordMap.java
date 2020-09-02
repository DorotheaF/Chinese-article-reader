package myPackage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myPackage.WordNode.charNode;
import myPackage.WordNode.wordNode;

public class WordMap {
	
	Map<String, charNode> wordList = new HashMap<>();
	
	public charNode addChar(String[] args) {
		WordNode Nn = new WordNode();
		WordNode.charNode newNode = Nn.new charNode(args);
		wordList.put(args[0].substring(0,1), newNode);
		return newNode;
	}
	
	public void addWord(String[] args) {
		WordNode Nn = new WordNode();
		WordNode.wordNode newNode = Nn.new wordNode(args); 
		//if charNode for first char exists, find it and add to list
		if(wordList.containsKey(args[0].substring(0, 1))){ 
			//if words list doesn't have it, add, otherwise say it exists
			boolean cont = false;
			cont = contains(newNode.chinWord, wordList.get(args[0].substring(0, 1)).words);
			if (cont == true) {
				//System.out.print("Already contains ");
				//System.out.print(args[0] + "\n\n");
			}
			else {
				wordList.get(args[0].substring(0, 1)).words.add(newNode);
				//System.out.print("Successfully added ");
				//System.out.print(newNode.chinWord + "\n\n");
			}
			
		}else if(!wordList.containsKey(args[0].substring(0, 1))) {
			charNode n = addChar(args);
			n.words.add(newNode);
		}
		
		
		
		//otherwise, make new charNode and then add to its array
	}
	
	public boolean contains(String word, List<wordNode> words) {
		for (int i = 0; i<words.size(); i++) {
			//System.out.print("Checking||" + word + "||against||" + words.get(i).chinWord + "||\n");
			if (words.get(i).chinWord.equals(word)) {
				return true;
			}			
		}
		return false;
		
	}
	
	public wordNode getNode(String word) {	
		//get word in charNode's list
		String first = word.substring(0,1);
		
		WordNode fN = new WordNode();
		
		wordNode foundNode = fN.new wordNode();
		
		if(!wordList.containsKey(first)) {
			return null;
		}
		
		for (int i=0; i < wordList.get(first).words.size(); i++) {
			if(wordList.get(first).words.get(i).chinWord.equals(word)) {
				foundNode = wordList.get(first).words.get(i);
				//System.out.println("Found " + foundNode.chinWord);
				return foundNode;
			}
		}
		return foundNode;
		
	}
	
	public void printNodes() {
		wordList.forEach((key, value) -> printHelper(key, value)); 
	}
	
	public void printHelper(String key, charNode value) {
		System.out.print("\n");
		System.out.print(key + " : " );
		value.words.forEach((item) -> System.out.print(item.chinWord + ", "));
	}
	
	public int[] diffCounter() {
		int[] diffLvl = {0,0,0,0,0,0,0,0, 0};
		
		System.out.print("\n");
		
		wordList.forEach((key, value) -> {
			value.words.forEach(node ->{
				if (node.diffLvl==-1) {
					diffLvl[0] = diffLvl[0] + 1;
					//System.out.print(node.chinWord + ", ");
				}
				else if (node.diffLvl==1) {
					diffLvl[1] = diffLvl[1] + 1;
				}
				else if (node.diffLvl==2) {
					diffLvl[2] = diffLvl[2] + 1;
				}
				else if (node.diffLvl==3) {
					diffLvl[3] = diffLvl[3] + 1;
				}
				else if (node.diffLvl==4) {
					diffLvl[4] = diffLvl[4] + 1;
				}
				else if (node.diffLvl==5) {
					diffLvl[5] = diffLvl[5] + 1;
				}
				else if (node.diffLvl==6) {
					diffLvl[6] = diffLvl[6] + 1;
				}
			});
		});
		return diffLvl;
	}
	/* character (unicode)
	 * edges to words starting with
	 *
	 **/
	
	//word node
	/*Chin word
	 * HSK source
	 * known status
	 * eng def
	 * 
	 */
	
	
}


