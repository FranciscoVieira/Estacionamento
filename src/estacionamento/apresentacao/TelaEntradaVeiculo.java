package estacionamento.apresentacao;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import estacionamento.controle.EstacionamentoController;
import estacionamento.controle.EstacionamentoException;
import estacionamento.controle.VeiculoException;

import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;

public class TelaEntradaVeiculo extends JFrame implements ActionListener {

	private JFrame parent;
	private JTextField txtMarca;
	private JTextField txtModelo;
	private JTextField txtCor;
	private JFormattedTextField txfPlaca;
	private JButton btnOk;
	private JButton btnCancel;

	// TODO tempórario
	public static void main(String[] args) {
		TelaEntradaVeiculo tela = new TelaEntradaVeiculo(null);
		tela.setVisible(true);
	}

	public TelaEntradaVeiculo(JFrame parent) {
		setResizable(false);
		setSize(400, 300);
		setTitle("Entrada de Ve\u00EDculo");
		this.parent = parent;
		getContentPane().setLayout(null);

		JLabel lblPlaca = new JLabel("Placa:");
		lblPlaca.setBounds(62, 41, 46, 14);
		getContentPane().add(lblPlaca);

		JLabel lblMarca = new JLabel("Marca:");
		lblMarca.setBounds(62, 89, 46, 14);
		getContentPane().add(lblMarca);

		JLabel lblModelo = new JLabel("Modelo:");
		lblModelo.setBounds(62, 131, 46, 14);
		getContentPane().add(lblModelo);

		JLabel lblCor = new JLabel("Cor:");
		lblCor.setBounds(62, 174, 46, 14);
		getContentPane().add(lblCor);

		txtMarca = new JTextField();
		txtMarca.setBounds(135, 86, 86, 20);
		getContentPane().add(txtMarca);
		txtMarca.setColumns(10);

		txtModelo = new JTextField();
		txtModelo.setBounds(135, 128, 86, 20);
		getContentPane().add(txtModelo);
		txtModelo.setColumns(10);

		txtCor = new JTextField();
		txtCor.setBounds(135, 171, 86, 20);
		getContentPane().add(txtCor);
		txtCor.setColumns(10);

		btnOk = new JButton("Ok");
		btnOk.setBounds(62, 237, 89, 23);
		btnOk.addActionListener(this);
		btnOk.setActionCommand("ok");
		getContentPane().add(btnOk);

		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnCancel.setBounds(250, 237, 89, 23);
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");
		getContentPane().add(btnCancel);

		try {
			txfPlaca = new JFormattedTextField(new MaskFormatter("UUU-####"));
		} catch (ParseException e) {
			assert false : "Padrão de placa inválido";
		}
		txfPlaca.setBounds(135, 38, 86, 20);
		getContentPane().add(txfPlaca);

		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		if (evento.getActionCommand().equals("ok")) {
			EstacionamentoController controle = new EstacionamentoController();
			try {
				controle.processarEntrada(txfPlaca.getText(), txtMarca.getText(), txtModelo.getText(),
						txtCor.getText());
				JOptionPane.showMessageDialog(null, "Veículo registrado com sucesso", "Entrada de veículo", JOptionPane.INFORMATION_MESSAGE);
			} catch (EstacionamentoException | VeiculoException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Falha na entrada", JOptionPane.ERROR_MESSAGE);
			}

		}
		this.parent.setVisible(true);
		this.dispose();
	}
}
