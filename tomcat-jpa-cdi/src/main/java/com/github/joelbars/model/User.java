package com.github.joelbars.model;

import javax.persistence.Entity;

@Entity
public class User extends Domain {

    private static final long serialVersionUID = -7506296210385708206L;

    private String nome;
    private String rg;
    private String login;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
