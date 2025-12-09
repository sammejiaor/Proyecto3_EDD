import java.io.*;
import java.util.*;

public class GestorJugadores {
    private HashMap<Integer, Jugador> jugadores = new HashMap<>();
    private int ultimoID = 0;

    public Jugador crearJugador(String nombre, int edad, String pais, String posicion, String rutaFoto, int dorsal) {
        ultimoID++;
        Jugador j = new Jugador(ultimoID, nombre, edad, pais, posicion, rutaFoto, dorsal);
        jugadores.put(ultimoID, j);
        return j;
    }

    public void eliminarJugador(int id) { jugadores.remove(id); }

    public Jugador getJugador(int id) { return jugadores.get(id); }

    public Collection<Jugador> listarJugadores() { return jugadores.values(); }

    // NUEVO MÉTODO: Actualizar jugador existente
    public void actualizarJugador(Jugador jugadorActualizado) {
        jugadores.put(jugadorActualizado.getId(), jugadorActualizado);
    }

    public void guardarEnArchivo(File f) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (Jugador j : jugadores.values()) pw.println(j.toFileString());
        }
    }

    public void cargarDesdeArchivo(File f) throws IOException {
        jugadores.clear(); ultimoID = 0;
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Jugador j = Jugador.fromFileString(line);
                if (j != null) {
                    jugadores.put(j.getId(), j);
                    if (j.getId() > ultimoID) ultimoID = j.getId();
                }
            }
        }
    }
}