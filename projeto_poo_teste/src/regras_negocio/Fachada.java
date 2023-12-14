package regras_negocio;

import java.util.ArrayList;

import modelo.Convidado;
import modelo.Evento;
import modelo.Ingresso;
import modelo.Participante;
import repositorio.Repositorio;

public class Fachada {
	private static Repositorio repositorio = new Repositorio();
	
	private Fachada() {}
	
	// CriaEvento
	public static void criarEvento(String data, String descricao, int capacidade, double preco) throws Exception{
		
		// 2-Um evento pode ter preço zero, mas nunca negativo 
		if(preco < 0) {
			throw new Exception("Preco do evento não pode ser negativo:" + preco); 
		}
		
		//3-A data e a descrição do evento são obrigatórias
		if(data == null || data.isEmpty()) {
			throw new Exception("A data do evento é obrigatória"); 
		}
		if(descricao == null || descricao.isEmpty()) {
			throw new Exception("A descrição do evento é obrigatório"); 
		}
		
		//4-A capacidade do evento deve ser de no mínimo 2 ingressos
		if(capacidade < 2) {
			throw new Exception("A capacidade do evento deve ser de no mínimo 2 ingressos");
		}
		
		
		int id = repositorio.gerarId();
		Evento e = new Evento(id, data, descricao, capacidade, preco);
		
		repositorio.adicionar(e);
		repositorio.salvarObjetos();
	}
	
	// CriaParticipante 
	public static void criarParticipante(String cpf, String nascimento) throws Exception{
		if(nascimento == null || nascimento.isEmpty()) {
			throw new Exception("Favor informar uma data"); // Lança uma exceção caso não seja informada uma data
		}
		
		Participante p = repositorio.localizarParticipante(cpf); // Verifica se o participante já está cadastrado
		
		if(p != null) { // Caso p seja diferente de nulo, ou seja, caso já exista algo com aquele mesmo cpf (que é o identificador do participante), lança uma exceção
			throw new Exception("Participante já cadastrado");
		}
		p = new Participante(cpf, nascimento); // Adiciona ao array
		repositorio.adicionar(p); // Adiciona ao repositório
		repositorio.salvarObjetos();
	}
	
	// CriaConvidado
	public static void criarConvidado(String cpf, String nascimento, String empresa) throws Exception{
		if(empresa.isBlank() || empresa.isEmpty() || empresa == null) {
			throw new Exception("Favor informar o nome de uma empresa");
		}
		Convidado c = (Convidado) repositorio.localizarParticipante(cpf); // Como o método localizarParticipante retorna um objeto do tipo Participante, é necessário fazer o casting
		
		if(c != null) {
			throw new Exception("Convidado já cadastrado");
		}
		
		// Verificar se é necessário criar um método diferente para os Convidados na classe Repositorio
		c = new Convidado(cpf, nascimento, empresa);
		repositorio.adicionar(c);
		repositorio.salvarObjetos();
	}
	
	//	CriaIngresso
	public static void criarIngresso(int id, String cpf, String telefone) throws Exception {
		Evento e = repositorio.localizarEvento(id);
		Participante p = repositorio.localizarParticipante(cpf);
		
		// 12-Um ingresso possui um telefone de contato obrigatório, que pode ser de qualquer pessoa
		if (telefone == null || telefone.isEmpty()) {
			throw new Exception("O número do telefone é obrigatório"); 
		}
		
		// Lança exceção caso o evento não exista
		if (e == null) {
			throw new Exception("Evento não encontrado");
		}
		
		// Lança exceção caso o participante não exista
		if (p == null) {
			throw new Exception("Participante não encontrado"); 
		}
		
		// Lança exceção caso o evento esteja lotado
		if(e.lotado()) {
			throw new Exception("Evento lotado"); 
		}
		
		// 11-Um ingresso é identificado por um código gerado pelo sistema no formato id + “-“ + cpf
		String codigo = id + "-" + cpf;
		Ingresso ingressoExistente = repositorio.localizarIngresso(codigo);
		
		if(ingressoExistente != null) {
			throw new Exception("Ingresso já cadastrado");
		}
		
		// Criando ingresso 
		Ingresso ingresso = new Ingresso(codigo, telefone);
	        
		// Configura relacionamento bidirecional
		ingresso.setEvento(e);
		ingresso.setParticipante(p);
	        
		// Adiciona o ingresso ao evento e ao participante
		e.adicionar(ingresso);
		p.adicionar(ingresso);
	        
		// Adicionar o ingresso ao repositório
		repositorio.adicionar(ingresso);
	        
		// Salvar objetos no repositório
		repositorio.salvarObjetos();
	}
	
	// listarEventos
	public static ArrayList<Evento> listarEventos() {
		return repositorio.getEventos();
	}
		
	// listarParticipantes
	public static ArrayList<Participante> listarParticipantes() {
		return repositorio.getParticipantes();
	}

	// listarIngressos
	public static ArrayList<Ingresso> listarIngressos() {
		return repositorio.getIngressos();
	}
	    
	// apagarEvento
	public static void apagarEvento(int id) throws Exception{
		Evento e = repositorio.localizarEvento(id);
		
		// Se evento não existir
		if (e == null) {
			throw new Exception("Evento não encontrado"); 
		}
		
		// Caso o evento possua ingressos, não será possível apagá-lo e será lançada uma exceção
		if(e.quantidadeIngressos() > 0) {
			throw new Exception("O evento possui ingressos"); 
		}
	    	
		repositorio.remover(e);
		repositorio.salvarObjetos();
	}
	    
	// apagarIngresso
	public static void apagarIngresso(String codigo) throws Exception{
		Ingresso i = repositorio.localizarIngresso(codigo);
	        
		if(i == null) {
			throw new Exception("Ingresso não encontrado");
		}
		
		// Remove o ingresso do evento
		i.getEvento().remover(i); 
		
		// Remove o ingresso do participante
		i.getParticipante().remover(i); 
	    
		// Remove o ingresso do repositório
		repositorio.remover(i);
		
		// Salva objetos no repositório
		repositorio.salvarObjetos(); 
	}
	    
	// apagarParticipante
	public static void apagarParticipante(String cpf) throws Exception {
		Participante p = repositorio.localizarParticipante(cpf);
		
		//Caso o participante não seja encontrado
		if (p == null) { 
			throw new Exception("Participante não encontrado");
		}

		// Verifica se o último ingresso do participante está ultrapassado
		Ingresso ultimoIngresso;

		if (p.getIngressos().isEmpty()) {
			ultimoIngresso = null;
		}
		else {
			ultimoIngresso = p.getIngressos().get(p.getIngressos().size() - 1);
		}

		if (ultimoIngresso != null && ultimoIngresso.verificaIngressoUltrapassado()) {
		    throw new Exception("O último ingresso não está ultrapassado");
		}

		// Remove todos os ingressos associados ao participante
		for (Ingresso ingresso : new ArrayList<>(p.getIngressos())) {
			apagarIngresso(ingresso.getCodigo());
		}

		// Remove o participante do repositório
		repositorio.remover(p);
		repositorio.salvarObjetos();
	}
}
