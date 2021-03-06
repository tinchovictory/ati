package ar.edu.itba.ati.model;

import ar.edu.itba.ati.Utils;
import ar.edu.itba.ati.model.Masks.HughCircularFilter;
import ar.edu.itba.ati.model.Masks.HoughFilter;
import ar.edu.itba.ati.model.Masks.SusanFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Image {
    private ImageType imageType;
    private ImageExtension imageExtension;
    private ImageColorChannel redChannel;
    private ImageColorChannel greenChannel;
    private ImageColorChannel blueChannel;
    private int width;
    private int height;

    public Image(int width, int height, ImageType imageType, ImageExtension imageExtension) {
        this.imageType = imageType;
        this.imageExtension = imageExtension;
        this.redChannel = new ImageColorChannel(width, height);
        this.greenChannel = new ImageColorChannel(width, height);
        this.blueChannel = new ImageColorChannel(width, height);
        this.width = width;
        this.height = height;
    }

    public Image(BufferedImage bufferedImage, ImageType imageType, ImageExtension imageExtension) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight(), imageType, imageExtension);

        for(int height = 0; height < bufferedImage.getHeight(); height++) {
            for(int width = 0; width < bufferedImage.getWidth(); width++) {
                Color color = new Color(bufferedImage.getRGB(width, height));
                setPixelColor(width, height, color);
            }
        }
    }

    public BufferedImage getBufferdImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster writableRaster = bufferedImage.getRaster();

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = getPixelColor(x, y);
                int redValue = color.getRed() <= 128 ? color.getRed() : color.getRed() - 256;
                int greenValue = color.getGreen() <= 128 ? color.getGreen() : color.getGreen() - 256;
                int blueValue = color.getBlue() <= 128 ? color.getBlue() : color.getBlue() - 256;
//                bufferedImage.setRGB(x, y, color.getRGB());
                writableRaster.setSample(x, y, 0, redValue);
                writableRaster.setSample(x, y, 1, greenValue);
                writableRaster.setSample(x, y, 2, blueValue);
            }
        }



        return bufferedImage;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public ImageExtension getImageExtension() {
        return imageExtension;
    }

    private int getBufferedImageType() {
        if(imageType == ImageType.GRAY_SCALE) {
            return BufferedImage.TYPE_BYTE_GRAY;
        }
        return BufferedImage.TYPE_INT_RGB;
    }

    public Color getPixelColor(int x, int y) {
        return new Color(redChannel.getPixel(x, y), greenChannel.getPixel(x, y), blueChannel.getPixel(x, y));
    }

    public void setPixelColor(int x, int y, Color color) {
        redChannel.setPixel(x, y, color.getRed());
        greenChannel.setPixel(x, y, color.getGreen());
        blueChannel.setPixel(x, y, color.getBlue());
    }

    public Color getPixelsMean(Point p1, Point p2) { // p1.x < p2.x
        double red = 0, green = 0, blue = 0;

        if(p2.getY() < p1.getY()) {
            return getPixelsMean(p2, p1);
        }

        /* p1.x < p2.x && p1.y < p2.y */

        for(int x = (int) p1.getX(); x < (int) p2.getX(); x++) {
            for(int y = (int) p1.getY(); y < (int) p2.getY(); y++) {
                red +=  redChannel.getPixel(x, y);
                green += greenChannel.getPixel(x, y);
                blue += blueChannel.getPixel(x, y);
            }
        }

        int pixelsAmount = (int) ((p2.getX() - p1.getX()) * (p2.getY() - p1.getY()));

        return new Color((int)(red/pixelsAmount), (int)(green/pixelsAmount), (int)(blue/pixelsAmount));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public HSVImage toHSV() {
        return new HSVImage(this);
    }

    public void toGrayScale() {
        HSVImage hsvImage = this.toHSV();
        ImageColorChannel valueChannel = hsvImage.getValueChannel();

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int greyColor = valueChannel.getPixel(x, y);
                Color color = new Color(greyColor, greyColor, greyColor);
                setPixelColor(x, y, color);
            }
        }
    }

    public BufferedImage getRedImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = getPixelColor(x, y);
                Color redColor = new Color(color.getRed(), 0, 0);
                bufferedImage.setRGB(x, y, redColor.getRGB());
            }
        }

        return bufferedImage;
    }

    public BufferedImage getGreenImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = getPixelColor(x, y);
                Color greenColor = new Color(0, color.getGreen(), 0);
                bufferedImage.setRGB(x, y, greenColor.getRGB());
            }
        }

        return bufferedImage;
    }


    public BufferedImage getBlueImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = getPixelColor(x, y);
                Color blueColor = new Color(0, 0, color.getBlue());
                bufferedImage.setRGB(x, y, blueColor.getRGB());
            }
        }

        return bufferedImage;
    }

    public Image cropImage(Point p1, Point p2) {
        int ymin = (int) p1.getY();
        int ymax = (int) p2.getY();
        if(ymax < ymin) {
            int aux = ymax;
            ymax = ymin;
            ymin = aux;
        }

        Image newImage = new Image((int) (p2.getX() - p1.getX()), ymax - ymin, imageType, imageExtension);


        for(int height = 0; height < newImage.getHeight(); height++) {
            for(int width = 0; width < newImage.getWidth(); width++) {
                newImage.setPixelColor(width, height, getPixelColor(width, height));
            }
        }

        return newImage;
    }

    public void add(Image other){
        if(other.getWidth() != this.getWidth() || other.getHeight() != this.getHeight()){
            throw new IllegalArgumentException("Images must be of the same size");
        }
        this.redChannel.add(other.redChannel);
        this.greenChannel.add(other.greenChannel);
        this.blueChannel.add(other.blueChannel);

    }

    public void substract(Image other){
        if(other.getWidth() != this.getWidth() || other.getHeight() != this.getHeight()){
            throw new IllegalArgumentException("Images must be of the same size");
        }
        this.redChannel.substract(other.redChannel);
        this.greenChannel.substract(other.greenChannel);
        this.blueChannel.substract(other.blueChannel);
    }

    public void multiplyBy(int value) {
        this.redChannel.multiplyBy(value);
        this.greenChannel.multiplyBy(value);
        this.blueChannel.multiplyBy(value);

        int highestPixel = getHighestPixel();

        this.redChannel.transformPixelsWithMax(highestPixel);
        this.greenChannel.transformPixelsWithMax(highestPixel);
        this.blueChannel.transformPixelsWithMax(highestPixel);
    }

    private int getHighestPixel() {
        int highest = redChannel.getHighestPixel();
        int greenHighest = greenChannel.getHighestPixel();
        int blueHighest = blueChannel.getHighestPixel();
        if(greenHighest > highest) {
            highest = greenHighest;
        }
        if(blueHighest > highest) {
            highest = blueHighest;
        }
        return highest;
    }

    public void compressDynamicRange(){
        this.redChannel.compressDynamicRange();
        this.greenChannel.compressDynamicRange();
        this.blueChannel.compressDynamicRange();
    }

    public void powerFunction(double gamma) {
        this.redChannel.powerFunction(gamma);
        this.greenChannel.powerFunction(gamma);
        this.blueChannel.powerFunction(gamma);
    }

    public void setNegative(){
        this.redChannel.setNegative();
        this.greenChannel.setNegative();
        this.blueChannel.setNegative();
    }

    public void applyContrast(int minContrast, int maxContrast) { // TODO: Get r1, r2 from image
        this.redChannel.applyContrast(minContrast, maxContrast);
        this.greenChannel.applyContrast(minContrast, maxContrast);
        this.blueChannel.applyContrast(minContrast, maxContrast);
    }

    public void applyThreshold(int threshold) {
        this.redChannel.applyThreshold(threshold);
        this.greenChannel.applyThreshold(threshold);
        this.blueChannel.applyThreshold(threshold);
    }

    public void applyGlobalThreshold() {
        this.redChannel.applyGlobalThreshold();
        this.greenChannel.applyGlobalThreshold();
        this.blueChannel.applyGlobalThreshold();
    }

    public void applyOtsuThreshold() {
        this.redChannel.applyOtsuThreshold();
        this.greenChannel.applyOtsuThreshold();
        this.blueChannel.applyOtsuThreshold();
    }

    public double[] getGreyFrequency() {
        Image copyImage = this.cloneImage();
        copyImage.toGrayScale();
        return copyImage.redChannel.getFrequency();
    }

    public void equalizeFrequencies() {
        equalizeColor(redChannel);
        equalizeColor(greenChannel);
        equalizeColor(blueChannel);
    }

    private void equalizeColor(ImageColorChannel colorChannel) {
        int[] equalizedColors = getEqualizedColors(colorChannel);
        colorChannel.updateWith(equalizedColors);
    }

    private int[] getEqualizedColors(ImageColorChannel colorChannel) {
        int[] equalizedColors = new int[256];

        double[] accumFrequency = getAccumFrequencies(colorChannel);
        double minFreq = min(accumFrequency);
        for(int i = 0; i < equalizedColors.length; i++) {
            equalizedColors[i] = (int) Math.floor( (accumFrequency[i] - minFreq) / (1.0 - minFreq) * 255 + 0.5 );
        }

        return equalizedColors;
    }

    private double[] getAccumFrequencies(ImageColorChannel colorChannel) {
        double[] accumFrequency = new double[256];
        double[] relativeFrequency = colorChannel.getFrequency();
        double accum = 0;

        for(int i = 0; i < accumFrequency.length; i++) {
            accum += relativeFrequency[i];
            accumFrequency[i] = accum;
        }

        return accumFrequency;
    }

    private double min(double[] array) {
        double min = Double.MAX_VALUE;
        for(int i = 0; i < array.length; i++) {
            if(array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }


    public Image cloneImage() {
        Image image = new Image(width, height, imageType, imageExtension);

        for(int height = 0; height < this.height; height++) {
            for(int width = 0; width < this.width; width++) {
                image.setPixelColor(width, height, this.getPixelColor(width, height));
            }
        }

        return image;
    }

    public void applyAditiveGaussNoise(double phi, double mu, double contamination) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(Math.random() < contamination) {
                    double noise = Utils.randomGaussNumber(phi * 100, mu);
                    addAllBandsPixel(x, y, noise);
                }
            }
        }
        normalizeImage();
    }

    public void applyMultiplicativeRayleighNoise(double epsilon, double contamination) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(Math.random() < contamination) {
                    double noise = Utils.randomRayeighNumber(epsilon);
                    multiplyAllBandsPixel(x, y, noise);
                }
            }
        }
        normalizeImage();
    }

    public void applyMultiplicativeExponentialNoise(double lambda, double contamination) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(Math.random() < contamination) {
                    double noise = Utils.randomExponentialNumber(lambda);
                    multiplyAllBandsPixel(x, y, noise);
                }
            }
        }
        normalizeImage();
    }

    private void addAllBandsPixel(int x, int y, double noise) {
        redChannel.addToPixel(x, y, noise);
        greenChannel.addToPixel(x, y, noise);
        blueChannel.addToPixel(x, y, noise);
    }

    private void multiplyAllBandsPixel(int x, int y, double noise) {
        redChannel.multiplyToPixel(x, y, noise);
        greenChannel.multiplyToPixel(x, y, noise);
        blueChannel.multiplyToPixel(x, y, noise);
    }

    public void applySaltAndPepperNoise(double deviation) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                double rand = Math.random();

                if(rand < deviation/2) {
                    setAllBandsPixel(x, y, 0);
                }
                if(rand > 1- deviation/2) {
                    setAllBandsPixel(x, y, 255);
                }
            }
        }
    }

    private void setAllBandsPixel(int x, int y, int color) {
        redChannel.setPixel(x, y, color);
        greenChannel.setPixel(x, y, color);
        blueChannel.setPixel(x, y, color);
    }


    public void applyMask(Mask mask) {
        redChannel = mask.applyTo(redChannel);
        greenChannel = mask.applyTo(greenChannel);
        blueChannel = mask.applyTo(blueChannel);

        normalizeImage(mask.getBorderLength());
    }

    public void applyIsotropicDiffusion(int iterations) {
        redChannel.applyIsotropicDiffusion(iterations);
        greenChannel.applyIsotropicDiffusion(iterations);
        blueChannel.applyIsotropicDiffusion(iterations);

        normalizeImage();
    }

    public void applyAnisotropicDiffusion(int iterations, double deviation, Utils.AnisotroplicDiffusionType type) {
        redChannel.applyAnisotropicDiffusion(iterations, 0.25, deviation, type);
        greenChannel.applyAnisotropicDiffusion(iterations, 0.25, deviation, type);
        blueChannel.applyAnisotropicDiffusion(iterations, 0.25, deviation, type);

        normalizeImage();
    }

    private int minPixel() {
        return minPixel(0);
    }

    private int minPixel(int border) {
        int start = border;
        int endX = this.width - border;
        int endY = this.height - border;

        int minPixel = redChannel.minPixel(start, start, endX, endY);
        int blueMinPixel = blueChannel.minPixel(start, start, endX, endY);
        int greenMinPixel = greenChannel.minPixel(start, start, endX, endY);

        if(minPixel > blueMinPixel) {
            minPixel = blueMinPixel;
        }

        if(minPixel > greenMinPixel) {
            minPixel = greenMinPixel;
        }
        return minPixel;
    }

    private int maxPixel() {
        return maxPixel(0);
    }

    private int maxPixel(int border) {
        int start = border;
        int endX = this.width - border;
        int endY = this.height - border;

        int maxPixel = redChannel.maxPixel(start, start, endX, endY);
        int blueMaxPixel = blueChannel.maxPixel(start, start, endX, endY);
        int greenMaxPixel = greenChannel.maxPixel(start, start, endX, endY);

        if(maxPixel < blueMaxPixel) {
            maxPixel = blueMaxPixel;
        }

        if(maxPixel < greenMaxPixel) {
            maxPixel = greenMaxPixel;
        }
        return maxPixel;
    }

    private void normalizeImage() {
        normalizeImage(0);
    }

    public void normalizeImage(int border) {
        int minPixel = minPixel(border);
        int maxPixel = maxPixel(border);

        redChannel.normalizePixels(minPixel, maxPixel, border);
        greenChannel.normalizePixels(minPixel, maxPixel, border);
        blueChannel.normalizePixels(minPixel, maxPixel, border);
    }

    public void susanFilter(){
        redChannel   = SusanFilter.apply(redChannel);
        greenChannel = SusanFilter.apply(greenChannel);
        blueChannel  = SusanFilter.apply(blueChannel);
        //normalizeImage();
    }

    public ImageColorChannel getRedChannel() {
        return this.redChannel;
    }

    public void setRedChannel(ImageColorChannel redChannel) {
        this.redChannel = redChannel;
    }

    public ImageColorChannel getGreenChannel() {
        return greenChannel;
    }

    public void setGreenChannel(ImageColorChannel greenChannel) {
        this.greenChannel = greenChannel;
    }

    public ImageColorChannel getBlueChannel() {
        return blueChannel;
    }

    public void setBlueChannel(ImageColorChannel blueChannel) {
        this.blueChannel = blueChannel;
    }

    @Override
    public String toString() {
        return redChannel.toString();
    }

    public void susanCornerFilter() {
        redChannel   = SusanFilter.applyCorner(redChannel);
        greenChannel = SusanFilter.applyCorner(greenChannel);
        blueChannel  = SusanFilter.applyCorner(blueChannel);
    }

    public void houghFilter(int lines){
        this.toGrayScale();
        HoughFilter filter = new HoughFilter();
        ImageColorChannel channel = filter.apply(redChannel,lines);
        redChannel   = channel;
        greenChannel = channel;
        blueChannel  = channel;
    }

    public void houghCircularFilter(){
        this.toGrayScale();
        HughCircularFilter filter = new HughCircularFilter();
        ImageColorChannel channel = filter.apply(redChannel);
        redChannel   = channel;
        greenChannel = channel;
        blueChannel  = channel;
    }
}
