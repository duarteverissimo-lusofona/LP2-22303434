package pt.ulusofona.lp2.greatprogrammingjourney.event;

public abstract class Evento {
    String nome;
    Integer id;
    String imagem;

    public Evento(String nome, Integer id, String imagem) {
        this.nome = nome;
        this.id = id;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public Integer getId() {
        return id;
    }

    public String getImagem() {
        return imagem;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public abstract void interargirJogador();
    
    // Polimorfismo: permite distinguir Tool de Abyss sem usar instanceof
    public abstract boolean isTool();

    @Override
    public String toString() {
        return nome;
    }
}
