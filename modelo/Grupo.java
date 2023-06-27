package modelo;

import java.util.ArrayList;

public class Grupo extends Participante {


    private ArrayList<Individual> individuos=new ArrayList<>();

    public Grupo(String nome ) {
        super(nome);

    }
    public ArrayList<Individual> getIndividuos() {
        return individuos;
    }

    public void adicionarIndividual(Individual ind) {
        individuos.add(ind);
    }

    public void removerIndividual(Individual ind){
        individuos.remove(ind);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append("\nindividuos do grupo:");
        for (Individual ind : individuos) {
            builder.append("\n-->").append(ind.getNome());
        }
        return builder.toString();
    }

}