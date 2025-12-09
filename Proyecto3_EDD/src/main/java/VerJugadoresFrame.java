import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class VerJugadoresFrame extends JFrame {
    private GestorJugadores gestor;
    private DefaultListModel<String> modelo;
    private JList<String> lista;
    private List<Integer> ids;

    private JLabel lblFoto;
    private JTextArea taDetalle;

    public VerJugadoresFrame(GestorJugadores gestor) {
        this.gestor = gestor;
        setTitle("👥 Lista de Jugadores");
        setSize(1000, 725);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        initComponents();
        refrescarLista();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("📋 LISTA DE JUGADORES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Unicode MS", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 50, 120));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(20, 20));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(245, 250, 255));

        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBackground(Color.WHITE);
        panelLista.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 220, 240), 2, true),
                new EmptyBorder(10, 10, 10, 10)));

        JLabel lblLista = new JLabel("Jugadores del Equipo", SwingConstants.CENTER);
        lblLista.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblLista.setForeground(new Color(0, 80, 160));
        lblLista.setBorder(new EmptyBorder(0, 0, 10, 0));

        modelo = new DefaultListModel<>();
        lista = new JList<>(modelo);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        lista.setBackground(new Color(250, 253, 255));
        lista.setCellRenderer(new JugadorListRenderer());

        ids = new ArrayList<>();

        JScrollPane sp = new JScrollPane(lista);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));

        panelLista.add(lblLista, BorderLayout.NORTH);
        panelLista.add(sp, BorderLayout.CENTER);

        JPanel panelDetalle = new JPanel(new BorderLayout(10, 10));
        panelDetalle.setBackground(Color.WHITE);
        panelDetalle.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 220, 240), 2, true),
                new EmptyBorder(20, 20, 20, 20)));

        lblFoto = new JLabel("", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(250, 250));
        lblFoto.setBackground(new Color(240, 248, 255));
        lblFoto.setOpaque(true);
        lblFoto.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 230, 240), 2),
                new EmptyBorder(5, 5, 5, 5)));

        JPanel panelInfo = new JPanel(new BorderLayout());
        JLabel lblInfo = new JLabel("📊 ESTADÍSTICAS DEL JUGADOR", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblInfo.setForeground(new Color(0, 80, 160));
        lblInfo.setBorder(new EmptyBorder(0, 0, 10, 0));

        taDetalle = new JTextArea();
        taDetalle.setEditable(false);
        taDetalle.setFont(new Font("Monospaced", Font.PLAIN, 13));
        taDetalle.setBackground(new Color(250, 253, 255));
        taDetalle.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));

        JScrollPane scrollDetalle = new JScrollPane(taDetalle);
        scrollDetalle.setBorder(BorderFactory.createTitledBorder("Información detallada"));

        panelInfo.add(lblInfo, BorderLayout.NORTH);
        panelInfo.add(scrollDetalle, BorderLayout.CENTER);

        panelDetalle.add(lblFoto, BorderLayout.NORTH);
        panelDetalle.add(panelInfo, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBackground(new Color(245, 250, 255));

        JButton btnEditar = createStyledButton("✏️ Editar Jugador", new Color(255, 165, 0));
        JButton btnEliminar = createStyledButton("🗑️ Eliminar Jugador", new Color(200, 50, 50));
        JButton btnCerrar = createStyledButton("✖ Cerrar", new Color(100, 100, 150));

        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        lista.addListSelectionListener(e -> mostrarDetalleSeleccionado());

        JPanel panelContenido = new JPanel(new GridLayout(1, 2, 20, 0));
        panelContenido.add(panelLista);
        panelContenido.add(panelDetalle);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(lblTitulo, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color.darker(), 1),
                new EmptyBorder(10, 25, 10, 25)));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(btn.getBackground().brighter());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    private void refrescarLista() {
        modelo.clear();
        ids.clear();

        List<Jugador> jugadoresOrdenados = new ArrayList<>(gestor.listarJugadores());
        jugadoresOrdenados.sort((a, b) -> Integer.compare(a.getDorsal(), b.getDorsal()));

        for (Jugador j : jugadoresOrdenados) {
            String posicionColor;
            switch (j.getPosicion()) {
                case "PORTERO":
                    posicionColor = "🧤";
                    break;
                case "LATERAL IZQUIERDO":
                    posicionColor = "🟩 LI";
                    break;
                case "LATERAL DERECHO":
                    posicionColor = "🟩 LD";
                    break;
                case "CENTRAL":
                    posicionColor = "🟢 C";
                    break;
                case "MEDIOCENTRO":
                    posicionColor = "🟠 MC";
                    break;
                case "INTERIOR":
                    posicionColor = "🟠 INT";
                    break;
                case "EXTREMO":
                    posicionColor = "🔴 EXT";
                    break;
                case "DELANTERO CENTRO":
                    posicionColor = "🔴 DC";
                    break;
                default:
                    posicionColor = "⚫";
            }

            modelo.addElement(String.format("#%02d %s %s - %s",
                    j.getDorsal(), posicionColor, j.getNombre(), j.getPosicion()));
            ids.add(j.getId());
        }

        lblFoto.setIcon(null);
        taDetalle.setText("");
    }

    public Image cargarFoto(String ruta) {
        File file = new File("resources" + ruta);
        System.out.println("Buscando imagen en: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("ERROR: No se encontró la imagen: " + file.getAbsolutePath());
            return null;
        }

        return new ImageIcon(file.getAbsolutePath()).getImage();
    }

    private void mostrarDetalleSeleccionado() {
        int idx = lista.getSelectedIndex();
        if (idx < 0 || idx >= ids.size()) {
            lblFoto.setIcon(null);
            taDetalle.setText("Selecciona un jugador para ver sus detalles.");
            return;
        }

        int id = ids.get(idx);
        Jugador j = gestor.getJugador(id);
        if (j == null)
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("═ INFORMACIÓN PERSONAL ══════════════════════\n");
        sb.append(" Nombre:    ").append(j.getNombre()).append("\n");
        sb.append(" Dorsal:    #").append(j.getDorsal()).append("\n");
        sb.append(" Edad:      ").append(j.getEdad()).append(" años\n");
        sb.append(" País:      ").append(j.getPais()).append("\n");
        sb.append(" Posición:  ").append(j.getPosicion()).append("\n\n");

        sb.append("═ ESTADÍSTICAS ══════════════════════════════\n");
        sb.append(" Goles:     ").append(j.getGoles()).append("\n");
        sb.append(" Asistencias:").append(j.getAsistencias()).append("\n");
        sb.append(" Pases clave:").append(j.getPasesClave()).append("\n\n");

        sb.append("═ ANÁLISIS DE RENDIMIENTO ══════════════════\n");
        int influencia = j.getGoles() * 3 + j.getAsistencias() * 2 + j.getPasesClave();
        sb.append(" Influencia total: ").append(influencia).append(" puntos\n");

        int totalAcciones = j.getGoles() + j.getAsistencias() + j.getPasesClave();
        if (totalAcciones > 0) {
            double eficiencia = (double) influencia / totalAcciones;
            sb.append(String.format(" Eficiencia/acción:  %.2f puntos\n", eficiencia));
        }

        sb.append("\n═ RENDIMIENTO POR POSICIÓN ════════════════\n");
        switch (j.getPosicion()) {
            case "PORTERO":
                sb.append(" • Función principal: Guardameta\n");
                sb.append(" • Contribución medida en: Pases clave\n");
                break;
            case "LATERAL IZQUIERDO":
            case "LATERAL DERECHO":
                sb.append(" • Función principal: Defensa por banda\n");
                sb.append(" • Contribución medida en: Pases clave\n");
                sb.append(" • Pases clave actuales: ").append(j.getPasesClave()).append("\n");
                break;
            case "CENTRAL":
                sb.append(" • Función principal: Defensa central\n");
                sb.append(" • Contribución medida en: Pases clave\n");
                sb.append(" • Pases clave actuales: ").append(j.getPasesClave()).append("\n");
                break;
            case "MEDIOCENTRO":
                sb.append(" • Función principal: Control del centro\n");
                sb.append(" • Contribución medida en: Asistencias y pases\n");
                sb.append(" • Asistencias: ").append(j.getAsistencias()).append("\n");
                sb.append(" • Pases clave: ").append(j.getPasesClave()).append("\n");
                break;
            case "INTERIOR":
                sb.append(" • Función principal: Creación de juego\n");
                sb.append(" • Contribución medida en: Asistencias\n");
                sb.append(" • Asistencias: ").append(j.getAsistencias()).append("\n");
                break;
            case "EXTREMO":
                sb.append(" • Función principal: Atacar por banda\n");
                sb.append(" • Contribución medida en: Goles y asistencias\n");
                sb.append(" • Contribución total: ").append(j.getGoles() + j.getAsistencias()).append(" (G+A)\n");
                break;
            case "DELANTERO CENTRO":
                sb.append(" • Función principal: Anotar goles\n");
                sb.append(" • Contribución medida en: Goles\n");
                sb.append(" • Goles actuales: ").append(j.getGoles()).append("\n");
                break;
        }

        taDetalle.setText(sb.toString());

        String ruta = j.getRutaFoto();

        if (ruta != null && !ruta.isEmpty()) {
            try {
                // Usar tu método centralizado
                Image foto = cargarFoto(ruta);

                if (foto != null) {
                    // Escalar antes de ponerlo en el JLabel
                    Image scaled = foto.getScaledInstance(240, 240, Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(scaled));
                } else {
                    // Si cargarFoto() devuelve null → ponemos ícono de dorsal
                    lblFoto.setIcon(crearIconoDorsal(j.getDorsal(), j.getPosicion()));
                }

            } catch (Exception ex) {
                // Si hay cualquier error inesperado → ícono de dorsal
                lblFoto.setIcon(crearIconoDorsal(j.getDorsal(), j.getPosicion()));
            }

        } else {
            // Si la ruta está vacía → ícono de dorsal
            lblFoto.setIcon(crearIconoDorsal(j.getDorsal(), j.getPosicion()));
        }
    }

    private ImageIcon crearIconoDorsal(int dorsal, String posicion) {
        Color colorFondo;
        switch (posicion) {
            case "PORTERO":
                colorFondo = new Color(200, 230, 255);
                break;
            case "LATERAL IZQUIERDO":
            case "LATERAL DERECHO":
            case "CENTRAL":
                colorFondo = new Color(200, 255, 220);
                break;
            case "MEDIOCENTRO":
            case "INTERIOR":
                colorFondo = new Color(255, 240, 200);
                break;
            case "EXTREMO":
            case "DELANTERO CENTRO":
                colorFondo = new Color(255, 200, 200);
                break;
            default:
                colorFondo = new Color(240, 240, 240);
        }

        java.awt.image.BufferedImage imagen = new java.awt.image.BufferedImage(
                200, 200, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagen.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(colorFondo);
        g2d.fillRoundRect(10, 10, 180, 180, 40, 40);

        g2d.setColor(colorFondo.darker());
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(10, 10, 180, 180, 40, 40);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial Unicode MS", Font.BOLD, 72));

        String dorsalStr = String.valueOf(dorsal);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (200 - fm.stringWidth(dorsalStr)) / 2;
        int y = (200 - fm.getHeight()) / 2 + fm.getAscent();

        g2d.drawString(dorsalStr, x, y);

        g2d.dispose();
        return new ImageIcon(imagen);
    }

    private void editarSeleccionado() {
        int idx = lista.getSelectedIndex();
        if (idx < 0 || idx >= ids.size()) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un jugador primero.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = ids.get(idx);
        Jugador j = gestor.getJugador(id);
        if (j == null) {
            refrescarLista();
            return;
        }

        EditarJugadorFrame editarFrame = new EditarJugadorFrame(gestor, j);
        editarFrame.setVisible(true);

        editarFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refrescarLista();
            }
        });
    }

    private void eliminarSeleccionado() {
        int idx = lista.getSelectedIndex();
        if (idx < 0 || idx >= ids.size()) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un jugador primero.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = ids.get(idx);
        Jugador j = gestor.getJugador(id);
        if (j == null) {
            refrescarLista();
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar permanentemente a " + j.getNombre() + " (#" + j.getDorsal() + ")?\n\n" +
                        "Posición: " + j.getPosicion() + "\n" +
                        "Esta acción no se puede deshacer.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (conf != JOptionPane.YES_OPTION)
            return;

        gestor.eliminarJugador(id);
        try {
            gestor.guardarEnArchivo(new File("jugadores.txt"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar cambios:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        refrescarLista();
        JOptionPane.showMessageDialog(this,
                "✅ Jugador eliminado exitosamente:\n" + j.getNombre() + " (#" + j.getDorsal() + ")",
                "Eliminación completada", JOptionPane.INFORMATION_MESSAGE);
    }

    class JugadorListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (index >= 0 && index < ids.size()) {
                Jugador j = gestor.getJugador(ids.get(index));
                if (j != null) {
                    Color bgColor;
                    switch (j.getPosicion()) {
                        case "PORTERO":
                            bgColor = new Color(230, 240, 255);
                            break;
                        case "LATERAL IZQUIERDO":
                        case "LATERAL DERECHO":
                        case "CENTRAL":
                            bgColor = new Color(230, 255, 240);
                            break;
                        case "MEDIOCENTRO":
                        case "INTERIOR":
                            bgColor = new Color(255, 245, 230);
                            break;
                        case "EXTREMO":
                        case "DELANTERO CENTRO":
                            bgColor = new Color(255, 230, 230);
                            break;
                        default:
                            bgColor = Color.WHITE;
                    }

                    if (isSelected) {
                        setBackground(bgColor.darker());
                        setForeground(Color.BLACK);
                    } else {
                        setBackground(bgColor);
                    }

                    setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 230, 240)),
                            new EmptyBorder(8, 10, 8, 10)));
                }
            }
            return this;
        }
    }
}