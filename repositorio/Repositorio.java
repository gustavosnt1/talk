package repositorio;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

import modelo.*;
public class Repositorio {


    TreeMap<String,Participante> participantes= new TreeMap<String,Participante>();
    ArrayList<Mensagem> mensagens = new ArrayList<>();

    public Repositorio(){
        this.carregarObjetos();
    }
    //adiciona participante
    public void adicionarParticipante(Participante part){
        participantes.put(part.getNome(), part);
    }
    //remove participante/ sem uso
    public void removerParticipante(Participante part){
        participantes.remove(part.getNome());
    }

    //gera id da mensagem
    public int gerarID() {
        //verifica se mensagens esta vazia caso esteja vazia retorna 1 < primeiro id da msg
        if (mensagens.isEmpty()) {
            return 1;
        } else {
            int maiorId = Integer.MIN_VALUE; //se nao tiver vazia a variavel maiorid guarda o valor minimo possivel
            for (Mensagem mensagem : mensagens) {
                if (mensagem.getId() > maiorId) { //verifica se o id da mensagem atual e maior que o atual armazenado na variavel maiorid
                    maiorId = mensagem.getId(); //se o id da msg atual for maior q a variavel 'maiorid' ela e atualizada com valor da msg atual
                }
            }
            return maiorId + 1; //dps de percorrer todas msg o valor de maiorid aumenta 1, garante que o novo id seja maior que
                                // todos ids das msgs existens
        }
    }

    //adiciona msg
    public void adicionarMensagem(Mensagem msg) {
        mensagens.add(msg);
    }

    //remove msg
    public void removerMensagem(Mensagem msg){
        mensagens.remove(msg);
    }

    //localiza participante
    public Participante localizarParticipante(String nome) {
        return participantes.get(nome);
    }

    //localizar individual
    public  Individual localizarIndividual(String nome) {
        Participante p =(Individual) participantes.get(nome);
        if (p !=null && p instanceof Individual ind) {
            return ind;
        }
        return null;
    }

    //localizar grupo
    public  Grupo localizarGrupo(String nome) {
        Participante p=(Grupo) participantes.get(nome);

        if(p !=null && p instanceof Grupo grp) {
            return grp;
        }
        return null;
    }

    public ArrayList<Grupo> getGrupos(){
        ArrayList<Grupo> grupos= new ArrayList<>();
        for(Participante p : participantes.values()) {
            if (p instanceof Grupo g) {
                grupos.add(g);
            }
        }
        return grupos;
    }
    public ArrayList<Individual> getIndividuos(){
        ArrayList<Individual> individuos= new ArrayList<>();
        for(Participante p : participantes.values()) {
            if (p instanceof Individual ind) {
                individuos.add(ind);
            }

        }
        return individuos;
    }

    public ArrayList<Participante> getParticipantes(){
        ArrayList<Participante> part = new ArrayList<>();
        for(Participante participante : participantes.values()){
            part.add(participante);
        }
        return part;
    }

    public ArrayList<Mensagem> getMensagens(){
            return mensagens;
    }

    public void carregarObjetos()  	{
        // carregar para o repositorio os objetos dos arquivos csv
        try {
            //caso os arquivos nao existam, serao criados vazios
            File f1 = new File( new File(".\\mensagens.csv").getCanonicalPath() ) ;
            File f2 = new File( new File(".\\individuos.csv").getCanonicalPath() ) ;
            File f3 = new File( new File(".\\grupos.csv").getCanonicalPath() ) ;
            if (!f1.exists() || !f2.exists() || !f3.exists() ) {
                //System.out.println("criando arquivo .csv vazio");
                FileWriter arquivo1 = new FileWriter(f1); arquivo1.close();
                FileWriter arquivo2 = new FileWriter(f2); arquivo2.close();
                FileWriter arquivo3 = new FileWriter(f3); arquivo3.close();
                return;
            }
        }
        catch(Exception ex)		{
            throw new RuntimeException("criacao dos arquivos vazios:"+ex.getMessage());
        }

        String linha;
        String[] partes;

        try	{
            String nome,senha,administrador;
            File f = new File( new File(".\\individuos.csv").getCanonicalPath())  ;
            Scanner arquivo1 = new Scanner(f);	 //  pasta do projeto
            while(arquivo1.hasNextLine()) 	{
                linha = arquivo1.nextLine().trim();
                partes = linha.split(";");
                //System.out.println(Arrays.toString(partes));
                nome = partes[0];
                senha = partes[1];
                administrador = partes[2];
                Individual ind = new Individual(nome,senha,Boolean.parseBoolean(administrador));
                participantes.put(ind.getNome(), ind);
            }
            arquivo1.close();
        }
        catch(Exception ex)		{
            throw new RuntimeException("leitura arquivo de individuos:"+ex.getMessage());
        }

        try	{
            String nome;
            Grupo grupo;
            Individual individuo;
            File f = new File( new File(".\\grupos.csv").getCanonicalPath())  ;
            Scanner arquivo2 = new Scanner(f);	 //  pasta do projeto
            while(arquivo2.hasNextLine()) 	{
                linha = arquivo2.nextLine().trim();
                partes = linha.split(";");
                //System.out.println(Arrays.toString(partes));
                nome = partes[0];
                grupo = new Grupo(nome);
                if(partes.length>1)
                    for(int i=1; i< partes.length; i++) {
                        individuo = this.localizarIndividual(partes[i]);
                        grupo.adicionarIndividual(individuo);
                        individuo.adicionarGrupo(grupo);
                    }
                participantes.put(grupo.getNome(), grupo);
            }
            arquivo2.close();
        }
        catch(Exception ex)		{
            throw new RuntimeException("leitura arquivo de grupos:"+ex.getMessage());
        }


        try	{
            String nomeemitente, nomedestinatario,texto;
            int id;
            LocalDateTime datahora;
            Mensagem m;
            Participante emitente,destinatario;
            File f = new File( new File(".\\mensagens.csv").getCanonicalPath() )  ;
            Scanner arquivo3 = new Scanner(f);	 //  pasta do projeto
            while(arquivo3.hasNextLine()) 	{
                linha = arquivo3.nextLine().trim();
                partes = linha.split(";");
                //System.out.println(Arrays.toString(partes));
                id = Integer.parseInt(partes[0]);
                texto = partes[1];
                nomeemitente = partes[2];
                nomedestinatario = partes[3];
                datahora = LocalDateTime.parse(partes[4], DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
                emitente = this.localizarParticipante(nomeemitente);
                destinatario = this.localizarParticipante(nomedestinatario);
                m = new Mensagem(id,texto, emitente, destinatario, datahora);
                this.adicionarMensagem(m);
                emitente.adicionarEnviadas(m);
                destinatario.adicionarRecebidas(m);
            }
            arquivo3.close();
        }
        catch(Exception ex)		{
            throw new RuntimeException("leitura arquivo de mensagens:"+ex.getMessage());
        }
    }


    public void	salvarObjetos()  {
        //gravar nos arquivos csv os objetos que estão no repositório
        try	{
            File f = new File( new File(".\\mensagens.csv").getCanonicalPath())  ;
            FileWriter arquivo1 = new FileWriter(f);
            for(Mensagem m : mensagens) 	{
                arquivo1.write(	m.getId()+";"+
                        m.getTexto()+";"+
                        m.getEmitente().getNome()+";"+
                        m.getDestinatario().getNome()+";"+
                        m.getDatahora().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"))+"\n");
            }
            arquivo1.close();
        }
        catch(Exception e){
            throw new RuntimeException("problema na criação do arquivo  mensagens "+e.getMessage());
        }

        try	{
            File f = new File( new File(".\\individuos.csv").getCanonicalPath())  ;
            FileWriter arquivo2 = new FileWriter(f) ;
            for(Individual ind : this.getIndividuos()) {
                arquivo2.write(ind.getNome() +";"+ ind.getSenha() +";"+ ind.getAdministrador() +"\n");
            }
            arquivo2.close();
        }
        catch (Exception e) {
            throw new RuntimeException("problema na criação do arquivo  individuos "+e.getMessage());
        }

        try	{
            File f = new File( new File(".\\grupos.csv").getCanonicalPath())  ;
            FileWriter arquivo3 = new FileWriter(f) ;
            for(Grupo g : this.getGrupos()) {
                String texto="";
                for(Individual ind : g.getIndividuos())
                    texto += ";" + ind.getNome();
                arquivo3.write(g.getNome() + texto + "\n");
            }
            arquivo3.close();
        }
        catch (Exception e) {
            throw new RuntimeException("problema na criação do arquivo  grupos "+e.getMessage());
        }
    }

}


