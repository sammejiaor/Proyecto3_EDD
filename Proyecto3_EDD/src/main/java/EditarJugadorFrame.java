import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class EditarJugadorFrame extends JFrame {
    private GestorJugadores gestor;
    private Jugador jugadorOriginal;
    private JTextField tfNombre, tfEdad, tfPais, tfDorsal;
    private JComboBox<String> cbPosicion;
    private JLabel lblFoto;
    private File archivoFoto = null;
    private JLabel lblId;

    public EditarJugadorFrame(GestorJugadores gestor, Jugador jugador) {
        this.gestor = gestor;
        this.jugadorOriginal = jugador;

        setTitle("✏️ Editar Jugador: " + jugador.getNombre());
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 250, 255));
        initComponents();
        cargarDatosJugador();
    }

    private void initComponents() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(245, 250, 255));

        JLabel lblTitulo = new JLabel("✏️ EDITAR JUGADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Unicode MS Black", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 50, 120));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 220, 240), 2, true),
                new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI Emoji", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI Emoji", Font.PLAIN, 13);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblIdLabel = new JLabel("ID del jugador:");
        lblIdLabel.setFont(labelFont);
        panelFormulario.add(lblIdLabel, gbc);

        lblId = new JLabel(String.valueOf(jugadorOriginal.getId()));
        lblId.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblId.setForeground(new Color(100, 100, 100));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panelFormulario.add(lblId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblNombre = new JLabel("Nombre completo:");
        lblNombre.setFont(labelFont);
        panelFormulario.add(lblNombre, gbc);

        tfNombre = new JTextField(20);
        tfNombre.setFont(fieldFont);
        tfNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panelFormulario.add(tfNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setFont(labelFont);
        panelFormulario.add(lblEdad, gbc);

        tfEdad = new JTextField(5);
        tfEdad.setFont(fieldFont);
        tfEdad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 1;
        panelFormulario.add(tfEdad, gbc);

        gbc.gridx = 2;
        JLabel lblDorsal = new JLabel("Dorsal (1-99):");
        lblDorsal.setFont(labelFont);
        panelFormulario.add(lblDorsal, gbc);

        tfDorsal = new JTextField(5);
        tfDorsal.setFont(fieldFont);
        tfDorsal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 3;
        panelFormulario.add(tfDorsal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel lblPais = new JLabel("País:");
        lblPais.setFont(labelFont);
        panelFormulario.add(lblPais, gbc);

        tfPais = new JTextField(20);
        tfPais.setFont(fieldFont);
        tfPais.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panelFormulario.add(tfPais, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel lblPosicion = new JLabel("Posición:");
        lblPosicion.setFont(labelFont);
        panelFormulario.add(lblPosicion, gbc);

        cbPosicion = new JComboBox<>(new String[] {
                "PORTERO",
                "LATERAL IZQUIERDO",
                "LATERAL DERECHO",
                "CENTRAL",
                "MEDIOCENTRO",
                "INTERIOR",
                "EXTREMO",
                "DELANTERO CENTRO"
        });
        cbPosicion.setFont(fieldFont);
        cbPosicion.setBackground(Color.WHITE);
        cbPosicion.setBorder(BorderFactory.createLineBorder(new Color(200, 220, 240)));

        cbPosicion.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected,
                    boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (index >= 0) {
                    String pos = (String) value;
                    switch (pos) {
                        case "PORTERO":
                            setForeground(new Color(0, 100, 200));
                            break;
                        case "LATERAL IZQUIERDO":
                        case "LATERAL DERECHO":
                        case "CENTRAL":
                            setForeground(new Color(0, 150, 0));
                            break;
                        case "MEDIOCENTRO":
                        case "INTERIOR":
                            setForeground(new Color(255, 140, 0));
                            break;
                        case "EXTREMO":
                        case "DELANTERO CENTRO":
                            setForeground(new Color(200, 0, 0));
                            break;
                    }
                }
                return c;
            }
        });
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panelFormulario.add(cbPosicion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JLabel lblFotoLabel = new JLabel("Foto del jugador:");
        lblFotoLabel.setFont(labelFont);
        panelFormulario.add(lblFotoLabel, gbc);

        lblFoto = new JLabel("(Opcional) Haz clic en 'Seleccionar' para cambiar la imagen");
        lblFoto.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 12));
        lblFoto.setForeground(new Color(150, 150, 150));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panelFormulario.add(lblFoto, gbc);

        JButton btnExaminar = new JButton("📷 Cambiar");
        btnExaminar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnExaminar.addActionListener(e -> seleccionarFoto());
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        panelFormulario.add(btnExaminar, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(new Color(245, 250, 255));

        JButton btnGuardar = new JButton("💾 Guardar Cambios");
        btnGuardar.setBackground(new Color(0, 150, 0));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnGuardar.addActionListener(e -> guardarCambios());

        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(200, 50, 50));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnResetEstadisticas = new JButton("🔄 Reset Estadísticas");
        btnResetEstadisticas.setBackground(new Color(255, 140, 0));
        btnResetEstadisticas.setForeground(Color.WHITE);
        btnResetEstadisticas.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnResetEstadisticas.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnResetEstadisticas.addActionListener(e -> resetEstadisticas());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnResetEstadisticas);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void cargarDatosJugador() {
        tfNombre.setText(jugadorOriginal.getNombre());
        tfEdad.setText(String.valueOf(jugadorOriginal.getEdad()));
        tfDorsal.setText(String.valueOf(jugadorOriginal.getDorsal()));
        tfPais.setText(jugadorOriginal.getPais());
        cbPosicion.setSelectedItem(jugadorOriginal.getPosicion());

        String rutaFoto = jugadorOriginal.getRutaFoto();
        if (rutaFoto != null && !rutaFoto.isEmpty()) {
            lblFoto.setText(rutaFoto);
            lblFoto.setForeground(new Color(0, 100, 200));
            lblFoto.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        }
    }

    private void seleccionarFoto() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes compatibles (JPG, PNG, GIF, BMP)",
                "jpg", "jpeg", "png", "gif", "bmp"));

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoFoto = fc.getSelectedFile();
            String nombreArchivo = archivoFoto.getName().toLowerCase();

            if (nombreArchivo.endsWith(".webp") || nombreArchivo.endsWith(".heic") ||
                    nombreArchivo.endsWith(".svg")) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Formato de imagen no compatible\n\n" +
                                "Formatos soportados:\n" +
                                "• JPG/JPEG (.jpg, .jpeg)\n" +
                                "• PNG (.png)\n" +
                                "• GIF (.gif)\n" +
                                "• BMP (.bmp)\n\n" +
                                "Convierte tu imagen a uno de estos formatos.",
                        "Formato no soportado", JOptionPane.WARNING_MESSAGE);
                archivoFoto = null;
                lblFoto.setText("(Opcional) Selecciona una imagen compatible");
                return;
            }

            lblFoto.setText(archivoFoto.getName());
            lblFoto.setForeground(new Color(0, 100, 200));
            lblFoto.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        }
    }

    private void guardarCambios() {
        String nombre = tfNombre.getText().trim();
        String edadS = tfEdad.getText().trim();
        String pais = tfPais.getText().trim();
        String dorsalS = tfDorsal.getText().trim();
        String posicion = (String) cbPosicion.getSelectedItem();
        String rutaFoto = (archivoFoto != null) ? archivoFoto.getAbsolutePath() : jugadorOriginal.getRutaFoto();

        if (nombre.isEmpty() || edadS.isEmpty() || pais.isEmpty() || dorsalS.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "❌ Completa todos los campos obligatorios:\n\n" +
                            "• Nombre completo\n" +
                            "• Edad\n" +
                            "• País\n" +
                            "• Dorsal",
                    "Campos incompletos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadS);
            if (edad < 16 || edad > 50) {
                JOptionPane.showMessageDialog(this,
                        "La edad debe estar entre 16 y 50 años.",
                        "Edad inválida", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "La edad debe ser un número válido (ej: 25).",
                    "Edad inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int dorsal;
        try {
            dorsal = Integer.parseInt(dorsalS);
            if (dorsal < 1 || dorsal > 99) {
                JOptionPane.showMessageDialog(this,
                        "El dorsal debe ser un número entre 1 y 99.",
                        "Dorsal inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Jugador j : gestor.listarJugadores()) {
                if (j.getId() != jugadorOriginal.getId() && j.getDorsal() == dorsal) {
                    JOptionPane.showMessageDialog(this,
                            "El dorsal #" + dorsal + " ya está asignado a:\n\n" +
                                    j.getNombre() + " (" + j.getPosicion() + ")\n\n" +
                                    "Por favor, elige otro número de dorsal.",
                            "Dorsal ya en uso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El dorsal debe ser un número (ej: 10, 7, 23).",
                    "Dorsal inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Guardar los siguientes cambios?\n\n" +
                        "Nombre: " + jugadorOriginal.getNombre() + " → " + nombre + "\n" +
                        "Edad: " + jugadorOriginal.getEdad() + " → " + edad + "\n" +
                        "Dorsal: #" + jugadorOriginal.getDorsal() + " → #" + dorsal + "\n" +
                        "País: " + jugadorOriginal.getPais() + " → " + pais + "\n" +
                        "Posición: " + jugadorOriginal.getPosicion() + " → " + posicion + "\n\n" +
                        "Las estadísticas (goles, asistencias, etc.) NO se modificarán.",
                "Confirmar cambios",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        jugadorOriginal.setNombre(nombre);
        jugadorOriginal.setEdad(edad);
        jugadorOriginal.setDorsal(dorsal);
        jugadorOriginal.setPais(pais);
        jugadorOriginal.setPosicion(posicion);

        if (archivoFoto != null) {
            jugadorOriginal.setRutaFoto(archivoFoto.getAbsolutePath());
        }

        try {
            gestor.guardarEnArchivo(new File("jugadores.txt"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar los cambios:\n" + ex.getMessage(),
                    "Error de guardado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "✅ JUGADOR ACTUALIZADO EXITOSAMENTE\n\n" +
                        "Nombre: " + jugadorOriginal.getNombre() + "\n" +
                        "Dorsal: #" + jugadorOriginal.getDorsal() + "\n" +
                        "Posición: " + jugadorOriginal.getPosicion() + "\n" +
                        "Edad: " + jugadorOriginal.getEdad() + " años\n" +
                        "País: " + jugadorOriginal.getPais(),
                "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    private void resetEstadisticas() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "⚠️ ¿ESTÁS SEGURO DE REINICIAR LAS ESTADÍSTICAS?\n\n" +
                        "Se resetearán a CERO:\n" +
                        "• Goles\n" +
                        "• Asistencias\n" +
                        "• Pases clave\n\n" +
                        "Esta acción NO se puede deshacer.",
                "Confirmar reinicio de estadísticas",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            jugadorOriginal.setGoles(0);
            jugadorOriginal.setAsistencias(0);
            jugadorOriginal.setPasesClave(0);

            try {
                gestor.guardarEnArchivo(new File("jugadores.txt"));
                JOptionPane.showMessageDialog(this,
                        "✅ Estadísticas reiniciadas exitosamente\n\n" +
                                "Todas las estadísticas se han establecido a cero.",
                        "Reinicio exitoso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar cambios: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}