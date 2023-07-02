package regras_negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

import modelo.Grupo;
import modelo.Individual;
import modelo.Mensagem;
import modelo.Participante;
import repositorio.Repositorio;


public class Fachada {

    private static Repositorio repositorio= new Repositorio();


    public  static void criarIndividuo(String nome, String senha) throws Exception {
        if(nome.isEmpty())
            throw new Exception("criar individual - nome vazio:");
        if(senha.isEmpty())
            throw new Exception("criar individual - senha vazia:");

            Participante p = repositorio.localizarParticipante(nome);
            if(p != null)
                throw new Exception("criar individual - nome ja existe:" + nome);


            Individual individuo = new Individual(nome,senha, false);
            repositorio.adicionarParticipante(individuo);
            repositorio.salvarObjetos();
        }


    public static Individual validarIndividuo(String nomeindividuo,String senha) {
        Individual individuo = localizarIndividual(nomeindividuo);
        if(individuo == null) {
            return null;
        }else{
            if(individuo.getSenha().equals(senha)){
                return individuo;
            }else{
                return null;
            }
        }

    }

    public static Individual localizarIndividual(String nomeindividuo) {

        return repositorio.localizarIndividual(nomeindividuo);
    }

    public static void criarAdministrador(String nome,String senha) throws Exception {

        if(nome.isEmpty())
            throw new Exception("criar individual - nome vazio:");
        if(senha.isEmpty())
            throw new Exception("criar individual - senha vazia:");

        Participante p = repositorio.localizarParticipante(nome);
        if(p != null)
            throw new Exception("criar admin - nome ja existe:" + nome);

        Individual individuo = new Individual(nome, senha, true);
        repositorio.adicionarParticipante(individuo);
        repositorio.salvarObjetos();

    }




    public static void criarGrupo(String nome) throws Exception {
        if(nome.isEmpty())
            throw new Exception("criar grupo - nome vazio:");

        Participante p = repositorio.localizarParticipante(nome);
        if(p != null)
            throw new Exception("criar grupo - nome ja existe:" + nome);

        Grupo grp = new Grupo(nome);
        repositorio.adicionarParticipante(grp);
        repositorio.salvarObjetos();
    }



    public static void inserirGrupo(String nomeindividuo,String nomegrupo) throws Exception {
        Individual ind = repositorio.localizarIndividual(nomeindividuo);
        if(ind == null)
            throw new Exception("inserir Grupo - individuo não existe:" + nomeindividuo);

        Grupo grp = repositorio.localizarGrupo(nomegrupo);
        if(grp == null)
            throw new Exception("inserir Grupo - grupo não existe:" + nomegrupo);

        ArrayList<Individual> individuo = grp.getIndividuos();
        grp.adicionarIndividual(ind);
        ind.adicionarGrupo(grp);
        repositorio.salvarObjetos();
        //Falta verificaçao se o individuo ja esta no grupo
    }



    public static void removerGrupo(String nomeindividuo,String nomegrupo) throws Exception {

        Individual ind = repositorio.localizarIndividual(nomeindividuo);
        if(ind == null)
            throw new Exception("remover Grupo - individuo não existe:" + nomeindividuo);

        Grupo grp = repositorio.localizarGrupo(nomegrupo);
        if(grp == null)
            throw new Exception("remover Grupo - grupo não existe:" + nomegrupo);

        if (!grp.getIndividuos().contains(ind))
            throw new Exception("remover Grupo - indivíduo não está no grupo:");

        grp.removerIndividual(ind);
        ind.removerGrupo(grp);
        repositorio.salvarObjetos();

    }


    public static void criarMensagem(String nomeind, String nomedest, String texto) throws Exception {

        if (texto.isEmpty())
            throw new Exception("criar mensagem - texto vazio:");

        Individual emitente = repositorio.localizarIndividual(nomeind);
        if (emitente == null)
            throw new Exception("criar mensagem - emitente nao existe:" + nomeind);

        Participante destinatario = repositorio.localizarParticipante(nomedest);
        if (destinatario == null)
            throw new Exception("criar mensagem - destinatario nao existe:" + nomedest);


        if (destinatario instanceof Grupo g && repositorio.localizarGrupo(g.getNome()) == null)
            throw new Exception("criar mensagem - grupo nao permitido:" + nomedest);

        int id = repositorio.gerarID();
        Mensagem msg = new Mensagem(id, texto, emitente, destinatario, LocalDateTime.now());
        emitente.adicionarEnviadas(msg);
        destinatario.adicionarRecebidas(msg);
        repositorio.adicionarMensagem(msg);

        if (destinatario instanceof Grupo) {
            Grupo grp = (Grupo) destinatario;
            for (Individual ind : grp.getIndividuos()) {
                if (!ind.equals(emitente)) {
                    String textoCopia = emitente.getNome() + "/" + texto;
                    Mensagem copia = new Mensagem(id, textoCopia, emitente, ind, msg.getDatahora());
                    grp.adicionarEnviadas(copia);
                    ind.adicionarRecebidas(copia);
                    repositorio.adicionarMensagem(copia);
                }
            }
        }
        repositorio.salvarObjetos();
    }




    public static ArrayList<Mensagem> obterConversa(String nomeemitente, String nomedestinatario) throws Exception{
        //localizar emitente no repositorio
        Individual emitente = repositorio.localizarIndividual(nomeemitente);
        if(emitente == null)
            throw new Exception("obter conversa - emitente nao existe:" + nomeemitente);

        //localizar destinatario no repositorio
        Participante destinatario = repositorio.localizarParticipante(nomedestinatario);
        if(destinatario == null)
            throw new Exception("obter conversa - destinatario nao existe:" + nomedestinatario);


        ArrayList<Mensagem> lista1 = emitente.getEnviadas();

        ArrayList<Mensagem> lista2 = destinatario.getEnviadas();

        ArrayList<Mensagem> conversa = new ArrayList<>();

        for(Mensagem m : lista1){
            if(m.getDestinatario().equals(destinatario)){
                conversa.add(m);
            }
        }

        for(Mensagem m : lista2){
            if(m.getDestinatario().equals(emitente)){
                conversa.add(m);
            }
        }

        //ordenar a lista conversa pelo id das mensagens
        conversa.sort(new Comparator<Mensagem>() {
            @Override
            public int compare(Mensagem m1, Mensagem m2) {
                return Integer.compare(m1.getId(), m2.getId());
            }
        });

        //retornar a lista conversa
        return conversa;
    }



    public static void apagarMensagem(String nomeindividuo, int id) throws  Exception{
        Individual emitente = repositorio.localizarIndividual(nomeindividuo);
        if(emitente == null)
            throw new Exception("apagar mensagem - nome nao existe:" + nomeindividuo);

        Mensagem m = emitente.localizarEnviada(id);
        if(m == null)
            throw new Exception("apagar mensagem - mensagem nao pertence a este individuo:" + id);

        emitente.removerEnviadas(m);
        Participante destinatario = m.getDestinatario();
        destinatario.removerRecebidas(m);
        repositorio.removerMensagem(m);

        if(destinatario instanceof Grupo g) {
            ArrayList<Mensagem> lista = destinatario.getEnviadas();
            lista.removeIf(new Predicate<>() {
                @Override
                public boolean test(Mensagem t) {
                    if (t.getId() == m.getId()) {
                        t.getDestinatario().removerRecebidas(t);
                        repositorio.removerMensagem(t);
                        return true;
                    } else
                        return false;
                }

            });

        }
        repositorio.salvarObjetos();
    }


    public static  ArrayList<Mensagem> listarMensagensEnviadas(String nome){

        Individual ind= repositorio.localizarIndividual(nome);
        //Falta excecao caso ind nao exista
        return ind.getEnviadas();

    }

    public static  ArrayList<Mensagem> listarMensagensRecebidas(String nome){

        Individual ind= repositorio.localizarIndividual(nome);
        //Falta excecao caso ind nao exista
        return ind.getRecebidas();

    }

    public  static ArrayList<Individual> listarIndividuos()
    {
       return repositorio.getIndividuos();
    }

    public static ArrayList<Grupo> listarGrupos(){

        return repositorio.getGrupos();

    }

    public static ArrayList<Mensagem> listarMensagens(){

        return repositorio.getMensagens();
    }


    public static ArrayList<Mensagem> espionarMensagens(String nomeadmin, String termo) throws Exception{
        Individual individuo = repositorio.localizarIndividual(nomeadmin);
        if(individuo == null)
            throw new Exception("espionar mensagem - individuo não existe:" + nomeadmin);

        if(!individuo.getAdministrador())
            throw new Exception("espionar mensagem - individuo não é administrador:" + nomeadmin);

        if(termo.isEmpty())
            return repositorio.getMensagens();

        ArrayList<Mensagem> espionarMsgs = new ArrayList<>();
        for(Mensagem msg : repositorio.getMensagens()) {
            if(msg.getTexto().contains(termo))
                espionarMsgs.add(msg);
        }
        return espionarMsgs;

    }



    public static ArrayList<String> ausentes(String nomeadmin) throws Exception {
        Individual individuo = repositorio.localizarIndividual(nomeadmin);

        if(!individuo.getAdministrador())
            throw new Exception("ausentes - individuo não é administrador:" + nomeadmin);

        ArrayList<String> ausentes = new ArrayList<>();
        for (Participante part : repositorio.getParticipantes()) {
            if(part.getEnviadas().isEmpty())
                ausentes.add(part.getNome());
        }
        return ausentes;
    }

    /*public static void gravarDados() {
        repositorio.salvarObjetos();
    }

    public static void lerDados() {
        repositorio.carregarObjetos();

        for(Grupo g : repositorio.getGrupos()) {           //Para cada grupo no repositorio
            for (Individual ind : g.getIndividuos()) {     //Para cada indivíduo na lista de participantes de cada grupo
                if(!ind.getGrupos().contains(g))           //Se o grupo ainda não se encontra na lista de grupos que aquele usuário está inserido
                    ind.adicionarGrupo(g);                       //Adiciona o grupo a lista
            }
        }

        for (Mensagem m : repositorio.getMensagens()) {            //Para cada mensagem no repositorio
            Individual emitente = (Individual) m.getEmitente();    //Pega o emitente
            Participante destinatario = m.getDestinatario();       //Pega o destinatário

            if(!emitente.getEnviadas().contains(m))          //Se a mensagem ainda não está na lista de mensagens enviadas do emitente
                emitente.adicionarEnviadas(m);                      //Adiciona

            if(!destinatario.getRecebidas().contains(m))     //Se a mensagem ainda não está na lista de mensagens recebidas do destinatário
                destinatario.adicionarRecebidas(m);                 //Adiciona

            if(destinatario instanceof Grupo) {              //Se o destinatário for um grupo, faz o mesmo processo descrito na linha 178
                Grupo g = (Grupo) destinatario;
                for(Individual ind : g.getIndividuos()) {
                    if(!ind.equals(emitente)) {
                        Mensagem copia = new Mensagem(m.getId(), m.getTexto(), g, ind, m.getDatahora());
                        g.adicionarEnviadas(copia);
                        ind.adicionarRecebidas(copia);
                    }
                }
            }
        }
    }*/

}

