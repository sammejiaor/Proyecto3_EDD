import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.util.List;
import java.net.URL;

public class CampoFutbolPanel extends JPanel {
    private List<Jugador> once;
    private List<Jugador> suplentes;
    private String formacion;

    // Colores para campo HORIZONTAL
    private final Color COLOR_CAMPO = new Color(0, 120, 0);
    private final Color COLOR_LINEAS = Color.WHITE;
    private final Color COLOR_AREA = new Color(0, 100, 0);
    private final Color COLOR_CIRCULO = Color.WHITE;
    private final Color COLOR_PORTERIA = Color.WHITE;

    // Posiciones del campo - CAMPO HORIZONTAL corregido
    private final double[][] posiciones442 = {
            // Portero (derecha)
            { 0.9, 0.5 },
            // Defensas: LI, DFC1, DFC2, LD
            { 0.75, 0.15 }, { 0.75, 0.35 }, { 0.75, 0.65 }, { 0.75, 0.85 },
            // Medios: LI interior, MC1, MC2, LD interior
            { 0.55, 0.15 }, { 0.55, 0.35 }, { 0.55, 0.65 }, { 0.55, 0.85 },
            // Delanteros: DC1, DC2
            { 0.3, 0.35 }, { 0.3, 0.65 }
    };

    private final double[][] posiciones433 = {
            // Portero (derecha)
            { 0.9, 0.5 },
            // Defensas: LI, DFC1, DFC2, LD
            { 0.75, 0.15 }, { 0.75, 0.35 }, { 0.75, 0.65 }, { 0.75, 0.85 },
            // Medios: MC1, MC2, MC3
            { 0.55, 0.25 }, { 0.55, 0.5 }, { 0.55, 0.75 },
            // Delanteros: EXT IZQ, DC, EXT DER
            { 0.25, 0.15 }, { 0.25, 0.5 }, { 0.25, 0.85 }
    };

    private final double[][] posiciones4231 = {
            // Portero (derecha)
            { 0.9, 0.5 },
            // Defensas: LI, DFC1, DFC2, LD
            { 0.75, 0.15 }, { 0.75, 0.35 }, { 0.75, 0.65 }, { 0.75, 0.85 },
            // Medios defensivos (2)
            { 0.6, 0.35 }, { 0.6, 0.65 },
            // Medios ofensivos (3): EXT IZQ, INT, EXT DER
            { 0.45, 0.15 }, { 0.45, 0.5 }, { 0.45, 0.85 },
            // Delantero (1)
            { 0.25, 0.5 }
    };

    private final double[][] posiciones352 = {
            // Portero (derecha)
            { 0.9, 0.5 },
            // Defensas (3)
            { 0.75, 0.25 }, { 0.75, 0.5 }, { 0.75, 0.75 },
            // Medios (5): LI, MC1, MC2, MC3, LD
            { 0.55, 0.1 }, { 0.55, 0.3 }, { 0.55, 0.5 }, { 0.55, 0.7 }, { 0.55, 0.9 },
            // Delanteros (2)
            { 0.3, 0.35 }, { 0.3, 0.65 }
    };

    private final double[][] posiciones343 = {
            // Portero (derecha)
            { 0.9, 0.5 },
            // Defensas (3)
            { 0.75, 0.25 }, { 0.75, 0.5 }, { 0.75, 0.75 },
            // Medios (4): LI, MC1, MC2, LD
            { 0.55, 0.15 }, { 0.55, 0.4 }, { 0.55, 0.6 }, { 0.55, 0.85 },
            // Delanteros (3): EXT IZQ, DC, EXT DER
            { 0.25, 0.15 }, { 0.25, 0.5 }, { 0.25, 0.85 }
    };

    public CampoFutbolPanel(List<Jugador> once, List<Jugador> suplentes, String formacion) {
        this.once = once;
        this.suplentes = suplentes;
        this.formacion = formacion;
        setBackground(new Color(240, 248, 255));
        setPreferredSize(new Dimension(1100, 700));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Área para el campo (77% del ancho total)
        int campoWidth = (int) (width * 0.77);
        int campoHeight = height - 30;

        // Dibujar campo horizontal
        dibujarCampoHorizontal(g2d, 0, 30, campoWidth, campoHeight);

        // Dibujar jugadores en el campo
        dibujarJugadoresEnCampo(g2d, 0, 30, campoWidth, campoHeight);

        // Dibujar suplentes en el lateral derecho
        dibujarSuplentes(g2d, campoWidth, 30, width - campoWidth, campoHeight);
    }

    private void dibujarCampoHorizontal(Graphics2D g2d, int x, int y, int width, int height) {
        // Campo principal (verde)
        g2d.setColor(COLOR_CAMPO);
        g2d.fillRect(x, y, width, height);

        // Líneas blancas del perímetro
        g2d.setColor(COLOR_LINEAS);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x, y, width, height);

        // Línea de medio campo (horizontal)
        g2d.drawLine(x + width / 2, y, x + width / 2, y + height);

        // Círculo central
        int radioCirculo = Math.min(width, height) / 10;
        g2d.setColor(COLOR_CIRCULO);
        g2d.drawOval(x + width / 2 - radioCirculo,
                y + height / 2 - radioCirculo,
                radioCirculo * 2, radioCirculo * 2);

        // Punto central
        g2d.fillOval(x + width / 2 - 5, y + height / 2 - 5, 10, 10);

        // Áreas (porterías izquierda y derecha)
        int areaAncho = width / 6;
        int areaAlto = height / 3;

        // Área izquierda
        g2d.setColor(COLOR_PORTERIA);
        g2d.drawRect(x, y + height / 2 - areaAlto / 2, areaAncho, areaAlto);

        // Área derecha
        g2d.drawRect(x + width - areaAncho, y + height / 2 - areaAlto / 2, areaAncho, areaAlto);

        // Porterías
        g2d.setColor(COLOR_PORTERIA);
        g2d.setStroke(new BasicStroke(2));
        int porteriaAncho = 15;
        int porteriaAlto = areaAlto / 2;

        // Portería izquierda
        g2d.drawRect(x - porteriaAncho, y + height / 2 - porteriaAlto / 2,
                porteriaAncho, porteriaAlto);

        // Portería derecha
        g2d.drawRect(x + width, y + height / 2 - porteriaAlto / 2,
                porteriaAncho, porteriaAlto);
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

    private void dibujarJugadoresEnCampo(Graphics2D g2d, int campoX, int campoY, int campoWidth, int campoHeight) {
        if (once == null || once.isEmpty())
            return;

        // Obtener posiciones según formación
        double[][] posiciones = getPosicionesFormacion();

        for (int i = 0; i < Math.min(once.size(), posiciones.length); i++) {
            Jugador jugador = once.get(i);
            double[] pos = posiciones[i];

            // Calcular posición en píxeles
            int x = campoX + (int) (pos[0] * campoWidth);
            int y = campoY + (int) (pos[1] * campoHeight);

            // Dibujar jugador con foto
            dibujarJugador(g2d, jugador, x, y);
        }
    }

    private void dibujarJugador(Graphics2D g2d, Jugador jugador, int x, int y) {
        int diametro = 60; // Tamaño del círculo

        // Dibujar círculo de fondo
        Color colorPosicion = getColorPosicion(jugador.getPosicion());
        g2d.setColor(colorPosicion);
        g2d.fillOval(x - diametro / 2, y - diametro / 2, diametro, diametro);

        // Borde blanco grueso
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x - diametro / 2, y - diametro / 2, diametro, diametro);

        // Dibujar foto si existe
        Image foto = cargarFoto(jugador.getRutaFoto());
        if (foto != null) {
            // Crear imagen circular recortada
            Image imagenCircular = crearImagenCircularSimple(foto, diametro - 8);

            // Dibujar la imagen circular
            g2d.drawImage(imagenCircular, x - (diametro - 8) / 2, y - (diametro - 8) / 2, null);

            // Dibujar borde interior para mejor contraste
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(x - (diametro - 8) / 2, y - (diametro - 8) / 2, diametro - 8, diametro - 8);
        } else {
            // Si no hay foto, mostrar dorsal grande en el centro
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial Unicode MS Black", Font.BOLD, 22));
            String dorsal = String.valueOf(jugador.getDorsal());
            FontMetrics fm = g2d.getFontMetrics();
            int textX = x - fm.stringWidth(dorsal) / 2;
            int textY = y + fm.getAscent() / 2 - 2;
            g2d.drawString(dorsal, textX, textY);
        }

        // NOMBRE COMPLETO CON DORSAL DEBAJO DE LA FOTO
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial Unicode MS", Font.BOLD, 10));
        String nombreConDorsal = "#" + jugador.getDorsal() + " " + getNombreCorto(jugador.getNombre());
        FontMetrics fmNombre = g2d.getFontMetrics();

        // Fondo oscuro semitransparente para el nombre
        int nombreWidth = fmNombre.stringWidth(nombreConDorsal) + 10;
        int nombreHeight = fmNombre.getHeight() + 4;
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(x - nombreWidth / 2, y + diametro / 2 + 5, nombreWidth, nombreHeight);

        // Borde blanco del fondo
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x - nombreWidth / 2, y + diametro / 2 + 5, nombreWidth, nombreHeight);

        // Texto del nombre con dorsal
        g2d.setColor(Color.WHITE);
        int textX = x - fmNombre.stringWidth(nombreConDorsal) / 2;
        int textY = y + diametro / 2 + fmNombre.getAscent() + 7;
        g2d.drawString(nombreConDorsal, textX, textY);

        // Posición (encima del círculo)
        String posicionCorta = getPosicionCorta(jugador.getPosicion());
        g2d.setFont(new Font("Arial Unicode MS", Font.BOLD, 10));
        FontMetrics fmPos = g2d.getFontMetrics();

        // Fondo para posición
        int posWidth = fmPos.stringWidth(posicionCorta) + 8;
        g2d.setColor(new Color(255, 255, 255, 220));
        g2d.fillRect(x - posWidth / 2, y - diametro / 2 - fmPos.getHeight() - 5, posWidth, fmPos.getHeight());

        // Borde del fondo de posición
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - posWidth / 2, y - diametro / 2 - fmPos.getHeight() - 5, posWidth, fmPos.getHeight());

        // Texto de posición
        g2d.setColor(Color.BLACK);
        textX = x - fmPos.stringWidth(posicionCorta) / 2;
        textY = y - diametro / 2 - 7;
        g2d.drawString(posicionCorta, textX, textY);
    }

    private Image crearImagenCircularSimple(Image imagenOriginal, int diametro) {
        // Convertir a BufferedImage para asegurar carga completa
        java.awt.image.BufferedImage bufferedOriginal = new java.awt.image.BufferedImage(
                imagenOriginal.getWidth(null),
                imagenOriginal.getHeight(null),
                java.awt.image.BufferedImage.TYPE_INT_ARGB);

        Graphics2D g0 = bufferedOriginal.createGraphics();
        g0.drawImage(imagenOriginal, 0, 0, null);
        g0.dispose();

        // Crear imagen destino circular
        java.awt.image.BufferedImage circular = new java.awt.image.BufferedImage(diametro, diametro,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = circular.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clip circular
        Shape clip = new Ellipse2D.Float(0, 0, diametro, diametro);
        g2d.setClip(clip);

        // Dibujar la imagen escalada manualmente
        g2d.drawImage(bufferedOriginal, 0, 0, diametro, diametro, null);

        g2d.dispose();
        return circular;
    }

    private void dibujarSuplentes(Graphics2D g2d, int x, int y, int width, int height) {
        if (suplentes == null || suplentes.isEmpty())
            return;

        // Fondo del panel de suplentes
        g2d.setColor(new Color(245, 250, 255));
        g2d.fillRect(x, y, width, height);

        // Borde
        g2d.setColor(new Color(200, 220, 240));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, width, height);

        // Título
        g2d.setColor(new Color(0, 80, 160));
        g2d.setFont(new Font("Arial Unicode MS", Font.BOLD, 16));
        String titulo = "SUPLENTES (" + suplentes.size() + ")";
        FontMetrics fm = g2d.getFontMetrics();
        int tituloX = x + (width - fm.stringWidth(titulo)) / 2;
        g2d.drawString(titulo, tituloX, y + 30);

        // Lista de suplentes
        int espacioPorSuplente = 85;
        int maxSuplentes = Math.min(suplentes.size(), (height - 60) / espacioPorSuplente);

        int posY = y + 70;

        for (int i = 0; i < maxSuplentes; i++) {
            Jugador jugador = suplentes.get(i);

            // Fondo alternado
            if (i % 2 == 0) {
                g2d.setColor(new Color(240, 245, 255));
            } else {
                g2d.setColor(Color.WHITE);
            }
            g2d.fillRect(x + 10, posY - 25, width - 20, espacioPorSuplente - 10);

            // Borde
            g2d.setColor(new Color(220, 230, 240));
            g2d.drawRect(x + 10, posY - 25, width - 20, espacioPorSuplente - 10);

            // Foto del suplente
            int fotoSize = 50;
            int fotoX = x + 15;
            int fotoY = posY - 13;

            // Dibujar círculo de fondo
            Color colorPos = getColorPosicion(jugador.getPosicion());
            g2d.setColor(colorPos);
            g2d.fillOval(fotoX, fotoY, fotoSize, fotoSize);

            // Borde blanco
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(fotoX, fotoY, fotoSize, fotoSize);

            Image foto = cargarFoto(jugador.getRutaFoto());
            if (foto != null) {
                // Dibujar foto circular
                Image circular = crearImagenCircularSimple(foto, fotoSize - 8);
                g2d.drawImage(circular, fotoX + 4, fotoY + 4, null);
            } else {
                // Dorsal si no hay foto
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String dorsal = String.valueOf(jugador.getDorsal());
                FontMetrics fmDorsal = g2d.getFontMetrics();
                int dorsalX = fotoX + (fotoSize - fmDorsal.stringWidth(dorsal)) / 2;
                int dorsalY = fotoY + fotoSize / 2 + fmDorsal.getAscent() / 2 - 2;
                g2d.drawString(dorsal, dorsalX, dorsalY);
            }

            // Información del suplente (al lado de la foto)
            int infoX = fotoX + fotoSize + 10;

            // Nombre con dorsal
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
            String nombreInfo = "#" + jugador.getDorsal() + " " + getNombreCorto(jugador.getNombre());
            g2d.drawString(nombreInfo, infoX, posY - 5);

            // Posición
            g2d.setColor(colorPos);
            g2d.setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
            String posicion = getPosicionCorta(jugador.getPosicion());
            g2d.drawString(posicion, infoX, posY + 12);

            // Estadísticas
            g2d.setColor(new Color(100, 100, 100));
            g2d.setFont(new Font("Arial Unicode MS", Font.PLAIN, 11));
            String stats = String.format("G:%d A:%d PC:%d",
                    jugador.getGoles(), jugador.getAsistencias(),
                    jugador.getPasesClave());
            g2d.drawString(stats, infoX, posY + 27);

            // Edad y país
            g2d.setFont(new Font("Arial Unicode MS", Font.ITALIC, 10));
            String edadPais = String.format("%d años | %s", jugador.getEdad(), jugador.getPais());
            g2d.drawString(edadPais, infoX, posY + 42);

            posY += espacioPorSuplente;
        }
    }

    private double[][] getPosicionesFormacion() {
        switch (formacion) {
            case "4-4-2":
                return posiciones442;
            case "4-3-3":
                return posiciones433;
            case "4-2-3-1":
                return posiciones4231;
            case "3-5-2":
                return posiciones352;
            case "3-4-3":
                return posiciones343;
            default:
                return posiciones442;
        }
    }

    private Color getColorPosicion(String posicion) {
        switch (posicion) {
            case "PORTERO":
                return new Color(0, 100, 200);
            case "LATERAL IZQUIERDO":
                return new Color(0, 150, 50);
            case "LATERAL DERECHO":
                return new Color(0, 150, 50);
            case "CENTRAL":
                return new Color(0, 150, 0);
            case "MEDIOCENTRO":
                return new Color(255, 140, 0);
            case "INTERIOR":
                return new Color(255, 165, 0);
            case "EXTREMO":
                return new Color(200, 0, 0);
            case "DELANTERO CENTRO":
                return new Color(220, 0, 0);
            default:
                return new Color(100, 100, 100);
        }
    }

    private String getNombreCorto(String nombreCompleto) {
        if (nombreCompleto.length() <= 15)
            return nombreCompleto;

        String[] partes = nombreCompleto.split(" ");
        if (partes.length > 1) {
            return partes[partes.length - 1];
        }
        return nombreCompleto.substring(0, 12) + "..";
    }

    private String getPosicionCorta(String posicion) {
        switch (posicion) {
            case "PORTERO":
                return "POR";
            case "LATERAL IZQUIERDO":
                return "LI";
            case "LATERAL DERECHO":
                return "LD";
            case "CENTRAL":
                return "DFC";
            case "MEDIOCENTRO":
                return "MC";
            case "INTERIOR":
                return "INT";
            case "EXTREMO":
                return "EXT";
            case "DELANTERO CENTRO":
                return "DC";
            default:
                return posicion.substring(0, Math.min(3, posicion.length()));
        }
    }
}