package myPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import layouts.WrapLayout;


public class GUI {
	
	JFrame frame;
	JLabel msg;
	JPanel menu;
	Dimension screen;
	
	JPanel full;
	JButton uploadFile;
	JButton uploadText;
	JButton readArticle;
	JLabel error;
	JButton menuReturn;
	JButton exit;
	
	JTextField chinTitleBox;
	JTextField engTitleBox;
	JTextField sourceBox;
	JTextArea textBox;
	JLabel chinTitle;
	JLabel engTitle;
	JLabel source;
	JLabel text;
	JPanel inputChin;
	JPanel inputEng;
	JPanel inputSource;
	JPanel inputText;
	JPanel inputFields;
	JButton enter;
	JButton enterFile;
	JButton finished;
	JButton close;
	
	MyProgram Instance;
	JList<String> articles;
	String selected;
	boolean onList;
	
	JLabel defText1;
	JLabel defText2;
	JLabel defText3;
	JButton addKnown;
	JButton addLearning;
	JPanel definition; 
	WordMap artMap;
	WordNode.wordNode defWord;
	JFrame notification;
	Timer wait;
	
	//TODO back from reading article to database selection
	//TODO add title on reading article page
	//TODO make search grammar and word function (across all articles, or a selection? -- for practicing things just learned)
	

	public static void main(String[] args) {
		GUI Instance = new GUI();
		Instance.MakeAWindow();
	}
	
	class myActionListener implements ActionListener{
	      public void actionPerformed(ActionEvent e) {
	         Object clicked = e.getSource();
	         
	         if (clicked==uploadFile) {
	        	 //uploadFileWindow(); //TODO impelent file reader
	        	 unimplementedError();
	         }
	         if (clicked==uploadText) {
	        	 //clear frame, add new instruction message, box for source, box for English, box for text
	        	 uploadTextWindow();
	         }
	         if (clicked==exit) {
	        	 //if MyProgram isn't still running
	        	 System.exit(0);
	         }
	         if (clicked==readArticle) {
	        	 frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	        	 try {
					readArticle();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	         }
	         if (clicked==enterFile) {	
	        	 
        	 	 String chinTitleStr = chinTitleBox.getText();
	        	 String engTitleStr = engTitleBox.getText();
	        	 String source = sourceBox.getText();
	        	 String text = textBox.getText();		        	 

		         String[] info = new String[5];	        	 
	        	 
		         info[0] = chinTitleStr;
	        	 info[1] = engTitleStr;
	        	 info[2] = source;
	        	 info[3] = text;
	        	 info[4] = "false";
	        	 
	        	 System.out.println(info[0]);
	        	 
	        	 if (!info[1].contentEquals("")) {      	 
	        		 //MyProgram.main(info);	
	        	 }
	        	 
	             frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); //do I need to do this, or will new window change it?
	             if (!info[0].contentEquals("")) {
	            	 thanksMessage(true);		//want this to appear right after pressing enter, but doesn't work. Multiple threads???
	             }else {
	            	 thanksMessage(false); //TODO change later
	             }//TODO delay Myprogram so that message shows, then add new message when article is ready to be read
	         }
	         if (clicked==enter) {	
	        	 
        	 	 String chinTitleStr = chinTitleBox.getText();
	        	 String engTitleStr = engTitleBox.getText();
	        	 String source = sourceBox.getText();
	        	 String text = textBox.getText();		        	 

		         String[] info = new String[5];	        	 
	        	 
		         info[0] = chinTitleStr;
	        	 info[1] = engTitleStr;
	        	 info[2] = source;
	        	 info[3] = text;
	        	 info[4] = "false";
	        	 
	        	 System.out.println(info[0]);
	        	 
	        	 if (!info[1].contentEquals("")) {      	 
	        		 MyProgram.main(info);	
	        	 }
	        	 
	             frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); //do I need to do this, or will new window change it?
	             if (!info[0].contentEquals("")) {
	            	 thanksMessage(true);		//want this to appear right after pressing enter, but doesn't work. Multiple threads???
	             }else {
	            	 thanksMessage(false);
	             }//TODO delay Myprogram so that message shows, then add new message when article is ready to be read
	         }
	      	 if (clicked==finished) {    	      		 
	      		 frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		             MakeAWindow();	        		 
	      	 }	 
	      	if (clicked==close) {    	      		 
	      		 frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));        		 
	      	 }
	      }
	}
	
	class myListListener implements ListSelectionListener{

	

		@Override
		public void valueChanged(ListSelectionEvent e) {			
			selected = articles.getSelectedValue().toString();
			selected = selected.split(", ")[0];
			System.out.println("Selected: " + selected);
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			try {
				ReadArticleWindow();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	class wordActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = ((AbstractButton) e.getSource()).getText();			
		
			defWord = artMap.getNode(text);
			
			defText2.setText(defWord.chinWord + " " + defWord.pinyin + " (" + defWord.diffLvl + ")");
			defText3.setText(defWord.engDef);
			//frame.invalidate();
			//frame.validate();
			frame.repaint();			
			
			
		}
		
	}
	
	class addActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = ((AbstractButton) e.getSource()).getText();
			if (text=="Add to Learning") {				
				try {
					onList = Instance.addToLearning(defWord); //TODO check if word on known (?? and remove from there?)
				} catch (IOException e1) {
					e1.printStackTrace();
				}		
				addWordNotification("learning");
				//notification.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}else if(text=="Add to Known") {				
				try {
					onList = Instance.addToKnown(defWord); 
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				addWordNotification("known");
				//notification.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		}		
	}
	
	class closeActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {				
			notification.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	class keyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			System.out.println("should be scrolling...");
			frame.invalidate();
			frame.validate();
			frame.repaint();
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			System.out.println("should be scrolling...");
			frame.invalidate();
			frame.validate();
			frame.repaint();
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			System.out.println("should be scrolling...");
			frame.invalidate();
			frame.validate();
			frame.repaint();
		}
		
	}
	
	public void MakeAWindow(){
		uploadFile = new JButton("Upload a File");
		uploadText = new JButton("Upload Text");
		readArticle = new JButton("Read Article");
		
		error = new JLabel("Sorry, that's coming later...");
		error.setFont(new Font("Arial", Font.PLAIN, 75));
		
		
		screen = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		screen.width = screen.width + 30;
		screen.height = screen.height - 80;
		
		
		frame = new JFrame("Let's Do this!");
		frame.setSize(screen);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocation(-15, 0);
		frame.validate();
		
		msg = new JLabel("Welcome to the Chinese Article Translator.");
		msg.setFont(new Font("Serif", Font.BOLD, 75));
		
		JLabel msg2 = new JLabel("Please choose an option below:");
		msg2.setFont(new Font("Serif", Font.ITALIC, 60));
		
		JLabel space = new JLabel(" ");
		space.setFont(new Font("Serif", Font.BOLD, 55));
		
		menu = new JPanel();
		menu.setLayout(new GridBagLayout());	//TODO add review flashcard option
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		uploadFile.setPreferredSize(new Dimension(350,110));
		uploadFile.setFont(new Font("Serif", Font.CENTER_BASELINE, 40));
		uploadFile.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
		uploadFile.setFocusable(false);
		menu.add(uploadFile, c);
		
		c.gridy = 1;
		JLabel space1 = new JLabel(" ");
		space1.setFont(new Font("Serif", Font.CENTER_BASELINE, 3));
		menu.add(space1, c);
		
		c.gridy = 2;
		uploadText.setPreferredSize(new Dimension(350,110));
		uploadText.setFont(new Font("Serif", Font.CENTER_BASELINE, 40));
		uploadText.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
		uploadText.setFocusable(false);
		menu.add(uploadText, c);
		
		c.gridy = 3;
		JLabel space2 = new JLabel(" ");
		space2.setFont(new Font("Serif", Font.CENTER_BASELINE, 3));
		menu.add(space2, c);
		
		c.gridy = 4;
		readArticle.setPreferredSize(new Dimension(350,110));
		readArticle.setFont(new Font("Serif", Font.CENTER_BASELINE, 40));
		readArticle.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
		readArticle.setFocusable(false);
		menu.add(readArticle, c);
		
		c.gridy = 5;
		JLabel space3 = new JLabel(" ");
		space3.setFont(new Font("Serif", Font.CENTER_BASELINE, 3));
		menu.add(space3, c);
		
		c.gridy = 6;
		exit = new JButton("Exit");
		exit.setPreferredSize(new Dimension(350,110));
		exit.setFont(new Font("Serif", Font.CENTER_BASELINE, 40));
		exit.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
		exit.setFocusable(false);
		menu.add(exit, c);
		
		full = new JPanel();		
		full.setLayout(new GridBagLayout());
  
        c.gridx = 0;
        c.gridy = 0;
        full.add(msg, c);
        
        c.gridy = 1;
        full.add(msg2, c);
        
        c.gridy = 2;
        full.add(space,c);
        
        c.gridy = 3;
        full.add(menu, c);
        
        uploadFile.addActionListener(new myActionListener());
		uploadText.addActionListener(new myActionListener());
		readArticle.addActionListener(new myActionListener());
		exit.addActionListener(new myActionListener());
        
		frame.add(full);
		
		frame.setVisible(true);
		
	}	
	
	public void unimplementedError() {
		frame = new JFrame("Sorry!");
		frame.setSize(2500, 200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		msg = new JLabel();
		msg.setFont(new Font("Serif", Font.PLAIN, 40));
		
		msg.setText("Thanks for selecting our 'upload a file' option. This is still in the process of being implemented. Please upload text for now.");
		
		
		close = new JButton("Close");
		close.setFont(new Font("Serif", Font.PLAIN, 40));
        close.addActionListener(new myActionListener());
		
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        
		frame.add(msg);
		frame.add(close);
		
		frame.setVisible(true);
	}
	
	public void uploadFileWindow() {
		frame.remove(full);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		
		Dimension inputDimension = new Dimension (600, 60);
		
		chinTitle = new JLabel("Chinese Title");
		chinTitleBox = new JTextField();
		chinTitle.setFont(new Font("Serif", Font.PLAIN, 50));
		chinTitleBox.setFont(new Font("Serif", Font.PLAIN, 40));
		chinTitleBox.setPreferredSize(inputDimension);
		
		engTitle = new JLabel("English Title");
		engTitleBox = new JTextField();
		engTitle.setFont(new Font("Serif", Font.PLAIN, 50));
		engTitleBox.setFont(new Font("Serif", Font.PLAIN, 40));
		engTitleBox.setPreferredSize(inputDimension);
		
		source = new JLabel("Source");
		sourceBox = new JTextField();
		source.setFont(new Font("Serif", Font.PLAIN, 50));
		sourceBox.setFont(new Font("Serif", Font.PLAIN, 40));
		sourceBox.setPreferredSize(inputDimension);
		
		text = new JLabel ("File Name (don't include .txt)"); //TODO make text wrap
		textBox = new JTextArea();
		text.setFont(new Font("Serif", Font.PLAIN, 50));
		textBox.setFont(new Font("Serif", Font.PLAIN, 40));
		textBox.setPreferredSize(inputDimension);
		
		inputChin = new JPanel();
		inputEng = new JPanel(); //make better interface!
		inputSource = new JPanel();
		inputText = new JPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		
		inputChin.setLayout(new GridBagLayout());
		inputChin.add(chinTitle, c);
		inputChin.add(chinTitleBox, c);
		
		inputEng.setLayout(new GridBagLayout());
		inputEng.add(engTitle, c);
		inputEng.add(engTitleBox, c);
		
		inputSource.setLayout(new GridBagLayout());
		inputSource.add(source, c);
		inputSource.add(sourceBox, c);
		
		inputText.setLayout(new GridBagLayout());
		inputText.add(text, c);
		inputText.add(textBox, c);
		
		inputFields = new JPanel();
		inputFields.setLayout(new GridBagLayout());
				
		c.gridx = 0;
		inputFields.add(inputChin, c);
		c.gridy = 1;
		inputFields.add(inputEng, c);
		c.gridy = 2;
		inputFields.add(inputSource, c);
		c.gridx = 0;
		c.gridy = 3;
		inputFields.add(inputText, c);

        enterFile = new JButton("Enter!");
        enterFile.setFont(new Font("Serif", Font.PLAIN, 80));
        enterFile.addActionListener(new myActionListener());
        
        //TODO make enter key work here!
        
        JPanel enterPanel = new JPanel(new GridBagLayout());        
        
        JLabel top = new JLabel(" ");
        top.setFont(new Font("Serif", Font.PLAIN, 80));
        JLabel bottom = new JLabel(" ");
        bottom.setFont(new Font("Serif", Font.PLAIN, 80));
        
        c.gridy = 0;
        c.gridx = 0;
        enterPanel.add(enterFile, c);
        c.gridy = 1;
        enterPanel.add(bottom, c);
        
        frame.add(inputFields);      
        frame.add(enterPanel);
        
        frame.validate();
        frame.repaint();
   
        //get input, have action lister for enter--ask user if ready, if yes pass all info to other program
        
	
	}
	
	public void uploadTextWindow() {
		frame.remove(full);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		
		Dimension inputDimension = new Dimension (600, 60);
		
		chinTitle = new JLabel("Chinese Title");
		chinTitleBox = new JTextField();
		chinTitle.setFont(new Font("Serif", Font.PLAIN, 50));
		chinTitleBox.setFont(new Font("Serif", Font.PLAIN, 40));
		chinTitleBox.setPreferredSize(inputDimension);
		
		engTitle = new JLabel("English Title");
		engTitleBox = new JTextField();
		engTitle.setFont(new Font("Serif", Font.PLAIN, 50));
		engTitleBox.setFont(new Font("Serif", Font.PLAIN, 40));
		engTitleBox.setPreferredSize(inputDimension);
		
		source = new JLabel("Source");
		sourceBox = new JTextField();
		source.setFont(new Font("Serif", Font.PLAIN, 50));
		sourceBox.setFont(new Font("Serif", Font.PLAIN, 40));
		sourceBox.setPreferredSize(inputDimension);		
		
		text = new JLabel ("Chinese Text"); //TODO make text wrap
		textBox = new JTextArea();
		textBox.setLineWrap(true);
		text.setFont(new Font("Serif", Font.PLAIN, 50));
		textBox.setFont(new Font("Serif", Font.PLAIN, 40));
		textBox.setPreferredSize(new Dimension(1000, 800));
		textBox.addKeyListener(new keyListener());
		
 
		JScrollPane scrollPane = new JScrollPane(textBox); //TODO make this work
		
		
		inputChin = new JPanel();
		inputEng = new JPanel(); //make better interface!
		inputSource = new JPanel();
		inputText = new JPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		
		inputChin.setLayout(new GridBagLayout());
		inputChin.add(chinTitle, c);
		inputChin.add(chinTitleBox, c);
		
		inputEng.setLayout(new GridBagLayout());
		inputEng.add(engTitle, c);
		inputEng.add(engTitleBox, c);
		
		inputSource.setLayout(new GridBagLayout());
		inputSource.add(source, c);
		inputSource.add(sourceBox, c);
		
		inputText.setLayout(new GridBagLayout());
		inputText.add(text, c);
		inputText.add(scrollPane, c);
		
		inputFields = new JPanel();
		inputFields.setLayout(new GridBagLayout());
				
		c.gridx = 0;
		inputFields.add(inputChin, c);
		c.gridy = 1;
		inputFields.add(inputEng, c);
		c.gridy = 2;
		inputFields.add(inputSource, c);
		c.gridx = 0;
		c.gridy = 3;
		inputFields.add(inputText, c);

        enter = new JButton("Enter!");
        enter.addActionListener(new myActionListener());
        enter.setPreferredSize(new Dimension(350,110));
        enter.setFont(new Font("Serif", Font.CENTER_BASELINE + Font.PLAIN, 60));
        //enter.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        
        //TODO make enter key work here!
        
        JPanel enterPanel = new JPanel(new GridBagLayout());        
        
        JLabel top = new JLabel(" ");
        top.setFont(new Font("Serif", Font.PLAIN, 50));
        JLabel bottom = new JLabel(" ");
        bottom.setFont(new Font("Serif", Font.PLAIN, 80));
        
        c.gridy = 0;
        c.gridx = 0;
        enterPanel.add(enter, c);
        c.gridy = 1;
        enterPanel.add(bottom, c);
        
        frame.add(inputFields);      
        frame.add(enterPanel);
        
        frame.validate();
        frame.repaint();
   
        //get input, have action lister for enter--ask user if ready, if yes pass all info to other program
        
	}
	
	public void thanksMessage(boolean success) {
		frame = new JFrame("Thanks!");
		frame.setSize(2500, 200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		msg = new JLabel();
		msg.setFont(new Font("Serif", Font.PLAIN, 40));
		
		if(success==true) {
			msg.setText("Thanks for submitting, the text is being processed now. Please return to the menu, and select 'Read Article' if you wish to read it.");
		}else {
			msg.setText("Sorry, nothing was entered. Please submit text next time!");
		}
		
		finished = new JButton("Finished!");
		finished.setFont(new Font("Serif", Font.PLAIN, 40));
        finished.addActionListener(new myActionListener());
		
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        
		frame.add(msg);
		frame.add(finished);
		
		frame.setVisible(true);
	}
	
	public void readArticle() throws IOException { //TODO make read in traditional option
		frame = new JFrame("Pick One");
		frame.setSize(screen);
		frame.setLocation(-15, 0);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		String[] artInfo = databaseReader();		
		
		articles = new JList<String>(artInfo); 
		articles.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		articles.setLayoutOrientation(JList.VERTICAL_WRAP);
		articles.setVisibleRowCount(-1);
		articles.setFont(new Font("Serif", Font.PLAIN, 40));
		articles.addListSelectionListener(new myListListener());
		
		JScrollPane articlesHolder = new JScrollPane(articles, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		articlesHolder.setPreferredSize(new Dimension(3000, 1500));
		
		finished = new JButton("Back");
		finished.setPreferredSize(new Dimension(350,110));
		finished.setFont(new Font("Serif", Font.CENTER_BASELINE + Font.BOLD, 40));
		finished.addActionListener(new myActionListener());
		
		full = new JPanel();		
		full.setLayout(new GridBagLayout());
  
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        full.add(articlesHolder, c);

        c.gridy = 1;
        
        full.add(finished, c);
		
		frame.add(full);		
		
		frame.setVisible(true);
		
	}
	
	public String[] databaseReader() throws IOException { //TODO rework article title display to allow commas
		BufferedReader in = new BufferedReader(new FileReader("C://Users//Dorot//OneDrive//Projects//EclipseWorkspace//ChineseArticleReader//src//textFiles//FinishedArticles//Directory.txt"));
		int num = 0;
		String tempy;
		List<String> temp = new ArrayList<String>();
		while((tempy=in.readLine())!=null) {
			temp.add(tempy);
			System.out.println("Added: " + temp.get(num));
			num = num + 1;
		}
		in.close();
		
		String artInfo[][] = new String[num][4];
		
		for (int i=0; i<num; i++) {
			tempy = temp.get(i);
			artInfo[i] = tempy.split(">");
			System.out.println("Added: " + artInfo[i][1]);
		}
		
		String[] articles = new String[num];
		
		for (int i=0; i<num; i++) {
			articles[i] = artInfo[i][0] + ", " + artInfo[i][1] + ", (" + artInfo[i][3] + ")";
		}
		
		return articles;
	}
	
	public void ReadArticleWindow() throws IOException {
		frame = new JFrame("Pick One");
		frame.setPreferredSize(screen);
		frame.setLocation(-15, 0);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		Instance = new MyProgram();	        
		List<String> artText = Instance.backToReadable(selected); 
		artMap = Instance.articleToMap(selected);
		
		JButton[] testy = new JButton[artText.size()];
		
		for (int i=0; i<artText.size(); i++) {
			testy[i] = new JButton(artText.get(i));
			testy[i].setFont(new Font("Serif", Font.BOLD, 55));
		}			
		
		JPanel artH = new JPanel(); //TODO add arrow keys, implement ability to copy text, ability to highlight sentence and translate
		artH.setLayout(new WrapLayout(WrapLayout.LEFT, 0, 5));
		
		for (int i=0; i<artText.size(); i++) {
			testy[i].setContentAreaFilled(false);
			testy[i].setVisible(true);
			testy[i].setMargin(new Insets(1,1,1,1));
			testy[i].setBorderPainted(false);
			testy[i].addActionListener(new wordActionListener());
			if (!testy[i].getText().contentEquals("\n")){
				artH.add(testy[i]);
			}else if (i<testy.length-1){
				if (!testy[i+1].getText().contentEquals("”")) {	
					
				}else {
					
				}
				//TODO add newline, or formatting
			}
		}	
		
		JLabel space = new JLabel(" ");
		space.setFont(new Font("Serif", Font.PLAIN, 70));
		
		defText1 = new JLabel ("中文 Text"); 
		defText1.setFont(new Font("Serif", Font.PLAIN, 40));
		
		defText2 = new JLabel ("Chinese and Difficulty Level Here"); 
		defText2.setFont(new Font("Serif", Font.PLAIN, 40));
		
		defText3 = new JLabel ("English Here"); 
		defText3.setFont(new Font("Serif", Font.PLAIN, 40));
		
		JLabel space1 = new JLabel(" ");
		space1.setFont(new Font("Serif", Font.PLAIN, 30));
		
		addKnown = new JButton("Add to Known");
		addKnown.setFont(new Font("Serif", Font.PLAIN, 40));
		addKnown.addActionListener(new addActionListener());
		
		addLearning = new JButton("Add to Learning");
		addLearning.setFont(new Font("Serif", Font.PLAIN, 40));	
		addLearning.addActionListener(new addActionListener());

        definition = new JPanel(new GridBagLayout());
        JPanel buttons = new JPanel(new GridBagLayout()); 
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0; 
        
        definition.add(space, c);
        c.gridy = 1;
        definition.add(defText1, c);
        c.gridy = 2;
        definition.add(defText2, c);
        c.gridy = 3;
        definition.add(defText3, c);        
        c.gridy = 0;
        c.gridx = 0;
        buttons.add(addKnown, c); 
        c.gridx = 1;
        buttons.add(addLearning, c);        
        c.gridx = 0;
        c.gridy = 4;
        definition.add(buttons, c);        
        c.gridx = 0;
        c.gridy = 5;
        definition.add(space1, c);	
        
        JLabel space2 = new JLabel(" ");
		space2.setFont(new Font("Serif", Font.PLAIN, 30));		
        
        finished = new JButton("Back");
		finished.setSize(new Dimension(350,350));
		finished.setFont(new Font("Serif", Font.PLAIN, 60));
		finished.addActionListener(new myActionListener());  
		
		JLabel space3 = new JLabel(" ");
		space3.setFont(new Font("Serif", Font.PLAIN, 70));
		
		JPanel finishedPanel = new JPanel(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		finishedPanel.add(space2, c);
		c.gridy = 1;
		finishedPanel.add(finished, c);
		c.gridy = 2;
		finishedPanel.add(space3, c);
		
		JPanel full = new JPanel(new BorderLayout());
		full.setSize(new Dimension(1000, 4000));
		
		JButton button = new JButton("test");
		button.setFont(new Font("Serif", Font.PLAIN, 40));
		
		frame.pack();
		
		JPanel right = new JPanel();
		right.setPreferredSize(new Dimension((frame.getContentPane().getSize().width)/20, (frame.getContentPane().getSize().height)));
		JPanel left = new JPanel();
		left.setPreferredSize(new Dimension((frame.getContentPane().getSize().width)/20, (frame.getContentPane().getSize().height)));
		
		UIManager.put("ScrollBar.width", (int) ((int) UIManager.get("ScrollBar.width")*3 )); 
		JScrollPane scrollPane = new JScrollPane(artH); 
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(11);
				
		UIManager.put("ScrollBar.width", (int) ((int) UIManager.get("ScrollBar.width")/3 ));
		
		frame.add(right, BorderLayout.EAST);
		frame.add(left, BorderLayout.WEST);
		full.add(scrollPane, BorderLayout.CENTER);
		frame.add(definition, BorderLayout.PAGE_START);
		frame.add(full, BorderLayout.CENTER);
		frame.add(finishedPanel, BorderLayout.PAGE_END);
		//frame.add(comp)
		frame.pack();
		frame.setVisible(true);		
		
	}
	
	public void addWordNotification(String list){
		notification = new JFrame(); 
		notification.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		notification.setResizable(false);
		notification.setLayout(new FlowLayout());
		notification.setUndecorated(true);
		//notification.setUndecorated(true);
		
		//JPanel textPanel = new JPanel();
		
		JLabel info = new JLabel();
		info.setFont(new Font("Serif", Font.PLAIN, 40));
		
		if (defWord==null) {
			info.setText("Please click a word first");
			System.out.println(info.getText());
		}else if (onList == true && list=="known") {
			info.setText(defWord.chinWord  + " is already on your Known list");
			System.out.println(info.getText());
		}else if (onList == true && list=="learning") {
			info.setText(defWord.chinWord  + " is already on your Learning list");
			System.out.println(info.getText());
		}else if(list=="known") {
			info.setText(defWord.chinWord + " added to your Known list.");
			System.out.println(info.getText());
		}else if(list=="learning") {
			info.setText(defWord.chinWord + " added to your Learning list.");
			System.out.println(info.getText());
		}
		
		notification.setLocation(3838/2, 398/2);
		notification.add(info);
		notification.pack();
		//notification.revalidate();
		notification.setVisible(true);
		waitAMo(5000);
	}

	public void waitAMo(int time) {
		wait = new Timer(time, new closeActionListener());
		wait.setInitialDelay(time);
		wait.start();
	}
}


