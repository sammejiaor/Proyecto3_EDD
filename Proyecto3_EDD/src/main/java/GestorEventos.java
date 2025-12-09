import java.io.*;
import java.util.*;

public class GestorEventos {
    private ArrayList<EventoPartido> eventos = new ArrayList<>();

    public void registrarEvento(EventoPartido e) { eventos.add(e); }

    public List<EventoPartido> listarEventos() { return eventos; }

    public void guardarEnArchivo(File f) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (EventoPartido e : eventos) pw.println(e.toFileString());
        }
    }

    public void cargarDesdeArchivo(File f) throws IOException {
        eventos.clear();
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                eventos.add(EventoPartido.fromFileString(line));
            }
        }
    }
}
