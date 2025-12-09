public class EstadisticaArista {
    private int asistencias;
    private int pasesClave;

    public EstadisticaArista() {
        this.asistencias = 0;
        this.pasesClave = 0;
    }

    public void sumarAsistencia() { asistencias++; pasesClave++; }
    public void sumarPaseClave() { pasesClave++; }

    public int getAsistencias() { return asistencias; }
    public int getPasesClave() { return pasesClave; }

    public int influencia() { return 2 * asistencias + pasesClave; }

    @Override
    public String toString() { return "A:" + asistencias + " KP:" + pasesClave; }
}
