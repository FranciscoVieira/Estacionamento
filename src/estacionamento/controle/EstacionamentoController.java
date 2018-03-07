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
	 * A partir dos dados do ve�culo informados pelo operador, realiza o fluxo
	 * de entrada do ve�culo no estacionamento registrando a movimenta��o
	 * gerada.
	 * 
	 * @param placa
	 *            Placa do ve�culo
	 * @param marca
	 *            Marca do ve�culo
	 * @param modelo
	 *            Modelo do ve�culo
	 * @param cor
	 *            cor do ve�culo
	 * @throws EstacionamentoException
	 *             Quando estacionamento estiver lotado
	 * @throws VeiculoException
	 *             Quando o padr�o da placa for inv�lido
	 */

	public void processarEntrada(String placa, String marca, String modelo, String cor)
			throws EstacionamentoException, VeiculoException {
		// verificar se o estacionamento est� cheio
		if (!Vaga.temVagaLivre()) {
			throw new EstacionamentoException("Estacionamento lotado");
		}
		
		// verificar o padr�o de string da placa
		if (!EstacionamentoUtil.validarPadraoPlaca(placa)) {
			throw new VeiculoException("Placa informada inv�lida");

		}
		
		// criar uma inst�ncia do ve�culo
		Veiculo veiculo = new Veiculo(placa, marca, modelo, cor);
		
		// criar a movimenta��o vinculando o ve�culo e com data de entrada corrente
		Movimentacao movimentacao = new Movimentacao(veiculo, LocalDateTime.now());
		
		// registrar na base de dados a informa��o
		DAOEstacionamento dao = new DAOEstacionamento();
		dao.criar(movimentacao);
		
		// atualizar o n�mero de vagas ocupadas
		Vaga.entrou();
		// fim
		// TODO implementar
	}

	public Movimentacao processarSaida(String placa) throws VeiculoException, EstacionamentoException {
		//Validar a placa
			if (!EstacionamentoUtil.validarPadraoPlaca(placa)) {
				throw new VeiculoException("Placa inv�lida");
		}

		//Buscar a movimenta��o aberta baseada na placa
			DAOEstacionamento dao = new DAOEstacionamento();
			Movimentacao movimentacao = dao.buscarMovimentacaAberta(placa);
			
			if (movimentacao == null) {
				throw new EstacionamentoException("Ve�culo n�o encontrado!");
			}

		//Fazer o c�lculo do valor a ser pago
			movimentacao.setDataHoraSaida(LocalDateTime.now());
			EstacionamentoUtil.calcularValorPago(movimentacao);

		//Atualizar os dados da movimenta��o
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
