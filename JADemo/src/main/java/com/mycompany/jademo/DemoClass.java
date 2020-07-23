package com.mycompany.jademo;

/**
 *
 * @author Nano
 */
public class DemoClass {

    public void helloWorld() {

        System.out.println("Su An Metod Calisiyor...");
        System.out.println("Toplama Isleminin Sonucu : " + topla(10, 20));

    }

    public int topla(int a, int b) {
        return a + b;
    }

    public int cikart(int a, int b) {
        return a - b;
    }

    public void yihu() {
        System.out.println("Yihuu!!");
        System.out.println("Yihu Metodundayim : " + topla(20, 20));
    }

    public String yaz() {
        yihu();
        System.out.println("Yaz Metodundayim : " + cikart(30, 10));
        return "Merhaba";
    }
}
