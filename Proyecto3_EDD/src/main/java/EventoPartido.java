public class EventoPartido {
    private int idOrigen;
    private int idDestino;
    private String tipo;

    public EventoPartido(int idOrigen, int idDestino, String tipo) {
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
        this.tipo = tipo;
    }

    public int getOrigen() { return idOrigen; }
    public int getDestino() { return idDestino; }
    public String getTipo() { return tipo; }

    public String toFileString() { return String.format("%d;%d;%s", idOrigen, idDestino, tipo); }

    public static EventoPartido fromFileString(String line) {
        String[] p = line.split(";", -1);
        int o = Integer.parseInt(p[0]);
        int d = Integer.parseInt(p[1]);
        String t = p[2];
        return new EventoPartido(o, d, t);
    }
}
