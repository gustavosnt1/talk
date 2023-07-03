/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Pesist~encia de Objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package appswing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Grupo;
import modelo.Individual;
import regras_negocio.Fachada;

public class TelaParticipante {
	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel label;
	private JLabel lblEmail;
	private JTextField textField;
	private JPasswordField passwordField;
	private JLabel lblSenha;
	private JButton button;
	private JPanel panel;

	private JRadioButton radioButton;
	private JRadioButton radioButton_1;
	private ButtonGroup grupobotoes;
	private Timer timer; // temporizador
	private JLabel label_1;
	private JLabel label_2;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton button_1;
	private JButton button_2;

	/**
	 * Launch the application.
	 */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// TelaParticipante window = new TelaParticipante();
	// window.frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	/**
	 * Create the application.
	 */
	public TelaParticipante() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				listagem();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				timer.stop();
			}
		});
		frame.setTitle("Participantes");
		frame.setBounds(100, 100, 538, 323);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 15, 478, 179);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.setDefaultEditor(Object.class, null); // desabilita edicao de celulas
		table.setGridColor(Color.BLACK);
		table.setRequestFocusEnabled(false);
		table.setFocusable(false);
		table.setBackground(Color.YELLOW);
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		label = new JLabel("");
		label.setForeground(Color.RED);
		label.setBounds(-115, 259, 373, 268);
		frame.getContentPane().add(label);

		lblEmail = new JLabel("Nome");
		lblEmail.setBounds(20, 204, 46, 14);
		frame.getContentPane().add(lblEmail);

		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(59, 202, 86, 20);
		frame.getContentPane().add(textField);

		passwordField = new JPasswordField();
		passwordField.setBounds(59, 229, 86, 20);
		frame.getContentPane().add(passwordField);

		lblSenha = new JLabel("Senha");
		lblSenha.setBounds(20, 235, 46, 14);
		frame.getContentPane().add(lblSenha);

		button = new JButton("Criar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = textField.getText();
					String senha = new String(passwordField.getPassword());
					if (radioButton.isSelected())
						Fachada.criarGrupo(nome);
					if (radioButton_1.isSelected())
						Fachada.criarIndividuo(nome, senha);
					label.setText("participante criado");
				} catch (Exception ex) {
					label.setText(ex.getMessage());
				}

			}
		});
		button.setBounds(257, 227, 74, 23);
		frame.getContentPane().add(button);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Tipo",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(151, 204, 107, 61);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(2, 0));

		radioButton = new JRadioButton("grupo");
		panel.add(radioButton);
		radioButton_1 = new JRadioButton("individual");
		panel.add(radioButton_1);
		radioButton_1.setSelected(true);
		grupobotoes = new ButtonGroup(); // permite selecao unica dos botoes
		grupobotoes.add(radioButton);
		grupobotoes.add(radioButton_1);
		
		label_1 = new JLabel("Nome");
		label_1.setBounds(341, 204, 45, 13);
		frame.getContentPane().add(label_1);
		
		label_2 = new JLabel("Grupo");
		label_2.setBounds(341, 232, 45, 13);
		frame.getContentPane().add(label_2);
		
		textField_1 = new JTextField();
		textField_1.setBounds(374, 202, 96, 19);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(374, 229, 96, 19);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		button_1 = new JButton("Inserir");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String nomeind = textField_1.getText();
					String nomegrupo = textField_2.getText();
					Fachada.inserirGrupo(nomeind, nomegrupo);
					label.setText("participante adicionado");
				}catch(Exception ex){
					label.setText(ex.getMessage());
				}
			}
		});
		button_1.setBounds(332, 255, 85, 21);
		frame.getContentPane().add(button_1);
		
		button_2 = new JButton("Remover");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String nomeind = textField_1.getText();
					String nomegrupo = textField_2.getText();
					Fachada.removerGrupo(nomeind, nomegrupo);
					label.setText("participante removido");
				}catch(Exception ex){
					label.setText(ex.getMessage());
				}

			}
		});
		button_2.setBounds(427, 252, 85, 21);
		frame.getContentPane().add(button_2);

		// temporizador
		timer = new Timer(0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
				String s = formatador.format(LocalDateTime.now());
				frame.setTitle("Participante: " + s);
				frame.repaint();
				listagem();
			}
		});
		timer.setRepeats(true);
		timer.setDelay(10000); // 8 segundos
		timer.start();

		frame.setVisible(true);
	}

	public void listagem() {
		try {
			List<Individual> lista1 = Fachada.listarIndividuos();
			List<Grupo> lista2 = Fachada.listarGrupos();

			DefaultTableModel model = new DefaultTableModel();
			// criar as colunas do table
			model.addColumn("Nome");
			model.addColumn("Tipo");
			model.addColumn("Grupos/Membros");

			// criar as linhas do table
			for (Individual individuo : lista1) {
				String texto = "";
				for (Grupo g : individuo.getGrupos())
					texto += g.getNome() + " ";
				model.addRow(new String[] { individuo.getNome(), "Individual", texto });
			}

			for (Grupo grupo : lista2) {
				String texto = "";
				for (Individual i : grupo.getIndividuos())
					texto += i.getNome() + " ";
				model.addRow(new String[] { grupo.getNome(), "Grupo", texto });
			}
			table.setModel(model);
		} catch (Exception erro) {
			JOptionPane.showMessageDialog(frame, erro.getMessage());
		}
	}
}
