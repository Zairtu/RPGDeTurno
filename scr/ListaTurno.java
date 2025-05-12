public class ListaTurno {
    No inicio;

    public void adicionar(Personagem p) {
        No novoNo =new No(p);
        if (inicio ==null) {
            inicio = novoNo;
        } else {
            No atual= inicio;
            while (atual.proximo != null) {
                atual = atual.proximo;
            }
            atual.proximo = novoNo;
        }
    }
    public Personagem proximo() {
        if (inicio == null) return null;
        Personagem personagem = inicio.personagem;
        inicio = inicio.proximo;
        return personagem;
    }

    public boolean temProximo() {
        return inicio != null;
    }
}
