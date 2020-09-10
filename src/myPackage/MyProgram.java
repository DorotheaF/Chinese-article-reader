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
	
	String chinTitle;
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
			Instance.graphFiller();
			Instance.textToSegmenter();
			
			Instance.writeArticleAsNodes();
			Instance.backToReadable(engTitle);
			Instance.difficultyCalculator();
			Instance.addToDir();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//end instance when? After other article is clicked, or after this one finishes?
		
	}
	
	public void graphFiller() throws IOException {
		//for HSK 1-6
		//for known
		//for learning
		//new; null
		//open file
		//for each ___, args = thing
		//myMap add node
		//on run through myVocab files updates known
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
		
		//String test = myMap.getNode("你好").chinWord;
		//System.out.println(test);
		//System.out.println(myM))
	}
	
	public void readInFile(String filename) throws IOException {
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
	
	public void addMyLists() throws IOException {
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
			//check if text is clean
			//try {
				//TODO check if text is trad or simp
				text = Text; //textToTraditional(Text);
			///} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
		}
		else if (Art=="true") {
			System.out.println("check variableFiller line 129 MyProgram");
			//call article cleaner and reader, return text and equal text
		}

	}
	
	public String textToSimplified(String text) throws IOException {
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
	
	public String textToTraditional(String text) throws IOException {
		String[] textTemp = new String[text.length()];
		textTemp = text.split("");
		Hashtable<String, String> chars = new Hashtable<String, String>();
		
		String tempy;
		String[] indivChar;
		BufferedReader in = new BufferedReader(new FileReader("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//TradToSimp.txt"));
		while ((tempy = in.readLine()) != null) {
			System.out.println(tempy);
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
	
	public void articleToText() {
		//open article, find title and source(2)--when only file is given--clean text, enter it into text variable 
	}
	
	public void textToSegmenter() {
		//takes text variable, separates each line, sends to segmenter, finishes with array of words.
		 Art2Seg seg = new Art2Seg();
		 seggedText = seg.returnsSegmented(text);
		 //System.out.println(seggedText);
	}
	
	public void writeArticleAsNodes() throws IOException { //takes segged text and writes it to file as nodes
		//takes segmented text, adds into graph
		//open file
		String filename = "C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//FinishedArticles//" + engTitle + ".txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		
		String vocablist = "C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//FinishedArticles//" + engTitle + "_vocab.txt";
		BufferedWriter outVocab = new BufferedWriter(new FileWriter(vocablist));		
		BufferedReader in = new BufferedReader(new FileReader(vocablist));
		
		out.write(chinTitle + "--" + engTitle + "--" + source + "--\n");
		
		Iterator<String> iterator = seggedText.iterator();
		
		String word;
		
		while (iterator.hasNext()) {			
			word = iterator.next();
			System.out.println(word);
			if (word.equals("。") || word.equals("?")) {
				//System.out.println("Adding punctuation...");
				out.write(">" + word + "\n");				
			}else if (word.equals("，")|| word.equals("、")||word.equals("；")||word.equals(":")||word.equals("·")||word.equals("“")||word.equals("”")||word.equals("《")||word.equals("》")) { 
				//System.out.println("Adding punctuation...");
				out.write(">" + word);				
			}else {
				if(!word.contentEquals("")) {					
					wordNode node = myMap.getNode(word);
					System.out.println(node.chinWord);
					if (node.chinWord != null) {//TODO FIX!!!
						out.write(">" + word + ", " + node.pinyin + ", " + node.engDef + ", " + node.diffLvl + ", " + node.known);						
						//System.out.println("adding " + node.chinWord);
						System.out.println("calling with " + node.chinWord);
						addToArtVocabList(outVocab, in, node, word); //TODO
						
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
	
	public void addToArtVocabList(BufferedWriter out, BufferedReader in, wordNode node, String word) throws IOException {
		System.out.println("mwahaha I got to add vocab list, with " + node.chinWord + " " + node.engDef + " " + node.known);
		
		boolean present = false;
		
		if (!node.chinWord.contentEquals(word)) {
			System.out.print("it's null ??");
		}
		
//		if (node.known.contentEquals("known")) {
//			System.out.print("not adding to vocab, is known word");
//			present = true;
//		}		
				
		
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
			System.out.println("added new word " + node.chinWord);
		}	
		
	}
	
	
	public boolean checkIfWordOnList(String filename, wordNode node) throws IOException {
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
