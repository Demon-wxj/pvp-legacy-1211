import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import javax.imageio.ImageIO;

/** Mechanical UV packing of the approved image; no newly drawn artwork. Run with Java 21. */
class PackCape {
    public static void main(String[] args) throws Exception {
        BufferedImage art = ImageIO.read(Path.of(args[0]).toFile());
        // Vanilla cape: 64x32 UV units; 40x64 faces keep the community-cape pixel scale.
        int s = 4;
        BufferedImage atlas = new BufferedImage(64*s, 32*s, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = atlas.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // Both wide faces, with side/top/bottom strips sampled from the same artwork.
        g.drawImage(art,s,s,11*s,17*s,0,0,art.getWidth(),art.getHeight(),null);
        g.drawImage(art,12*s,s,22*s,17*s,0,0,art.getWidth(),art.getHeight(),null);
        g.drawImage(art,0,s,s,17*s,0,0,1,art.getHeight(),null);
        g.drawImage(art,11*s,s,12*s,17*s,art.getWidth()-1,0,art.getWidth(),art.getHeight(),null);
        g.drawImage(art,s,0,11*s,s,0,0,art.getWidth(),1,null);
        g.drawImage(art,11*s,0,21*s,s,0,art.getHeight()-1,art.getWidth(),art.getHeight(),null);
        g.dispose();
        Path out = Path.of(args[1]); Files.createDirectories(out.getParent());
        ImageIO.write(atlas,"png",out.toFile());
        System.out.println("Cape atlas: " + out + " (256x128, face 40x64)");
    }
}
