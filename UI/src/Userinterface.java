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
  		JPanel panel1 = new JPanel();
		 panel1.setLayout( new BorderLayout() );
		   JPanel subPanel = new JPanel();
		frame.setBounds(100,100, 550, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500,8000);
       JButton button2 = new JButton("Select File");
       button2.setBounds(12, 348, 100, 100);
       button2.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setBounds(0, 0, 704, 397);
			frame.getContentPane().add(fileChooser);
			if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
			String filePath= fileChooser.getSelectedFile().toString();
       	// String filePath = "C:\\Users\\gameb\\Downloads\\Assignment (1)\\Assignment\\latest.csv";
  		List<String> testing=readCsv(filePath);
  		
  		table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        String header[] = new String[] { "Prority" };
        dtm.setColumnIdentifiers(header);
        table.setModel(dtm);
  		
  		
  		
  		
  		
  		
  		
  		
  		int i =0;
  	   array =testing.toArray(new String[testing.size()]);
  		
  		for(i =0; i < 10;i++) {
  			 JLabel jlabels[] = new JLabel[10];
  			 jlabels[i]=new JLabel(array[i]+"/n");
  			 System.out.println(array[i]);
  		    //We create a sub-panel. Notice, that we don't use any layout-manager,
  		    //Because we want it to use the default FlowLayout	
  		    subPanel.add(jlabels[i]);
  		    //Now we simply add it to your main panel.
  		    panel1.add(subPanel); 
  		  frame.getContentPane().add(panel1);
  		  //panel1.setBounds(0, 0,1000, 1000);
  		   // frame.getContentPane().add(new JLabel(array[i]));

  			//label.setText(array[i]);

  			//System.out.println(array[i]);
  			
  		}
        for (int count = 1; count <= 30; count++) {
            dtm.addRow(new Object[] { array[count]
                    });
     }
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.getColumnModel().getColumn(0).setPreferredWidth(179); 
        table.setBounds(0, 0, 1400,1200);
        frame.getContentPane().add(table);
		  frame.validate();
          frame.repaint();
  		
  		}
       	}

       });
       frame.getContentPane().setLayout(null);
       

       frame.getContentPane().add(button2);
	   frame.getContentPane().add(panel1,BorderLayout.SOUTH);
  
		
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
