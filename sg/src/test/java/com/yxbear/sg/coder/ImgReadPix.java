package com.yxbear.sg.coder;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.yxbear.sg.coder.ImgReadPix.Img;

import lombok.SneakyThrows;

public class ImgReadPix {

    static File rsm = new File("rsm");

    static File images = new File(rsm, "images");

    public static class Img {

        public static Img from(File p) {
            return new Img(p);
        }

        public static Img from(String p) {
            return new Img(new File(p));
        }

        BufferedImage img;

        @SneakyThrows
        public Img(File png) {
            super();
            this.img = ImageIO.read(png);
        }

        public void printSize() {
            System.out.println("w: " + img.getWidth() + ",h: " + img.getHeight());
        }

        static record RGBA(byte r, byte g, byte b, byte a) {

            public String toHex() {
                return "#" + String.format("%02x", r) + //
                        String.format("%02x", g) + //
                        String.format("%02x", b) + //
                        String.format("%02x", a); //
            }

        }

        public RGBA getRGBA(int x, int y) {
            int pixel = img.getRGB(x, y);
            return new RGBA((byte) ((pixel >> 16) & 0xff), //
                    (byte) ((pixel >> 8) & 0xff), //
                    (byte) (pixel & 0xff), //
                    (byte) ((pixel >> 24) & 0xff));
        }

        public String readHex(int x, int y) {
            int pixel = img.getRGB(x, y);
            // 提取 RGBA 分量，并转换为十六进制字符串
            String alpha = String.format("%02x", (pixel >> 24) & 0xff);
            String red = String.format("%02x", (pixel >> 16) & 0xff);
            String green = String.format("%02x", (pixel >> 8) & 0xff);
            String blue = String.format("%02x", pixel & 0xff);
            // Color c = new Color(pixel, true);
            // System.out.println(c.getRed() + " " + c.getGreen() + " " + c.getBlue() + " " + c.getAlpha());
            // 组合为 #AARRGGBB 格式并打印
            String hexColor = "#" + red + green + blue + alpha;
            return hexColor;
        }

    }

    public static void main(String[] args) throws Exception {
        File f = new File("D:\\projects\\sanguo\\图片\\New Folder\\images\\127.png");
        Img png = Img.from(f);
        png.printSize();
        System.out.println(png.readHex(30, 25));
    }

}
