package ricardo.com.atager.modelo;

public class Empresa extends Colaborador{
    private int empresa_id;
    private String empresa_nome;

    public Empresa() {
    }

    public int getEmpresa_id() {
        return empresa_id;
    }

    public void setEmpresa_id(int empresa_id) {
        this.empresa_id = empresa_id;
    }

    public String getEmpresa_nome() {
        return empresa_nome;
    }

    public void setEmpresa_nome(String empresa_nome) {
        this.empresa_nome = empresa_nome;
    }
}