package estacionamento.negocio;

import estacionamento.controle.EstacionamentoController;

/**
 * Representa as informa��es relativas � vagas do estacionamento ou status de
 * ocupa��o.
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
	 * @return true Se houver alguma vaga livre e n�o caso se estiver lotado.
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
	 * Retorna o n�mero de vagas ocupadas.
	 * 
	 * @return n�mero total de vagas ocupadas num determindo instante.
	 */
	public static int ocupadas() {
		return Vaga.vagasOcupadas;
	}

	/**
	 * Retorna o n�mero de vagas livres
	 * 
	 * @return Retorna o total de vagas livres num determinado instante.
	 */

	public static int livres() {
		return TOTAL_VAGAS - Vaga.vagasOcupadas;
	}

	/**
	 * Atualiza o n�mero de vagas ocupadas ap�s a entrada do ve�culo.
	 * 
	 */
	public static void entrou() {
		Vaga.vagasOcupadas++;
	}

	public static void saiu() {
		Vaga.vagasOcupadas--;
		
	}

}
