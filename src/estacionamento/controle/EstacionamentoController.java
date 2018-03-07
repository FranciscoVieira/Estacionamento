package estacionamento.controle;

import java.time.LocalDateTime;
import java.util.List;

import estacionamento.negocio.Movimentacao;
import estacionamento.negocio.Vaga;
import estacionamento.negocio.Veiculo;
import estacionamento.negocio.persistencia.DAOEstacionamento;
import estacionamento.utilitario.EstacionamentoUtil;

public class EstacionamentoController {

	/**
	 * A partir dos dados do veículo informados pelo operador, realiza o fluxo
	 * de entrada do veículo no estacionamento registrando a movimentação
	 * gerada.
	 * 
	 * @param placa
	 *            Placa do veículo
	 * @param marca
	 *            Marca do veículo
	 * @param modelo
	 *            Modelo do veículo
	 * @param cor
	 *            cor do veículo
	 * @throws EstacionamentoException
	 *             Quando estacionamento estiver lotado
	 * @throws VeiculoException
	 *             Quando o padrão da placa for inválido
	 */

	public void processarEntrada(String placa, String marca, String modelo, String cor)
			throws EstacionamentoException, VeiculoException {
		// verificar se o estacionamento está cheio
		if (!Vaga.temVagaLivre()) {
			throw new EstacionamentoException("Estacionamento lotado");
		}
		
		// verificar o padrão de string da placa
		if (!EstacionamentoUtil.validarPadraoPlaca(placa)) {
			throw new VeiculoException("Placa informada inválida");

		}
		
		// criar uma instância do veículo
		Veiculo veiculo = new Veiculo(placa, marca, modelo, cor);
		
		// criar a movimentação vinculando o veículo e com data de entrada corrente
		Movimentacao movimentacao = new Movimentacao(veiculo, LocalDateTime.now());
		
		// registrar na base de dados a informação
		DAOEstacionamento dao = new DAOEstacionamento();
		dao.criar(movimentacao);
		
		// atualizar o número de vagas ocupadas
		Vaga.entrou();
		// fim
		// TODO implementar
	}

	public Movimentacao processarSaida(String placa) throws VeiculoException, EstacionamentoException {
		//Validar a placa
			if (!EstacionamentoUtil.validarPadraoPlaca(placa)) {
				throw new VeiculoException("Placa inválida");
		}

		//Buscar a movimentação aberta baseada na placa
			DAOEstacionamento dao = new DAOEstacionamento();
			Movimentacao movimentacao = dao.buscarMovimentacaAberta(placa);
			
			if (movimentacao == null) {
				throw new EstacionamentoException("Veículo não encontrado!");
			}

		//Fazer o cálculo do valor a ser pago
			movimentacao.setDataHoraSaida(LocalDateTime.now());
			EstacionamentoUtil.calcularValorPago(movimentacao);

		//Atualizar os dados da movimentação
			dao.atualizar(movimentacao);

		//Atualizar o status da vaga
			Vaga.saiu();


		return movimentacao;
	}

	public List<Movimentacao> emitirRelatorio(LocalDateTime data) {
		DAOEstacionamento dao = new DAOEstacionamento();
		
		return dao.consultarMovimentacoes(data);
	}

	public int inicializarOcupadas() {
		DAOEstacionamento dao = new DAOEstacionamento();
		return dao.getOcupadas();
	}

}
