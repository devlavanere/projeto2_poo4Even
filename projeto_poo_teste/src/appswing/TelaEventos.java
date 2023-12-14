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

import modelo.Evento;
import modelo.Ingresso;
import regras_negocio.Fachada;
import repositorio.Repositorio;

public class TelaEventos {
    private JFrame frame;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel label;
    private JLabel log;
    private JLabel labelData;
    private JTextField textFieldData;
    private JButton btnCriarEvento;
    private JButton btnApagarEvento;
    private JButton btnListarEventos;
    private JTextField textFieldDescricao;
    private JLabel lblPreco;
    private JLabel lblCapacidade;
    private JTextField textFieldPreco;
    private JTextField textFieldCapacidade;
    private JButton btnExibirIngressos;
    
    public Repositorio repositorio;

    public TelaEventos() {
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
        frame.setTitle("Eventos");
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

        labelData = new JLabel("Data:");
        labelData.setHorizontalAlignment(SwingConstants.LEFT);
        labelData.setFont(new Font("Tahoma", Font.PLAIN, 12));
        labelData.setBounds(21, 257, 71, 14);
        frame.getContentPane().add(labelData);

        textFieldData = new JTextField();
        textFieldData.setFont(new Font("Dialog", Font.PLAIN, 12));
        textFieldData.setColumns(10);
        textFieldData.setBackground(Color.WHITE);
        textFieldData.setBounds(97, 254, 134, 20);
        frame.getContentPane().add(textFieldData);

        btnCriarEvento = new JButton("Criar evento");
        btnCriarEvento.setToolTipText("");
        btnCriarEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (textFieldData.getText().isEmpty()) {
                        label.setText("A data é obrigatória");
                        return;
                    }
                    if (textFieldDescricao.getText().isEmpty()) {
                        label.setText("A descrição é obrigatória");
                        return;
                    }
                    
                    String data = textFieldData.getText();
                    String descricao = textFieldDescricao.getText();
                    double preco = Double.parseDouble(textFieldPreco.getText());
                    int capacidade = Integer.parseInt(textFieldCapacidade.getText());
                    Fachada.criarEvento(data, descricao, capacidade, preco);
                    
                    label.setText("Evento criado: ");
                    listagem();
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });
        btnCriarEvento.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnCriarEvento.setBounds(462, 42, 160, 23);
        frame.getContentPane().add(btnCriarEvento);

        btnApagarEvento = new JButton("Apagar evento");
        btnApagarEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (table.getSelectedRow() >= 0) {
                        Integer id = (Integer) table.getValueAt(table.getSelectedRow(), 0);
                        // confirmação
                        Object[] options = { "Confirmar", "Cancelar" };
                        int escolha = JOptionPane.showOptionDialog(null, "Confirma exclusão: " + id, "Alerta",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                        if (escolha == 0) {
                            Fachada.apagarEvento(id); // Apagar evento
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
        btnApagarEvento.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnApagarEvento.setBounds(462, 110, 160, 23);
        frame.getContentPane().add(btnApagarEvento);

        btnListarEventos = new JButton("Listar eventos");
        btnListarEventos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listagem();
            }
        });
        btnListarEventos.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnListarEventos.setBounds(462, 76, 160, 23);
        frame.getContentPane().add(btnListarEventos);
        
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblDescricao.setBounds(21, 282, 71, 14);
        frame.getContentPane().add(lblDescricao);
        
        textFieldDescricao = new JTextField();
        textFieldDescricao.setBounds(97, 280, 134, 20);
        frame.getContentPane().add(textFieldDescricao);
        textFieldDescricao.setColumns(10);
        
        lblPreco = new JLabel("Preço:");
        lblPreco.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblPreco.setBounds(298, 258, 46, 14);
        frame.getContentPane().add(lblPreco);
        
        lblCapacidade = new JLabel("Capacidade:");
        lblCapacidade.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblCapacidade.setBounds(298, 283, 77, 14);
        frame.getContentPane().add(lblCapacidade);
        
        textFieldPreco = new JTextField();
        textFieldPreco.setBounds(377, 255, 75, 20);
        frame.getContentPane().add(textFieldPreco);
        textFieldPreco.setColumns(10);
        
        textFieldCapacidade = new JTextField();
        textFieldCapacidade.setColumns(10);
        textFieldCapacidade.setBounds(377, 280, 75, 20);
        frame.getContentPane().add(textFieldCapacidade);
        
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
            List<Evento> lista = Fachada.listarEventos(); // Listar eventos

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Id");
            model.addColumn("Data");
            model.addColumn("Capacidade");
            model.addColumn("Preço");
            model.addColumn("Quantidade de ingressos");
            model.addColumn("Total arrecadado");
            model.addColumn("Lotado");
            
            

            for (Evento evento : lista) {
                model.addRow(new Object[] { evento.getId(), evento.getData(), evento.getCapacidade(),
                        evento.getPreco(), evento.quantidadeIngressos(), evento.totalArrecadado(), evento.lotado()});
            }
            table.setModel(model);
            log.setText("resultados: " + lista.size() + " linhas   - selecione uma linha");

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setMaxWidth(50);
            table.getColumnModel().getColumn(1).setMaxWidth(100);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        } catch (Exception erro) {
            label.setText(erro.getMessage());
        }
    }
}