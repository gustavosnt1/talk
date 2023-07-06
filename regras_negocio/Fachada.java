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
        //verifica se texto nome e vazio
        if(nome.isEmpty())
            throw new Exception("criar individual - nome vazio:");
        //verifica se texto senha e vazio
        if(senha.isEmpty())
            throw new Exception("criar individual - senha vazia:");
        //localiza nome repositorio
        Participante p = repositorio.localizarParticipante(nome);
        //verifica se nome ja existe
        if(p != null)
            throw new Exception("criar individual - nome ja existe:" + nome);
        //cria individuo
        Individual individuo = new Individual(nome,senha, false);
        repositorio.adicionarParticipante(individuo);
        repositorio.salvarObjetos();
        }


    public static Individual validarIndividuo(String nomeindividuo,String senha) {
        //localiza individuo
        Individual individuo = repositorio.localizarIndividual(nomeindividuo);
        if (individuo == null) {
            //retorna null se o indivíduo for nulo
            return null;
        } else if (individuo.getSenha().equals(senha)) {
            //retorna o indivíduo se a senha estiver correta
            return individuo;
        } else {
            //retorna null caso contrário
            return null;
        }
    }


    public static void criarAdministrador(String nome,String senha) throws Exception {
        //verifica se texto nome ta vazio
        if(nome.isEmpty())
            throw new Exception("criar individual - nome vazio:");
        //verifica se texto senha ta vazio
        if(senha.isEmpty())
            throw new Exception("criar individual - senha vazia:");
        //localizar nome no repositorio
        Participante p = repositorio.localizarParticipante(nome);
        //verifica se o nome ja e admin
        if(p != null)
            throw new Exception("criar admin - nome ja existe:" + nome);
        //cria administrador
        Individual individuo = new Individual(nome, senha, true);
        repositorio.adicionarParticipante(individuo);
        repositorio.salvarObjetos();

    }




    public static void criarGrupo(String nome) throws Exception {
        //verifica se texto vazio
        if(nome.isEmpty())
            throw new Exception("criar grupo - nome vazio:");
        //localizar nome do grupo no repositorio
        Grupo grp = repositorio.localizarGrupo(nome);
        //verifica se grupo ja existe
        if(grp != null)
            throw new Exception("criar grupo - nome ja existe:" + nome);
        //criar o grupo
        Grupo grupo = new Grupo(nome);
        repositorio.adicionarParticipante(grupo);
        repositorio.salvarObjetos();
    }



    public static void inserirGrupo(String nomeindividuo,String nomegrupo) throws Exception {
        //localizar nomeindividuo no repositorio
        Individual ind = repositorio.localizarIndividual(nomeindividuo);
        //verifica se individuo nao existe
        if(ind == null)
            throw new Exception("inserir Grupo - individuo não existe:" + nomeindividuo);
        //localizar nomegrupo no repositorio
        Grupo grp = repositorio.localizarGrupo(nomegrupo);
        //verifica se grupo nao existe
        if(grp == null)
            throw new Exception("inserir Grupo - grupo não existe:" + nomegrupo);
        //verificar se individuo ja esta no grupo
        ArrayList<Individual> individuos = grp.getIndividuos(); //Arraylist contendo individuos associados aos grupos
        for (int i = 0; i < individuos.size(); i++) { //pecorre elementos individuos o contador i inicia com 0 enquatnto o i for menor que a lista
            Individual individuo = individuos.get(i); // cada interacao da loop  um individuo e obtido pelo metodo individuos.get
            if (individuo.getNome().equals(nomeindividuo)) { //se o nome do  individuo e igual o parametro nomeindividuo da a exception
                throw new Exception("inserir - indivíduo já está no grupo: " + nomeindividuo);
            }
        }
        //adicionar individuo com o grupo e vice-versa
        grp.adicionarIndividual(ind);
        ind.adicionarGrupo(grp);
        repositorio.salvarObjetos();

    }



    public static void removerGrupo(String nomeindividuo,String nomegrupo) throws Exception {
        //localizar nomeindividuo no repositorio
        Individual ind = repositorio.localizarIndividual(nomeindividuo);
        //verifica se o individuo existe
        if(ind == null)
            throw new Exception("remover Grupo - individuo não existe:" + nomeindividuo);
        //localizar nomegrupo no repositorio
        Grupo grp = repositorio.localizarGrupo(nomegrupo);
        //verifica se o grupo existe
        if(grp == null)
            throw new Exception("remover Grupo - grupo não existe:" + nomegrupo);
        //verificar se individuo nao esta no grupo
        if (!grp.getIndividuos().contains(ind))
            throw new Exception("remover Grupo - indivíduo não está no grupo:" + nomeindividuo);
        //remover individuo com o grupo e vice-versa
        grp.removerIndividual(ind);
        ind.removerGrupo(grp);
        repositorio.salvarObjetos();

    }


    public static void criarMensagem(String nomeind, String nomedest, String texto) throws Exception {
        //verifica se o texto e vazio
        if (texto.isEmpty())
            throw new Exception("criar mensagem - texto vazio:");
        //localiza emitente no repositorio
        Individual emitente = repositorio.localizarIndividual(nomeind);
        //verifica se o emitente existe
        if (emitente == null)
            throw new Exception("criar mensagem - emitente nao existe:" + nomeind);
        //localiza destinatario no repositorio
        Participante destinatario = repositorio.localizarParticipante(nomedest);
        //verifica se destinatario existe
        if (destinatario == null)
            throw new Exception("criar mensagem - destinatario nao existe:" + nomedest);
        //cont.
        //gerar id no repositorio para a mensagem
        int id = repositorio.gerarID();
        //criar mensagem
        Mensagem msg = new Mensagem(id, texto, emitente, destinatario, LocalDateTime.now());
        //adicionar mensagem ao emitente e destinatario
        emitente.adicionarEnviadas(msg);
        destinatario.adicionarRecebidas(msg);
        //adicionar mensagem ao repositorio
        repositorio.adicionarMensagem(msg);
        //Se o destinatário for um grupo, envia cópias da mensagem (com mesmo id) do grupo para os membros desse grupo
        //(exceto para emitente que criou a mensagem original), concatenando ao texto o nome    do emitente, na forma “nome/texto”.
        if (destinatario instanceof Grupo) {
            //realiza um castingpara atribuir o objeto destinatario a variável grp como uma instância da
            // classe Grupo. isso permite acessar os metodos e propriedades específicos da classe Grupo.
            Grupo grp = (Grupo) destinatario;
            //Para cada indivíduo, executa o código dentro do loop.
            // verifica se o indivíduo não é o emitente original
            for (Individual ind : grp.getIndividuos()) {
                if (!ind.equals(emitente)) {
                    //cria o texto da cópia da mensagem
                    String textoCopia = emitente.getNome() + "/" + texto;
                    //cria uma nova mensagem copiando os dados da mensagem original
                    Mensagem copia = new Mensagem(id, textoCopia, grp, ind, msg.getDatahora());
                    //adiciona a cópia à lista de mensagens enviadas pelo grupo
                    grp.adicionarEnviadas(copia);
                    //adiciona a cópia à lista de mensagens recebidas pelo indivíduo
                    ind.adicionarRecebidas(copia);
                    //adicionar as copias no repositório
                    repositorio.adicionarMensagem(copia);
                }
            }
        }
        repositorio.salvarObjetos();
    }




        public static ArrayList<Mensagem> obterConversa(String nomeemitente, String nomedestinatario) throws Exception{
            //localizar emitente no repositorio
            Individual emitente = repositorio.localizarIndividual(nomeemitente);
            //verifica se emitente existe
            if(emitente == null)
                throw new Exception("obter conversa - emitente nao existe:" + nomeemitente);
            //localizar destinatario no repositorio
            Participante destinatario = repositorio.localizarParticipante(nomedestinatario);
        //verifica se destinatario existe
        if(destinatario == null)
            throw new Exception("obter conversa - destinatario nao existe:" + nomedestinatario);

        //obter do emitente a lista  enviadas (lista1)
        ArrayList<Mensagem> lista1 = emitente.getEnviadas();
        //obter do destinatario a lista  enviadas (lista2)
        ArrayList<Mensagem> lista2 = destinatario.getEnviadas();
        //criar a lista conversa
        ArrayList<Mensagem> conversa = new ArrayList<>();
        //Adicionar na conversa as mensagens da lista1 cujo destinatario é igual ao parametro destinatario
        for(Mensagem m : lista1){
            if(m.getDestinatario().equals(destinatario)){
                conversa.add(m);
            }
        }
        //Adicionar na conversa as mensagens da lista2 cujo destinatario é igual ao parametro emitente
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


    public static ArrayList<Mensagem> listarMensagensEnviadas(String nome) throws Exception{
        //localizar individuo no repositorio
        Individual ind = repositorio.localizarIndividual(nome);
        //verifica se o individuo existe
        if(ind == null)
            throw new Exception("listar  mensagens enviadas - nome nao existe:" + nome);
        //retorna as mensagens enviadas do individuo
        return ind.getEnviadas();
    }

    public static  ArrayList<Mensagem> listarMensagensRecebidas (String nome) throws Exception{
        //localizar individuo no repositorio
        Individual ind= repositorio.localizarIndividual(nome);
        //verifica se o individuo existe
        if(ind == null)
            throw new Exception("listar  mensagens enviadas - nome nao existe:" + nome);
        //retorna as mensagens recebidas do individuo
        return ind.getRecebidas();
    }

    public  static ArrayList<Individual> listarIndividuos(){
        //lista os individiuos no repositorio
       return repositorio.getIndividuos();
    }

    public static ArrayList<Grupo> listarGrupos(){
        //lista os grupos no repositorio
        return repositorio.getGrupos();

    }

    public static ArrayList<Mensagem> listarMensagens(){
        //lista as mensagens no repositorio
        return repositorio.getMensagens();
    }


    public static ArrayList<Mensagem> espionarMensagens(String nomeadmin, String termo) throws Exception{
        //localizar individuo no repositorio
        Individual individuo = repositorio.localizarIndividual(nomeadmin);
        if(individuo == null)
            throw new Exception("espionar mensagem - individuo não existe:" + nomeadmin);
        //verificar se individuo é administrador
        if(!individuo.getAdministrador())
            throw new Exception("espionar mensagem - individuo não é administrador:" + nomeadmin);
        //listar as mensagens que contem o termo no texto
        ArrayList<Mensagem> lista = new ArrayList<>();
        //inicia um loop for que itera sobre cada mensagem obtida do repositorio de msgs
        for(Mensagem msg : repositorio.getMensagens()) {
            //verifica se o texto da mensagem contem o termo se retorna true adiciona na lista
            if(msg.getTexto().contains(termo))
                lista.add(msg);
        }
        return lista;

    }

    public static ArrayList<String> ausentes(String nomeadmin) throws Exception {
        //localizar individuo no repositorio
        Individual individuo = repositorio.localizarIndividual(nomeadmin);
        if(nomeadmin == null)
            throw new Exception("nome adm nao existe" + nomeadmin);
        //verificar se individuo é administrador
        if(!individuo.getAdministrador())
            throw new Exception("ausentes - individuo não é administrador:" + nomeadmin);
        //listar os nomes dos participante que nao enviaram mensagens
        ArrayList<String> listaAusentes = new ArrayList<>();
        //inicia um loop for que itera sobre cada participante obtido do repositorio de participantes
        for (Participante part : repositorio.getParticipantes()) {
            //verifica se a lista de mensagem enviadas pelo particinpante e vazia se retorna true adiciona na listaAusentes
            if(part.getEnviadas().isEmpty())
                listaAusentes.add(part.getNome());
        }
        return listaAusentes;
    }
}

