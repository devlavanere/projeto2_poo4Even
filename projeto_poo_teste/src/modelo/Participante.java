package modelo;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Participante {
	
	private String cpf;
	private String nascimento;
	private ArrayList<Ingresso> ingressos = new ArrayList<Ingresso>(); // Relacionamento 1:N com a classe Ingresso
	
	public Participante(String cpf, String nascimento) {
		super();
		this.cpf = cpf;
		this.nascimento = nascimento;
	}
	
	public void adicionar(Ingresso i) {
		ingressos.add(i);
	}
	
	public void remover(Ingresso i) {
		ingressos.remove(i);
	}
	
	public Ingresso localizar(String codigo) { // Verificar se este método é realmente necessário, visto que apenas segui o exemplo da classe Prateleteira
		for(Ingresso i : ingressos) {
			if(i.getCodigo().equals(codigo)) {
				return i;
			}
		}
		return null;
	}
	
	// Calcula a idade do participante
	public int calcularIdade() {
		DateTimeFormatter f;
		// Setando o tipo de formatação da data
		f = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
		
		// Aplicando a formatação à idade do participante
		LocalDate dataNasc = LocalDate.parse(this.nascimento, f); 
		// Obtendo a data atual
        LocalDate hoje = LocalDate.now(); 
        // Calculando a diferença entre as datas
        Period periodo = Period.between(dataNasc, hoje); 
        
        // Retornando a idade (em anos)
        return periodo.getYears(); 
	}
	
	
	public String getCpf() {
		return cpf;
	}
	
	public String getNascimento() {
		return nascimento;
	}

	public ArrayList<Ingresso> getIngressos() {
		return ingressos;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setNascimento(String nascimento) {
		this.nascimento = nascimento;
	}
	
	@Override
	public String toString() {
		return "Participante: CPF = " + cpf + "| Data nasc. = " + nascimento + "| Ingressos = " + ingressos + ";";
	}
}

