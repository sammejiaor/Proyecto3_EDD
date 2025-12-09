import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DuplasFrame extends JFrame {
    private AnalizadorEstadistico analizador;
    private JTextArea taDuplas;

    public DuplasFrame(AnalizadorEstadistico analizador) {
        this.analizador = analizador;

        setTitle("🤝 Duplas Destacadas");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        cargarDuplas();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("⚡ DUPLAS MÁS DESTACADAS DEL EQUIPO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Unicode MS", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 80, 160));

        taDuplas = new JTextArea();
        taDuplas.setEditable(false);
        taDuplas.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollDuplas = new JScrollPane(taDuplas);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnTop5 = new JButton("Top 5");
        JButton btnTop10 = new JButton("Top 10");
        JButton btnCerrar = new JButton("Cerrar");

        btnTop5.addActionListener(e -> cargarDuplas(5));
        btnTop10.addActionListener(e -> cargarDuplas(10));
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnTop5);
        panelBotones.add(btnTop10);
        panelBotones.add(btnCerrar);

        add(lblTitulo, BorderLayout.NORTH);
        add(scrollDuplas, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDuplas() {
        cargarDuplas(5);
    }

    private void cargarDuplas(int cantidad) {
        List<DuplaEstadistica> duplas = analizador.topNDuplas(cantidad);
        StringBuilder sb = new StringBuilder();

        sb.append("╔══════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                    TOP " + cantidad + " DUPLAS DESTACADAS                    ║\n");
        sb.append("╚══════════════════════════════════════════════════════════════════╝\n\n");

        for (int i = 0; i < duplas.size(); i++) {
            DuplaEstadistica d = duplas.get(i);
            Jugador origen = analizador.getGestor().getJugador(d.origen);
            Jugador destino = analizador.getGestor().getJugador(d.destino);

            if (origen != null && destino != null) {
                sb.append(String.format("   🏆 POSICIÓN #%d\n", i + 1));
                sb.append(String.format("   🧍 ORIGEN:  %s (%s)\n", origen.getNombre(), origen.getPosicion()));
                sb.append(String.format("   🎯 DESTINO: %s (%s)\n", destino.getNombre(), destino.getPosicion()));
                sb.append(String.format("   📊 ESTADÍSTICAS: Asistencias: %d | Pases clave: %d | Total: %d\n",
                        d.asistencias, d.pasesClave, d.peso));

                // Recomendación táctica simple
                String recomendacion = "";
                if (d.asistencias > 5) {
                    recomendacion = "Combinación letal - Usar en ataque";
                } else if (d.pasesClave > 10) {
                    recomendacion = "Buena conexión en construcción";
                } else {
                    recomendacion = "Conexión prometedora";
                }

                sb.append(String.format("   💡 RECOMENDACIÓN: %s\n", recomendacion));
                sb.append("   " + "-".repeat(60) + "\n\n");
            }
        }

        taDuplas.setText(sb.toString());
    }
}