package modelo;

public class Convidado extends Participante {
	
	private String empresa;
	
	public Convidado(String cpf, String nascimento, String empresa) {
		super(cpf, nascimento); // Indica que é uma subclasse e que herdará estes atributos da superclasse Participante
		this.empresa = empresa;
	}
	
	public String getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	@Override
	public String toString() {
		return "Convidado: CPF = " + getCpf() + "| Data nasc. = " + getNascimento() + "| Emp = " + empresa + "| Ingressos = " + getIngressos() + ";";
	}
}
