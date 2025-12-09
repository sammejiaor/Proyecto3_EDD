import java.util.*;

public class AnalizadorEstadistico {

    private GrafoEstadistico grafo;
    private GestorJugadores gestor;

    public AnalizadorEstadistico(GrafoEstadistico grafo, GestorJugadores gestor) {
        this.grafo = grafo;
        this.gestor = gestor;
    }

    private List<Map.Entry<Integer, Integer>> topNFromMap(Map<Integer, Integer> map, int n) {
        List<Map.Entry<Integer, Integer>> l = new ArrayList<>(map.entrySet());
        l.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        if (l.size() > n) {
            return l.subList(0, n);
        }
        return l;
    }

    public List<Jugador> topNGoleadores(int n) {
        HashMap<Integer, Integer> goles = grafo.calcularGolesPorJugador();
        List<Map.Entry<Integer, Integer>> top = topNFromMap(goles, n);
        List<Jugador> res = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : top) {
            Jugador j = gestor.getJugador(e.getKey());
            if (j != null) {
                res.add(j);
            }
        }
        return res;
    }

    public List<Jugador> topNAsistidores(int n) {
        HashMap<Integer, Integer> asist = grafo.calcularAsistenciasPorJugador();
        List<Map.Entry<Integer, Integer>> top = topNFromMap(asist, n);
        List<Jugador> res = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : top) {
            Jugador j = gestor.getJugador(e.getKey());
            if (j != null) {
                res.add(j);
            }
        }
        return res;
    }

    public List<Jugador> topNInfluyentes(int n) {
        List<Jugador> todos = new ArrayList<>(gestor.listarJugadores());
        
        List<JugadorConValor> listaConValor = new ArrayList<>();
        for (Jugador j : todos) {
            int influencia = calcularInfluencia(j);
            listaConValor.add(new JugadorConValor(j, influencia));
        }
        
        listaConValor.sort((a, b) -> Integer.compare(b.valor, a.valor));
        
        List<Jugador> res = new ArrayList<>();
        for (int i = 0; i < Math.min(n, listaConValor.size()); i++) {
            res.add(listaConValor.get(i).jugador);
        }
        return res;
    }
    
    private int calcularInfluencia(Jugador j) {
        return j.getGoles() * 3 + j.getAsistencias() * 2 + j.getPasesClave();
    }

    public List<Jugador> topNEfectividad(int n) {
        HashMap<Integer, Integer> goles = grafo.calcularGolesPorJugador();
        HashMap<Integer, Integer> asistenciasRecibidas = calcularAsistenciasRecibidas();
        HashMap<Integer, Integer> pasesRecibidos = grafo.calcularPasesClaveRecibidos();
        
        List<JugadorConEfectividad> listaEfectividad = new ArrayList<>();
        
        for (Jugador j : gestor.listarJugadores()) {
            int golesJugador = goles.getOrDefault(j.getId(), 0);
            int asistenciasRecibidasJugador = asistenciasRecibidas.getOrDefault(j.getId(), 0);
            int pasesRecibidosJugador = pasesRecibidos.getOrDefault(j.getId(), 0);
            
            int oportunidadesRecibidas = asistenciasRecibidasJugador + pasesRecibidosJugador;
            
            if (oportunidadesRecibidas > 0) {
                double efectividad = (double) golesJugador / oportunidadesRecibidas * 100.0;
                listaEfectividad.add(new JugadorConEfectividad(j, efectividad, oportunidadesRecibidas));
            }
        }
        
        listaEfectividad.sort((a, b) -> Double.compare(b.efectividad, a.efectividad));
        
        List<Jugador> res = new ArrayList<>();
        for (int i = 0; i < Math.min(n, listaEfectividad.size()); i++) {
            res.add(listaEfectividad.get(i).jugador);
        }
        return res;
    }
    
    private HashMap<Integer, Integer> calcularAsistenciasRecibidas() {
        HashMap<Integer, Integer> asistenciasRecibidas = new HashMap<>();
        HashMap<Integer, Integer> goles = grafo.calcularGolesPorJugador();
        
        for (Map.Entry<Integer, Integer> entry : goles.entrySet()) {
            int jugadorId = entry.getKey();
            int golesJugador = entry.getValue();
            asistenciasRecibidas.put(jugadorId, golesJugador);
        }
        
        return asistenciasRecibidas;
    }
    
    public double calcularEfectividadJugador(Jugador j) {
        HashMap<Integer, Integer> goles = grafo.calcularGolesPorJugador();
        HashMap<Integer, Integer> asistenciasRecibidas = calcularAsistenciasRecibidas();
        HashMap<Integer, Integer> pasesRecibidos = grafo.calcularPasesClaveRecibidos();
        
        int golesJugador = goles.getOrDefault(j.getId(), 0);
        int asistenciasRecibidasJugador = asistenciasRecibidas.getOrDefault(j.getId(), 0);
        int pasesRecibidosJugador = pasesRecibidos.getOrDefault(j.getId(), 0);
        
        int oportunidadesRecibidas = asistenciasRecibidasJugador + pasesRecibidosJugador;
        
        if (oportunidadesRecibidas > 0) {
            return (double) golesJugador / oportunidadesRecibidas * 100.0;
        }
        return 0.0;
    }
    
    public int getOportunidadesRecibidas(Jugador j) {
        HashMap<Integer, Integer> asistenciasRecibidas = calcularAsistenciasRecibidas();
        HashMap<Integer, Integer> pasesRecibidos = grafo.calcularPasesClaveRecibidos();
        
        return asistenciasRecibidas.getOrDefault(j.getId(), 0) + 
               pasesRecibidos.getOrDefault(j.getId(), 0);
    }
    
    public List<Jugador> topNJovenesPromesas(int n) {
        List<Jugador> todosJugadores = new ArrayList<>(gestor.listarJugadores());
        List<Jugador> jovenes = new ArrayList<>();
        
        for (Jugador j : todosJugadores) {
            if (j.getEdad() < 25) {
                jovenes.add(j);
            }
        }
        
        jovenes.sort((a, b) -> {
            int potencialA = calcularPotencial(a);
            int potencialB = calcularPotencial(b);
            return Integer.compare(potencialB, potencialA);
        });
        
        if (jovenes.size() > n) {
            return jovenes.subList(0, n);
        }
        return jovenes;
    }
    
    private int calcularPotencial(Jugador j) {
        int base = j.getGoles() * 4 + j.getAsistencias() * 3 + j.getPasesClave() * 2;
        int bonusJuventud = (25 - j.getEdad()) * 5;
        
        return base + bonusJuventud;
    }

    public List<DuplaEstadistica> topNDuplas(int n) {
        return grafo.calcularDuplasDestacadas(n);
    }

    public GestorJugadores getGestor() {
        return gestor;
    }
    
    private class JugadorConValor {
        Jugador jugador;
        int valor;
        
        JugadorConValor(Jugador j, int v) {
            this.jugador = j;
            this.valor = v;
        }
    }
    
    private class JugadorConEfectividad {
        Jugador jugador;
        double efectividad;
        int oportunidadesRecibidas;
        
        JugadorConEfectividad(Jugador j, double e, int or) {
            this.jugador = j;
            this.efectividad = e;
            this.oportunidadesRecibidas = or;
        }
    }
}