import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class studentEnrollmentSystem {

	// update window

	// dashboard ni siya
	public static class signUpSwap extends JFrame {

		public static JTable table;
		public static DefaultTableModel tableModel;// decalring a table model
		public static final String FILE = "student.text";
		public static final String SPLIT = "#";
		public int selectedRow;

		public signUpSwap() {

			setTitle("Dashboard");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(1366, 768);
			setLocationRelativeTo(null);
			setLayout(null);

			// top
			JPanel topPanel = new JPanel();
			topPanel.setLayout(null);
			topPanel.setBounds(0, 0, 1366, 113);
			topPanel.setBackground(new Color(248, 124, 248));
			add(topPanel);

			ImageIcon imageIcon = new ImageIcon("C:\\Users\\jonat\\images\\COLLEGE OF.png"); // declaring the image
			Image image = imageIcon.getImage();// getting
			Image resizedImage = image.getScaledInstance(180, 180, Image.SCALE_SMOOTH);// giving a size
			ImageIcon resizedImageIcon = new ImageIcon(resizedImage);// resized image

			// pagbutang nag image
			JLabel label = new JLabel(resizedImageIcon);
			label.setBounds(4, 6, 100, 100);
			setBackground(new Color(248, 124, 248));
			topPanel.add(label);// adddded to the topPAnl

			JLabel searchLabel = new JLabel("Search:");
			searchLabel.setFont(new Font("Aptos", Font.BOLD, 30));
			searchLabel.setForeground(Color.white);
			searchLabel.setBounds(260, 25, 150, 50);
			topPanel.add(searchLabel);

			JTextField searchField = new JTextField();
			searchField.setFont(new Font("Aptos", Font.PLAIN, 18));
			searchField.setBounds(433, 25, 500, 50);
			topPanel.add(searchField);

			JLabel tableLabel = new JLabel("Student Information");
			tableLabel.setFont(new Font("Aptos", Font.BOLD, 24));
			tableLabel.setBounds(50, 130, 300, 30);
			add(tableLabel);

			// table nani
			String[] row = { "Full Name", "Age", "Gender", "Program", "Address", "Contact Number", "Email", "Birthdate",
					"Guardian Name", "Guardian Contact", "Prev School", "GWA" };

			tableModel = new DefaultTableModel(row, 0);

			table = new JTable(tableModel);
			table.setFont(new Font("Aptos", Font.PLAIN, 14));
			table.setRowHeight(30);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getTableHeader().setReorderingAllowed(false);
			loadTab();// method call

			// for table
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(50, 170, 1250, 400);
			add(scrollPane);

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(null);
			buttonPanel.setBounds(50, 600, 1250, 100);
			add(buttonPanel);

			// JButton view = new JButton("View");
			// view.setBounds(50, 20, 150, 50);
			// view.setFont(new Font("Aptos", Font.BOLD, 18));
			// view.setBackground(new Color(255, 180, 200));
			// buttonPanel.add(view);

			JButton add = new JButton("Add");
			add.setBounds(250, 20, 150, 50);
			add.setFont(new Font("Aptos", Font.BOLD, 18));
			add.setBackground(new Color(255, 180, 200));
			buttonPanel.add(add);

			JButton update = new JButton("Update");
			update.setBounds(450, 20, 150, 50);
			update.setFont(new Font("Aptos", Font.BOLD, 18));
			update.setBackground(new Color(255, 180, 200));
			buttonPanel.add(update);

			JButton delete = new JButton("Delete");
			delete.setBounds(650, 20, 150, 50);
			delete.setFont(new Font("Aptos", Font.BOLD, 18));
			delete.setBackground(new Color(255, 180, 200));
			buttonPanel.add(delete);

			JButton exit = new JButton("Exit");
			exit.setBounds(850, 20, 150, 50);
			exit.setFont(new Font("Aptos", Font.BOLD, 18));
			exit.setBackground(new Color(255, 100, 100));
			buttonPanel.add(exit);

			add.addActionListener(e -> new dashboard());

			update.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						onUpdateData(false); // pass false since we're updating dili mag add
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null, "Error updating data: " + ex.getMessage());
					}
				}
			});

			// used for selecting the row
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					selectedRow = table.getSelectedRow();
				}
			});

			// exit
			exit.addActionListener(e -> System.exit(0));

			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int selectedRow = table.getSelectedRow();

					if (selectedRow == -1) {
						JOptionPane.showMessageDialog(null, "Please select a row to delete");
						return;
					}

					int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?",
							"Confirm Delete", JOptionPane.YES_NO_OPTION);

					if (choice == JOptionPane.YES_OPTION) {
						tableModel.removeRow(selectedRow);

						try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
							for (int i = 0; i < tableModel.getRowCount(); i++) {
								StringBuilder line = new StringBuilder();
								for (int j = 0; j < tableModel.getColumnCount(); j++) {
									line.append(tableModel.getValueAt(i, j));
									if (j < tableModel.getColumnCount() - 1) {
										line.append(SPLIT);
									}
								}
								writer.write(line.toString());
								writer.newLine();
							}
							JOptionPane.showMessageDialog(null, "Record deleted successfully!");
						} catch (IOException ex) {
							JOptionPane.showMessageDialog(null, "Error saving changes: " + ex.getMessage());
						}
					}
				}
			});

			setVisible(true);
		}

		private void loadTab() {
			try {
				List<String> lines = Files.readAllLines(Paths.get(FILE));
				for (String line : lines) {
					String[] rowData = line.split(SPLIT);
					if (rowData.length == 12) {
						tableModel.addRow(rowData);// adding all the data sa tableModel--the loadTab now has a value
					} else {
						System.err.println("Skipping malformed line: " + line);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void onUpdateData(boolean adding) throws IOException {
			if (table.getSelectedRow() < 0 && !adding) {
				JOptionPane.showMessageDialog(null, "Please select a row to continue!");
				return;
			}

			update updateForm = new update();

			if (!adding) {
				int selectedRow = table.getSelectedRow();
				//to place each field with the corresponding table data
				updateForm.nameField.setText((String) table.getValueAt(selectedRow, 0));
				updateForm.ageField.setText((String) table.getValueAt(selectedRow, 1));
				updateForm.genderBox.setSelectedItem((String) table.getValueAt(selectedRow, 2));
				updateForm.programField.setText((String) table.getValueAt(selectedRow, 3));
				updateForm.addressField.setText((String) table.getValueAt(selectedRow, 4));
				updateForm.contactField.setText((String) table.getValueAt(selectedRow, 5));
				updateForm.emailField.setText((String) table.getValueAt(selectedRow, 6));
				updateForm.birthdateField.setText((String) table.getValueAt(selectedRow, 7));
				updateForm.guardianNameField.setText((String) table.getValueAt(selectedRow, 8));
				updateForm.guardianContactField.setText((String) table.getValueAt(selectedRow, 9));
				updateForm.prevSchoolField.setText((String) table.getValueAt(selectedRow, 10));
				updateForm.gwaField.setText((String) table.getValueAt(selectedRow, 11));
			}
		}

	}

	// the update window
	public static class update extends JFrame {

		public JTextField nameField, ageField, programField, addressField, contactField, emailField, birthdateField,
				guardianNameField, guardianContactField, prevSchoolField, gwaField;
		public JComboBox<String> genderBox;

		public static String SPLIT = "#";

		public update() {

			setTitle("Update");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setSize(700, 600);
			setLocationRelativeTo(null);
			setLayout(null);

			// Full Name
			JLabel name = new JLabel("Full Name");
			name.setFont(new Font("Aptos", Font.PLAIN, 12));
			name.setBounds(20, 20, 150, 30);
			add(name);

			nameField = new JTextField();
			nameField.setBounds(180, 20, 480, 30);
			add(nameField);

			// Age
			JLabel age = new JLabel("Age");
			age.setFont(new Font("Aptos", Font.PLAIN, 12));
			age.setBounds(20, 60, 150, 30);
			add(age);

			ageField = new JTextField();
			ageField.setBounds(180, 60, 100, 30);
			add(ageField);

			// Gender (Combo Box)
			JLabel gender = new JLabel("Gender");
			gender.setFont(new Font("Aptos", Font.PLAIN, 12));
			gender.setBounds(300, 60, 150, 30);
			add(gender);

			String[] genders = { "Male", "Female", "Other" };
			genderBox = new JComboBox<>(genders);
			genderBox.setBounds(360, 60, 150, 30);
			add(genderBox);

			// Program
			JLabel program = new JLabel("Program");
			program.setFont(new Font("Aptos", Font.PLAIN, 12));
			program.setBounds(20, 100, 150, 30);
			add(program);

			programField = new JTextField();
			programField.setBounds(180, 100, 480, 30);
			add(programField);

			// Address
			JLabel address = new JLabel("Address");
			address.setFont(new Font("Aptos", Font.PLAIN, 12));
			address.setBounds(20, 140, 150, 30);
			add(address);

			addressField = new JTextField();
			addressField.setBounds(180, 140, 480, 30);
			add(addressField);

			// Contact Number
			JLabel contact = new JLabel("Contact Number");
			contact.setFont(new Font("Aptos", Font.PLAIN, 12));
			contact.setBounds(20, 180, 150, 30);
			add(contact);

			contactField = new JTextField();
			contactField.setBounds(180, 180, 200, 30);
			add(contactField);

			// Email
			JLabel email = new JLabel("Email");
			email.setFont(new Font("Aptos", Font.PLAIN, 12));
			email.setBounds(400, 180, 150, 30);
			add(email);

			emailField = new JTextField();
			emailField.setBounds(460, 180, 200, 30);
			add(emailField);

			// Birthdate
			JLabel birthdate = new JLabel("Birthdate");
			birthdate.setFont(new Font("Aptos", Font.PLAIN, 12));
			birthdate.setBounds(20, 220, 150, 30);
			add(birthdate);

			birthdateField = new JTextField();
			birthdateField.setBounds(180, 220, 200, 30);
			add(birthdateField);

			// Guardian Name
			JLabel guardianName = new JLabel("Guardian Name");
			guardianName.setFont(new Font("Aptos", Font.PLAIN, 12));
			guardianName.setBounds(20, 260, 150, 30);
			add(guardianName);

			guardianNameField = new JTextField();
			guardianNameField.setBounds(180, 260, 480, 30);
			add(guardianNameField);

			// Guardian Contact
			JLabel guardianContact = new JLabel("Guardian Contact");
			guardianContact.setFont(new Font("Aptos", Font.PLAIN, 12));
			guardianContact.setBounds(20, 300, 150, 30);
			add(guardianContact);

			guardianContactField = new JTextField();
			guardianContactField.setBounds(180, 300, 200, 30);
			add(guardianContactField);

			// Previous School
			JLabel prevSchool = new JLabel("Previous School");
			prevSchool.setFont(new Font("Aptos", Font.PLAIN, 12));
			prevSchool.setBounds(20, 340, 150, 30);
			add(prevSchool);

			prevSchoolField = new JTextField();
			prevSchoolField.setBounds(180, 340, 480, 30);
			add(prevSchoolField);

			// GWA
			JLabel gwa = new JLabel("GWA");
			gwa.setFont(new Font("Aptos", Font.PLAIN, 12));
			gwa.setBounds(20, 380, 150, 30);
			add(gwa);

			gwaField = new JTextField();
			gwaField.setBounds(180, 380, 100, 30);
			add(gwaField);

			// cancel
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setBounds(210, 430, 120, 40);
			add(cancelButton);

			cancelButton.addActionListener(e -> dispose());

			// Submit Button
			JButton submitButton = new JButton("Update");
			submitButton.setBounds(360, 430, 120, 40);
			add(submitButton);

			submitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String fullName = nameField.getText();
					String age = ageField.getText();
					String gender = (String) genderBox.getSelectedItem();
					String program = programField.getText();
					String address = addressField.getText();
					String contact = contactField.getText();
					String email = emailField.getText();
					String birthday = birthdateField.getText();
					String parentName = guardianNameField.getText();
					String parentContact = guardianContactField.getText();
					String prevSchool = prevSchoolField.getText();
					String gwa = gwaField.getText();

					int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to update?", "Confirmation",
							JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.YES_OPTION) {
						try {
							// Get the selected row from the main table
							int selectedRow = signUpSwap.table.getSelectedRow();

							// Update the table model
							signUpSwap.tableModel.setValueAt(fullName, selectedRow, 0);
							signUpSwap.tableModel.setValueAt(age, selectedRow, 1);
							signUpSwap.tableModel.setValueAt(gender, selectedRow, 2);
							signUpSwap.tableModel.setValueAt(program, selectedRow, 3);
							signUpSwap.tableModel.setValueAt(address, selectedRow, 4);
							signUpSwap.tableModel.setValueAt(contact, selectedRow, 5);
							signUpSwap.tableModel.setValueAt(email, selectedRow, 6);
							signUpSwap.tableModel.setValueAt(birthday, selectedRow, 7);
							signUpSwap.tableModel.setValueAt(parentName, selectedRow, 8);
							signUpSwap.tableModel.setValueAt(parentContact, selectedRow, 9);
							signUpSwap.tableModel.setValueAt(prevSchool, selectedRow, 10);
							signUpSwap.tableModel.setValueAt(gwa, selectedRow, 11);

							// update the file
							try (BufferedWriter writer = new BufferedWriter(new FileWriter(signUpSwap.FILE))) {
								for (int i = 0; i < signUpSwap.tableModel.getRowCount(); i++) {
									StringBuilder line = new StringBuilder();
									for (int j = 0; j < signUpSwap.tableModel.getColumnCount(); j++) {
										line.append(signUpSwap.tableModel.getValueAt(i, j));
										if (j < signUpSwap.tableModel.getColumnCount() - 1) {
											line.append(SPLIT);
										}
									}
									writer.write(line.toString());
									writer.newLine();
								}
							}

							JOptionPane.showMessageDialog(null, "Record updated successfully!");
							dispose();
						} catch (IOException ex) {
							JOptionPane.showMessageDialog(null, "Error updating record: " + ex.getMessage());
						}
					} else {
						JOptionPane.showMessageDialog(null, "Update cancelled");
						dispose();
					}
				}
			});

			setVisible(true);
		}
	}

	// this class kay register ni siya dashboard lang pangalan
	public static class dashboard extends JFrame {

		private static final String FILE = "student.text";
		private static final String SPLIT = "#";

		public dashboard() {

			setTitle("Register");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(1366, 768);
			setLocationRelativeTo(null);
			setLayout(null);

			JLabel title = new JLabel();
			title.setFont(new Font("Aptos", Font.BOLD, 30));
			title.setText("<html>College of Millan Enrollment System<html>");
			title.setForeground(new Color(255, 253, 233));
			title.setBounds(417, 15, 532, 50);
			add(title);

			JLabel subTitle = new JLabel();
			subTitle.setFont(new Font("Aptos", Font.BOLD, 20));
			subTitle.setText("Student Information");
			subTitle.setForeground(new Color(255, 253, 233));
			subTitle.setBounds(563, 60, 532, 50);
			add(subTitle);

			JPanel top = new JPanel();
			top.setLayout(null);
			top.setBounds(0, 0, 1366, 113);
			top.setBackground(new Color(248, 124, 248));
			add(top);

			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(null);
			leftPanel.setBackground(new Color(248, 182, 248));
			leftPanel.setBounds(30, 173, 405, 500);
			add(leftPanel);

			Border border = BorderFactory.createLineBorder(Color.white, 2);

			JLabel studentName = new JLabel();
			studentName.setLayout(null);
			studentName.setText("Full Name");
			studentName.setFont(new Font("Aptos", Font.PLAIN, 13));
			studentName.setBounds(20, 0, 200, 50);
			leftPanel.add(studentName);

			JTextField nameField = new JTextField();
			nameField.setLayout(null);
			nameField.setBounds(20, 40, 350, 40);
			nameField.setForeground(Color.black);
			nameField.setBackground(new Color(255, 222, 249));
			nameField.setBorder(border);
			leftPanel.add(nameField);

			JLabel age = new JLabel();
			age.setLayout(null);
			age.setText("Age");
			age.setFont(new Font("Aptos", Font.PLAIN, 13));
			age.setBounds(20, 80, 200, 50);
			leftPanel.add(age);

			JTextField ageField = new JTextField();
			ageField.setLayout(null);
			ageField.setBounds(20, 120, 150, 40);
			ageField.setForeground(Color.black);
			ageField.setBackground(new Color(255, 222, 249));
			ageField.setBorder(border);
			leftPanel.add(ageField);

			String[] gender = { "Male", "Female", "Others" };

			JLabel textGender = new JLabel();
			textGender.setLayout(null);
			textGender.setText("Gender");
			textGender.setFont(new Font("Aptos", Font.PLAIN, 13));
			textGender.setBounds(215, 80, 150, 50);
			leftPanel.add(textGender);

			JComboBox<String> genderOpt = new JComboBox<String>();
			genderOpt.setModel(new DefaultComboBoxModel<>(gender));
			genderOpt.setFont(new Font("Aptos", Font.PLAIN, 13));
			genderOpt.setBounds(215, 120, 150, 40);
			genderOpt.setForeground(Color.black);
			genderOpt.setBackground(new Color(255, 222, 249));
			genderOpt.setBorder(border);
			leftPanel.add(genderOpt);

			JLabel program = new JLabel();
			program.setLayout(null);
			program.setText("Program");
			program.setFont(new Font("Aptos", Font.PLAIN, 13));
			program.setBounds(20, 160, 200, 50);
			leftPanel.add(program);

			JTextField programField = new JTextField();
			programField.setLayout(null);
			programField.setBounds(20, 200, 350, 40);
			programField.setForeground(Color.black);
			programField.setBackground(new Color(255, 222, 249));
			programField.setBorder(border);
			leftPanel.add(programField);

			// address
			JLabel address = new JLabel();
			address.setLayout(null);
			address.setText("Address");
			address.setFont(new Font("Aptos", Font.PLAIN, 13));
			address.setBounds(20, 240, 200, 50);
			leftPanel.add(address);
			// textfield address
			JTextField addressField = new JTextField();
			addressField.setLayout(null);
			addressField.setBounds(20, 280, 350, 40);
			addressField.setForeground(Color.black);
			addressField.setBackground(new Color(255, 222, 249));
			addressField.setBorder(border);
			leftPanel.add(addressField);

			// contact
			JLabel contact = new JLabel();
			contact.setLayout(null);
			contact.setText("Contact Number");
			contact.setFont(new Font("Aptos", Font.PLAIN, 13));
			contact.setBounds(20, 320, 200, 50);
			leftPanel.add(contact);
			// textfiels contact
			JTextField contactField = new JTextField();
			contactField.setLayout(null);
			contactField.setBounds(20, 360, 350, 40);
			contactField.setForeground(Color.black);
			contactField.setBackground(new Color(255, 222, 249));
			contactField.setBorder(border);
			leftPanel.add(contactField);

			// email
			JLabel email = new JLabel();
			email.setLayout(null);
			email.setText("Email");
			email.setFont(new Font("Aptos", Font.PLAIN, 13));
			email.setBounds(20, 400, 200, 50);
			leftPanel.add(email);
			// textfriel email
			JTextField emailField = new JTextField();
			emailField.setLayout(null);
			emailField.setBounds(20, 440, 350, 40);
			emailField.setForeground(Color.black);
			emailField.setBackground(new Color(255, 222, 249));
			emailField.setBorder(border);
			leftPanel.add(emailField);

			// middle

			JPanel middlePanel = new JPanel();
			middlePanel.setLayout(null);
			middlePanel.setBackground(new Color(248, 182, 248));
			middlePanel.setBounds(465, 173, 405, 255);
			add(middlePanel);

			// Date of Birth
			JLabel dobLabel = new JLabel();
			dobLabel.setLayout(null);
			dobLabel.setText("Date of Birth");
			dobLabel.setFont(new Font("Aptos", Font.PLAIN, 13));
			dobLabel.setBounds(20, 0, 200, 50);
			middlePanel.add(dobLabel);

			JTextField dobField = new JTextField();
			dobField.setLayout(null);
			dobField.setBounds(20, 40, 350, 40);
			dobField.setForeground(Color.black);
			dobField.setBackground(new Color(255, 222, 249));
			dobField.setBorder(border);
			middlePanel.add(dobField);

			// Parent/Guardian Name
			JLabel guardianLabel = new JLabel();
			guardianLabel.setLayout(null);
			guardianLabel.setText("Parent/Guardian Name");
			guardianLabel.setFont(new Font("Aptos", Font.PLAIN, 13));
			guardianLabel.setBounds(20, 80, 200, 50);
			middlePanel.add(guardianLabel);

			JTextField guardianField = new JTextField();
			guardianField.setLayout(null);
			guardianField.setBounds(20, 120, 350, 40);
			guardianField.setForeground(Color.black);
			guardianField.setBackground(new Color(255, 222, 249));
			guardianField.setBorder(border);
			middlePanel.add(guardianField);

			// Parent/Guardian Contact Number
			JLabel guardianContactLabel = new JLabel();
			guardianContactLabel.setLayout(null);
			guardianContactLabel.setText("Parent/Guardian Contact Number");
			guardianContactLabel.setFont(new Font("Aptos", Font.PLAIN, 13));
			guardianContactLabel.setBounds(20, 160, 250, 50);
			middlePanel.add(guardianContactLabel);

			JTextField guardianContactField = new JTextField();
			guardianContactField.setLayout(null);
			guardianContactField.setBounds(20, 200, 350, 40);
			guardianContactField.setForeground(Color.black);
			guardianContactField.setBackground(new Color(255, 222, 249));
			guardianContactField.setBorder(border);
			middlePanel.add(guardianContactField);

			// right

			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(null);
			rightPanel.setBackground(new Color(248, 182, 248));
			rightPanel.setBounds(900, 173, 405, 255);
			add(rightPanel);

			// academic Information Title
			JLabel academicTitle = new JLabel();
			academicTitle.setLayout(null);
			academicTitle.setText("Academic Information");
			academicTitle.setFont(new Font("Aptos", Font.BOLD, 15));
			academicTitle.setBounds(20, 0, 250, 50);
			rightPanel.add(academicTitle);

			// previous School Attended
			JLabel schoolLabel = new JLabel();
			schoolLabel.setLayout(null);
			schoolLabel.setText("Previous School Attended");
			schoolLabel.setFont(new Font("Aptos", Font.PLAIN, 13));
			schoolLabel.setBounds(20, 40, 250, 50);
			rightPanel.add(schoolLabel);

			JTextField schoolField = new JTextField();
			schoolField.setLayout(null);
			schoolField.setBounds(20, 80, 350, 40);
			schoolField.setForeground(Color.black);
			schoolField.setBackground(new Color(255, 222, 249));
			schoolField.setBorder(border);
			rightPanel.add(schoolField);

			// GWA
			JLabel gwaLabel = new JLabel();
			gwaLabel.setLayout(null);
			gwaLabel.setText("General Weighted Average (GWA)");
			gwaLabel.setFont(new Font("Aptos", Font.PLAIN, 13));
			gwaLabel.setBounds(20, 130, 250, 50);
			rightPanel.add(gwaLabel);

			JTextField gwaField = new JTextField();
			gwaField.setLayout(null);
			gwaField.setBounds(20, 170, 350, 40);
			gwaField.setForeground(Color.black);
			gwaField.setBackground(new Color(255, 222, 249));
			gwaField.setBorder(border);
			rightPanel.add(gwaField);

			JButton cancel = new JButton();
			cancel.setLayout(null);
			cancel.setBounds(1045, 530, 100, 40);
			cancel.setText("Cancel");
			cancel.setFont(new Font("Aptos", Font.BOLD, 14));
			cancel.setBackground(new Color(255, 180, 200));
			add(cancel);

			JButton proceed = new JButton();
			proceed.setLayout(null);
			proceed.setBounds(1045, 580, 100, 40);
			proceed.setText("Proceed");
			proceed.setFont(new Font("Aptos", Font.BOLD, 14));
			proceed.setBackground(new Color(183, 254, 190));
			add(proceed);

			JTextArea printing = new JTextArea();
			printing.setLayout(null);
			printing.setEditable(false);
			printing.setFocusable(false);
			printing.setBounds(465, 450, 405, 225);
			printing.setBorder(BorderFactory.createLineBorder(new Color(248, 182, 248), 4));
			add(printing);

			proceed.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					try (FileWriter fw = new FileWriter(FILE, true); BufferedWriter bw = new BufferedWriter(fw)) {

						String fullName = nameField.getText();
						String age = ageField.getText();
						String gender = (String) genderOpt.getSelectedItem();
						String program = programField.getText();
						String address = addressField.getText();
						String contact = contactField.getText();
						String email = emailField.getText();
						String birthday = dobField.getText();
						String parentName = guardianField.getText();
						String parentContact = guardianContactField.getText();
						String prevSchool = schoolField.getText();
						String gwa = gwaField.getText();

						if (fullName.isEmpty() || age.isEmpty() || gender.isEmpty() || program.isEmpty()
								|| address.isEmpty() || contact.isEmpty() || email.isEmpty() || birthday.isEmpty()
								|| parentName.isEmpty() || parentName.isEmpty() || prevSchool.isEmpty()
								|| gwa.isEmpty()) {

							JOptionPane.showMessageDialog(null, "Please Input The Blank Fields", "Error",
									JOptionPane.WARNING_MESSAGE);
							return;

						}

						bw.write(fullName + SPLIT + age + SPLIT + gender + SPLIT + program + SPLIT + address + SPLIT
								+ contact + SPLIT + email + SPLIT + birthday + SPLIT + parentName + SPLIT
								+ parentContact + SPLIT + prevSchool + SPLIT + gwa);

						bw.newLine();

						String getText = ("College Of Millan" + "\nFull Name: " + nameField.getText() + "\nAge: "
								+ ageField.getText() + "\nGender: " + genderOpt.getSelectedItem() + "\nProgram: "
								+ programField.getText() + "\nContact :" + contactField.getText() + "\nEmail: "
								+ emailField.getText() + "\nBirthdate: " + dobField.getText() + "\nParent Name: "
								+ guardianField.getText() + "\nParent Contact: " + guardianContactField.getText()
								+ "\nPreviouse School Attended: " + schoolField.getText()
								+ "\nGeneral Weighted Average: " + gwaField.getText());

						printing.setText(getText);
						printing.setFont(new Font("Aptos", Font.BOLD, 13));

						JOptionPane.showMessageDialog(null, "Enrolled");

					} catch (IOException x) {
						JOptionPane.showMessageDialog(null, "Error Saving...", "Error", JOptionPane.ERROR_MESSAGE);
					}

					new signUpSwap();

				}

			});

			setVisible(true);

		}
	}

	public static class loginGUI extends JFrame {

		String userName = "Admin";
		String passWord = "admin";

		public loginGUI() {

			setTitle("College of Millan");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(1366, 768);
			setLayout(null);

			JPanel sidePanel = new JPanel();
			sidePanel.setLayout(null);
			sidePanel.setBackground(new Color(248, 124, 248));
			sidePanel.setBounds(0, 0, 600, 768);
			add(sidePanel);

			JLabel title = new JLabel();
			title.setLayout(null);
			title.setText("<html>College of<br>Millan<html>");
			title.setFont(new Font("Poppins", Font.BOLD, 30));
			title.setForeground(Color.white);
			title.setBounds(125, 150, 350, 90);
			sidePanel.add(title);

			JLabel line = new JLabel();
			line.setLayout(null);
			line.setText("Enrollment System");
			line.setFont(new Font("Aptos", Font.BOLD, 20));
			line.setForeground(Color.white);
			line.setBounds(125, 250, 390, 50);
			sidePanel.add(line);

			JLabel body = new JLabel();
			body.setText(
					"<html>Enter to the competiive and high Quality<br>education in the Philippines<br>providing students a quality learning.</html>");
			body.setLayout(null);
			body.setFont(new Font("Aptos", Font.PLAIN, 15));
			body.setForeground(Color.white);
			body.setBounds(125, 320, 350, 60);
			sidePanel.add(body);

			JLabel bottom = new JLabel();
			bottom.setText("Enroll Now!");
			bottom.setLayout(null);
			bottom.setFont(new Font("Aptos", Font.BOLD, 25));
			bottom.setForeground(Color.white);
			bottom.setBounds(125, 600, 350, 50);
			sidePanel.add(bottom);

			JLabel userLabel = new JLabel("Email/Username:");
			userLabel.setFont(new Font("Aptos", Font.PLAIN, 12));
			userLabel.setBounds(866, 228, 250, 30);
			add(userLabel);

			JTextField user = new JTextField();
			user.setLayout(null);
			user.setBounds(866, 260, 250, 40);
			user.setBackground(Color.white);
			add(user);

			user.setText(userName);

			JLabel passLabel = new JLabel("Password:");
			passLabel.setFont(new Font("Aptos", Font.PLAIN, 12));
			passLabel.setBounds(866, 308, 250, 30);
			add(passLabel);

			JPasswordField pass = new JPasswordField();
			pass.setLayout(null);
			pass.setBounds(866, 335, 250, 40);
			pass.setBackground(Color.white);
			add(pass);

			pass.setText(passWord);

			JButton login = new JButton("Login");
			login.setFont(new Font("Aptos", Font.BOLD, 15));
			login.setLayout(null);
			login.setBounds(866, 410, 250, 40);
			login.setForeground(Color.white);
			login.setBackground(new Color(248, 124, 248));
			add(login);

			JRadioButton remember = new JRadioButton();
			remember.setText("Remember Me");
			remember.setFont(new Font("Aptos", Font.PLAIN, 10));
			remember.setLayout(null);
			remember.setForeground(Color.black);
			remember.setBounds(866, 375, 250, 30);
			add(remember);

			JLabel statement = new JLabel("No Account Yet?");
			statement.setFont(new Font("Aptos", Font.PLAIN, 11));
			statement.setBounds(941, 590, 150, 50);
			statement.setLayout(null);
			add(statement);

			JLabel createAcc = new JLabel("Create Account");
			createAcc.setFont(new Font("Aptos", Font.BOLD, 12));
			createAcc.setBounds(940, 608, 150, 50);
			createAcc.setLayout(null);
			add(createAcc);

			createAcc.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseExited(MouseEvent e) {
					createAcc.setForeground(Color.black);

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					createAcc.setForeground(new Color(248, 124, 248));

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					dispose();
					new dashboard();

				}
			});

			login.addActionListener(new ActionListener() {

				@Override

				public void actionPerformed(ActionEvent e) {

					String userGet = user.getText();
					String passGet = new String(pass.getPassword());

					if (userGet.equals(userName) && passGet.equals(passWord)) {

						JOptionPane.showMessageDialog(null, "Log in Successful");

						dispose();
						new signUpSwap();
					} else {

						JOptionPane.showMessageDialog(null, "Log in Failed", "Failed", JOptionPane.ERROR_MESSAGE);
						pass.setText("");
					}

				}
			});

			setVisible(true);
		}

		public static void main(String[] args) {

			new loginGUI();

		}
	}
}
