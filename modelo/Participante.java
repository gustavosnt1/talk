package modelo;

import java.util.ArrayList;

public class Participante {


    private String nome;
    private ArrayList<Mensagem> recebidas=new ArrayList<>();
    private ArrayList<Mensagem> enviadas= new ArrayList<>();


    public Participante(String nome) {

        this.nome=nome;

    }

    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public void adicionarRecebidas(Mensagem msg){
        recebidas.add(msg);
    }

    public void adicionarEnviadas(Mensagem msg){
        enviadas.add(msg);
    }

    public void removerRecebidas(Mensagem msg){
        recebidas.remove(msg);
    }

    public void removerEnviadas(Mensagem msg){
        enviadas.remove(msg);
    }


    public ArrayList<Mensagem> getRecebidas() {
        return recebidas;
    }

    public ArrayList<Mensagem> getEnviadas() {
        return enviadas;
    }


    public Mensagem localizarEnviada(int id) {

        for(Mensagem m : this.getEnviadas()) {
            if(m.getId()==id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        String string = "nome" + nome + "mensagens enviadas: ";
        for(Mensagem msg : enviadas) {
            string += "-->" + msg;
        }

        string += "mensagens recebidas:";
        for(Mensagem msg : recebidas){
            string += "-->" + msg;
        }
        return string;
    }

}