package de.pueski.supernova.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import de.matthiasmann.twl.utils.PNGDecoder;

public class TextureUtil {

	public static int loadTexture(InputStream is) {
		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(tmp);
		tmp.rewind();
		try {

			PNGDecoder decoder = new PNGDecoder(is);
			ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			buf.flip();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

		}
		catch (IOException e) {
			System.out.println("Error decoding " + is);
		}
		tmp.rewind();

		int texId = tmp.get(0);

		return texId;
	}

	public static BufferedImage createImageFromFont(String s, Font f, Color c) {
		if (s == null)
			return null;
		BufferedImage bi = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bi.getGraphics();
		g2d.setFont(f);
		g2d.setColor(Color.BLACK);
		g2d.drawString(s, 0, 0);
		FontMetrics fm = g2d.getFontMetrics();
		int height = fm.getHeight();
		int width = fm.stringWidth(s) + 10;

		if (width == 0 || height == 0)
			return null;

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		BufferedImage si = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D) si.getGraphics();
		g2d.setRenderingHints(rh);
		g2d.setFont(f);
		g2d.setColor(c);
		g2d.drawString(s, 5, height - fm.getDescent());
		return si;
	}

	public static ByteBuffer convertBufferedImageToByteBuffer(BufferedImage image) throws Exception {

		image = ImageUtils.flipHorizontal(image);

		int[] pixels = ImageUtils.getArrayFromImage(image, image.getWidth(), image.getHeight());

		ByteBuffer buf = ByteBuffer.allocateDirect(4 * pixels.length);
		for (int i = 0; i < pixels.length; i++) {
			buf.putInt(pixels[i]);
		}

		buf.rewind();

		return buf;
	}
	
	public static void drawTexturedSquare(float angle, float x, float y, float size, int texId) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		// GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
		// GL11.glScalef(scale, scale, scale);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex2f(-size / 2, -size / 2);
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(size / 2, -size / 2);
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(size / 2, size / 2);
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(-size / 2, size / 2);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public static void drawTexture(float angle, float x, float y, float size, int texId) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
		// GL11.glScalef(scale, scale, scale);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glBegin(GL11.GL_POLYGON);
		
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex2f(-size, -size);
		
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(size, -size);
		
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(size, size);
		
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(-size, size);
		GL11.glEnd();
		GL11.glPopMatrix();
	}

}
