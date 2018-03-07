package estacionamento.apresentacao;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import estacionamento.controle.EstacionamentoController;
import estacionamento.negocio.Movimentacao;

public class TelaInicialReleatorio extends JFrame implements ActionListener {
	
	private JComboBox cboAno;
	private JComboBox cboMes;
	
	public TelaInicialReleatorio() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(600, 140));
		setResizable(false);
		setTitle("Filtro de Relat\u00E1rio");
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 15, 40));
		
		JLabel lblAno = new JLabel("Ano:");
		lblAno.setFont(new Font("Tahoma", Font.BOLD, 14));
		getContentPane().add(lblAno);
		
		cboAno = new JComboBox();
		cboAno.setModel(new DefaultComboBoxModel(new String[] {"2017", "2016", "2015", "2014", "2013", "2012"}));
		cboAno.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(cboAno);
		
		JLabel lblMes = new JLabel("M\u00EAs:");
		lblMes.setFont(new Font("Tahoma", Font.BOLD, 14));
		getContentPane().add(lblMes);
		
		cboMes = new JComboBox();
		cboMes.setModel(new DefaultComboBoxModel(new String[] {"Janeiro", "Fevereiro", "Mar\u00E7o", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"}));
		cboMes.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(cboMes);
		
		JButton btnGerar = new JButton("Gerar");
		btnGerar.addActionListener(this);
		btnGerar.setFont(new Font("Tahoma", Font.BOLD, 14));
		getContentPane().add(btnGerar);
		
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		//recupera do combo o ano e mês escolhido
		int ano = Integer.parseInt((String) cboAno.getSelectedItem());
		int mes = (Integer) cboMes.getSelectedIndex() + 1;
		
		//buscar as movimentações
		EstacionamentoController controle = new EstacionamentoController();
		LocalDateTime data = LocalDateTime.of(ano, mes, 1, 0, 0);
		List<Movimentacao> movimentacoes = controle.emitirRelatorio(data);
		
		//exibe a tela de conteúdo e faturamento
		TelaResultadoRelatorio relatorio = new TelaResultadoRelatorio(this, movimentacoes, data);
		
		relatorio.setVisible(true);
		dispose();
	}

}
