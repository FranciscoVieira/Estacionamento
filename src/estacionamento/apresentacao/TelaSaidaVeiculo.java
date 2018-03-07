package estacionamento.apresentacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import estacionamento.controle.EstacionamentoController;
import estacionamento.controle.EstacionamentoException;
import estacionamento.controle.VeiculoException;
import estacionamento.negocio.Movimentacao;

public class TelaSaidaVeiculo extends JFrame implements ActionListener {

	private JFrame parent;
	private JFormattedTextField txtPlaca;
	
	public TelaSaidaVeiculo(JFrame parent) {
		setResizable(false);
		setTitle("Sa\u00EDda de ve\u00EDculo");
		setSize(new Dimension(523, 196));
		
		this.parent = parent;
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(this);
		btnOk.setActionCommand("ok");
		panel.add(btnOk);
		
		JButton btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");
		panel.add(btnCancel);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(20);
		getContentPane().add(panel_1, BorderLayout.CENTER);
		
		JLabel lblPlaca = new JLabel("Placa:");
		lblPlaca.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_1.add(lblPlaca);
		
		try {
			txtPlaca = new JFormattedTextField(new MaskFormatter("UUU-####"));
		} catch (ParseException e) {
			assert false : "Formato do padrão inválido";
		}
		txtPlaca.setForeground(Color.BLUE);
		txtPlaca.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtPlaca.setColumns(8);
		panel_1.add(txtPlaca);
		
		setLocationRelativeTo(null);
	}
	@Override
	public void actionPerformed(ActionEvent evento) {
		String cmd = evento.getActionCommand();
		
		if (cmd.equals("ok")) {
			EstacionamentoController controle = new EstacionamentoController();
			Movimentacao movimentacao = null;
			try {
				movimentacao = controle.processarSaida(txtPlaca.getText());
			} catch (VeiculoException | EstacionamentoException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Falha na saída", JOptionPane.ERROR_MESSAGE);
	
			}
			TelaResumoPagamento telaResumo = new TelaResumoPagamento(movimentacao, parent);
			telaResumo.setVisible(true);
			
		} else {
			parent.setVisible(true);
		}
		
		dispose();
		
	}

}
