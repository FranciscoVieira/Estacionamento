package estacionamento.apresentacao;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import estacionamento.negocio.Movimentacao;
import estacionamento.utilitario.EstacionamentoUtil;

public class TelaResumoPagamento extends JFrame implements ActionListener {
	private JFrame parent;

	public TelaResumoPagamento(Movimentacao movimentacao, JFrame parent) {
		this.parent = parent;
		getContentPane().setFont(new Font("Dialog", Font.BOLD, 12));
		setSize(new Dimension(430, 300));
		setResizable(false);
		setTitle("Resumo de Pagamento");
		getContentPane().setLayout(null);
		
		JLabel lblPlaca = new JLabel("Placa: ");
		lblPlaca.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPlaca.setBounds(84, 47, 46, 14);
		getContentPane().add(lblPlaca);
		
		JLabel lblDataEntrada = new JLabel("Entrada:");
		lblDataEntrada.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDataEntrada.setBounds(63, 80, 89, 14);
		getContentPane().add(lblDataEntrada);
		
		JLabel lblDataSaida = new JLabel("Sa\u00EDda:");
		lblDataSaida.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDataSaida.setBounds(85, 144, 89, 14);
		getContentPane().add(lblDataSaida);
		
		JLabel lblValor = new JLabel("Valor:");
		lblValor.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblValor.setBounds(84, 199, 46, 14);
		getContentPane().add(lblValor);
		
		String splaca = movimentacao.getVeiculo().getPlaca();
		JLabel lblValPlaca = new JLabel(splaca);
		lblValPlaca.setFont(new Font("Dialog", Font.BOLD, 12));
		lblValPlaca.setBounds(242, 48, 97, 14);
		getContentPane().add(lblValPlaca);
		
		String sEntrada = EstacionamentoUtil.getDisplayData(movimentacao.getDataHoraEntrada());
		JLabel lblValDataEntrada = new JLabel(sEntrada);
		lblValDataEntrada.setFont(new Font("Dialog", Font.BOLD, 12));
		lblValDataEntrada.setBounds(242, 80, 97, 14);
		getContentPane().add(lblValDataEntrada);
		
		String sSaida = EstacionamentoUtil.getDisplayData(movimentacao.getDataHoraSaida());
		JLabel lblValDataSaida = new JLabel(sSaida);
		lblValDataSaida.setFont(new Font("Dialog", Font.BOLD, 12));
		lblValDataSaida.setBounds(242, 133, 97, 14);
		getContentPane().add(lblValDataSaida);
		
		String sValor = "R$ " + movimentacao.getValor();
		JLabel lblValValor = new JLabel(sValor);
		lblValValor.setFont(new Font("Dialog", Font.BOLD, 12));
		lblValValor.setBounds(242, 199, 72, 14);
		getContentPane().add(lblValValor);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(this);
		btnOk.setBounds(140, 236, 89, 23);
		getContentPane().add(btnOk);
		
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		parent.setVisible(true);
		dispose();
		
	}
}
