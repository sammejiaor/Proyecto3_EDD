import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class RegistrarJugadorFrame extends JFrame {
    private GestorJugadores gestor;
    private JTextField tfNombre, tfEdad, tfPais, tfDorsal;
    private JComboBox<String> cbPosicion;
    private JLabel lblFoto;
    private File archivoFoto = null;

    public RegistrarJugadorFrame(GestorJugadores gestor) {
        this.gestor = gestor;
        setTitle("➕ Registrar Nuevo Jugador");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 250, 255));
        initComponents();
    }

    private void initComponents() {
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(245, 250, 255));

        // Título
        JLabel lblTitulo = new JLabel("REGISTRAR NUEVO JUGADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Unicode MS Black", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 50, 120));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 220, 240), 2, true),
                new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Fuentes
        Font labelFont = new Font("Segoe UI Emoji", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI Emoji", Font.PLAIN, 13);

        // 1. NOMBRE
        gbc.gridx = 0;
        gbc.gridy = 0;
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

        // 2. EDAD
        gbc.gridx = 0;
        gbc.gridy = 1;
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

        // 3. DORSAL
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

        // 4. PAÍS
        gbc.gridx = 0;
        gbc.gridy = 2;
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

        // 5. POSICIÓN (actualizada)
        gbc.gridx = 0;
        gbc.gridy = 3;
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

        // Renderer para colores de posición
        cbPosicion.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected,
                    boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (index >= 0) {
                    String pos = (String) value;
                    // Colores por tipo de posición
                    switch (pos) {
                        case "PORTERO":
                            setForeground(new Color(0, 100, 200)); // Azul
                            break;
                        case "LATERAL IZQUIERDO":
                        case "LATERAL DERECHO":
                        case "CENTRAL":
                            setForeground(new Color(0, 150, 0)); // Verde
                            break;
                        case "MEDIOCENTRO":
                        case "INTERIOR":
                            setForeground(new Color(255, 140, 0)); // Naranja
                            break;
                        case "EXTREMO":
                        case "DELANTERO CENTRO":
                            setForeground(new Color(200, 0, 0)); // Rojo
                            break;
                    }
                }
                return c;
            }
        });
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panelFormulario.add(cbPosicion, gbc);

        // 6. FOTO
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel lblFotoLabel = new JLabel("Foto del jugador:");
        lblFotoLabel.setFont(labelFont);
        panelFormulario.add(lblFotoLabel, gbc);

        lblFoto = new JLabel("(Opcional) Haz clic en 'Seleccionar' para elegir una imagen");
        lblFoto.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 12));
        lblFoto.setForeground(new Color(150, 150, 150));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panelFormulario.add(lblFoto, gbc);

        JButton btnExaminar = new JButton("📷 Seleccionar");
        btnExaminar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnExaminar.addActionListener(e -> seleccionarFoto());
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        panelFormulario.add(btnExaminar, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(new Color(245, 250, 255));

        JButton btnGuardar = new JButton("💾 Guardar Jugador");
        btnGuardar.setBackground(new Color(0, 150, 0));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnGuardar.addActionListener(e -> guardarJugador());

        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(200, 50, 50));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Agregar todo al panel principal
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void seleccionarFoto() {
        JFileChooser fc = new JFileChooser();
        // Filtro para solo imágenes compatibles
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes compatibles (JPG, PNG, GIF, BMP)",
                "jpg", "jpeg", "png", "gif", "bmp"));

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoFoto = fc.getSelectedFile();
            String nombreArchivo = archivoFoto.getName().toLowerCase();

            // Verificar extensión compatible
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
            lblFoto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
    }

    private void guardarJugador() {
        // Obtener valores de los campos
        String nombre = tfNombre.getText().trim();
        String edadS = tfEdad.getText().trim();
        String pais = tfPais.getText().trim();
        String dorsalS = tfDorsal.getText().trim();
        String posicion = (String) cbPosicion.getSelectedItem();
        String rutaFoto = (archivoFoto != null) ? archivoFoto.getAbsolutePath() : "";

        // Validación 1: Campos obligatorios
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

        // Validación 2: Edad (número entre 16 y 50)
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

        // Validación 3: Dorsal (número entre 1 y 99, único)
        int dorsal;
        try {
            dorsal = Integer.parseInt(dorsalS);
            if (dorsal < 1 || dorsal > 99) {
                JOptionPane.showMessageDialog(this,
                        "El dorsal debe ser un número entre 1 y 99.",
                        "Dorsal inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar que el dorsal no esté repetido
            for (Jugador j : gestor.listarJugadores()) {
                if (j.getDorsal() == dorsal) {
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

        // Crear el jugador (CON DORSAL - 6 parámetros)
        Jugador j = gestor.crearJugador(nombre, edad, pais, posicion, rutaFoto, dorsal);

        // Guardar en archivo
        try {
            gestor.guardarEnArchivo(new File("jugadores.txt"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar los datos:\n" + ex.getMessage(),
                    "Error de guardado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(this,
                "✅ JUGADOR REGISTRADO EXITOSAMENTE\n\n" +
                        "Nombre: " + j.getNombre() + "\n" +
                        "Dorsal: #" + j.getDorsal() + "\n" +
                        "Posición: " + j.getPosicion() + "\n" +
                        "Edad: " + j.getEdad() + " años\n" +
                        "País: " + j.getPais() + "\n" +
                        "ID asignado: " + j.getId() + "\n\n" +
                        "Los datos han sido guardados correctamente.",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

        dispose(); // Cerrar ventana
    }
}