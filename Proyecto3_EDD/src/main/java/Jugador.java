import java.util.Objects;

public class Jugador {
    private int id;
    private String nombre;
    private int edad;
    private String pais;
    private String posicion;
    private String rutaFoto;
    private int dorsal;

    private int goles;
    private int asistencias;
    private int pasesClave;

    public Jugador(int id, String nombre, int edad, String pais, String posicion, String rutaFoto, int dorsal) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.pais = pais;
        this.posicion = posicion;
        this.rutaFoto = rutaFoto;
        this.dorsal = dorsal;
        this.goles = 0;
        this.asistencias = 0;
        this.pasesClave = 0;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getPais() { return pais; }
    public String getPosicion() { return posicion; }
    public String getRutaFoto() { return rutaFoto; }
    public int getDorsal() { return dorsal; }

    public int getGoles() { return goles; }
    public int getAsistencias() { return asistencias; }
    public int getPasesClave() { return pasesClave; }

    // SETTERS
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setPais(String pais) { this.pais = pais; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
    public void setRutaFoto(String rutaFoto) { this.rutaFoto = rutaFoto; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }
    
    public void setGoles(int goles) { this.goles = goles; }
    public void setAsistencias(int asistencias) { this.asistencias = asistencias; }
    public void setPasesClave(int pasesClave) { this.pasesClave = pasesClave; }

    // MÉTODOS PARA AGREGAR ESTADÍSTICAS
    public void agregarGol() { goles++; }
    public void agregarAsistencia() { asistencias++; pasesClave++; }
    public void agregarPaseClave() { pasesClave++; }

    public String toFileString() {
        return String.format("%d;%s;%d;%s;%s;%s;%d;%d;%d;%d",
                id, escape(nombre), edad, escape(pais), posicion, escape(rutaFoto), dorsal,
                goles, asistencias, pasesClave);
    }

    public static Jugador fromFileString(String line) {
        try {
            String[] p = line.split(";", -1);
            int id = Integer.parseInt(p[0]);
            String nombre = unescape(p[1]);
            int edad = Integer.parseInt(p[2]);
            String pais = unescape(p[3]);
            String posicion = p[4];
            String rutaFoto = unescape(p[5]);
            int dorsal = Integer.parseInt(p[6]);
            Jugador j = new Jugador(id, nombre, edad, pais, posicion, rutaFoto, dorsal);
            j.goles = Integer.parseInt(p[7]);
            j.asistencias = Integer.parseInt(p[8]);
            j.pasesClave = Integer.parseInt(p[9]);
            return j;
        } catch (Exception e) {
            return null;
        }
    }

    private static String escape(String s) { return s == null ? "" : s.replace(";", ","); }
    private static String unescape(String s) { return s == null ? "" : s; }

    @Override
    public String toString() {
        return String.format("%d - %s (#%d) (%s) - %s | G:%d A:%d PC:%d",
                id, nombre, dorsal, posicion, pais, goles, asistencias, pasesClave);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jugador)) return false;
        Jugador jugador = (Jugador) o;
        return id == jugador.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}