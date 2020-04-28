package com.example.finderfood;

//Classe para armazenar as receitas criadas
public class Feed {

    private String username;
    private String tituloReceitaç;
    private String descricao;
    private String fotoUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTituloReceitaç() {
        return tituloReceitaç;
    }

    public void setTituloReceitaç(String tituloReceitaç) {
        this.tituloReceitaç = tituloReceitaç;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
