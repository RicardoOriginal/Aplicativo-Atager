package ricardo.com.atager.modelo;

public class Divisao extends Unidade{
    private int divisao_id;
    private String divisao_nome;

    public Divisao() {
    }

    public int getDivisao_id() {
        return divisao_id;
    }

    public void setDivisao_id(int divisao_id) {
        this.divisao_id = divisao_id;
    }

    public String getDivisao_nome() {
        return divisao_nome;
    }

    public void setDivisao_nome(String divisao_nome) {
        this.divisao_nome = divisao_nome;
    }
}