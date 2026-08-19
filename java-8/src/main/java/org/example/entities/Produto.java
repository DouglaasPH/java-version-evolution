package org.example.entities;

import java.math.BigDecimal;

public class Produto {
    private final String nome;
    private final BigDecimal preco;

    public Produto(String nome, BigDecimal preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "nome='" + nome + '\'' +
                ", preco=" + preco +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return nome.equals(produto.nome) && preco.equals(produto.preco);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + preco.hashCode();
        return result;
    }
}
