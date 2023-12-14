package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ingresso {
	
	private String codigo;
	private String telefone;
	
	private Evento evento; // Relacionamento 1:1 com a classe Evento
	private Participante participante; // Relacionamento 1:1 com a classe Participante
	
	public Ingresso(String codigo, String telefone) {
		this.codigo = codigo;
		this.telefone = telefone;
	}

	// Getters e setters
	public String getCodigo() {
		return codigo;
	}

	public String getTelefone() {
		return telefone;
	}

	public Evento getEvento() {
		return evento;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	
	public void setParticipante(Participante participante) {
		this.participante = participante;
	}
	
	// O preço do ingresso é o preço do evento menos o desconto que é calculado de acordo com a idade do
	// participante < 18 = preco do evento - 10% ; >= 60 = preco do evento - 20% ; Um convidado de uma empresa acumula o desconto da idade com um desconto de 50% no preço do
	// evento e entre > 18 e <=60 não tem desconto
	public double calcularPreco() {
		double precoEvento = evento.getPreco(); // classe Evento
	    int idadeParticipante = participante.calcularIdade(); // Supondo que a classe Participante tenha um método getIdade() que retorna a idade do participante
	    boolean convidadoEmpresa = (participante instanceof Convidado); // Supondo que a classe Participante tenha um método isConvidadoEmpresa() que retorna se é um convidado de uma empresa

	    double descontoIdade = 0;
	    double descontoEmpresa = 0;

	    if (idadeParticipante < 18) {
	        descontoIdade = 0.10; // 10% de desconto para participantes com menos de 18 anos
	    } else if (idadeParticipante >= 60) {
	        descontoIdade = 0.20; // 20% de desconto para participantes com 60 anos ou mais
	    }

	    if (convidadoEmpresa) {
	        descontoEmpresa = 0.50; // 50% de desconto para convidados de empresas
	    }

	    double precoComDesconto = precoEvento - (precoEvento * (descontoIdade + descontoEmpresa));

	    return precoComDesconto > 0 ? precoComDesconto : 0; // Garante que o preço nunca seja negativo
	}
	
	// Método para verificar se o último ingresso está ultrapassado
	 public boolean verificaIngressoUltrapassado() {
	        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate dataEvento = LocalDate.parse(this.getEvento().getData(), f);
	        LocalDate hoje = LocalDate.now();
	        
	        // Se a data do evento for maior do que a data de hoje, retorna verdadeiro, provando que a data do evento não está ultrapassada
	        return dataEvento.isAfter(hoje); 
	    }

	@Override
	public String toString() {
        return "Ingresso: Cod = " + codigo + "| Tel = " + telefone + "| Cod Evento = " + evento.getId() + "| CPF = " + participante.getCpf();
    }
}
