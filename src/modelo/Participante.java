package modelo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Participante {
    private String nome;
    private ArrayList<Mensagem> recebidas;
    private ArrayList<Mensagem> enviadas;

    public Participante(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public ArrayList<Mensagem> getRecebidas() {
        return recebidas;
    }


    public void setRecebidas(ArrayList<Mensagem> recebidas) {
        this.recebidas = recebidas;
    }


    public ArrayList<Mensagem> getEnviadas() {
        return enviadas;
    }


    public void setEnviadas(ArrayList<Mensagem> enviadas) {
        this.enviadas = enviadas;
    }
}

// falta o ToString