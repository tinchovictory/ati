package ar.edu.itba.ati.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HSVImage {
    private ImageType imageType;
    private ImageExtension imageExtension;
    //private ImageColorChannel hue;
    //private ImageColorChannel saturation;
    //private ImageColorChannel value;
    private float[][] hue;
    private float[][] saturation;
    private float[][] value;
    private int width;
    private int height;


    public HSVImage(Image image) {
        this.imageType = image.getImageType();
        this.imageExtension = image.getImageExtension();
        this.width = image.getWidth();
        this.height = image.getHeight();
//        this.hue = new ImageColorChannel(width, height);
//        this.saturation = new ImageColorChannel(width, height);
//        this.value = new ImageColorChannel(width, height);
        this.hue = new float[width][height];
        this.saturation = new float[width][height];
        this.value = new float[width][height];

        fillColorChannelsFrom(image);
    }

    private void fillColorChannelsFrom(Image image) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Color c = image.getPixelColor(x, y);
                float[] hsv = new float[3];
                hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);

                hue[x][y] = hsv[0];
                saturation[x][y] = hsv[1];
                value[x][y] = hsv[2];
            }
        }

    }

    private int getBufferedImageType() {
        if(imageType == ImageType.GRAY_SCALE) {
            return BufferedImage.TYPE_BYTE_GRAY;
        }
        return BufferedImage.TYPE_INT_RGB;
    }

    public BufferedImage getHueImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int hueColor = (int) (hue[x][y] * 255);
                Color color = new Color(hueColor, hueColor, hueColor);
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        return bufferedImage;
    }

    public BufferedImage getSaturationImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int saturationColor = (int) (saturation[x][y] * 255);
                Color color = new Color(saturationColor, saturationColor, saturationColor);
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        return bufferedImage;
    }


    public BufferedImage getValueImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int valueColor = (int) (value[x][y] * 255);
                Color color = new Color(valueColor, valueColor, valueColor);
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        return bufferedImage;
    }
}
