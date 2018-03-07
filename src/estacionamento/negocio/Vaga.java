package estacionamento.negocio;

import estacionamento.controle.EstacionamentoController;

/**
 * Representa as informações relativas à vagas do estacionamento ou status de
 * ocupação.
 * 
 * @author Francisco Vieria
 *
 */
public class Vaga {

	public static int TOTAL_VAGAS = 100;
	private static int vagasOcupadas = inicializarOcupadas();;
	
	//Construtor privado
	private Vaga() {
		
	}

	/**
	 * Verificar se existe vagas livres no estacionamento
	 * 
	 * @return true Se houver alguma vaga livre e não caso se estiver lotado.
	 */
	public static boolean temVagaLivre() {
		return (vagasOcupadas < TOTAL_VAGAS);
	}

	/**
	 * Buscar o status atual das vagas do estacionamento.
	 */
	public static int inicializarOcupadas() {
		EstacionamentoController controle = new EstacionamentoController();
		return controle.inicializarOcupadas();
	}

	/**
	 * Retorna o número de vagas ocupadas.
	 * 
	 * @return número total de vagas ocupadas num determindo instante.
	 */
	public static int ocupadas() {
		return Vaga.vagasOcupadas;
	}

	/**
	 * Retorna o número de vagas livres
	 * 
	 * @return Retorna o total de vagas livres num determinado instante.
	 */

	public static int livres() {
		return TOTAL_VAGAS - Vaga.vagasOcupadas;
	}

	/**
	 * Atualiza o número de vagas ocupadas após a entrada do veículo.
	 * 
	 */
	public static void entrou() {
		Vaga.vagasOcupadas++;
	}

	public static void saiu() {
		Vaga.vagasOcupadas--;
		
	}

}
