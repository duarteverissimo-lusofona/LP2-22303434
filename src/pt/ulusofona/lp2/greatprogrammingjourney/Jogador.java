package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;

public class Jogador {

    int id;
    String nome;
    Cor cor;
    ArrayList<String> linguagensProgramacao;
    Estado estado;
    ArrayList<String> ferramentas;
    String primeiraLinguagem;  // Guarda a primeira linguagem para verificar restrições de movimento


    public Jogador(int id, String nome, Cor cor, ArrayList<String> linguagensProgramacao, int posicao) {
        this.id = id;
        this.nome = nome;
        this.cor = cor;
        this.linguagensProgramacao = linguagensProgramacao;
        this.estado = Estado.EM_JOGO;
        this.ferramentas = new ArrayList<>();
        
        // Guardar a primeira linguagem (para restrições de movimento)
        if (linguagensProgramacao != null && !linguagensProgramacao.isEmpty()) {
            this.primeiraLinguagem = linguagensProgramacao.get(0).trim();
        } else {
            this.primeiraLinguagem = null;
        }
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
    
    public String getPrimeiraLinguagem() {
        return primeiraLinguagem;
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
