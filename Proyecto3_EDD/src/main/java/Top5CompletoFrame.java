import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Top5CompletoFrame extends JFrame {
    private AnalizadorEstadistico analizador;
    private JTextArea taResultados;

    public Top5CompletoFrame(AnalizadorEstadistico analizador) {
        this.analizador = analizador;

        setTitle("📊 Top 5 - Análisis Estadístico");
        setSize(850, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        mostrarTop5();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("🏆 TOP 5 ESTADÍSTICAS DEL EQUIPO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Unicode MS", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 80, 160));

        taResultados = new JTextArea();
        taResultados.setEditable(false);
        taResultados.setFont(new Font("Monospaced", Font.PLAIN, 14));
        taResultados.setBackground(new Color(255, 253, 245));

        JScrollPane scrollPane = new JScrollPane(taResultados);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("🔄 Actualizar");
        JButton btnCerrar = new JButton("✖ Cerrar");

        btnActualizar.addActionListener(e -> mostrarTop5());
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(lblTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void mostrarTop5() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                             TOP 5 ESTADÍSTICAS                                  ║\n");
        sb.append("╚══════════════════════════════════════════════════════════════════════════════════╝\n\n");

        // 1. TOP 5 GOLEADORES
        sb.append("══════════════════════════ G O L E A D O R E S ═══════════════════════════\n");
        List<Jugador> goleadores = analizador.topNGoleadores(5);
        if (goleadores.isEmpty()) {
            sb.append("   No hay datos de goleadores disponibles.\n");
        } else {
            for (int i = 0; i < goleadores.size(); i++) {
                Jugador j = goleadores.get(i);
                sb.append(String.format(" %d. %-25s | Goles: %2d | Pos: %-15s | Dorsal: #%d\n",
                        i + 1, j.getNombre(), j.getGoles(), j.getPosicion(), j.getDorsal()));
            }
        }
        sb.append("\n");

        // 2. TOP 5 ASISTIDORES
        sb.append("════════════════════════ A S I S T E N T E S ═══════════════════════════\n");
        List<Jugador> asistidores = analizador.topNAsistidores(5);
        if (asistidores.isEmpty()) {
            sb.append("   No hay datos de asistentes disponibles.\n");
        } else {
            for (int i = 0; i < asistidores.size(); i++) {
                Jugador j = asistidores.get(i);
                sb.append(String.format(" %d. %-25s | Asistencias: %2d | Pos: %-15s | PC: %2d\n",
                        i + 1, j.getNombre(), j.getAsistencias(), j.getPosicion(), j.getPasesClave()));
            }
        }
        sb.append("\n");

        // 3. TOP 5 INFLUYENTES
        sb.append("════════════════════════ I N F L U Y E N T E S ═══════════════════════════\n");
        List<Jugador> influyentes = analizador.topNInfluyentes(5);
        if (influyentes.isEmpty()) {
            sb.append("   No hay datos de influencia disponibles.\n");
        } else {
            for (int i = 0; i < influyentes.size(); i++) {
                Jugador j = influyentes.get(i);
                int influencia = j.getGoles() * 3 + j.getAsistencias() * 2 + j.getPasesClave();
                sb.append(String.format(" %d. %-25s | Influencia: %3d | G:%2d A:%2d PC:%2d\n",
                        i + 1, j.getNombre(), influencia, j.getGoles(), j.getAsistencias(), j.getPasesClave()));
            }
        }
        sb.append("\n");

        // 4. TOP 5 EFECTIVIDAD
        sb.append("════════════════════════ E F E C T I V I D A D ═══════════════════════════\n");
        sb.append(" Fórmula: (Goles / Oportunidades Recibidas) × 100\n");
        sb.append(" Oportunidades = Asistencias recibidas + Pases clave recibidos\n");
        sb.append("──────────────────────────────────────────────────────────────────────────\n");

        List<Jugador> efectivos = analizador.topNEfectividad(5);
        if (efectivos.isEmpty()) {
            sb.append("   No hay datos de efectividad disponibles.\n");
            sb.append("   (Se requieren oportunidades recibidas > 0 para calcular efectividad)\n");
        } else {
            for (int i = 0; i < efectivos.size(); i++) {
                Jugador j = efectivos.get(i);
                double efectividad = analizador.calcularEfectividadJugador(j);
                int oportunidades = analizador.getOportunidadesRecibidas(j);

                sb.append(String.format(" %d. %-25s | Efectividad: %6.2f%% | G:%2d / Oport:%2d\n",
                        i + 1, j.getNombre(), efectividad, j.getGoles(), oportunidades));
            }
        }
        sb.append("\n");

        // 5. TOP 5 JÓVENES PROMESAS
        sb.append("════════════════════ J Ó V E N E S  P R O M E S A ════════════════════════\n");
        sb.append(" (Jugadores menores de 25 años ordenados por potencial)\n");
        sb.append("──────────────────────────────────────────────────────────────────────────\n");

        List<Jugador> jovenes = analizador.topNJovenesPromesas(5);
        if (jovenes.isEmpty()) {
            sb.append("   No hay jugadores menores de 25 años en el equipo.\n");
        } else {
            for (int i = 0; i < jovenes.size(); i++) {
                Jugador j = jovenes.get(i);
                int potencial = calcularPotencial(j);
                sb.append(String.format(" %d. %-25s | Edad: %2d | Potencial: %3d | G:%2d A:%2d PC:%2d\n",
                        i + 1, j.getNombre(), j.getEdad(), potencial,
                        j.getGoles(), j.getAsistencias(), j.getPasesClave()));
            }
        }

        taResultados.setText(sb.toString());
    }

    private int calcularPotencial(Jugador j) {
        int base = j.getGoles() * 4 + j.getAsistencias() * 3 + j.getPasesClave() * 2;
        int bonusJuventud = (25 - j.getEdad()) * 5;

        return base + bonusJuventud;
    }
}