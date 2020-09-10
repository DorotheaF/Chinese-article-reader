package myPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import myPackage.WordNode.wordNode;
import segger.Art2Seg;

public class MyProgram {
	
	//The way this works:
	//User uploads text in the UI, it gets sent here as an array of [Chinese title, English Title, Source, text, (not yet calculated) Difficulty]
		//If the user loads an article instead of text, it gets cleaned and formatted as simple text with certain characters removed (this is still in progress)
		//Either eay, the text gets converted to the traditional character set
	//The text gets sent to the Stanford NLP Segmenter which returns a list of separated words
	//The program loads in the Chinese dictionary and hsk lists into a WordMap of Chinese word nodes (Chinese word, pronunciation, English definition, difficulty level, known/learning/unknown)
	//The program then creates a files, EnglishTitle.txt, to store the words with all their info. This file name gets added to the directory file.
	//Then it iterates through the segmented list, and compares each word with nodes from the dictionary WordMap 
		//If it finds the node, it writes it (and any punctuation) the file with the format of > Word, info, more info > Word, info, more info (i.e >你, nǐ, you (singular), 1, known>好, hǎo, good, 1, known>。)
		//If it doesn't find the node, it writes the info fields as "-", which usually means the NLP segmenter split the words incorrectly
	
	//The second step, once one or more articles have been processed, is reading the article
	//The user click the title of an article in the UI, and that calls the backToReadable function, which creates a WordMap out of the file
	//The WordMap gets sent back to the UI, which makes an array of buttons, each with a word. Clicking on a button calls the associated node and displays its info at the top of the screen (as well as add to vocab list options)
	
	
	String chinTitle; //these variables are the name and content of the uploaded text
	static String engTitle;
	String source;
	String articleFileName; //if the user input a file instead of text
	String text;
	int diffLvl[];
	static java.util.List<String> seggedText; //from the Stanford Segmenter
	static WordMap artMap = new WordMap(); //the map holding all the nodes for the article
	static WordMap myMap = new WordMap(); //the map holding all the nodes for the vocab lists and dictionary
	
	
	public static void main(String[] args) {
		MyProgram Instance = new MyProgram();
		Instance.variableFiller(args[0], args[1], args[2], args[3], args[4]);
		//Instance.check();
		
		//System.out.println(args[0]);
		
		try {
			Instance.graphFiller(); //fills the dictionary WordMap
			Instance.textToSegmenter(); //Segments text 			
			Instance.writeArticleAsNodes(); //Writes words/nodes to new file
			//Instance.backToReadable(engTitle); 
			Instance.difficultyCalculator(); //Not yet implemented. Will calculate difficulty level of the article based on percentages of words of different difficulties
			Instance.addToDir(); //adds the new file to the directory 
		} catch (IOException e) {
			e.printStackTrace();
		}
		//end instance when? After other article is clicked, or after this one finishes?
		
	}
	
	public void graphFiller() throws IOException {
		//for HSK 1-6, and large c-dict 
		readInFile("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//HSK1.txt"); 
		readInFile("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//HSK2.txt"); 
		readInFile("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//HSK3.txt"); 
		readInFile("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//HSK4.txt"); 
		readInFile("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//HSK5.txt"); 
		readInFile("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//HSK6.txt"); 
		readInFile("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//c-dict.txt"); 
		addMyLists();
		
		//System.out.println("Graphfilled"); 
		//myMap.printNodes();
	}
	
	public void readInFile(String filename) throws IOException { //This is not functional for now
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String [] args = new String[5];
		String tempy;
		while((tempy=in.readLine())!=null) {
			//System.out.println(tempy);
			String splitted[] = tempy.split(", ");
			args[0] = splitted[0];
			args[1] = splitted[1];
			args[2] = splitted[2];
			args[3] = splitted[3];
			args[4] = splitted[4];
			//System.out.println(args[0] + "::" + args[1] + "::" + args[2] + "::" + args[3] + "::" + args[4]);
			myMap.addWord(args);
		}
		in.close();
	}
	
	public void addMyLists() throws IOException { //reads in my known words (adds to last field in node if known/learning)
		BufferedReader in = new BufferedReader(new FileReader("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//knownWords.txt"));
		String tempy;
		while((tempy=in.readLine())!=null) {
			tempy = tempy.split(",")[0];
			wordNode node = myMap.getNode(tempy);
			node.known = "known";
		}	
		in.close();
		
		BufferedReader in2 = new BufferedReader(new FileReader("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//learningWords.txt"));
		String tempy2;
		while((tempy2=in2.readLine())!=null) {
			wordNode node = myMap.getNode(tempy2);
			node.known = "learning";
		}	
		in2.close();		
	}
	
	public void variableFiller(String ChinTitle, String EngTitle, String Source, String Text, String Art) {
		chinTitle = ChinTitle;
		if (chinTitle==null) {
			//add first sentence as title
		}
		engTitle = EngTitle;
		source = Source;
		if (Art=="false") { 
			//TODO check if text is traditional or simplified, implement choice for user for display
			try {
				text = textToTraditional(Text);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		else if (Art=="true") {
			System.out.println("check variableFiller line 129 MyProgram");
			//TODO implement article file cleaner
			//call article cleaner and reader, return text and equal text
		}

	}
	
	public String textToSimplified(String text) throws IOException { //convert text to simplified
		String[] textTemp = new String[text.length()];
		textTemp = text.split("");
		Hashtable<String, String> chars = new Hashtable<String, String>();
		
		String tempy;
		String[] indivChar;
		BufferedReader in = new BufferedReader(new FileReader("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//TradToSimp.txt"));
		while ((tempy = in.readLine()) != null) {
			System.out.println(tempy);
			indivChar = tempy.split("	");
			chars.put(indivChar[0], indivChar[1]);
			//System.out.println("added " + indivChar[0] + " and " + indivChar[1]);
		}
		
		in.close();
		
		for (int i = 0; i < textTemp.length; i++) {
			if(chars.containsKey(textTemp[i])) {
				System.out.println("replacing " + textTemp[i] + " with " + chars.get(textTemp[i]));
				textTemp[i] = chars.get(textTemp[i]);
			}
		}
		
		String backToText = "";
		
		for (int i = 0; i < textTemp.length; i++) {
			backToText += textTemp[i];			
		}
		
		text = backToText;
		System.out.println("The simplified text is " + text);
		return text;
	}
	
	public String textToTraditional(String text) throws IOException { //convert text to traditional 
		String[] textTemp = new String[text.length()];
		textTemp = text.split("");
		Hashtable<String, String> chars = new Hashtable<String, String>();
		
		String tempy;
		String[] indivChar;
		BufferedReader in = new BufferedReader(new FileReader("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//TradToSimp.txt"));
		while ((tempy = in.readLine()) != null) {
			//System.out.println(tempy);
			indivChar = tempy.split("	");
			chars.put(indivChar[1], indivChar[0]);
			//System.out.println("added " + indivChar[1] + " and " + indivChar[0]);
		}
		
		in.close();
		
		for (int i = 0; i < textTemp.length; i++) {
			if(chars.containsKey(textTemp[i])) {
				System.out.println("replacing " + textTemp[i] + " with " + chars.get(textTemp[i]));
				textTemp[i] = chars.get(textTemp[i]);
			}
		}
		
		String backToText = "";
		
		for (int i = 0; i < textTemp.length; i++) {
			backToText += textTemp[i];			
		}
		
		text = backToText;
		System.out.println("The traditional text is " + text);
		return text;
	}
	
	public void articleToText() { //TODO
		//open article, find title and source(2)--when only file is given--clean text, enter it into text variable 
	}
	
	public void textToSegmenter() {
		//takes text variable, separates each line, sends to segmenter, finishes with array of words.
		 Art2Seg seg = new Art2Seg(); //Stanford NLP function
		 seggedText = seg.returnsSegmented(text);
		 //System.out.println(seggedText);
	}
	
	public void writeArticleAsNodes() throws IOException { //takes segmented text list and writes it to file as nodes (finds thim in the WordMap)
		String filename = "C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//FinishedArticles//" + engTitle + ".txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		
		String vocablist = "C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//FinishedArticles//" + engTitle + "_vocab.txt";
		BufferedWriter outVocab = new BufferedWriter(new FileWriter(vocablist));		
		BufferedReader in = new BufferedReader(new FileReader(vocablist));
		
		out.write(chinTitle + "--" + engTitle + "--" + source + "--\n");
		
		Iterator<String> iterator = seggedText.iterator();
		
		String word;
		
		while (iterator.hasNext()) { //for each word, write it to the file as a node or punctuation. If the word isn't in a dictionary, print an empty node.			
			word = iterator.next();
			//System.out.println(word);
			if (word.equals("。") || word.equals("?")) {
				out.write(">" + word + "\n");				
			}else if (word.equals("，")|| word.equals("、")||word.equals("；")||word.equals(":")||word.equals("·")||word.equals("“")||word.equals("”")||word.equals("《")||word.equals("》")) { 				
				out.write(">" + word);				
			}else {
				if(!word.contentEquals("")) {					
					wordNode node = myMap.getNode(word);
					//System.out.println(node.chinWord);
					if (node.chinWord != null) {//Note, this was functioning poorly before. Fixed it, just make sure it works in the future
						out.write(">" + word + ", " + node.pinyin + ", " + node.engDef + ", " + node.diffLvl + ", " + node.known);						
						//System.out.println("adding " + node.chinWord);
						System.out.println("writing " + node.chinWord);
						addToArtVocabList(outVocab, in, node, word); 
						
					}else {
						out.write(">" + word + ", " + "-" + ", " + "-"+ ", " + "-" + ", " + "-");
						System.out.println("writing blank node " + word);
					}
				}				
			}			
		}		
		out.close();
		outVocab.close();
		in.close();
	}
	
	public void difficultyCalculator() {
		//counts up words of various levels, known/learning
		
		try {
			articleToMap(engTitle);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//artMap.printNodes();
		
		diffLvl = artMap.diffCounter();
		System.out.println("Diff Levels : " + diffLvl[1] + ", " + diffLvl[2] + ", " + diffLvl[3] + ", " + diffLvl[4] + ", " + diffLvl[5] + ", " + diffLvl[6] + ". Unknown: " + diffLvl[0]);
		diffLvl[7] = diffLvl[0] + diffLvl[1] + diffLvl[2] + diffLvl[3] + diffLvl[4] + diffLvl[5] + diffLvl[6];
		//TODO make calculator algorithms
		//diffLvl[8] = (diffLvl[0]/diffLvl[7])*10;// + (diffLvl[6]/diffLvl[7])*6 + (diffLvl[5]/diffLvl[7])*5 + (diffLvl[4]/diffLvl[7])*4 + (diffLvl[3]/diffLvl[7])*3 - (diffLvl[2]/diffLvl[7])*2 - (diffLvl[1]/diffLvl[7])*1;//make better algorithm for this
		System.out.println(diffLvl[8]);
		float difflvl;
		difflvl = (diffLvl[0]/diffLvl[7])*10;// + (diffLvl[6]/diffLvl[7])*6 + (diffLvl[5]/diffLvl[7])*5 + (diffLvl[4]/diffLvl[7])*4 + (diffLvl[3]/diffLvl[7])*3 - (diffLvl[2]/diffLvl[7])*2 - (diffLvl[1]/diffLvl[7])*1;//make better algorithm for this
		System.out.println(difflvl);
		diffLvl[8] = (int) difflvl;
	}
	
	public WordMap articleToMap(String engTitle) throws IOException {
		String filename = "C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//FinishedArticles//" + engTitle + ".txt";
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String tempy;		
		tempy=in.readLine();
		
		while((tempy=in.readLine())!=null) {
			String splitted[] = tempy.split(">");
			//System.out.println(tempy);
			for (int i = 0; i < splitted.length; i++) {
				//System.out.println(splitted[i]);
				String node = splitted[i];
				String args[] = node.split(", ");
				if (args.length!=1){
					if (!args[3].contentEquals("-")) {
						artMap.addWord(args);
						if (args[4]!="und") {
							artMap.getNode(args[0]).known = args[4];
						}
						//totalWords = totalWords + 1; 
					}
				}								
			}			
		}
		//System.out.println("Diff Levels : " + diffLvl[1] + ", " + diffLvl[2] + ", " + diffLvl[3] + ", " + diffLvl[4] + ", " + diffLvl[5] + ", " + diffLvl[6] + ". Unknown: " + diffLvl[0] + ", " + diffLvl[7]);
		in.close();
		//tempMap.printNodes();
		return artMap;
	}
	
	public List<String> backToReadable(String engTitle) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//FinishedArticles//" + engTitle + ".txt"));
		BufferedWriter out = new BufferedWriter(new FileWriter("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//FinishedArticles//ReadableTestText.txt"));
		List<String> text = new ArrayList<String>();
		String tempy;
		tempy=in.readLine();
		String splitted[] = tempy.split(">");
		out.write(splitted[0] + "\n");
		while((tempy=in.readLine())!=null) {
			splitted = tempy.split(">");
			for (int i = 0; i < splitted.length; i++) {
				String node = splitted[i];
				String word = node.split(",")[0];
				out.write(word);
				text.add(word);
				//System.out.print(word);
				if (word.equals("。")) {
					out.write("\n");
					text.add("\n");
				}
			}		
			
		}
		in.close();
		out.close();
		//System.out.println(text);
		return text;
	}
	
	public void addToDir() throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//FinishedArticles//Directory.txt", true));
		out.append(engTitle + ">" + chinTitle + ">" + source + ">" + diffLvl[8] + "\n"); //+tags list (for search and sorting functionality later)
		out.close();
	}

	public void check() {
		System.out.println(engTitle);
		System.out.println(source);
		System.out.println(text);
	}
	
	public boolean addToLearning(wordNode node) throws IOException { //check if it is already on list
		String filename = "C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//learningWords.txt";
		boolean add = checkIfWordOnList(filename, node);
		if (add==false) {	
			BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
			out.append(node.chinWord + ", " + node.pinyin + ", " + node.engDef + ", " + node.diffLvl + ", " + "learning" + "\n"); //+tags list (for search and sorting functionality later)
			out.close();
		}
		return add;
	}
	
	public boolean addToKnown(wordNode node) throws IOException {
		String filename = "C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//knownWords.txt";
		boolean add = checkIfWordOnList(filename, node);
		if (add==false) {			
			BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
			out.append(node.chinWord + ", " + node.pinyin + ", " + node.engDef + ", " + node.diffLvl + ", " + "known" + "\n"); //+tags list (for search and sorting functionality later)
			out.close();
		}
		//TODO remove word from learning
		return add;
	}
	
	public void addToArtVocabList(BufferedWriter out, BufferedReader in, wordNode node, String word) throws IOException { //writes node to vocab list file, and increments number of occurrences
		//TODO finish implementing and debugging
		//System.out.println("mwahaha I got to add vocab list, with " + node.chinWord + " " + node.engDef + " " + node.known);
		
		boolean present = false;
		
		if (!node.chinWord.contentEquals(word)) {
			System.out.print("it's null ??");
		}
		
		//if (node.known.contentEquals("known")) {
		//	System.out.print("not adding to vocab, is known word");
		//	present = true;
		//}		
				
		
		String tempy = new String();
		String tempy2[] = new String[3];
		
		while((tempy=in.readLine())!=null){
			tempy2=tempy.split(",");
			System.out.print("looking at line " + tempy2[0] + " " + tempy2[1]);
					
			//System.out.println(tempy);
			if(tempy2[1].contentEquals(node.chinWord)) {
				System.out.println("already in list");
				int num = Integer.parseInt(tempy2[0]) + 1;
				out.write(num + "," + tempy2[1] + "," + tempy2[2] + "\n");
				present = true;
				
				return;
			}
		}
		if (present == false) {
			out.append("1" + "," + word + "," + node.engDef + "\n");
			//System.out.println("added new word " + node.chinWord);
		}	
		
	}
	
	
	public boolean checkIfWordOnList(String filename, wordNode node) throws IOException { //check if a word is in the vocab file (known or learning) before adding it 
		//TODO: modify for addToArtVocabList as well, to make code more modular
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String tempy = new String();
		boolean present = false;
		
		
		
		while((tempy=in.readLine())!=null){
			tempy=tempy.split(",")[0];
			//System.out.println(tempy);
			if(tempy.contentEquals(node.chinWord)) {
				present = true;
				System.out.println("already in list");
				in.close();
				return present;
			}
		}
		in.close();		
		return present;
	}
}
