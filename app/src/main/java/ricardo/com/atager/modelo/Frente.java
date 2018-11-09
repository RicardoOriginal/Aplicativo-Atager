package ricardo.com.atager.modelo;

public class Frente extends Divisao{
    private int frente_id;
    private String frente_nome;

    public Frente() {
    }

    public int getFrente_id() {
        return frente_id;
    }

    public void setFrente_id(int frente_id) {
        this.frente_id = frente_id;
    }

    public String getFrente_nome() {
        return frente_nome;
    }

    public void setFrente_nome(String frente_nome) {
        this.frente_nome = frente_nome;
    }
}