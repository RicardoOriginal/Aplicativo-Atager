package ricardo.com.atager.modelo;

public class Maquina extends TipoMaquina {
    private int maquina_id;
    private int Frota;
    private String modelo;
    private Double horas_trabalhadas;
    private boolean ativo;

    public Maquina() {
    }

    public int getMaquina_id() {
        return maquina_id;
    }

    public void setMaquina_id(int maquina_id) {
        this.maquina_id = maquina_id;
    }

    public int getFrota() {
        return Frota;
    }

    public void setFrota(int Frota) {
        this.Frota = Frota;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Double getHoras_trabalhadas() {
        return horas_trabalhadas;
    }

    public void setHoras_trabalhadas(Double horas_trabalhadas) {
        this.horas_trabalhadas = horas_trabalhadas;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}