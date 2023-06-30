package appconsole;

/**********************************
 * IFPB - TSI - POO
 * Prof. Fausto Ayres
 *
 * Teste do sistema 4TALK
 *********************************/

import java.util.ArrayList;

import modelo.Grupo;
import modelo.Individual;
import modelo.Mensagem;
import regras_negocio.Fachada;


//---- atualizado em 30/06


public class Teste1 {

    public Teste1() {

        /**
         * PARTICIPANTE INDIVIDUAL
         */
        try {
            System.out.println("\ncriar individuos");
            Fachada.criarIndividuo("joao", "123");
            Fachada.criarIndividuo("maria", "123");
            Fachada.criarIndividuo("jose", "123");
            System.out.println("criou individuos");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /**
         * MENSAGEM
         */
        try {
            System.out.println("\ncriar mensagens");
            Fachada.criarMensagem("joao", "maria", "oi maria tudo bem?");
            Fachada.criarMensagem("maria", "joao", "tudo bem joao!");
            Fachada.criarMensagem("joao", "maria", "vamos fazer juntos?");
            Fachada.criarMensagem("maria", "joao", "vou criar um grupo e chamar jose tb");
            Fachada.criarMensagem("joao", "joao", "teste");
            System.out.println("criou mensagens");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("*********************************************");

        System.out.println("\nlistar mensagens do sistema");
        for (Mensagem m : Fachada.listarMensagens())
            System.out.println(m);

        System.out.println("\nlistar individuos ");
        for (Individual ind : Fachada.listarIndividuos())
            System.out.println(ind);

        try {
            System.out.println("\nconversa entre joao e maria");
            for (Mensagem m : Fachada.obterConversa("joao", "maria"))
                System.out.println(m);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n*******************************************************");

        System.out.println("*********************************************");


        System.out.println("fim do programa");
    }

    // =================================================
    public static void main(String[] args) {
        new Teste1();
    }
}