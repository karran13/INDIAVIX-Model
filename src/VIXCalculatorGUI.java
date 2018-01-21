
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VIXCalculatorGUI {

	private String DEFAULT_DATA_PATH="C:\\Users\\Binki\\Documents\\Stock Markets and Trading\\VIX\\Data";
	private JFrame frmVolatilityCalculator;
	private JTextField textField_1;
	private JTextField textField;
	private JLabel lblVolatility;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;

	/**
	 * Launch the application.
	 * You have to enter a time and proper Data folder
	 * Format is specified in VolCalculations class
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VIXCalculatorGUI window = new VIXCalculatorGUI();
					window.frmVolatilityCalculator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VIXCalculatorGUI() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVolatilityCalculator = new JFrame();
		frmVolatilityCalculator.setTitle("Volatility Calculator");
		frmVolatilityCalculator.setBounds(100, 100, 550, 300);
		frmVolatilityCalculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVolatilityCalculator.getContentPane().setLayout(null);

		textField_1 = new JTextField();
		textField_1.setBounds(37, 49, 349, 20);
		frmVolatilityCalculator.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		textField_1.setText(DEFAULT_DATA_PATH);
		

		JButton btnNewButton = new JButton("Run"); //add action perform
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				VolatilityDriver d = new VolatilityDriver();
				Double d1 = d.drive(textField_1.getText(),Integer.parseInt(textField_2.getText()),Integer.parseInt(textField_3.getText()),Integer.parseInt(textField_4.getText()));
				String f = new Double(d1).toString();
				textField.setText(f);
				textField.setHorizontalAlignment(SwingConstants.LEFT);

			}
		});
		btnNewButton.setBounds(183, 92, 106, 31);
		frmVolatilityCalculator.getContentPane().add(btnNewButton);
		
				JButton btnBrowse = new JButton("Browse");
				btnBrowse.setBounds(406, 49, 88, 21);
				frmVolatilityCalculator.getContentPane().add(btnBrowse);
				
				textField = new JTextField();
				textField.setEditable(false);
				textField.setBounds(37, 180, 219, 20);
				frmVolatilityCalculator.getContentPane().add(textField);
				textField.setColumns(10);
				
				JLabel lblInputPathFor = new JLabel("Input Path for Options Data");
				lblInputPathFor.setBounds(37, 24, 158, 18);
				frmVolatilityCalculator.getContentPane().add(lblInputPathFor);
				
				lblVolatility = new JLabel("Volatility");
				lblVolatility.setBounds(37, 148, 60, 28);
				frmVolatilityCalculator.getContentPane().add(lblVolatility);
				
				textField_2 = new JTextField();
				textField_2.setText("0");
				textField_2.setBounds(406, 124, 86, 20);
				frmVolatilityCalculator.getContentPane().add(textField_2);
				textField_2.setColumns(10);
				
				textField_3 = new JTextField();
				textField_3.setText("0");
				textField_3.setBounds(406, 180, 86, 20);
				frmVolatilityCalculator.getContentPane().add(textField_3);
				textField_3.setColumns(10);
				
				textField_4 = new JTextField();
				textField_4.setText("0");
				textField_4.setBounds(406, 230, 86, 20);
				frmVolatilityCalculator.getContentPane().add(textField_4);
				textField_4.setColumns(10);
				
				JLabel lblHours = new JLabel("Hours");
				lblHours.setBounds(406, 99, 46, 14);
				frmVolatilityCalculator.getContentPane().add(lblHours);
				
				JLabel lblMinutes = new JLabel("Minutes");
				lblMinutes.setBounds(406, 155, 46, 14);
				frmVolatilityCalculator.getContentPane().add(lblMinutes);
				
				JLabel lblSeconds = new JLabel("Seconds");
				lblSeconds.setBounds(406, 211, 88, 14);
				frmVolatilityCalculator.getContentPane().add(lblSeconds);
				
				JLabel lblSetTime = new JLabel("Set time");
				lblSetTime.setBounds(310, 127, 76, 14);
				frmVolatilityCalculator.getContentPane().add(lblSetTime);

				btnBrowse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser j = new JFileChooser();
						j.setFileSelectionMode(2);
						int retval = j.showOpenDialog(null);
						File file = j.getSelectedFile();
						if(file != null)
							textField_1.setText(file.getPath());
						textField_1.setHorizontalAlignment(SwingConstants.LEFT);
					
					}
				});
	}
}
