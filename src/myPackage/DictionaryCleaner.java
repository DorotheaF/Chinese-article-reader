package myPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class DictionaryCleaner {
	public static void main(String args[]) {
		String filename = "C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//cedict_ts.txt";
		String fileout = "C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//c-dict.txt";
		DictionaryCleaner instance = new DictionaryCleaner();
		try {
			instance.readInFile(filename, fileout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readInFile(String filename, String fileout) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		BufferedWriter out = new BufferedWriter(new FileWriter(fileout));
		String [] args = new String[5];
		String tempy;
		while((tempy=in.readLine())!=null) {
			System.out.println(tempy);
			
			//String splitted[] = new String[3];
			//tempy.replaceAll("\\s", "");
			
			String splitter[] = tempy.split(" ", 3); //simplified
			args[0] = splitter[1];
			
			//System.out.println(splitter[0] + "::" + splitter[1] + "::" + splitter[2]);
			
			String split = splitter[2].replace("[", "");
			
			split = split.replace("]", "");
			
			splitter = split.split(" /");
			args[1] = splitter[0];
			
			args[2] = splitter[1].replace("/", " ");
			//splitter = splitter[1].trim().split(" ", 2);
			//splitter = splitter[1].trim().split(" ", 2);
			
			
			
			args[3] = "-1";
			args[4] = "und";
			System.out.println(args[0] + "::" + args[1] + "::" + args[2] + "::" + args[3] + "::" + args[4]);
			out.write(args[0] + ", " + args[1] + ", " + args[2] + ", " + args[3] + ", " + args[4] + "\n");
		}
		in.close();
		out.close();
	}
}
