package ricardo.com.atager.modelo;

public class Unidade extends Empresa{
    private int unidade_id;
    private String unidade_nome;

    public Unidade() {
    }

    public int getUnidade_id() {
        return unidade_id;
    }

    public void setUnidade_id(int unidade_id) {
        this.unidade_id = unidade_id;
    }

    public String getUnidade_nome() {
        return unidade_nome;
    }

    public void setUnidade_nome(String unidade_nome) {
        this.unidade_nome = unidade_nome;
    }
}