import java.io.*;
import java.util.List;

public class ExportadorCSV {
    
    public static void exportarTop5(AnalizadorEstadistico analizador, File archivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("Categoría,Posición,Jugador,Goles,Asistencias,PasesClave,PosiciónJugador,Edad");
            
            // Goleadores
            List<Jugador> goleadores = analizador.topNGoleadores(5);
            for (int i = 0; i < goleadores.size(); i++) {
                Jugador j = goleadores.get(i);
                pw.println(String.format("Goleadores,%d,%s,%d,%d,%d,%s,%d",
                    i+1, j.getNombre(), j.getGoles(), j.getAsistencias(),
                    j.getPasesClave(), j.getPosicion(), j.getEdad()));
            }
            
            // Asistentes
            List<Jugador> asistentes = analizador.topNAsistidores(5);
            for (int i = 0; i < asistentes.size(); i++) {
                Jugador j = asistentes.get(i);
                pw.println(String.format("Asistentes,%d,%s,%d,%d,%d,%s,%d",
                    i+1, j.getNombre(), j.getGoles(), j.getAsistencias(),
                    j.getPasesClave(), j.getPosicion(), j.getEdad()));
            }
        }
    }
    
    public static void exportarJugadoresCompleto(GestorJugadores gestor, File archivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("ID,Dorsal,Nombre,Edad,País,Posición,Goles,Asistencias,PasesClave");
            
            for (Jugador j : gestor.listarJugadores()) {
                pw.println(String.format("%d,%d,%s,%d,%s,%s,%d,%d,%d",
                    j.getId(), j.getDorsal(), j.getNombre(), j.getEdad(),
                    j.getPais(), j.getPosicion(), j.getGoles(),
                    j.getAsistencias(), j.getPasesClave()));
            }
        }
    }
    
    public static void exportarEstadisticasCompletas(GestorJugadores gestor, AnalizadorEstadistico analizador, File archivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("Tipo,Dorsal,Nombre,Posición,Edad,Goles,Asistencias,PasesClave,Influencia");
            
            // Todos los jugadores
            for (Jugador j : gestor.listarJugadores()) {
                int influencia = j.getGoles() * 3 + j.getAsistencias() * 2 + j.getPasesClave();
                pw.println(String.format("General,%d,%s,%s,%d,%d,%d,%d,%d",
                    j.getDorsal(), j.getNombre(), j.getPosicion(), j.getEdad(),
                    j.getGoles(), j.getAsistencias(), j.getPasesClave(), influencia));
            }
            
            // Línea separadora
            pw.println();
            pw.println("TOP 5 ESTADÍSTICAS");
            pw.println("Categoría,Posición,Nombre,Dorsal,Valor");
            
            // Top 5 Goleadores
            List<Jugador> goleadores = analizador.topNGoleadores(5);
            for (int i = 0; i < goleadores.size(); i++) {
                Jugador j = goleadores.get(i);
                pw.println(String.format("Goleadores,%d,%s,%d,%d",
                    i+1, j.getNombre(), j.getDorsal(), j.getGoles()));
            }
            
            // Top 5 Asistentes
            List<Jugador> asistentes = analizador.topNAsistidores(5);
            for (int i = 0; i < asistentes.size(); i++) {
                Jugador j = asistentes.get(i);
                pw.println(String.format("Asistentes,%d,%s,%d,%d",
                    i+1, j.getNombre(), j.getDorsal(), j.getAsistencias()));
            }
            
            // Top 5 Influencia
            List<Jugador> influyentes = analizador.topNInfluyentes(5);
            for (int i = 0; i < influyentes.size(); i++) {
                Jugador j = influyentes.get(i);
                int influencia = j.getGoles() * 3 + j.getAsistencias() * 2 + j.getPasesClave();
                pw.println(String.format("Influencia,%d,%s,%d,%d",
                    i+1, j.getNombre(), j.getDorsal(), influencia));
            }
        }
    }
}