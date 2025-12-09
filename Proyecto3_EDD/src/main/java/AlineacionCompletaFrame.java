import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AlineacionCompletaFrame extends JFrame {
    private Alineador alineador;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    private JPanel panelCards;
    private JLabel lblFormacionActual;
    private JTextArea taEstadisticas;

    public AlineacionCompletaFrame(Alineador alineador) {
        this.alineador = alineador;

        setTitle("🎯 Alineación Visual del Equipo");
        setSize(1250, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        generarAlineacion("4-4-2");
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(0, 70, 140));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("⚽ ALINEACIÓN VISUAL DEL EQUIPO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Unicode MS Black", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);

        lblFormacionActual = new JLabel("Formación: 4-4-2", SwingConstants.RIGHT);
        lblFormacionActual.setFont(new Font("Arial Unicode MS", Font.BOLD, 14));
        lblFormacionActual.setForeground(new Color(255, 255, 200));

        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        panelSuperior.add(lblFormacionActual, BorderLayout.EAST);

        panelCards = new JPanel();
        cardLayout = new CardLayout();
        panelCards.setLayout(cardLayout);

        JPanel panelFormaciones = crearPanelFormaciones();

        JPanel panelEstadisticas = crearPanelEstadisticas();

        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCards, BorderLayout.CENTER);
        panelPrincipal.add(panelFormaciones, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelPrincipal, panelEstadisticas);
        splitPane.setDividerLocation(900);
        splitPane.setResizeWeight(0.8);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelFormaciones() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] formaciones = { "4-4-2", "4-3-3", "3-5-2", "4-2-3-1", "3-4-3" };
        String[] iconos = { "📋", "🔢", "🎯", "⚙️", "🌟" };

        for (int i = 0; i < formaciones.length; i++) {
            JButton btn = new JButton(iconos[i] + " " + formaciones[i]);
            btn.setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
            btn.setBackground(new Color(230, 240, 255));
            btn.setForeground(new Color(0, 70, 140));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 200, 230), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            final String formacion = formaciones[i];
            btn.addActionListener(e -> generarAlineacion(formacion));

            panel.add(btn);
        }

        JButton btnCerrar = new JButton("✖ Cerrar");
        btnCerrar.setBackground(new Color(200, 50, 50));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnCerrar);

        return panel;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(100, 0));
        panel.setBackground(new Color(245, 250, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 240), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel lblTitulo = new JLabel("📊 ESTADÍSTICAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Unicode MS", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(0, 80, 160));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        taEstadisticas = new JTextArea();
        taEstadisticas.setEditable(false);
        taEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 11));
        taEstadisticas.setBackground(new Color(255, 253, 245));
        taEstadisticas.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));
        taEstadisticas.setText("Selecciona una formación para ver estadísticas...");

        JScrollPane scroll = new JScrollPane(taEstadisticas);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void generarAlineacion(String formacion) {
        List<Jugador> once = alineador.recomendarOnce(formacion);
        List<Jugador> suplentes = alineador.recomendarSuplentesBalanceados(once, 7);

        lblFormacionActual.setText("Formación: " + formacion);

        CampoFutbolPanel campoPanel = new CampoFutbolPanel(once, suplentes, formacion);

        JPanel panelInfo = crearPanelInformacion(once, suplentes, formacion);

        JPanel panelFormacion = new JPanel(new BorderLayout());
        panelFormacion.add(campoPanel, BorderLayout.CENTER);
        panelFormacion.add(panelInfo, BorderLayout.SOUTH);

        panelCards.add(panelFormacion, formacion);
        cardLayout.show(panelCards, formacion);

        actualizarEstadisticas(once, suplentes);
    }

    private JPanel crearPanelInformacion(List<Jugador> once, List<Jugador> suplentes, String formacion) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        int influenciaTotal = 0;
        for (Jugador j : once) {
            influenciaTotal += j.getGoles() * 3 + j.getAsistencias() * 2 + j.getPasesClave();
        }

        JLabel lblInfo = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "<b>Formación " + formacion + "</b> | " +
                        "Titulares: " + once.size() + " | " +
                        "Suplentes: " + suplentes.size() + " | " +
                        "Influencia total: <b>" + influenciaTotal + " puntos</b>" +
                        "</div></html>");
        lblInfo.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(0, 80, 160));

        panel.add(lblInfo, BorderLayout.CENTER);

        return panel;
    }

    private void actualizarEstadisticas(List<Jugador> once, List<Jugador> suplentes) {
        StringBuilder sb = new StringBuilder();
        sb.append("═ ESTADÍSTICAS DEL EQUIPO ═\n\n");

        sb.append("TITULARES (").append(once.size()).append("):\n");
        sb.append("────────────────────────────────────\n");

        int totalGoles = 0;
        int totalAsistencias = 0;
        int totalPasesClave = 0;
        int influenciaTotal = 0;

        for (Jugador j : once) {
            totalGoles += j.getGoles();
            totalAsistencias += j.getAsistencias();
            totalPasesClave += j.getPasesClave();

            int influenciaJugador = j.getGoles() * 3 + j.getAsistencias() * 2 + j.getPasesClave();
            influenciaTotal += influenciaJugador;

            sb.append(String.format("#%02d %-18s G:%2d A:%2d PC:%2d Inf:%3d\n",
                    j.getDorsal(), getNombreCorto(j.getNombre()),
                    j.getGoles(), j.getAsistencias(), j.getPasesClave(), influenciaJugador));
        }

        sb.append("\nSUPLENTES (").append(suplentes.size()).append("):\n");
        sb.append("────────────────────────────────────\n");

        for (Jugador j : suplentes) {
            sb.append(String.format("#%02d %-18s G:%2d A:%2d PC:%2d\n",
                    j.getDorsal(), getNombreCorto(j.getNombre()),
                    j.getGoles(), j.getAsistencias(), j.getPasesClave()));
        }

        sb.append("\n═ TOTALES (SOLO TITULARES) ═\n");
        sb.append("────────────────────────────────────\n");
        sb.append(String.format("Goles totales:        %d\n", totalGoles));
        sb.append(String.format("Asistencias total:    %d\n", totalAsistencias));
        sb.append(String.format("Pases clave:          %d\n", totalPasesClave));
        sb.append(String.format("INFLUENCIA TOTAL:     %d puntos\n", influenciaTotal));

        double promedioGoles = once.size() > 0 ? (double) totalGoles / once.size() : 0;
        double promedioAsistencias = once.size() > 0 ? (double) totalAsistencias / once.size() : 0;
        double promedioPasesClave = once.size() > 0 ? (double) totalPasesClave / once.size() : 0;
        double promedioInfluencia = once.size() > 0 ? (double) influenciaTotal / once.size() : 0;

        sb.append("\n═ PROMEDIOS POR TITULAR ═\n");
        sb.append("────────────────────────────────────\n");
        sb.append(String.format("Goles/jugador:        %.2f\n", promedioGoles));
        sb.append(String.format("Asistencias/jugador:  %.2f\n", promedioAsistencias));
        sb.append(String.format("Pases clave/jugador:  %.2f\n", promedioPasesClave));
        sb.append(String.format("Influencia/jugador:   %.1f\n", promedioInfluencia));

        int totalAcciones = totalGoles + totalAsistencias + totalPasesClave;
        double eficiencia = totalAcciones > 0 ? (double) influenciaTotal / totalAcciones : 0;
        sb.append(String.format("Eficiencia/acción:    %.2f\n", eficiencia));

        taEstadisticas.setText(sb.toString());
    }

    private String getNombreCorto(String nombreCompleto) {
        if (nombreCompleto.length() <= 12)
            return nombreCompleto;

        String[] partes = nombreCompleto.split(" ");
        if (partes.length > 1) {
            return partes[partes.length - 1];
        }
        return nombreCompleto.substring(0, 10) + "..";
    }
}