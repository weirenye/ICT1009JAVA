import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.io.IOException;

public class Userinterface {

	private JFrame frame;
	private JTable table;
	String[] array=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Userinterface window = new Userinterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		 String filePath = "C:\\Users\\gameb\\Downloads\\Assignment (1)\\Assignment\\latest.csv";
		  
		  System.out.println("starting write user.csv file: " + filePath);
		 // writeCsv(filePath);
		  
		  System.out.println("starting read user.csv file");
		  readCsv(filePath);
		
		 }
	
	
	
	
	
	
	
	
	

	/**
	 * Create the application.
	 */
	public Userinterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
  		final JPanel panel1 = new JPanel();
		frame = new JFrame();
  		JPanel panel2 = new JPanel();
		 panel2.setLayout( new BorderLayout() );
		 panel1.setLayout( new BorderLayout() );
		   JPanel subPanel = new JPanel();
		frame.setBounds(100,100, 350, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300,800);
        
 	   JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
 	   tabbedPane.setBounds(10,32,1000,1000);   
 	   frame.getContentPane().add(tabbedPane);
 	   tabbedPane.addTab("Main",null,panel2,null);
 	   tabbedPane.addTab("Twitter",null,panel1,null);
 	   JButton button2 = new JButton("Select File");
 	   panel1.add(button2, BorderLayout.NORTH);
        
        
        
        
        
        
      
       button2.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setBounds(0, 0, 704, 397);
			frame.getContentPane().add(fileChooser);
			if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
			String filePath= fileChooser.getSelectedFile().toString();
  		List<String> testing=readCsv(filePath);
  		table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        String header[] = new String[] { "Comments","Date","Time","Sentiment" };
        dtm.setColumnIdentifiers(header);
        table.setModel(dtm);
  		
  		
  		
  		
  		
  		
  		
  		
  		int i =0;
  	   array =testing.toArray(new String[testing.size()]);
		SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
		sentimentAnalyzer.initialize();


        for (int count = 1; count <= 30; count++) {
    		SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(array[count]);
        	
            dtm.addRow(new Object[] { array[count],"testing","Dummydata",sentimentResult.getSentimentType()
                    });
            

            
            
            
            
     }
        JScrollPane sPane = new JScrollPane();
        sPane.setBounds(20, 30, 1000, 393); // set the size of the table 
        //panel2.add(sPane);
        sPane.setViewportView(table);
        final TableColumnModel columnModel = table.getColumnModel();
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.getColumnModel().getColumn(0).setPreferredWidth(179); 
        table.setBounds(0, 0, 1000,1000);
        panel1.removeAll();
        panel1.add(sPane);
        panel1.validate();
        panel1.repaint();
        //tabbedPane.addTab("Updated",null,panel2,null);
        //frame.getContentPane().add(panel);
		 // frame.validate();
        //  frame.repaint();
  		
  		}
       	}

       });
       frame.getContentPane().setLayout(null);
      // frame.getContentPane().add(button2);
	  // frame.getContentPane().add(panel1,BorderLayout.SOUTH);
	
  
		
       frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	 
	 /*public static void writeCsv(String filePath) {
	  List<User> users = new ArrayList<User>();
	  
	  //create demo Users
	  User user = new User();
	  user.setId(1);
	  user.setFirstName("Jack");
	  user.setLastName("Rutorial 1");
	  users.add(user);
	  
	  user = new User();
	  user.setId(2);
	  user.setFirstName("Jack");
	  user.setLastName("Rutorial 2");
	  users.add(user);
	  
	  user = new User();
	  user.setId(3);
	  user.setFirstName("Jack");
	  user.setLastName("Rutorial 3");
	  users.add(user);
	  
	  FileWriter fileWriter = null;
	  try {
	   fileWriter = new FileWriter(filePath);
	   
	   fileWriter.append("Id, First Name, Last Name\n");
	   for(User u: users) {
	    fileWriter.append(String.valueOf(u.getId()));
	    fileWriter.append(",");
	    fileWriter.append(u.getFirstName());
	    fileWriter.append(",");
	    fileWriter.append(u.getLastName());
	    fileWriter.append("\n");
	   }
	  } catch (Exception ex) {
	   ex.printStackTrace();
	  } finally {
	   try {
	    fileWriter.flush();
	    fileWriter.close();
	   } catch (Exception e) {
	    e.printStackTrace();
	   }
	  }
	 }*/
	 
	 public static List<String> readCsv(String filePath) {
	  BufferedReader reader = null;
	  List <String> arrlist=new ArrayList<String>();
	  try {
	   List<User> users = new ArrayList<User>();
	   String line = "";
	   reader = new BufferedReader(new FileReader(filePath));
	   reader.readLine();
	   
	   while((line = reader.readLine()) != null) {
		  // String[] fields = line.split(",");
	     arrlist.add(line);
	    
	  //  if(fields.length > 0) {
	    // User user = new User();
	    // user.setId(Integer.parseInt(fields[0]));
	   //  user.setFirstName(fields[1]);
	   //  user.setLastName(fields[2]);
	  //   users.add(user);
	   
	   // }
	   }
	   
	  /* for(User u: users) {
	    System.out.printf("[userId=%d, firstName=%s, lastName=%s]\n", u.getId(), u.getFirstName(), u.getLastName());
	   }*/
	   
	  } catch (Exception ex) {
	   ex.printStackTrace();
	  } finally {
	   try {
	    reader.close();
	   } catch (Exception e) {
	    e.printStackTrace();
	   }
	  }
   return arrlist;
	 }
}
