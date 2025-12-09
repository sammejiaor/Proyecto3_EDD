import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegistrarEventoFrame extends JFrame {
    private GestorJugadores gestorJug;
    private GestorEventos gestorEvt;
    private GrafoEstadistico grafo;
    private JComboBox<String> cbOrigen, cbDestino;
    private JSpinner spAsistencias, spPasesClave;

    public RegistrarEventoFrame(GestorJugadores gestorJug, GestorEventos gestorEvt, GrafoEstadistico grafo) {
        this.gestorJug = gestorJug;
        this.gestorEvt = gestorEvt;
        this.grafo = grafo;

        setTitle("Registrar Eventos de Partido");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        cargarJugadoresEnCombos();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        JLabel lblTitulo = new JLabel("⚽ REGISTRAR EVENTOS DE PARTIDO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Unicode MS Black", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 70, 140));
        panel.add(lblTitulo, gbc);

        // Subtítulo
        gbc.gridy = 1;
        JLabel lblSubtitulo = new JLabel("Registra asistencias y pases clave entre dos jugadores",
                SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 12));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        panel.add(lblSubtitulo, gbc);

        // Línea separadora
        gbc.gridy = 2;
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 220, 240));
        panel.add(separator, gbc);

        // Jugador Origen
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel lblOrigen = new JLabel("Jugador Origen:");
        lblOrigen.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        panel.add(lblOrigen, gbc);

        cbOrigen = new JComboBox<>();
        cbOrigen.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panel.add(cbOrigen, gbc);

        // Jugador Destino
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel lblDestino = new JLabel("Jugador Destino:");
        lblDestino.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        panel.add(lblDestino, gbc);

        cbDestino = new JComboBox<>();
        cbDestino.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        panel.add(cbDestino, gbc);

        // Línea separadora
        gbc.gridy = 5;
        JSeparator separator2 = new JSeparator();
        separator2.setForeground(new Color(200, 220, 240));
        panel.add(separator2, gbc);

        // Panel para estadísticas
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        JPanel panelEstadisticas = new JPanel(new GridLayout(2, 1, 10, 10));
        panelEstadisticas.setBackground(new Color(245, 250, 255));
        panelEstadisticas.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 230), 2),
                "📊 CANTIDAD DE EVENTOS"));

        // Panel para Asistencias
        JPanel panelAsistencias = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelAsistencias.setBackground(new Color(230, 245, 255));
        panelAsistencias.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblAsistencias = new JLabel("⚽ Asistencias:");
        lblAsistencias.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblAsistencias.setForeground(new Color(0, 100, 200));

        spAsistencias = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        spAsistencias.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        spAsistencias.setBackground(Color.WHITE);
        JSpinner.DefaultEditor editorAsist = (JSpinner.DefaultEditor) spAsistencias.getEditor();
        editorAsist.getTextField().setHorizontalAlignment(JTextField.CENTER);
        editorAsist.getTextField().setColumns(4);

        // Botones rápidos para asistencias
        JButton btnAsist5 = crearBotonRapido("+5", spAsistencias, 5);
        JButton btnAsist10 = crearBotonRapido("+10", spAsistencias, 10);

        panelAsistencias.add(lblAsistencias);
        panelAsistencias.add(spAsistencias);
        panelAsistencias.add(btnAsist5);
        panelAsistencias.add(btnAsist10);

        // Panel para Pases Clave
        JPanel panelPases = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelPases.setBackground(new Color(255, 245, 230));
        panelPases.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblPases = new JLabel("🎯 Pases Clave:");
        lblPases.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblPases.setForeground(new Color(200, 100, 0));

        spPasesClave = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        spPasesClave.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        spPasesClave.setBackground(Color.WHITE);
        JSpinner.DefaultEditor editorPases = (JSpinner.DefaultEditor) spPasesClave.getEditor();
        editorPases.getTextField().setHorizontalAlignment(JTextField.CENTER);
        editorPases.getTextField().setColumns(4);

        // Botones rápidos para pases clave
        JButton btnPases5 = crearBotonRapido("+5", spPasesClave, 5);
        JButton btnPases10 = crearBotonRapido("+10", spPasesClave, 10);

        panelPases.add(lblPases);
        panelPases.add(spPasesClave);
        panelPases.add(btnPases5);
        panelPases.add(btnPases10);

        panelEstadisticas.add(panelAsistencias);
        panelEstadisticas.add(panelPases);
        panel.add(panelEstadisticas, gbc);

        // Panel de botones principales
        gbc.gridy = 7;
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setBackground(new Color(245, 250, 255));

        JButton btnRegistrar = new JButton("✅ Registrar Eventos");
        btnRegistrar.setBackground(new Color(0, 150, 0));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        btnRegistrar.addActionListener(e -> registrarEvento());

        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(200, 50, 50));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);

        panel.add(panelBotones, gbc);

        // Panel de información
        gbc.gridy = 8;
        JLabel lblInfo = new JLabel(
                "<html><div style='text-align: center; font-size: 11px; color: #666; padding: 5px;'>" +
                        "⚽ <b>ASISTENCIA:</b> Pase que resulta en gol (suma 1 gol al destino)<br>" +
                        "🎯 <b>PASE_CLAVE:</b> Pase importante que crea oportunidad de gol" +
                        "</div></html>",
                SwingConstants.CENTER);

        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBackground(new Color(240, 248, 255));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 240)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        panelInfo.add(lblInfo, BorderLayout.CENTER);

        panel.add(panelInfo, gbc);

        // Configurar panel principal
        panel.setBackground(new Color(245, 250, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
    }

    private JButton crearBotonRapido(String texto, JSpinner spinner, int valor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        btn.setBackground(new Color(240, 240, 240));
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            int actual = (int) spinner.getValue();
            spinner.setValue(actual + valor);
        });
        return btn;
    }

    private void cargarJugadoresEnCombos() {
        cbOrigen.removeAllItems();
        cbDestino.removeAllItems();

        List<Jugador> jugadoresOrdenados = obtenerJugadoresOrdenadosPorDorsal();

        if (jugadoresOrdenados.isEmpty()) {
            cbOrigen.addItem("⚠️ No hay jugadores registrados");
            cbDestino.addItem("⚠️ No hay jugadores registrados");
            cbOrigen.setEnabled(false);
            cbDestino.setEnabled(false);
            spAsistencias.setEnabled(false);
            spPasesClave.setEnabled(false);
            return;
        }

        cbOrigen.addItem("-- Selecciona un jugador --");
        cbDestino.addItem("-- Selecciona un jugador --");

        for (Jugador j : jugadoresOrdenados) {
            String emojiPosicion = obtenerEmojiPosicion(j.getPosicion());
            String item = String.format("#%02d %s %s - %s",
                    j.getDorsal(), emojiPosicion, j.getNombre(), j.getPosicion());
            cbOrigen.addItem(item);
            cbDestino.addItem(item);
        }
    }

    private List<Jugador> obtenerJugadoresOrdenadosPorDorsal() {
        List<Jugador> jugadoresOrdenados = new ArrayList<>();
        for (Jugador j : gestorJug.listarJugadores()) {
            jugadoresOrdenados.add(j);
        }

        jugadoresOrdenados.sort((a, b) -> Integer.compare(a.getDorsal(), b.getDorsal()));
        return jugadoresOrdenados;
    }

    private String obtenerEmojiPosicion(String posicion) {
        switch (posicion) {
            case "PORTERO":
                return "🧤";
            case "LATERAL IZQUIERDO":
                return "🟩 LI";
            case "LATERAL DERECHO":
                return "🟩 LD";
            case "CENTRAL":
                return "🟢 C";
            case "MEDIOCENTRO":
                return "🟠 MC";
            case "INTERIOR":
                return "🟠 INT";
            case "EXTREMO":
                return "🔴 EXT";
            case "DELANTERO CENTRO":
                return "🔴 DC";
            default:
                return "⚫";
        }
    }

    private int obtenerIdDesdeTexto(String textoItem) {
        if (textoItem.startsWith("#")) {
            String[] partes = textoItem.split(" ");
            if (partes.length > 0) {
                String dorsalStr = partes[0].replace("#", "");
                try {
                    int dorsal = Integer.parseInt(dorsalStr);

                    for (Jugador j : gestorJug.listarJugadores()) {
                        if (j.getDorsal() == dorsal) {
                            return j.getId();
                        }
                    }
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
        return -1;
    }

    private void registrarEvento() {
        if (gestorJug.listarJugadores().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "❌ No hay jugadores registrados.\n" +
                            "Primero registra algunos jugadores.",
                    "Sin jugadores", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cbOrigen.getSelectedIndex() <= 0 || cbDestino.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona ambos jugadores",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String origenItem = (String) cbOrigen.getSelectedItem();
        String destinoItem = (String) cbDestino.getSelectedItem();

        if (origenItem.contains("-- Selecciona") || destinoItem.contains("-- Selecciona")) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona jugadores válidos",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idOrigen = obtenerIdDesdeTexto(origenItem);
        int idDestino = obtenerIdDesdeTexto(destinoItem);

        if (idOrigen == -1 || idDestino == -1) {
            JOptionPane.showMessageDialog(this,
                    "Error al identificar los jugadores",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idOrigen == idDestino) {
            JOptionPane.showMessageDialog(this,
                    "El jugador origen y destino no pueden ser el mismo",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Jugador jugadorOrigen = gestorJug.getJugador(idOrigen);
        Jugador jugadorDestino = gestorJug.getJugador(idDestino);

        if (jugadorOrigen == null || jugadorDestino == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron los jugadores seleccionados",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int asistencias = (int) spAsistencias.getValue();
        int pasesClave = (int) spPasesClave.getValue();

        if (asistencias == 0 && pasesClave == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debes especificar al menos una asistencia o un pase clave",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int totalEventos = asistencias + pasesClave;
        if (totalEventos > 50) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Estás registrando " + totalEventos + " eventos a la vez.\n" +
                            "¿Estás seguro de que es correcto?",
                    "Confirmar cantidad grande",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION)
                return;
        }

        String mensajeConfirmacion = String.format(
                "<html><div style='font-size: 13px;'>" +
                        "<b>¿Registrar los siguientes eventos?</b><br><br>" +
                        "<b>Origen:</b> %s (#%d)<br>" +
                        "<b>Destino:</b> %s (#%d)<br><br>" +
                        "<b>Eventos a registrar:</b><br>" +
                        "• ⚽ Asistencias: <b>%d</b><br>" +
                        "• 🎯 Pases clave: <b>%d</b><br>" +
                        "• 📊 Total: <b>%d eventos</b><br><br>" +
                        "<i>Se guardarán en el historial.</i>" +
                        "</div></html>",
                jugadorOrigen.getNombre(), jugadorOrigen.getDorsal(),
                jugadorDestino.getNombre(), jugadorDestino.getDorsal(),
                asistencias, pasesClave, totalEventos);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                mensajeConfirmacion, "Confirmar registro",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Registrar asistencias
        if (asistencias > 0) {
            for (int i = 0; i < asistencias; i++) {
                grafo.registrarAsistencia(idOrigen, idDestino);
                jugadorOrigen.agregarAsistencia();
                jugadorDestino.agregarGol();
                EventoPartido evento = new EventoPartido(idOrigen, idDestino, "ASISTENCIA");
                gestorEvt.registrarEvento(evento);
            }
        }

        // Registrar pases clave
        if (pasesClave > 0) {
            for (int i = 0; i < pasesClave; i++) {
                grafo.registrarPaseClave(idOrigen, idDestino);
                jugadorOrigen.agregarPaseClave();
                EventoPartido evento = new EventoPartido(idOrigen, idDestino, "PASE_CLAVE");
                gestorEvt.registrarEvento(evento);
            }
        }

        // Guardar en archivos
        try {
            gestorEvt.guardarEnArchivo(new File("eventos.txt"));
            gestorJug.guardarEnArchivo(new File("jugadores.txt"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Limpiar selecciones para próximo registro
        cbOrigen.setSelectedIndex(0);
        cbDestino.setSelectedIndex(0);
        spAsistencias.setValue(0);
        spPasesClave.setValue(0);
    }
}