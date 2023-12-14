package modelo;

import java.util.ArrayList;

public class Evento {

	// Atributos
	private int id;
	private String data;
	private String descricao;
	private int capacidade;
	private double preco;
	private ArrayList<Ingresso> ingressos = new ArrayList<Ingresso>(); // Relacionamento 1:N com a classe Ingresso
	
	// Construtor
	public Evento(int id, String data, String descricao, int capacidade, double preco) {
		this.id = id;
		this.data = data;
		this.descricao = descricao;
		this.capacidade = capacidade;
		this.preco = preco;
	}
	
	// Métodos
	public void adicionar(Ingresso i) {
		ingressos.add(i);
	}
	
	public void remover(Ingresso i) {
		ingressos.remove(i);
	}
	
	public Ingresso localizar(String codigo) {
		for(Ingresso i : ingressos) {
			if(i.getCodigo().equals(codigo)) {
				return i;
			}
		}
		return null;
	}
	
	// Verifica se o evento está lotado
	public boolean lotado() {
	    return ingressos.size() >= capacidade;
	}
	
	// Retorna a quantidade de ingressos
	public int quantidadeIngressos() {
		return ingressos.size(); // A ser testado
	}
	
	// Retorna o valor total arrecadado pelo evento
	public double totalArrecadado() {
		double total = 0.0;
		
		for (Ingresso i : ingressos) {
			total += i.calcularPreco(); // Calcula o valor do ingresso, já com o desconto, e o incrementa na variável total que será retornada
		}
		return total;
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getData() {
		return data;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public double getPreco() {
		return preco;
	}

	private ArrayList<Ingresso> getIngressos() {
		return ingressos;
	}
	
	// Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	// ToString
	@Override
	public String toString() {
		return "Evento: ID = " + id + "| Data = " + data + "| Desc = " + descricao + "| Capac = " + capacidade + "| Preco= R$ " + preco + ";";
	}
}
