package appswing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Convidado;
import modelo.Ingresso;
//import modelo.Evento;
//import modelo.Ingresso;
import modelo.Participante;
import regras_negocio.Fachada;

public class TelaParticipantes {
    private JFrame frame;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel label;
    private JLabel log;
    private JLabel lblCpf;
    private JTextField textFieldCpf;
    private JButton btnCriarParticipante;
    private JButton btnApagarParticipante;
    private JButton btnListarParticipantes;
    private JTextField textFieldNascimento;
    private JLabel lblEmpresa;
    private JTextField textFieldEmpresa;
    private JButton btnExibirIngressos;

    public TelaParticipantes() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                listagem();
            }
        });
        frame.setTitle("Participantes");
        frame.setBounds(100, 100, 659, 362);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(21, 11, 431, 207);
        frame.getContentPane().add(scrollPane);

        table = new JTable() {
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() >= 0)
                    log.setText("selecionado=" + table.getValueAt(table.getSelectedRow(), 0));
            }
        });
        table.setGridColor(Color.BLACK);
        table.setRequestFocusEnabled(false);
        table.setFocusable(false);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scrollPane.setViewportView(table);
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        label = new JLabel("");
        label.setForeground(Color.RED);
        label.setBounds(21, 296, 587, 14);
        frame.getContentPane().add(label);

        log = new JLabel("selecione");
        log.setBounds(21, 216, 394, 14);
        frame.getContentPane().add(log);

        lblCpf = new JLabel("CPF:");
        lblCpf.setHorizontalAlignment(SwingConstants.LEFT);
        lblCpf.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblCpf.setBounds(21, 257, 71, 14);
        frame.getContentPane().add(lblCpf);

        textFieldCpf = new JTextField();
        textFieldCpf.setFont(new Font("Dialog", Font.PLAIN, 12));
        textFieldCpf.setColumns(10);
        textFieldCpf.setBackground(Color.WHITE);
        textFieldCpf.setBounds(97, 254, 134, 20);
        frame.getContentPane().add(textFieldCpf);

        btnCriarParticipante = new JButton("Criar participante");
        btnCriarParticipante.setToolTipText("");
        btnCriarParticipante.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (textFieldCpf.getText().isEmpty()) {
                        label.setText("O cpf é obrigatório");
                        return;
                    }
                    if (textFieldNascimento.getText().isEmpty()) {
                        label.setText("A data de nascimento é obrigatória");
                        return;
                    }
                    
                    String cpf = textFieldCpf.getText();
                    String dataNasc = textFieldNascimento.getText();
                    String empresa = textFieldEmpresa.getText();
                    
                    if(empresa.isEmpty()) {
                        Fachada.criarParticipante(cpf, dataNasc); // Criar participante
                        label.setText("Participante cadastrado: ");
                    }
                    else {
                    	Fachada.criarConvidado(cpf, dataNasc, empresa); // Criar convidado
                    	label.setText("Convidado cadastrado: ");
                    }
                    
                    listagem();
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });
        btnCriarParticipante.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnCriarParticipante.setBounds(462, 42, 160, 23);
        frame.getContentPane().add(btnCriarParticipante);

        btnApagarParticipante = new JButton("Apagar participante");
        btnApagarParticipante.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (table.getSelectedRow() >= 0) {
                        String cpf = (String) table.getValueAt(table.getSelectedRow(), 0);
                        // confirmação
                        Object[] options = { "Confirmar", "Cancelar" };
                        int escolha = JOptionPane.showOptionDialog(null, "Confirma exclusão: " + cpf, "Alerta",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                        if (escolha == 0) {
                            Fachada.apagarParticipante(cpf); // Apagar participante
                            label.setText("exclusão realizada");
                            listagem();
                        } else
                            label.setText("exclusao cancelada");
                    } else
                        label.setText("selecione uma linha");
                } catch (Exception erro) {
                    label.setText(erro.getMessage());
                }
            }
        });
        btnApagarParticipante.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnApagarParticipante.setBounds(462, 110, 160, 23);
        frame.getContentPane().add(btnApagarParticipante);

        btnListarParticipantes = new JButton("Listar participantes"); // Listar participantes
        btnListarParticipantes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listagem();
            }
        });
        btnListarParticipantes.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnListarParticipantes.setBounds(462, 76, 160, 23);
        frame.getContentPane().add(btnListarParticipantes);
        
        JLabel lblNascimento = new JLabel("Nascimento:");
        lblNascimento.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblNascimento.setBounds(21, 282, 71, 14);
        frame.getContentPane().add(lblNascimento);
        
        textFieldNascimento = new JTextField();
        textFieldNascimento.setBounds(97, 280, 134, 20);
        frame.getContentPane().add(textFieldNascimento);
        textFieldNascimento.setColumns(10);
        
        lblEmpresa = new JLabel("Empresa:");
        lblEmpresa.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblEmpresa.setBounds(298, 258, 59, 14);
        frame.getContentPane().add(lblEmpresa);
        
        textFieldEmpresa = new JTextField();
        textFieldEmpresa.setBounds(377, 255, 75, 20);
        frame.getContentPane().add(textFieldEmpresa);
        textFieldEmpresa.setColumns(10);
        
        btnExibirIngressos = new JButton("Exibir ingressos");
        btnExibirIngressos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
	        		ArrayList<Ingresso> ingressos = Fachada.listarIngressos();
	        		StringBuilder relacaoIngressos = new StringBuilder("Relação dos ingressos:\n--------------------\n");

	                for (Ingresso ingresso : ingressos) {
	                    relacaoIngressos.append("Código: ").append(ingresso.getCodigo()).append(", Telefone: ")
	                            .append(ingresso.getTelefone()).append("\n");
	                }

	                JOptionPane.showMessageDialog(null, relacaoIngressos.toString(), "Detalhes dos Ingressos", JOptionPane.INFORMATION_MESSAGE);        		}
        		
        		catch (Exception erro) {
                    label.setText(erro.getMessage());
        		}
        	}
        });
        btnExibirIngressos.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnExibirIngressos.setBounds(462, 144, 160, 23);
        frame.getContentPane().add(btnExibirIngressos);
    }

    public void listagem() {
    	try {
            List<Participante> lista = Fachada.listarParticipantes(); // Listar participantes

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("CPF");
            model.addColumn("Data de nasc");
            model.addColumn("Idade");
            model.addColumn("Empresa");

            for (Participante participante : lista) {
                if (participante instanceof Convidado) {
                    Convidado convidado = (Convidado) participante;
                    model.addRow(new Object[]{participante.getCpf(), participante.getNascimento(), participante.calcularIdade(), convidado.getEmpresa()});
                } else {
                    model.addRow(new Object[]{participante.getCpf(), participante.getNascimento(), participante.calcularIdade(), ""});
                }
            }

            table.setModel(model);
            log.setText("resultados: " + lista.size() + " linhas - selecione uma linha");

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setMaxWidth(50);
            table.getColumnModel().getColumn(1).setMaxWidth(100);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        } catch (Exception erro) {
            label.setText(erro.getMessage());
        }
    }
}