package regras_negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
        }





    public static boolean validarIndividuo(String nomeindividuo,String senha) {

        if(repositorio.localizarIndividual(nomeindividuo)!=null)
        {
            return true;
        }

        return false;


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

    }




    public static void criarGrupo(String nome) throws Exception {
        if(nome.isEmpty())
            throw new Exception("criar grupo - nome vazio:");

        Participante p = repositorio.localizarParticipante(nome);
        if(p != null)
            throw new Exception("criar grupo - nome ja existe:" + nome);

        Grupo grp = new Grupo(nome);
        repositorio.adicionarParticipante(grp);
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
                    int idCopia = id;
                    String textoCopia = emitente.getNome() + "/" + texto;
                    Mensagem copia = new Mensagem(idCopia, textoCopia, emitente, ind, msg.getDatahora());
                    grp.adicionarEnviadas(copia);
                    ind.adicionarRecebidas(copia);
                    repositorio.adicionarMensagem(copia);
                }
            }
        }
    }




    public static ArrayList<Mensagem> obterConversa(String nomeindividuo, String nomedestinatario) throws Exception{


        Individual emitente = repositorio.localizarIndividual(nomeindividuo);
        if(emitente == null)
            throw new Exception("Uusário emitente nao encontrado");

        Participante destinatario = repositorio.localizarParticipante(nomedestinatario);
        if(destinatario == null)
            throw new Exception("Uusário destinitário nao encontrado");


        ArrayList<Mensagem> enviadas= emitente.getEnviadas();
        ArrayList<Mensagem> recebidas= emitente.getRecebidas();
        ArrayList<Mensagem> conversa= new ArrayList<>();


        for(Mensagem m : enviadas) {
            if(m.getDestinatario().getNome()==nomedestinatario) {
                conversa.add(m);
            }
        }

        for(Mensagem m : recebidas) {
            if(m.getEmitente().getNome()==nomedestinatario) {
                conversa.add(m);
            }
        }


        conversa.sort(new Comparator<Mensagem>() {
            public int compare(Mensagem m1,Mensagem m2) {
                return Integer.compare(m1.getId(), m2.getId());
            }
        });
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

}