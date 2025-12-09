import java.util.*;

public class GrafoEstadistico {
    private HashMap<Integer, HashMap<Integer, EstadisticaArista>> adyacencia = new HashMap<>();

    public void agregarJugadorSiNoExiste(int id) { adyacencia.putIfAbsent(id, new HashMap<>()); }

    public void registrarAsistencia(int idOrigen, int idDestino) {
        agregarJugadorSiNoExiste(idOrigen);
        agregarJugadorSiNoExiste(idDestino);
        HashMap<Integer, EstadisticaArista> edges = adyacencia.get(idOrigen);
        EstadisticaArista ea = edges.get(idDestino);
        if (ea == null) { ea = new EstadisticaArista(); edges.put(idDestino, ea); }
        ea.sumarAsistencia();
    }
 
    public void registrarPaseClave(int idOrigen, int idDestino) {
        agregarJugadorSiNoExiste(idOrigen);
        agregarJugadorSiNoExiste(idDestino);
        HashMap<Integer, EstadisticaArista> edges = adyacencia.get(idOrigen);
        EstadisticaArista ea = edges.get(idDestino);
        if (ea == null) { ea = new EstadisticaArista(); edges.put(idDestino, ea); }
        ea.sumarPaseClave();
    }

    public EstadisticaArista getArista(int idOrigen, int idDestino) {
        if (!adyacencia.containsKey(idOrigen)) return null;
        return adyacencia.get(idOrigen).get(idDestino);
    }

    public Set<Integer> getNodos() { return adyacencia.keySet(); }

    public HashMap<Integer,Integer> calcularGolesPorJugador() {
        HashMap<Integer,Integer> goles = new HashMap<>();
        for (Integer origen : adyacencia.keySet()) {
            for (Integer destino : adyacencia.get(origen).keySet()) {
                EstadisticaArista ea = adyacencia.get(origen).get(destino);
                int a = ea.getAsistencias();
                goles.put(destino, goles.getOrDefault(destino, 0) + a);
            }
        }
        return goles;
    }

    public HashMap<Integer,Integer> calcularAsistenciasPorJugador() {
        HashMap<Integer,Integer> asist = new HashMap<>();
        for (Integer origen : adyacencia.keySet()) {
            for (Integer destino : adyacencia.get(origen).keySet()) {
                EstadisticaArista ea = adyacencia.get(origen).get(destino);
                int a = ea.getAsistencias();
                asist.put(origen, asist.getOrDefault(origen, 0) + a);
            }
        }
        return asist;
    }

    public HashMap<Integer,Integer> calcularPasesClaveRecibidos() {
        HashMap<Integer,Integer> pk = new HashMap<>();
        for (Integer origen : adyacencia.keySet()) {
            for (Integer destino : adyacencia.get(origen).keySet()) {
                EstadisticaArista ea = adyacencia.get(origen).get(destino);
                int k = ea.getPasesClave();
                pk.put(destino, pk.getOrDefault(destino, 0) + k);
            }
        }
        return pk;
    }

    public HashMap<Integer,Integer> calcularInfluenciaPorJugador() {
        HashMap<Integer,Integer> infl = new HashMap<>();
        for (Integer origen : adyacencia.keySet()) {
            int total = 0;
            for (Integer destino : adyacencia.get(origen).keySet()) total += adyacencia.get(origen).get(destino).influencia();
            infl.put(origen, total);
        }
        return infl;
    }

    public List<DuplaEstadistica> calcularDuplasDestacadas(int topN) {
        List<DuplaEstadistica> lista = new ArrayList<>();
        for (Integer origen : adyacencia.keySet()) {
            for (Integer destino : adyacencia.get(origen).keySet()) {
                EstadisticaArista ea = adyacencia.get(origen).get(destino);
                int peso = ea.getAsistencias() + ea.getPasesClave();
                lista.add(new DuplaEstadistica(origen, destino, peso, ea.getAsistencias(), ea.getPasesClave()));
            }
        }
        lista.sort((a,b) -> Integer.compare(b.peso, a.peso));
        if (lista.size() > topN) return lista.subList(0, topN);
        return lista;
    }
}
