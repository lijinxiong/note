package servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by jinxiong on 2017/3/3.
 */
@WebServlet(name = "ImageServlet", urlPatterns = "/servlet/ImageServlet")
public class ImageServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int width = 90;
        int height = 25;

        String codes = "0123456789qwertyuipasdfghjkzxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM";

        Random random = new Random();

        BufferedImage image = new BufferedImage(width, height
                , BufferedImage.TYPE_INT_RGB);

        Graphics graphics = image.getGraphics();

        graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        graphics.fillRect(0, 0, width, height);
        graphics.setFont(new Font(Font.SANS_SERIF,Font.ITALIC,height));
        StringBuilder builder = new StringBuilder();
        graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        for (int i = 0; i < 4; i++) {
            char[] c = {codes.charAt(random.nextInt(codes.length()))};
            graphics.drawChars(c, 0, c.length, 16 * i, height);
            builder.append(c);
        }

        resp.setHeader("code", builder.toString());
        ImageIO.write(image, "JPEG", resp.getOutputStream());



    }
}
