package org.remedy.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class EditorMain extends JFrame {

	private JTextField remedyName;
	private JList<String> remedyList;
	private DefaultListModel<String> remedyListModel;

	private JList<String> categoryList;
	private DefaultListModel<String> categoryListModel;
	private JTextField categoryName;

	private JList<String> symptomList;
	private DefaultListModel<String> symptomListModel;
	private JTextField symptomName;

	private JList<String> currentSymptomList;
	private DefaultListModel<String> currentSymptomListModel;

	private JTextField chosenRemedyName;
	private JButton addRemedyButton;
	private JButton removeRemedyButton;
	private JButton addCategoryButton;
	private JButton removeCategoryButton;
	private JButton removeSymptomButton;
	private JButton addSymptomButton;
	private JButton addToCurrentSymptomsButton;

	private JPanel createRemedyListPanel() {
		addRemedyButton = new JButton("Add");
		removeRemedyButton = new JButton("Remove");
		remedyName = new JTextField(20);

		remedyName.getDocument().addDocumentListener(new DocumentListener() {

			private void documentChanged() {
				if (remedyName.getText().length() == 0) {
					addRemedyButton.setEnabled(false);
				} else {
					addRemedyButton.setEnabled(true);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				documentChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				documentChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				documentChanged();
			}
		});

		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);
		panel.add(new JLabel("Remedy"), c);

		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		panel.add(remedyName, c);

		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		panel.add(addRemedyButton, c);

		remedyListModel = new DefaultListModel<>();
		remedyList = new JList<>(remedyListModel);
		ListSelectionModel selectionModel = remedyList.getSelectionModel();
		JScrollPane scrollPane = new JScrollPane(remedyList);
		scrollPane.setBorder(new BevelBorder(NORMAL));

		selectionModel.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedIndex = remedyList.getSelectedIndex();
				if (selectedIndex == -1) {
					// Clear all other buttons/text.
					chosenRemedyName.setText("");
					removeRemedyButton.setEnabled(false);
					return;
				}
				String remedy = remedyListModel.get(selectedIndex);
				chosenRemedyName.setText(remedy);
				removeRemedyButton.setEnabled(true);
			}
		});

		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 3; c.gridheight = 3;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		addRemedyButton.setEnabled(false);
		addRemedyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String remedy = remedyName.getText();
				remedyListModel.addElement(remedy);
				remedyName.setText("");
			}
		});


		c.gridx = 0; c.gridy = 4;
		c.gridwidth = 1; c.gridheight = 1;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		panel.add(removeRemedyButton, c);

		removeRemedyButton.setEnabled(false);
		removeRemedyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedItem = remedyList.getSelectedIndex();
				remedyListModel.remove(selectedItem);
			}
		});

		panel.setBorder(new TitledBorder("List of remedies"));
		return panel;
	}

	private void updateButtonState() {
		int selectedCategoryIndex = categoryList.getSelectedIndex();
		if (selectedCategoryIndex != -1) {
			removeCategoryButton.setEnabled(true);
			symptomName.setEnabled(true);
			if (symptomName.getText().length() > 0) {
				addSymptomButton.setEnabled(true);
			} else {
				addSymptomButton.setEnabled(false);
			}
		} else {
			removeCategoryButton.setEnabled(false);
			symptomName.setEnabled(false);
			addSymptomButton.setEnabled(false);
		}

		boolean enabled = false;
		int selectedSymptomIndex = symptomList.getSelectedIndex();
		if (selectedSymptomIndex != -1 && selectedCategoryIndex != -1) {
			enabled = true;
		}
		removeSymptomButton.setEnabled(enabled);
		addToCurrentSymptomsButton.setEnabled(enabled);
	}

	private JPanel createCategoryPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);

		addCategoryButton = new JButton("Add");
		addCategoryButton.setEnabled(false);
		removeCategoryButton = new JButton("Remove");
		removeCategoryButton.setEnabled(false);

		categoryName = new JTextField(20);
		categoryName.getDocument().addDocumentListener(new DocumentListener() {

			private void documentChanged() {
				if (categoryName.getText().length() == 0) {
					addCategoryButton.setEnabled(false);
				} else {
					addCategoryButton.setEnabled(true);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				documentChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				documentChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				documentChanged();
			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		panel.add(categoryName, c);

		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		panel.add(addCategoryButton, c);

		categoryListModel = new DefaultListModel<>();
		categoryList = new JList<>(categoryListModel);
		ListSelectionModel selectionModel = categoryList.getSelectionModel();
		JScrollPane scrollPane = new JScrollPane(categoryList);
		scrollPane.setBorder(new BevelBorder(NORMAL));
		c.gridy++; c.gridx = 0;
		c.gridheight = 3; c.gridwidth = 3;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateButtonState();
			}
		});

		addCategoryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String category = categoryName.getText();
				categoryListModel.addElement(category);
				categoryName.setText("");
			}
		});

		c.gridx = 0; c.gridy += 3;
		c.gridwidth = 1; c.gridheight = 1;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;

		panel.add(removeCategoryButton, c);
		removeCategoryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = categoryList.getSelectedIndex();
				if (selectedIndex != -1) {
					categoryListModel.remove(selectedIndex);
				}
			}
		});

		panel.setBorder(new TitledBorder("Categories"));
		return panel;
	}

	private JPanel createSymptomListPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);

		addSymptomButton = new JButton("Add");
		addSymptomButton.setEnabled(false);
		removeSymptomButton = new JButton("Remove");
		removeSymptomButton.setEnabled(false);
		addToCurrentSymptomsButton = new JButton("Add to current symptoms");
		addToCurrentSymptomsButton.setEnabled(false);
		symptomName = new JTextField(20);
		symptomName.setEnabled(false);
		symptomName.getDocument().addDocumentListener(new DocumentListener() {

			private void documentChanged() {
				if (symptomName.getText().length() == 0) {
					addSymptomButton.setEnabled(false);
				} else {
					addSymptomButton.setEnabled(true);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				documentChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				documentChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				documentChanged();
			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		panel.add(symptomName, c);

		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		panel.add(addSymptomButton, c);

		symptomListModel = new DefaultListModel<>();
		symptomList = new JList<>(symptomListModel);
		ListSelectionModel selectionModel = symptomList.getSelectionModel();
		JScrollPane scrollPane = new JScrollPane(symptomList);
		scrollPane.setBorder(new BevelBorder(NORMAL));
		c.gridy++; c.gridx = 0;
		c.gridheight = 3; c.gridwidth = 3;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		selectionModel.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateButtonState();
			}
		});

		addSymptomButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				symptomListModel.addElement(symptomName.getText());
				symptomName.setText("");
			}
		});

		c.gridx = 0; c.gridy += 3;
		c.gridwidth = 1; c.gridheight = 1;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;

		panel.add(addToCurrentSymptomsButton, c);

		addToCurrentSymptomsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedCategoryIndex = categoryList.getSelectedIndex();
				if (selectedCategoryIndex == -1) {
					System.out.println("No category selected");
					return;
				}
				int selectedSymptomIndex = symptomList.getSelectedIndex();
				if (selectedSymptomIndex == -1) {
					System.out.println("No symptom selected");
					return;
				}
				String category = categoryListModel.get(selectedCategoryIndex);
				String symptom = symptomListModel.get(selectedSymptomIndex);
				currentSymptomListModel.addElement(category + ": " + symptom);
			}
		});

		c.gridx++;

		panel.add(removeSymptomButton, c);

		removeSymptomButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = symptomList.getSelectedIndex();
				if (selectedIndex != -1) {
					symptomListModel.remove(selectedIndex);
				}
			}
		});

		panel.setBorder(new TitledBorder("Symptom list"));
		return panel;
	}

	private JPanel createCurrentSymptomPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);

		chosenRemedyName = new JTextField(20);
		chosenRemedyName.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		panel.add(chosenRemedyName, c);

		currentSymptomListModel = new DefaultListModel<>();
		currentSymptomList = new JList<>(currentSymptomListModel);
		JScrollPane scrollPane = new JScrollPane(currentSymptomList);
		scrollPane.setBorder(new BevelBorder(NORMAL));
		c.gridx = 0; c.gridy++;
		c.gridheight = 3; c.gridwidth = 2;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		JPanel buttonPanel = new JPanel();
		c.gridx = 0; c.gridy += 3;
		c.gridwidth = 1; c.gridheight = 1;
		c.weighty = 0.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton saveButton = new JButton("Save");
		buttonPanel.add(saveButton);
		JButton removeButton = new JButton("Remove");
		buttonPanel.add(removeButton);
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedIndex = currentSymptomList.getSelectedIndex();
				if (selectedIndex == -1) {
					return;
				}
				currentSymptomListModel.remove(selectedIndex);
			}
		});

		panel.add(buttonPanel, c);
		TitledBorder border = new TitledBorder(
				new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK), "Current symptoms");
		border.setTitleColor(Color.BLACK);
		border.setTitleJustification(TitledBorder.CENTER);
		panel.setBorder(border);
		return panel;
	}

	public EditorMain() {
		setSize(1024, 768);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Remedy editor");
		Container mainPane = getContentPane();
		GridBagLayout layout = new GridBagLayout();
		mainPane.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1.0;
		c.weighty = 0.25;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;

		JPanel remedyListPanel = createRemedyListPanel();
		mainPane.add(remedyListPanel, c);

		c.gridx++;
		JPanel categoryPanel = createCategoryPanel();
		mainPane.add(categoryPanel, c);

		c.gridx++;
		JPanel symptomListPanel = createSymptomListPanel();
		mainPane.add(symptomListPanel, c);

		c.gridx = 0; c.gridy++;
		c.weightx = 1.0; c.weighty = 1.0;
		c.gridwidth = 3;
		c.insets.top = 20;
		c.fill = GridBagConstraints.BOTH;
		JPanel currentSymptomPanel = createCurrentSymptomPanel();
		mainPane.add(currentSymptomPanel, c);
	}

	public static void main(String[] args) {
		EditorMain editorMain = new EditorMain();
		editorMain.setVisible(true);
	}
}
