package modelo;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {

    public static final int MAX_SIDE = 600;

    private Images() {
    }

    public static byte[] readReduced(File file) throws IOException {
        BufferedImage original = ImageIO.read(file);
        if (original == null) {
            throw new IOException("Formato de imagem nao suportado: " + file.getName());
        }
        return toJpeg(reduce(original, MAX_SIDE));
    }

    public static BufferedImage reduce(BufferedImage source, int maxSide) {
        int width = source.getWidth();
        int height = source.getHeight();

        double scale = Math.min(1.0, (double) maxSide / Math.max(width, height));
        int newWidth = (int) Math.round(width * scale);
        int newHeight = (int) Math.round(height * scale);

        BufferedImage target = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(source, 0, 0, newWidth, newHeight, java.awt.Color.WHITE, null);
        g.dispose();
        return target;
    }

    public static byte[] toJpeg(BufferedImage img) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", out);
        return out.toByteArray();
    }

    public static BufferedImage fromBytes(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return null;
        }
        return ImageIO.read(new ByteArrayInputStream(data));
    }
}
