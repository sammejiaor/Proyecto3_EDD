import java.util.*;

public class Alineador {
    private GestorJugadores gestor;
    private AnalizadorEstadistico analizador;
    private HashMap<String, Formacion> formaciones = new HashMap<>();

    public Alineador(GestorJugadores gestor, AnalizadorEstadistico analizador) {
        this.gestor = gestor;
        this.analizador = analizador;
        // Formaciones tácticas con distribución específica
        formaciones.put("4-4-2", new Formacion(1, 4, 4, 2));
        formaciones.put("4-3-3", new Formacion(1, 4, 3, 3));
        formaciones.put("4-2-3-1", new Formacion(1, 4, 5, 1));
        formaciones.put("3-5-2", new Formacion(1, 3, 5, 2));
        formaciones.put("3-4-3", new Formacion(1, 3, 4, 3));
    }

    public Formacion getFormacion(String nombre) { return formaciones.get(nombre); }
    
    private int calcularInfluencia(Jugador j) {
        return j.getGoles() * 3 + j.getAsistencias() * 2 + j.getPasesClave();
    }

    public List<Jugador> recomendarOnce(String nombre) {
        Formacion f = formaciones.get(nombre);
        if (f == null) return new ArrayList<>();
        
        List<Jugador> todos = new ArrayList<>(gestor.listarJugadores());
        
        // Ordenar por influencia descendente
        Comparator<Jugador> cmpInfluencia = (a, b) -> 
            Integer.compare(calcularInfluencia(b), calcularInfluencia(a));
        
        // Filtrar por posiciones específicas
        List<Jugador> porteros = filterByPos(todos, "PORTERO");
        List<Jugador> lateralesIzq = filterByPos(todos, "LATERAL IZQUIERDO");
        List<Jugador> lateralesDer = filterByPos(todos, "LATERAL DERECHO");
        List<Jugador> centrales = filterByPos(todos, "CENTRAL");
        List<Jugador> mediocentros = filterByPos(todos, "MEDIOCENTRO");
        List<Jugador> interiores = filterByPos(todos, "INTERIOR");
        List<Jugador> extremos = filterByPos(todos, "EXTREMO");
        List<Jugador> delanterosCentro = filterByPos(todos, "DELANTERO CENTRO");
        
        // Ordenar cada lista por influencia
        porteros.sort(cmpInfluencia);
        lateralesIzq.sort(cmpInfluencia);
        lateralesDer.sort(cmpInfluencia);
        centrales.sort(cmpInfluencia);
        mediocentros.sort(cmpInfluencia);
        interiores.sort(cmpInfluencia);
        extremos.sort(cmpInfluencia);
        delanterosCentro.sort(cmpInfluencia);
        
        // Construir alineación según formación específica
        List<Jugador> once = new ArrayList<>();
        
        // PORTERO (siempre 1)
        if (!porteros.isEmpty() && f.porteros > 0) {
            once.add(porteros.get(0));
        }
        
        // SELECCIÓN ESPECÍFICA POR FORMACIÓN
        switch(nombre) {
            case "4-4-2":
                once.addAll(seleccionar442(lateralesIzq, lateralesDer, centrales, mediocentros, interiores, delanterosCentro, extremos, cmpInfluencia));
                break;
            case "4-3-3":
                once.addAll(seleccionar433(lateralesIzq, lateralesDer, centrales, mediocentros, interiores, delanterosCentro, extremos, cmpInfluencia));
                break;
            case "4-2-3-1":
                once.addAll(seleccionar4231(lateralesIzq, lateralesDer, centrales, mediocentros, interiores, delanterosCentro, extremos, cmpInfluencia));
                break;
            case "3-5-2":
                once.addAll(seleccionar352(lateralesIzq, lateralesDer, centrales, mediocentros, interiores, delanterosCentro, extremos, cmpInfluencia));
                break;
            case "3-4-3":
                once.addAll(seleccionar343(lateralesIzq, lateralesDer, centrales, mediocentros, interiores, delanterosCentro, extremos, cmpInfluencia));
                break;
        }
        
        return once;
    }
    
    private List<Jugador> seleccionar442(List<Jugador> lateralesIzq, List<Jugador> lateralesDer,
                                        List<Jugador> centrales, List<Jugador> mediocentros,
                                        List<Jugador> interiores, List<Jugador> delanterosCentro,
                                        List<Jugador> extremos, Comparator<Jugador> cmp) {
        List<Jugador> seleccionados = new ArrayList<>();
        
        // DEFENSAS: LI, DFC1, DFC2, LD
        if (!lateralesIzq.isEmpty()) seleccionados.add(lateralesIzq.get(0));
        if (centrales.size() >= 2) {
            seleccionados.add(centrales.get(0));
            seleccionados.add(centrales.get(1));
        } else if (!centrales.isEmpty()) {
            seleccionados.add(centrales.get(0));
        }
        if (!lateralesDer.isEmpty()) seleccionados.add(lateralesDer.get(0));
        
        // MEDIOS: 4 jugadores (2 mediocentros, 2 interiores)
        int mediosTomados = 0;
        for (int i = 0; i < mediocentros.size() && mediosTomados < 2; i++) {
            seleccionados.add(mediocentros.get(i));
            mediosTomados++;
        }
        for (int i = 0; i < interiores.size() && mediosTomados < 4; i++) {
            seleccionados.add(interiores.get(i));
            mediosTomados++;
        }
        
        // DELANTEROS: 2 delanteros centro
        for (int i = 0; i < Math.min(2, delanterosCentro.size()); i++) {
            seleccionados.add(delanterosCentro.get(i));
        }
        
        return seleccionados;
    }
    
    private List<Jugador> seleccionar433(List<Jugador> lateralesIzq, List<Jugador> lateralesDer,
                                        List<Jugador> centrales, List<Jugador> mediocentros,
                                        List<Jugador> interiores, List<Jugador> delanterosCentro,
                                        List<Jugador> extremos, Comparator<Jugador> cmp) {
        List<Jugador> seleccionados = new ArrayList<>();
        
        // DEFENSAS: LI, DFC1, DFC2, LD
        if (!lateralesIzq.isEmpty()) seleccionados.add(lateralesIzq.get(0));
        if (centrales.size() >= 2) {
            seleccionados.add(centrales.get(0));
            seleccionados.add(centrales.get(1));
        } else if (!centrales.isEmpty()) {
            seleccionados.add(centrales.get(0));
        }
        if (!lateralesDer.isEmpty()) seleccionados.add(lateralesDer.get(0));
        
        // MEDIOS: 3 mediocentros
        for (int i = 0; i < Math.min(3, mediocentros.size()); i++) {
            seleccionados.add(mediocentros.get(i));
        }
        
        // DELANTEROS: EXT IZQ, DC, EXT DER
        if (!extremos.isEmpty()) {
            seleccionados.add(extremos.get(0)); // Extremo izquierdo
        }
        if (!delanterosCentro.isEmpty()) {
            seleccionados.add(delanterosCentro.get(0)); // Delantero centro
        }
        if (extremos.size() > 1) {
            seleccionados.add(extremos.get(1)); // Extremo derecho
        }
        
        return seleccionados;
    }
    
    private List<Jugador> seleccionar4231(List<Jugador> lateralesIzq, List<Jugador> lateralesDer,
                                         List<Jugador> centrales, List<Jugador> mediocentros,
                                         List<Jugador> interiores, List<Jugador> delanterosCentro,
                                         List<Jugador> extremos, Comparator<Jugador> cmp) {
        List<Jugador> seleccionados = new ArrayList<>();
        
        // DEFENSAS: LI, DFC1, DFC2, LD
        if (!lateralesIzq.isEmpty()) seleccionados.add(lateralesIzq.get(0));
        if (centrales.size() >= 2) {
            seleccionados.add(centrales.get(0));
            seleccionados.add(centrales.get(1));
        } else if (!centrales.isEmpty()) {
            seleccionados.add(centrales.get(0));
        }
        if (!lateralesDer.isEmpty()) seleccionados.add(lateralesDer.get(0));
        
        // MEDIOS DEFENSIVOS: 2 mediocentros
        for (int i = 0; i < Math.min(2, mediocentros.size()); i++) {
            seleccionados.add(mediocentros.get(i));
        }
        
        // MEDIOS OFENSIVOS: EXT IZQ, INT CENTRO, EXT DER
        if (!extremos.isEmpty()) {
            seleccionados.add(extremos.get(0)); // Extremo izquierdo
        }
        if (!interiores.isEmpty()) {
            seleccionados.add(interiores.get(0)); // Interior central
        } else if (!mediocentros.isEmpty() && mediocentros.size() > 2) {
            seleccionados.add(mediocentros.get(2)); // Otro mediocentro
        }
        if (extremos.size() > 1) {
            seleccionados.add(extremos.get(1)); // Extremo derecho
        }
        
        // DELANTERO: 1 delantero centro
        if (!delanterosCentro.isEmpty()) {
            seleccionados.add(delanterosCentro.get(0));
        }
        
        return seleccionados;
    }
    
    private List<Jugador> seleccionar352(List<Jugador> lateralesIzq, List<Jugador> lateralesDer,
                                        List<Jugador> centrales, List<Jugador> mediocentros,
                                        List<Jugador> interiores, List<Jugador> delanterosCentro,
                                        List<Jugador> extremos, Comparator<Jugador> cmp) {
        List<Jugador> seleccionados = new ArrayList<>();
        
        // DEFENSAS: 3 centrales
        for (int i = 0; i < Math.min(3, centrales.size()); i++) {
            seleccionados.add(centrales.get(i));
        }
        
        // MEDIOS: LI, MC1, MC2, MC3, LD
        if (!lateralesIzq.isEmpty()) seleccionados.add(lateralesIzq.get(0));
        for (int i = 0; i < Math.min(3, mediocentros.size()); i++) {
            seleccionados.add(mediocentros.get(i));
        }
        if (!lateralesDer.isEmpty()) seleccionados.add(lateralesDer.get(0));
        
        // DELANTEROS: 2 delanteros centro
        for (int i = 0; i < Math.min(2, delanterosCentro.size()); i++) {
            seleccionados.add(delanterosCentro.get(i));
        }
        
        return seleccionados;
    }
    
    private List<Jugador> seleccionar343(List<Jugador> lateralesIzq, List<Jugador> lateralesDer,
                                        List<Jugador> centrales, List<Jugador> mediocentros,
                                        List<Jugador> interiores, List<Jugador> delanterosCentro,
                                        List<Jugador> extremos, Comparator<Jugador> cmp) {
        List<Jugador> seleccionados = new ArrayList<>();
        
        // DEFENSAS: 3 centrales
        for (int i = 0; i < Math.min(3, centrales.size()); i++) {
            seleccionados.add(centrales.get(i));
        }
        
        // MEDIOS: LI, MC1, MC2, LD
        if (!lateralesIzq.isEmpty()) seleccionados.add(lateralesIzq.get(0));
        for (int i = 0; i < Math.min(2, mediocentros.size()); i++) {
            seleccionados.add(mediocentros.get(i));
        }
        if (!lateralesDer.isEmpty()) seleccionados.add(lateralesDer.get(0));
        
        // DELANTEROS: EXT IZQ, DC, EXT DER
        if (!extremos.isEmpty()) {
            seleccionados.add(extremos.get(0));
        }
        if (!delanterosCentro.isEmpty()) {
            seleccionados.add(delanterosCentro.get(0));
        }
        if (extremos.size() > 1) {
            seleccionados.add(extremos.get(1));
        }
        
        return seleccionados;
    }

    private List<Jugador> filterByPos(List<Jugador> list, String pos) {
        List<Jugador> out = new ArrayList<>();
        for (Jugador j : list) {
            if (j.getPosicion().equalsIgnoreCase(pos)) {
                out.add(j);
            }
        }
        return out;
    }
    
    // MÉTODO MEJORADO PARA SUPLENTES BALANCEADOS
    public List<Jugador> recomendarSuplentesBalanceados(List<Jugador> titulares, int cantidad) {
        List<Jugador> todos = new ArrayList<>(gestor.listarJugadores());
        
        // Excluir titulares
        List<Jugador> candidatos = new ArrayList<>();
        for (Jugador j : todos) {
            boolean esTitular = false;
            for (Jugador titular : titulares) {
                if (j.getId() == titular.getId()) {
                    esTitular = true;
                    break;
                }
            }
            if (!esTitular) {
                candidatos.add(j);
            }
        }
        
        // Ordenar por influencia descendente
        candidatos.sort((a, b) -> 
            Integer.compare(calcularInfluencia(b), calcularInfluencia(a)));
        
        // AGREGAR AL MENOS 2 DE CADA TIPO DE POSICIÓN
        List<Jugador> suplentes = new ArrayList<>();
        Map<String, Integer> posicionesContadas = new HashMap<>();
        
        // Inicializar contadores
        posicionesContadas.put("PORTERO", 0);
        posicionesContadas.put("LATERAL IZQUIERDO", 0);
        posicionesContadas.put("LATERAL DERECHO", 0);
        posicionesContadas.put("CENTRAL", 0);
        posicionesContadas.put("MEDIOCENTRO", 0);
        posicionesContadas.put("INTERIOR", 0);
        posicionesContadas.put("EXTREMO", 0);
        posicionesContadas.put("DELANTERO CENTRO", 0);
        
        // PRIMERA PASADA: Tomar al menos 1 de cada posición (hasta 2 si hay)
        for (Jugador j : candidatos) {
            if (suplentes.size() >= cantidad) break;
            
            String pos = j.getPosicion();
            int actual = posicionesContadas.getOrDefault(pos, 0);
            
            // Tomar hasta 2 de cada posición
            if (actual < 2) {
                suplentes.add(j);
                posicionesContadas.put(pos, actual + 1);
            }
        }
        
        // SEGUNDA PASADA: Si aún faltan, completar con los mejores sin importar posición
        if (suplentes.size() < cantidad) {
            for (Jugador j : candidatos) {
                if (suplentes.size() >= cantidad) break;
                if (!suplentes.contains(j)) {
                    suplentes.add(j);
                }
            }
        }
        
        // Ordenar suplentes por influencia para presentación
        suplentes.sort((a, b) -> 
            Integer.compare(calcularInfluencia(b), calcularInfluencia(a)));
        
        return suplentes;
    }
    
    // Método para obtener la distribución de suplentes
    public Map<String, Integer> getDistribucionSuplentes(List<Jugador> suplentes) {
        Map<String, Integer> distribucion = new HashMap<>();
        
        for (Jugador j : suplentes) {
            String pos = j.getPosicion();
            distribucion.put(pos, distribucion.getOrDefault(pos, 0) + 1);
        }
        
        return distribucion;
    }
}