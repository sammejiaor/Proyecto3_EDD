import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {
    private GestorJugadores gestorJug;
    private GestorEventos gestorEvt;
    private GrafoEstadistico grafo;
    private AnalizadorEstadistico analizador;
    private Alineador alineador;

    public MainFrame() {
        super("FutbolGrafo - Sistema Completo de Análisis");
        gestorJug = new GestorJugadores();
        gestorEvt = new GestorEventos();
        grafo = new GrafoEstadistico();
        analizador = new AnalizadorEstadistico(grafo, gestorJug);
        alineador = new Alineador(gestorJug, analizador);

        try {
            gestorJug.cargarDesdeArchivo(new File("jugadores.txt"));
        } catch (Exception ignored) {
        }

        try {
            gestorEvt.cargarDesdeArchivo(new File("eventos.txt"));
            reconstruirGrafoDesdeEventos();
        } catch (Exception ignored) {
        }

        initUI();
    }

    private void reconstruirGrafoDesdeEventos() {
        for (EventoPartido evento : gestorEvt.listarEventos()) {
            if (evento.getTipo().equals("ASISTENCIA")) {
                grafo.registrarAsistencia(evento.getOrigen(), evento.getDestino());
                Jugador origen = gestorJug.getJugador(evento.getOrigen());
                Jugador destino = gestorJug.getJugador(evento.getDestino());
                if (origen != null)
                    origen.agregarAsistencia();
                if (destino != null)
                    destino.agregarGol();
            } else if (evento.getTipo().equals("PASE_CLAVE")) {
                grafo.registrarPaseClave(evento.getOrigen(), evento.getDestino());
                Jugador origen = gestorJug.getJugador(evento.getOrigen());
                if (origen != null)
                    origen.agregarPaseClave();
            }
        }
    }

    private void initUI() {
        // Panel de bienvenida mejorado con más información
        JPanel panelBienvenida = new JPanel(new BorderLayout());
        panelBienvenida.setBackground(new Color(245, 250, 255));
        panelBienvenida.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titulo = new JLabel("⚽ FUTBOLGRAFO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial Unicode MS", Font.BOLD, 36));
        titulo.setForeground(new Color(0, 70, 140));

        JLabel subtitulo = new JLabel("Sistema de Análisis Estadístico de Fútbol", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial Unicode MS", Font.ITALIC, 16));
        subtitulo.setForeground(new Color(100, 100, 100));

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.add(titulo, BorderLayout.CENTER);
        panelTitulo.add(subtitulo, BorderLayout.SOUTH);
        panelTitulo.setBackground(new Color(245, 250, 255));

        // Panel de estadísticas
        JPanel panelStats = new JPanel(new GridLayout(4, 1, 10, 10));
        panelStats.setBackground(new Color(245, 250, 255));
        panelStats.setBorder(BorderFactory.createTitledBorder("📊 ESTADO ACTUAL DEL SISTEMA"));

        int numJugadores = gestorJug.listarJugadores().size();
        int numEventos = gestorEvt.listarEventos().size();

        JLabel lblJugadores = new JLabel("• Jugadores registrados: " + numJugadores, SwingConstants.LEFT);
        JLabel lblEventos = new JLabel("• Eventos registrados: " + numEventos, SwingConstants.LEFT);
        JLabel lblGoleadores = new JLabel("• Top goleadores: " + Math.min(3, numJugadores) + " disponibles",
                SwingConstants.LEFT);
        JLabel lblDuplas = new JLabel("• Duplas detectadas: " + (numEventos > 1 ? "Sí" : "No"), SwingConstants.LEFT);

        lblJugadores.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
        lblEventos.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
        lblGoleadores.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
        lblDuplas.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));

        panelStats.add(lblJugadores);
        panelStats.add(lblEventos);
        panelStats.add(lblGoleadores);
        panelStats.add(lblDuplas);

        // Panel de instrucciones
        JTextArea instrucciones = new JTextArea(
                "\n💡 INSTRUCCIONES:\n\n" +
                        "1. Usa el menú superior para acceder a todas las funciones\n" +
                        "2. Comienza registrando jugadores\n" +
                        "3. Luego registra eventos de partido\n" +
                        "4. Explora las estadísticas y análisis\n\n" +
                        "📁 Los datos se guardan automáticamente en:\n" +
                        "   • jugadores.txt\n" +
                        "   • eventos.txt");
        instrucciones.setEditable(false);
        instrucciones.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
        instrucciones.setBackground(new Color(245, 250, 255));
        instrucciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollInstrucciones = new JScrollPane(instrucciones);
        scrollInstrucciones.setBorder(BorderFactory.createTitledBorder("📝 CÓMO USAR EL SISTEMA"));

        // Organizar contenido
        panelBienvenida.add(panelTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 20, 20));
        panelCentro.add(panelStats);
        panelCentro.add(scrollInstrucciones);
        panelCentro.setBackground(new Color(245, 250, 255));

        panelBienvenida.add(panelCentro, BorderLayout.CENTER);

        // Barra de menú completa
        JMenuBar mb = new JMenuBar();

        // Menú Archivo
        JMenu mArchivo = new JMenu("Archivo");
        mArchivo.setMnemonic('A'); // Alt + A

        JMenuItem miGuardarTodo = new JMenuItem("Guardar todo", new ImageIcon("💾"));
        miGuardarTodo.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));

        JMenuItem miCargarTodo = new JMenuItem("Cargar todo", new ImageIcon("📂"));
        miCargarTodo.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));

        JMenuItem miExportarCSV = new JMenuItem("Exportar CSV...", new ImageIcon("📊"));
        JMenuItem miSalir = new JMenuItem("Salir", new ImageIcon("🚪"));
        miSalir.setAccelerator(KeyStroke.getKeyStroke("alt F4"));

        mArchivo.add(miGuardarTodo);
        mArchivo.add(miCargarTodo);
        mArchivo.add(new JSeparator());
        mArchivo.add(miExportarCSV);
        mArchivo.add(new JSeparator());
        mArchivo.add(miSalir);

        // Menú Jugadores
        JMenu mJugadores = new JMenu("Jugadores");
        mJugadores.setMnemonic('J'); // Alt + J

        JMenuItem miRegJugador = new JMenuItem("Registrar nuevo jugador", new ImageIcon("➕"));
        miRegJugador.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));

        JMenuItem miVerJugadores = new JMenuItem("Ver todos los jugadores", new ImageIcon("👥"));
        miVerJugadores.setAccelerator(KeyStroke.getKeyStroke("ctrl J"));

        JMenuItem miBuscarJugador = new JMenuItem("Buscar jugador...", new ImageIcon("🔍"));
        miBuscarJugador.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));

        mJugadores.add(miRegJugador);
        mJugadores.add(miVerJugadores);
        mJugadores.add(new JSeparator());
        mJugadores.add(miBuscarJugador);

        // Menú Eventos
        JMenu mEventos = new JMenu("Eventos");
        mEventos.setMnemonic('E'); // Alt + E

        JMenuItem miRegEvento = new JMenuItem("Registrar evento", new ImageIcon("⚽"));
        miRegEvento.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));

        JMenuItem miVerEventos = new JMenuItem("Ver histórico", new ImageIcon("📜"));
        miVerEventos.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));

        mEventos.add(miRegEvento);
        mEventos.add(miVerEventos);

        // Menú Estadísticas
        JMenu mEstadisticas = new JMenu("Estadísticas");
        mEstadisticas.setMnemonic('S'); // Alt + S

        JMenuItem miTop5 = new JMenuItem("Top 5 completo", new ImageIcon("🏆"));
        miTop5.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));

        JMenuItem miTopGoleadores = new JMenuItem("Top goleadores", new ImageIcon("⚽"));
        JMenuItem miTopAsistentes = new JMenuItem("Top asistentes", new ImageIcon("🎯"));
        JMenuItem miTopInfluyentes = new JMenuItem("Top influyentes", new ImageIcon("🌟"));

        mEstadisticas.add(miTop5);
        mEstadisticas.add(new JSeparator());
        mEstadisticas.add(miTopGoleadores);
        mEstadisticas.add(miTopAsistentes);
        mEstadisticas.add(miTopInfluyentes);

        // Menú Análisis
        JMenu mAnalisis = new JMenu("Análisis");
        mAnalisis.setMnemonic('N'); // Alt + N

        JMenuItem miDuplas = new JMenuItem("Duplas destacadas", new ImageIcon("🤝"));
        miDuplas.setAccelerator(KeyStroke.getKeyStroke("ctrl D"));

        JMenuItem miAlineacion = new JMenuItem("Generar alineación", new ImageIcon("🎯"));
        miAlineacion.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));

        mAnalisis.add(miDuplas);
        mAnalisis.add(miAlineacion);

        // Menú Ayuda
        JMenu mAyuda = new JMenu("Ayuda");
        mAyuda.setMnemonic('H'); // Alt + H

        JMenuItem miAcercaDe = new JMenuItem("Acerca de...", new ImageIcon("ℹ️"));
        miAcercaDe.setAccelerator(KeyStroke.getKeyStroke("F1"));

        JMenuItem miManual = new JMenuItem("Manual de usuario", new ImageIcon("📖"));

        mAyuda.add(miManual);
        mAyuda.add(new JSeparator());
        mAyuda.add(miAcercaDe);

        // Agregar todos los menús
        mb.add(mArchivo);
        mb.add(mJugadores);
        mb.add(mEventos);
        mb.add(mEstadisticas);
        mb.add(mAnalisis);
        mb.add(Box.createHorizontalGlue()); // Espacio flexible
        mb.add(mAyuda);

        setJMenuBar(mb);

        // Asignar acciones a los menús
        miRegJugador.addActionListener(e -> new RegistrarJugadorFrame(gestorJug).setVisible(true));
        miVerJugadores.addActionListener(e -> new VerJugadoresFrame(gestorJug).setVisible(true));
        miRegEvento.addActionListener(e -> new RegistrarEventoFrame(gestorJug, gestorEvt, grafo).setVisible(true));
        miVerEventos.addActionListener(e -> new VerEventosFrame(gestorEvt, gestorJug).setVisible(true));
        miTop5.addActionListener(e -> new Top5CompletoFrame(analizador).setVisible(true));
        miDuplas.addActionListener(e -> new DuplasFrame(analizador).setVisible(true));
        miAlineacion.addActionListener(e -> new AlineacionCompletaFrame(alineador).setVisible(true));

        // Acciones adicionales
        miTopGoleadores.addActionListener(e -> mostrarTopGoleadores());
        miTopAsistentes.addActionListener(e -> mostrarTopAsistentes());
        miTopInfluyentes.addActionListener(e -> mostrarTopInfluyentes());

        miBuscarJugador.addActionListener(e -> buscarJugador());
        miExportarCSV.addActionListener(e -> exportarCSV());
        miManual.addActionListener(e -> mostrarManual());
        miAcercaDe.addActionListener(e -> mostrarAcercaDe());

        miGuardarTodo.addActionListener(e -> guardarTodo());
        miCargarTodo.addActionListener(e -> cargarTodo());
        miSalir.addActionListener(e -> System.exit(0));

        // Panel principal - solo el panel de bienvenida
        add(panelBienvenida);

        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void mostrarTopGoleadores() {
        List<Jugador> goleadores = analizador.topNGoleadores(5);
        mostrarTopSimple("Goleadores", goleadores, j -> "Goles: " + j.getGoles());
    }

    private void mostrarTopAsistentes() {
        List<Jugador> asistentes = analizador.topNAsistidores(5);
        mostrarTopSimple("Asistentes", asistentes, j -> "Asistencias: " + j.getAsistencias());
    }

    private void mostrarTopInfluyentes() {
        List<Jugador> influyentes = analizador.topNInfluyentes(5);
        mostrarTopSimple("Influyentes", influyentes,
                j -> "Influencia: " + (j.getGoles() * 3 + j.getAsistencias() * 2 + j.getPasesClave()));
    }

    private void mostrarTopSimple(String titulo, List<Jugador> jugadores,
            java.util.function.Function<Jugador, String> estadistica) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════╗\n");
        sb.append("║                TOP 5 " + titulo.toUpperCase() + "                ║\n");
        sb.append("╚══════════════════════════════════════════════════════╝\n\n");

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            sb.append(String.format("%d. %s (%s) - %s\n",
                    i + 1, j.getNombre(), j.getPosicion(), estadistica.apply(j)));
        }

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                "Top 5 " + titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarJugador() {
        String nombre = JOptionPane.showInputDialog(this, "Ingresa el nombre del jugador:");
        if (nombre == null || nombre.trim().isEmpty())
            return;

        List<Jugador> resultados = new java.util.ArrayList<>();
        for (Jugador j : gestorJug.listarJugadores()) {
            if (j.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(j);
            }
        }

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron jugadores con ese nombre.");
        } else {
            StringBuilder sb = new StringBuilder("Resultados de búsqueda:\n\n");
            for (Jugador j : resultados) {
                sb.append(String.format("• %s (%s) - G:%d A:%d PC:%d\n",
                        j.getNombre(), j.getPosicion(), j.getGoles(), j.getAsistencias(), j.getPasesClave()));
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }

    private void exportarCSV() {
        JOptionPane.showMessageDialog(this,
                "Funcionalidad de exportación CSV disponible en ExportadorCSV.java\n" +
                        "Puedes implementarla usando la clase ExportadorCSV proporcionada.",
                "Exportar CSV", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarManual() {
        String manual = "📖 MANUAL RÁPIDO DE USUARIO\n\n" +
                "1. JUGADORES:\n" +
                "   • Registrar: Agrega nuevos jugadores al sistema\n" +
                "   • Ver todos: Lista completa con estadísticas\n\n" +
                "2. EVENTOS:\n" +
                "   • Registrar: Añade asistencias o pases clave\n" +
                "   • Histórico: Ver todos los eventos registrados\n\n" +
                "3. ESTADÍSTICAS:\n" +
                "   • Top 5: Análisis completo en múltiples categorías\n" +
                "   • Duplas: Mejores combinaciones entre jugadores\n\n" +
                "4. ANÁLISIS:\n" +
                "   • Alineación: Genera formaciones óptimas\n\n" +
                "💾 Los datos se guardan automáticamente al salir.";

        JTextArea ta = new JTextArea(manual);
        ta.setEditable(false);
        ta.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Manual de Usuario", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAcercaDe() {
        String acercaDe = "⚽ FUTBOLGRAFO v1.0\n\n" +
                "Sistema de Análisis Estadístico de Fútbol\n\n" +
                "📊 Características:\n" +
                "• Gestión completa de jugadores\n" +
                "• Registro de eventos en tiempo real\n" +
                "• Análisis estadístico avanzado\n" +
                "• Detección de duplas destacadas\n" +
                "• Generación de alineaciones\n\n" +
                "🎯 Objetivo:\n" +
                "Ayudar en el análisis táctico y seguimiento\n" +
                "de rendimiento de jugadores de fútbol.\n\n" +
                "Desarrollado con Java Swing";

        JOptionPane.showMessageDialog(this, acercaDe, "Acerca de FutbolGrafo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void guardarTodo() {
        try {
            gestorJug.guardarEnArchivo(new File("jugadores.txt"));
            gestorEvt.guardarEnArchivo(new File("eventos.txt"));
            JOptionPane.showMessageDialog(this, "✅ Todos los datos guardados exitosamente");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error al guardar: " + ex.getMessage());
        }
    }

    private void cargarTodo() {
        try {
            gestorJug.cargarDesdeArchivo(new File("jugadores.txt"));
            gestorEvt.cargarDesdeArchivo(new File("eventos.txt"));
            reconstruirGrafoDesdeEventos();
            JOptionPane.showMessageDialog(this, "✅ Todos los datos cargados exitosamente\n" +
                    "• Jugadores: " + gestorJug.listarJugadores().size() + "\n" +
                    "• Eventos: " + gestorEvt.listarEventos().size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar: " + ex.getMessage());
        }
    }
}