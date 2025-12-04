package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.Arrays;

public class Jogador {

    int id;
    String nome;
    Cor cor;
    ArrayList<String> linguagensProgramacao;
    Estado estado;
    ArrayList<String> ferramentas;


    public Jogador(int id, String nome, Cor cor, ArrayList<String> linguagensProgramacao, int posicao) {
        this.id = id;
        this.nome = nome;
        this.cor = cor;
        this.linguagensProgramacao = linguagensProgramacao;
        this.estado = Estado.EM_JOGO;
        this.ferramentas = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Cor getCor() {
        return cor;
    }

    public ArrayList<String> getLinguagens() {
        return linguagensProgramacao;
    }

    public ArrayList<String> getFerramentas() {
        return ferramentas;
    }

    public void addFerramenta(String ferramenta) {
        this.ferramentas.add(ferramenta);
    }

    public ArrayList<String> ordenarLang(){

        ArrayList<String> lingOrdenadas = new ArrayList<>(linguagensProgramacao);
        lingOrdenadas.sort(String.CASE_INSENSITIVE_ORDER);
        return lingOrdenadas;
    }



    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }


    @Override
    public String toString() {
        return "Jogador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cor=" + cor +
                ", lp=" + linguagensProgramacao +
                ", estado=" + estado +
                '}';
    }
}
