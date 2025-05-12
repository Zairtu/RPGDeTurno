public class Personagem {
    private String nome;
    private int vida,ataque;

    public Personagem(String nome, int vida, int ataque) {
        this.nome = nome;
        this.vida = vida;
        this.ataque = ataque;
    }

    public String getStatus() {
        return nome + " (Vida: " + vida + ")";
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVida() {
        return vida;
    }
    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getAtaque() {
        return ataque;
    }
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public void atacar(Personagem alvo) {
        System.out.println(nome + " ataca " +alvo.getNome() + "!");
        alvo.setVida(alvo.getVida() - this.ataque);
        System.out.println(alvo.getNome() + " agora tem " +alvo.getVida() + " da vida.");

    }

    public void receberDano(int dano) {
        this.vida -= dano;
        if (this.vida < 0) this.vida = 0;
    }

    public boolean estaVivo() {
        return vida > 0;
    }
}
