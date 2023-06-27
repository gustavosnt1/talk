package modelo;

import java.util.ArrayList;

public class Individual extends Participante {


    private String senha;
    private boolean administrador;
    private ArrayList<Grupo> grupos=new ArrayList<>();

    public Individual(String nome, String senha, boolean administrador) {
        super(nome);
        this.senha=senha;
        this.administrador=administrador;
        //
    }



    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean getAdministrador() {
        return administrador;
    }

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }

    public void adicionarGrupo(Grupo grp){
        grupos.add(grp);
    }

    public void removerGrupo(Grupo grp){
        grupos.remove(grp);
    }

    @Override
    public String toString(){
        String string = super.toString() + "Grupos:";
        for(Grupo grp : grupos){
            string += "-->" + grp.getNome();
        }
        return string;
    }

}