package ar.edu.itba.ati;

import ar.edu.itba.ati.GUI.MainWindow;
import ar.edu.itba.ati.model.HSVImage;
import ar.edu.itba.ati.model.Image;
import ar.edu.itba.ati.model.Mask;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ControllerImpl implements ar.edu.itba.ati.Interface.Controller {
    Image image;

    MainWindow mainWindow;

    @Override
    public Color getPixelValue(int x, int y) {
        return image.getPixelColor(x, y);
    }

    @Override
    public void editPixelValue(int x, int y, Color newValue) {
        image.setPixelColor(x, y, newValue);
    }

    @Override
    public BufferedImage loadImage(File imagePath) {
        try {
            image = ImageManager.loadImage(imagePath);
        } catch (IOException e) {
            //TODO: Show msg to user
            System.out.println("Unable to load image");
        } catch (ImageReadException e) {
            //TODO: Show msg to user
            System.out.println("Unable to load image");
        }

        return image.getBufferdImage();
    }

    @Override
    public BufferedImage loadRawImage(File imagePath, int width, int height) {
        try {
            image = ImageManager.loadRawImage(imagePath, width, height);
        } catch(IOException e) {
            //TODO: Show msg to user
            System.out.println("Unable to load image");
        }
        return image.getBufferdImage();
    }

    @Override
    public BufferedImage getImage() {
        return image.getBufferdImage();
    }

    @Override
    public void saveImage(File imagePath) {
       saveImage(imagePath, image);
    }

    private void saveImage(File imagePath, Image image) {
        try {
            ImageManager.saveImage(imagePath, image);
        } catch (IOException e) {
            //TODO: Show msg to user
            System.out.println("Unable to save image");
        } catch (ImageWriteException e) {
            //TODO: Show msg to user
            System.out.println("Unable to save image");
        }
    }

    @Override
    public void cropImage( Point p1, Point p2) {
        Image croppedImage = image.cropImage(p1, p2);
        image = croppedImage;
    }

    @Override
    public BufferedImage createSquare() {
         image = ImageCreator.buildSquare();
         return image.getBufferdImage();
    }

    @Override
    public BufferedImage createCircle() {
        image = ImageCreator.buildCircle();
        return image.getBufferdImage();
    }

    @Override
    public BufferedImage createColorGradient() {
        image =  ImageCreator.buildColorGradient();
        return image.getBufferdImage();
    }

    @Override
    public BufferedImage createGreyGradient() {
        image = ImageCreator.buildGreyGradient();
        return image.getBufferdImage();
    }

    @Override
    public BufferedImage createSolidImage(int r, int g, int b) {
        image = ImageCreator.buildSolidColor(r, g, b);
        return image.getBufferdImage();
    }

    public Color getPixelsMean(Point p1, Point p2) {
        return image.getPixelsMean(p1, p2);
    }

    @Override
    public BufferedImage getRedImage() {
        return image.getRedImage();
    }

    @Override
    public BufferedImage getGreenImage() {
        return image.getGreenImage();
    }

    @Override
    public BufferedImage getBlueImage() {
        return image.getBlueImage();
    }

    @Override
    public BufferedImage getHueImage() {
        HSVImage hsvImage = image.toHSV();
        return hsvImage.getHueImage();
    }

    @Override
    public BufferedImage getSaturationImage() {
        HSVImage hsvImage = image.toHSV();
        return hsvImage.getSaturationImage();
    }

    @Override
    public BufferedImage getValueImage() {
        HSVImage hsvImage = image.toHSV();
        return hsvImage.getValueImage();
    }

    @Override
    public void addImage(File imageFile) {
        Image image2;
        try{
            image2 = ImageManager.loadImage(imageFile);
        }catch (ImageReadException e){
            e.printStackTrace();
            return;//TODO manage errors
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        image.add(image2);
    }

    @Override
    public void substractImage(File imageFile) {
        Image image2;
        try{
            image2 = ImageManager.loadImage(imageFile);
        }catch (ImageReadException e){
            e.printStackTrace();
            return;//TODO manage errors
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        image.substract(image2);
    }

    @Override
    public void compressDynamicRange() {
        image.compressDynamicRange();
    }

    @Override
    public void getNegative() {
         image.setNegative();
    }

    @Override
    public BufferedImage applyContrast(int minContrast, int maxContrast) {
        Image copy = image.cloneImage();
        copy.applyContrast(minContrast, maxContrast);
        return copy.getBufferdImage();
    }

    @Override
    public double[] getHisotgram() {
        return image.getGreyFrequency();
    }

    public void setMainWindow(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    @Override
    public MainWindow getMainWindow() {
        return this.mainWindow;
    }

    @Override
    public void equalizeImage() {
        image.equalizeFrequencies();
    }

    @Override
    public void applyMeanMask(int size) {
        Mask mask = new Mask(size, Mask.Type.MEAN);
        image.applyMask(mask);
    }

    @Override
    public void applyMedianMask(int size) {
        Mask mask = new Mask(size, Mask.Type.MEDIAN);
        image.applyMask(mask);
    }

    @Override
    public void applyGaussMask(int size) {
        Mask mask = new Mask(size, Mask.Type.GAUSS);
        image.applyMask(mask);
    }

    @Override
    public void applyBorderMask(int size) {
        Mask mask = new Mask(size, Mask.Type.BORDERS);
        image.applyMask(mask);
    }

    @Override
    public void setContrast(int minContrast, int maxContrast) {
        image.applyContrast(minContrast,maxContrast);
    }

    @Override
    public BufferedImage applyThreshold(int threshold) {
        Image copy = image.cloneImage();
        copy.applyThreshold(threshold);
        return copy.getBufferdImage();
    }

    @Override
    public void setThreshold(int threshold) {
        image.applyThreshold(threshold);
    }

    @Override
    public BufferedImage applyAditiveGaussNoise(double phi, double mu) {
        Image copy = image.cloneImage();
        copy.applyAditiveGaussNoise(phi,mu);
        return copy.getBufferdImage();
    }

    @Override
    public BufferedImage applyMultiplicativeRayleighNoise(double epsilon) {
        Image copy = image.cloneImage();
        copy.applyMultiplicativeRayleighNoise(epsilon);
        return copy.getBufferdImage();
    }

    @Override
    public BufferedImage applyMultiplicativeExponentialNoise(double lambda) {
        Image copy = image.cloneImage();
        copy.applyMultiplicativeExponentialNoise(lambda);
        return copy.getBufferdImage();
    }

    @Override
    public BufferedImage applySaltAndPepperNoise(double deviation) {
        Image copy = image.cloneImage();
        copy.applySaltAndPepperNoise(deviation);
        return copy.getBufferdImage();
    }

    @Override
    public void setAditiveGaussNoise(double phi, double mu) {
        image.applyAditiveGaussNoise(phi,mu);
    }

    @Override
    public void setMultiplicativeRayleighNoise(double epsilon) {
        image.applyMultiplicativeRayleighNoise(epsilon);
    }

    @Override
    public void setMultiplicativeExponentialNoise(double lambda) {
        image.applyMultiplicativeExponentialNoise(lambda);
    }

    @Override
    public void setSaltAndPepperNoise(double deviation) {
        image.applySaltAndPepperNoise(deviation);
    }
}