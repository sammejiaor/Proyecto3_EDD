import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class VerEventosFrame extends JFrame {
    private GestorEventos gestorEvt;
    private GestorJugadores gestorJug;
    private JTextArea taEventos;

    public VerEventosFrame(GestorEventos gestorEvt, GestorJugadores gestorJug) {
        this.gestorEvt = gestorEvt;
        this.gestorJug = gestorJug;

        setTitle("Histórico de Eventos");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        cargarEventos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("📜 HISTÓRICO DE EVENTOS DEL PARTIDO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Unicode MS", Font.BOLD, 16));

        taEventos = new JTextArea();
        taEventos.setEditable(false);
        taEventos.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(taEventos);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnLimpiar = new JButton("Limpiar Histórico");
        JButton btnCerrar = new JButton("Cerrar");

        btnActualizar.addActionListener(e -> cargarEventos());
        btnLimpiar.addActionListener(e -> limpiarEventos());
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnActualizar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCerrar);

        add(lblTitulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarEventos() {
        List<EventoPartido> eventos = gestorEvt.listarEventos();
        StringBuilder sb = new StringBuilder();

        sb.append("╔══════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                    HISTÓRICO DE EVENTOS                         ║\n");
        sb.append("╚══════════════════════════════════════════════════════════════════╝\n\n");

        if (eventos.isEmpty()) {
            sb.append("📭 No hay eventos registrados todavía.\n");
            sb.append("   Usa 'Registrar evento de partido' para comenzar.\n");
        } else {
            sb.append(String.format("📊 Total de eventos: %d\n\n", eventos.size()));

            int contador = 1;
            for (EventoPartido evento : eventos) {
                Jugador origen = gestorJug.getJugador(evento.getOrigen());
                Jugador destino = gestorJug.getJugador(evento.getDestino());

                String nombreOrigen = (origen != null) ? origen.getNombre() : "Jugador " + evento.getOrigen();
                String nombreDestino = (destino != null) ? destino.getNombre() : "Jugador " + evento.getDestino();

                String icono = evento.getTipo().equals("ASISTENCIA") ? "🎯" : "🔀";

                sb.append(String.format("%3d. %s %s → %s [%s]\n",
                        contador++, icono, nombreOrigen, nombreDestino, evento.getTipo()));
            }

            // Resumen - USANDO BUCLE TRADICIONAL (sin Stream API)
            sb.append("\n═══════════════════════════════════════════════\n");
            sb.append("📈 RESUMEN ESTADÍSTICO:\n");

            int asistencias = 0;
            int pases = 0;

            for (EventoPartido evento : eventos) {
                if (evento.getTipo().equals("ASISTENCIA")) {
                    asistencias++;
                } else if (evento.getTipo().equals("PASE_CLAVE")) {
                    pases++;
                }
            }

            double porcentajeAsistencias = eventos.size() > 0 ? (double) asistencias / eventos.size() * 100 : 0;
            double porcentajePases = eventos.size() > 0 ? (double) pases / eventos.size() * 100 : 0;

            sb.append(String.format("• Asistencias: %d (%.1f%%)\n", asistencias, porcentajeAsistencias));
            sb.append(String.format("• Pases clave: %d (%.1f%%)\n", pases, porcentajePases));
        }

        taEventos.setText(sb.toString());
    }

    private void limpiarEventos() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres limpiar todo el histórico de eventos?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Limpiar la lista de eventos en el gestor existente
            List<EventoPartido> eventos = gestorEvt.listarEventos();
            eventos.clear(); // Esto limpia la lista compartida

            // Intentar limpiar archivo
            try {
                gestorEvt.guardarEnArchivo(new File("eventos.txt"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

            cargarEventos();
            JOptionPane.showMessageDialog(this, "✅ Histórico limpiado");
        }
    }
}