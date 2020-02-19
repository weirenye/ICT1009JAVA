import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class test {  
public static void main(String[] args) {  
	JTable table;
    JFrame f=new JFrame("WELCOME TO 1009");   
    
    
    // TABLE FUNCTION TO VIEW COMMENTS FROM THE FILE 
  JPanel panel = new JPanel();
  panel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "OOP PROJECT", TitledBorder.CENTER, TitledBorder.TOP));
  final JTable table1 = new JTable();
  panel.add(new JScrollPane(table1));
  JScrollPane sPane = new JScrollPane();
  sPane.setBounds(20, 30, 1000, 393); // set the size of the table 
  f.add(sPane);
  sPane.setViewportView(table1);
  final TableColumnModel columnModel = table1.getColumnModel();
  f.setSize(1050, 500);
  
  JButton buttonfile=new JButton("Choose File");  
  buttonfile.setBounds(350,475,200,30);  
  f.add(buttonfile);
  buttonfile.addActionListener(new ActionListener(){  
  	public void actionPerformed(ActionEvent e){  
  		JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
        }
       FileReader selectedFile;
	try {
		selectedFile = new FileReader(fileChooser.getSelectedFile().getPath());
		DefaultTableModel model = (DefaultTableModel)table1.getModel();
		model.setRowCount(0);
		BufferedReader br = new BufferedReader(selectedFile);
		String firstLine = br.readLine().trim();
	      String[] columnsName = firstLine.split(",");
	      
	      model.setColumnIdentifiers(columnsName);
	      
	      // get lines from txt or csv file
	      Object[] tableLines = br.lines().toArray();
	      
	      // extratct data from lines
	      // set data to jtable model
	      for(int i = 0; i < tableLines.length; i++)
	      {
	          String line = tableLines[i].toString().trim();
	          String[] dataRow = line.split(",");
	          model.addRow(dataRow);
	          columnModel.getColumn(2).setPreferredWidth(550);
	      }
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
      }  
  });  
    
    f.setSize(1450,800);  //change the size to 700/700
    f.setLayout(null);  
    f.setVisible(true);   
}  

}  