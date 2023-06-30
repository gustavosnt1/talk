package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Mensagem {


    private int id;
    private String texto;
    private Participante emitente;
    private Participante destinatario;
    private LocalDateTime datahora;

    public Mensagem(int id,String texto, Participante emitente, Participante destinatario, LocalDateTime datahora) {
        this.id = id;
        this.texto=texto;
        this.emitente=emitente;
        this.destinatario=destinatario;
        this.datahora=datahora;
    }


    public int getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }


    public Participante getEmitente() {
        return emitente;
    }


    public Participante getDestinatario() {
        return destinatario;
    }

    public LocalDateTime getDatahora() {
        return datahora;
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id)
                .append(": emitente=")
                .append(emitente.getNome())
                .append(", destinatario=")
                .append(destinatario.getNome())
                .append(", texto=")
                .append(texto)
                .append(", datahora=")
                .append(datahora.format(DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss")));

        return builder.toString();
    }
}