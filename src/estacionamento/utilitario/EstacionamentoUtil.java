package estacionamento.utilitario;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import estacionamento.negocio.Movimentacao;
import estacionamento.negocio.Tarifario;

/**
 * Representa uma classe de apoio às demais do sistema
 * @author Francisco Vieria
 *
 */
public class EstacionamentoUtil {
	
	/**
	 * Valida a placa com o valor padrao, LLL - NNN
	 * L = Letra
	 * N = Número
	 * @param placa Placa do veículo
	 * @return true se atender padrão e false senão
	 */
	public static boolean validarPadraoPlaca(String placa) {
		String padrao = "[A-Z][A-Z][A-Z]-\\d\\d\\d\\d";
		Pattern p = Pattern.compile(padrao);
		Matcher m = p.matcher(placa);
		return m.matches();
	}
	
	/**
	 * O cálculo da estada do veículo baseado no tarifário e
	 * na hora de entrada e saída do veículo.
	 * 
	 * Altera a própria instância do parâmetro 
	 * 
	 * @param movimentacao Instancia de movimentação
	 */
	public void calcularPagemento(Movimentacao movimentacao) {
		
		
	}
/**
 * Recupera uma prorpriedade do arquivo de configuração da aplicação.
 * Configuration.txt
 * @param propriedade
 * @return Valor associado a propriedade
 */
	public static String get(String propriedade) {
		
		Properties prop = new Properties();
		String valor = null;
		try {
			prop.load(EstacionamentoUtil.class.getResourceAsStream("/recursos/configuration.txt"));
			valor = prop.getProperty(propriedade);
		} catch (IOException e) {
			
			assert false : "Configuração não disponível";
		}


		return valor;
	}

public static String getDataAsString(LocalDateTime dataHoraEntrada) {
	return dataHoraEntrada.toString();
}

public static void calcularValorPago(Movimentacao movimentacao) {
	LocalDateTime inicio = movimentacao.getDataHoraEntrada();
	LocalDateTime fim = movimentacao.getDataHoraSaida();
	double valor = 0;
	
	long diffHoras = inicio.until(fim, ChronoUnit.HOURS);
	
	if (diffHoras > 0) {
		valor += Tarifario.VALOR_HORA;
		fim = fim.minus(1, ChronoUnit.HOURS);
	}
	
	long diffMinutos = inicio.until(fim, ChronoUnit.MINUTES);
	valor += (diffMinutos/Tarifario.INCREMENTO_MINUTOS) * Tarifario.VALOR_INCREMENTAL;
	movimentacao.setValor(valor);
	
}

public static LocalDateTime getDate(String rdataEntrada) {
	
	return LocalDateTime.parse(rdataEntrada, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
}

public static String getDisplayData(LocalDateTime data) {
	return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
}

public static String gerarTextoFaturamento(LocalDateTime data, List<Movimentacao> movimentacoes) {
	double totalFaturado = 0;
	String texto = "";
	for (Movimentacao movimentacao : movimentacoes) {
		totalFaturado += movimentacao.getValor();
	}
	
	String sAno = "" + data.getYear();
	String sMes = data.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
	
	texto = "Faturamento do mês de " + sMes;
	texto += " de " + sAno + "foi de R$ " + totalFaturado;
	
	return null;
}

}
