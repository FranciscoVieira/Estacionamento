package estacionamento.negocio.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import com.mysql.jdbc.PreparedStatement;

import estacionamento.controle.EstacionamentoException;
import estacionamento.negocio.Movimentacao;
import estacionamento.negocio.Vaga;
import estacionamento.negocio.Veiculo;
import estacionamento.utilitario.EstacionamentoUtil;

/**
 * Representa a camada de persistência da aplicação. Realiza o mapaeamento
 * objeto-relacional.
 * 
 * @author Francisco Vieira
 *
 */

public class DAOEstacionamento {

	/**
	 * Armazena os dados da movimentação.
	 * 
	 * @param movimentacao
	 *            Instância da movimentação.
	 * @throws EstacionamentoException Se houver erro de registro
	 */

	public void criar(Movimentacao movimentacao) throws EstacionamentoException {
		String cmd1 = EstacionamentoUtil.get("insertMov");
		String cmd2 = EstacionamentoUtil.get("atualizaVaga");

		Connection conexao = null;
		try {
			conexao = getConnection();
			conexao.setAutoCommit(false);

			java.sql.PreparedStatement stmt = conexao.prepareStatement(cmd1);
			stmt.setString(1, movimentacao.getVeiculo().getPlaca());
			stmt.setString(2, movimentacao.getVeiculo().getMarca());
			stmt.setString(3, movimentacao.getVeiculo().getModelo());
			stmt.setString(4, movimentacao.getVeiculo().getCor());
			stmt.setString(5, EstacionamentoUtil.getDataAsString(movimentacao.getDataHoraEntrada()));

			stmt.execute();
			stmt = conexao.prepareStatement(cmd2);
			stmt.setInt(1, Vaga.ocupadas() + 1);

			stmt.execute();
			conexao.commit();
			stmt.close();
			conexao.close();
			
		} catch (SQLException e) {
			try {
				conexao.rollback();
				throw new EstacionamentoException("Erro ao registrar veículo");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Atualiza os dados de data de saída do valor da movimentação.
	 * 
	 * @param movimentacao
	 *            Instância da movimentação.
	 * @throws EstacionamentoException 
	 */

	public void atualizar(Movimentacao movimentacao) throws EstacionamentoException {
		String cmd1 = EstacionamentoUtil.get("updateMov");
		String cmd2 = EstacionamentoUtil.get("atualizaVaga");

		Connection conexao = null;
		try {
			conexao = getConnection();
			conexao.setAutoCommit(false);

			java.sql.PreparedStatement stmt = conexao.prepareStatement(cmd1);
			stmt.setDouble(1, movimentacao.getValor());
			stmt.setString(2, EstacionamentoUtil.getDataAsString(movimentacao.getDataHoraSaida()));
			stmt.setString(3, movimentacao.getVeiculo().getPlaca());
			
			stmt.execute();
			stmt = conexao.prepareStatement(cmd2);
			stmt.setInt(1, Vaga.ocupadas() - 1);
			stmt.execute();
			
			conexao.commit();
		} catch (SQLException e) {
			try {
				conexao.rollback();
				throw new EstacionamentoException("Erro ao registrar veículo");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Busca movimentação cujo veículo tenha a placa informada que ainda está
	 * estacionado (data de saída nul).
	 * 
	 * @param placa
	 *            A placa do veículo.
	 * 
	 * @return A movimentação encontrada ou null se não houver.
	 */

	public Movimentacao buscarMovimentacaAberta(String placa) {
		String cmd = EstacionamentoUtil.get("getMovAberta");
		Connection conexao = null;
		Movimentacao movimentacao = null;
		
		try {
			conexao = getConnection();
			java.sql.PreparedStatement ps = conexao.prepareStatement(cmd);
			ps.setString(1, placa);
			
			ResultSet resultado = ps.executeQuery();
			
			if (resultado.next()) {
				String rplaca = resultado.getString("placa");
				String rdataEntrada = resultado.getString("data_entrada");
				Veiculo veiculo = new Veiculo(rplaca);
				movimentacao = new Movimentacao(veiculo, EstacionamentoUtil.getDate(rdataEntrada));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conexao);
		}
		
		return movimentacao;
	}

	/**
	 * Consulta todas as movimentações fechadas (pagas e com data de saída e
	 * preenchida) no mês da data informada.
	 * 
	 * @param data
	 *            Data consulta
	 * 
	 * @return Lista de movimentações do ano e mês informados.
	 */

	public List<Movimentacao> consultarMovimentacoes(LocalDateTime data) {
		Connection conexao = null;
		String cmd = EstacionamentoUtil.get("selectMovRelatorio");
		List<Movimentacao> movimentacoes = new ArrayList<>();
		
		
		try {
			conexao = getConnection();
			java.sql.PreparedStatement ps = conexao.prepareStatement(cmd);
			ps.setString(1, data.toString());
			data = data.with(TemporalAdjusters.lastDayOfMonth());
			ps.setString(2, data.toString());
			
			ResultSet resultado = ps.executeQuery();
			while (resultado.next()) {
				String placa = resultado.getString("placa");
				LocalDateTime entrada = EstacionamentoUtil.getDate(resultado.getString("data_entrada"));
				LocalDateTime saida = EstacionamentoUtil.getDate(resultado.getString("data_saida"));
				double valor = resultado.getDouble("valor");
				
				Veiculo veiculo = new Veiculo(placa);
				Movimentacao movimentacao = new Movimentacao(veiculo, entrada);
				movimentacao.setDataHoraEntrada(saida);
				movimentacao.setValor(valor);
				
				movimentacoes.add(movimentacao);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conexao);
		}
		return movimentacoes;
	}

	public static Connection getConnection() throws SQLException {

		String url = EstacionamentoUtil.get("url");
		String usuario = EstacionamentoUtil.get("usuario");
		String senha = EstacionamentoUtil.get("senha");

		Connection conexao = DriverManager.getConnection(url, usuario, senha);

		return conexao;
	}

	public static void closeConnection(Connection conexao) {
		if (conexao != null) {
			try {
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public int getOcupadas() {
		int ocupadas = 1;
		Connection conexao = null;
		String cmd = EstacionamentoUtil.get("consultaOcupadas");														
		try {
			conexao = getConnection();
			Statement ps = conexao.prepareStatement(cmd);
			
			ResultSet resultado = ps.executeQuery(cmd);
			if (resultado.next()) {
				ocupadas = resultado.getInt(ocupadas);
			}
			
		} catch (SQLException e) {	
			e.printStackTrace();
		} finally {
			closeConnection(conexao);
		}
		return ocupadas;
	}

}
