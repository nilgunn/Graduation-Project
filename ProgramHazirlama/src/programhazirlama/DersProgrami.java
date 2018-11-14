package programhazirlama;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class DersProgrami {

    static boolean optimal = false;       // En iyi çözümün uygunluk değeri 1'e eşit ise true döndürür
    static int havuz = 1000;              //  Çözüm havuzundaki program sayısı
    static int eniyi = 0;                 //  Performansı en iyi olan çözüm
    static int nesil = 20;                //  Havuzdaki çözümlerin seçilim ve mutasyon sonucu oluşturduğu nesil sayısı   
    static int seçilenProg = havuz / 5;   //  Elitizm sonucu seçilen birey oranı
    static int dersProgSayısı = 2;        //  Kaç bölüme ait ders programı oluşturduğunu gösterir.
    static double ortPerformans = 0;      //  Havuzda bulunan çözümlerin ortalama performans değeri
    static int yeniboyut = 0;             //  En iyilerin seçildiği çözümlerden oluşan havuzun boyutu
    static int çözüm = 0;                 //  Bir havuzdaki çözüm sayısı
    static double performanslar[] = new double[havuz];   				// Havuzdaki çözümlerin performans değerlerini tutar
    static Dersler[][][] program = new Dersler[20][9][dersProgSayısı];  // Ders programına işlem uygulanırken burada tutulur.
    static Dersler[][][][] programHavuzu = new Dersler[20][9][dersProgSayısı][havuz];      //   Tüm çözümlerin tutulduğu havuz
    static Dersler[][][][] seçilenProgramlar = new Dersler[20][9][dersProgSayısı][havuz];  //   Eleme işlemi sonucu seçilen çözümlerin tutulduğu matris
    static ArrayList<Dersler> kayıt = new ArrayList<Dersler>();
    public static Object[][] data = new Object[3000][1000];

    // Veritabanı bağlantısı
    public static void Baglan() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Connection baglanti = DriverManager.getConnection("jdbc:odbc:veritabani");
            Statement sorgu = baglanti.createStatement();
            ResultSet dersler = sorgu.executeQuery("SELECT * FROM Dersler");
            // Bütün derslere ait bilgilerin veritabanından alınması
            while (dersler.next()) {
                kayıt.add(new Dersler(dersler.getString("dersAdı"), dersler.getInt("dersID"), dersler.getInt("hocaID"), dersler.getInt("sinif"),
                        dersler.getInt("saat"), dersler.getString("sinifNo"), dersler.getInt("bolumNo"), dersler.getInt("saatDuzeni"),
                        dersler.getString("HocaIsim")));
            }
            Collections.sort(kayıt);
            dersler.close();
            baglanti.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DersPrg() {
        Baglan();

//        Optimal çözümü buluncaya kadar çözüm havuzunu oluşturur ve genetik işlemleri uygular.
        while (optimal == false) {
            boşProgramOluştur();
            boşHavuzOluştur(programHavuzu);
            boşHavuzOluştur(seçilenProgramlar);
            rastgeleProgramOluştur();
//         İlk rastgele program oluşturulduktan sonra genetik algoritma uygulanır ve performans sonuçları karşılaştırılır.
            GenetikUygula();
        }

        data[0][0] = "PAZARTESİ";
        data[27][0] = "SALI";
        data[54][0] = "ÇARŞAMBA";
        data[81][0] = "PERŞEMBE";
        data[108][0] = "CUMA";
        int k = 0;
        while (k < 135) {
            data[k][1] = "8:30 - 9:15 ";
            k += 27;
        }
        k = 3;
        while (k < 135) {
            data[k][1] = "9:30 - 10:15 ";
            k += 27;
        }
        k = 6;
        while (k < 135) {
            data[k][1] = "10:30 - 11:15 ";
            k += 27;
        }
        k = 9;
        while (k < 135) {
            data[k][1] = "11:30 - 12:15 ";
            k += 27;
        }
        k = 12;
        while (k < 135) {
            data[k][1] = "13:15 - 14:00 ";
            k += 27;
        }
        k = 15;
        while (k < 135) {
            data[k][1] = "14:15 - 15:00 ";
            k += 27;
        }
        k = 18;
        while (k < 135) {
            data[k][1] = "15:15 - 16:00 ";
            k += 27;
        }
        k = 21;
        while (k < 135) {
            data[k][1] = "16:15 - 17:00 ";
            k += 27;
        }
        k = 24;
        while (k < 135) {
            data[k][1] = "17:15 - 18:00 ";
            k += 27;
        }
        int a = 0;
        int b = 2;
        int j = 0;
        while (j < 20) {
            for (k = 0; k < 9; k++) {
                data[a++][b] = program[j][k][0].dersAdı;
                data[a++][b] = program[j][k][0].Hocaisim;
                data[a++][b] = program[j][k][0].sinifNo;
            }

            j += 5;
            if (j < 19) {
                b++;
                a -= 27;
            } else if (j > 19) {
                j = j - 19;
                b = 2;
            } else {
                j = 22;
            }
        }
        a = 0;
        b = 6;
        j = 0;
        while (j < 20) {
            for (k = 0; k < 9; k++) {
                data[a++][b] = program[j][k][1].dersAdı;
                data[a++][b] = program[j][k][1].Hocaisim;
                data[a++][b] = program[j][k][1].sinifNo;
            }
            j += 5;
            if (j < 19) {
                b++;
                a -= 27;
            } else if (j > 19) {
                j = j - 19;
                b = 6;
            } else {
                j = 22;
            }
        }
    }

// Rastgele oluşturulmuş programlardan oluşan çözüm havuzunu oluşturur.   
    public static void rastgeleProgramOluştur() {
        int gun;
        int saat;
        int prg;             // Ders programlarını bölüme göre numaralandırır.  (1- TRK Bilgisayar. Muh.  2- ING Bilgisayar. Muh.)
        int s;               // Kaçıncı sınıf olduğunu tutar
        int digerprog;       // Aynı gündeki 1., 2., 3. ve 4. sınıf bilgilerini tutar
        int i = 0;           // Sırasıyle yerleştirilen derslerin takibi yapılır.
        int saatDuzeni;      // Ders işleme sistemini belirtir.( 2+1, 2+2 gibi )
        int hID;             // Ders hocasının id'sini tutar
        String sNO;          // Dersin işlendiği sınıfın id'sini tutar
        çözüm = 0;
        for (int j = 0; j < havuz; j++) {
            i = 0;           // Yeni bir program oluşturmak için indis 0'a eşitlenerek dersler tekrardan yerleştirilir
            while (i < kayıt.size()) {
                Dersler n = kayıt.get(i);
                hID = n.hocaID;
                sNO = n.sinifNo;
                prg = n.bolumNo - 1;  // Kaçıncı ders programı olduğunu belirtir.
                int kalan = n.saat;   // Dersin yerleştirilmesi gereken kaç saatinin kaldığını tutar
                s = n.sinif;
                saatDuzeni = n.saatDuzeni;
                // 4 saat art arda işlenen lab derslerinin yerleştirilmesi yapılır
                if (saatDuzeni == 1) {
                    if (kalan > 0) {
                        gun = (int) (Math.random() * 5);
                        saat = (int) (Math.random() * 2);
                        if (saat == 0) {
                            saat = 4;
                        } else {
                            saat = 5;
                        }
                        digerprog = gun + 5 * (s - 1);
                        if (program[digerprog][saat][prg].dersAdı == null && program[digerprog][saat + 1][prg].dersAdı == null
                                && program[digerprog][saat + 2][prg].dersAdı == null && program[digerprog][saat + 3][prg].dersAdı == null) {
                            program[digerprog][saat][prg] = n;
                            program[digerprog][saat + 1][prg] = n;
                            program[digerprog][saat + 2][prg] = n;
                            program[digerprog][saat + 3][prg] = n;
                            kalan = 0;
                        }
                    }
                } // 3 saat  art arda işlenen derslerin yerleştirilmesi yapılır
                else if (saatDuzeni == 2) {
                    while (kalan > 0) {
                        gun = (int) (Math.random() * 5);
                        saat = (int) (Math.random() * 7);
                        while (saat == 2 || saat == 3) {
                            saat = (int) (Math.random() * 7);
                        }
                        digerprog = gun + 5 * (s - 1);
                        if (program[digerprog][saat][prg].dersAdı == null || program[digerprog][saat + 1][prg].dersAdı == null || program[digerprog][saat + 2][prg].dersAdı == null) {
                            program[digerprog][saat][prg] = n;
                            program[digerprog][saat + 1][prg] = n;
                            program[digerprog][saat + 2][prg] = n;
                            kalan = 0;
                        }
                    }
                    // 2+2, 2+1 ve 2 olarak işlenen derslerin ataması yapılır
                } else if (saatDuzeni == 3 || saatDuzeni == 4 || saatDuzeni == 5) {
                    while (kalan > 0) {
                        gun = (int) (Math.random() * 5);
                        saat = (int) (Math.random() * 9);
                        while (saat == 8 || saat == 3) {
                            saat = (int) (Math.random() * 9);
                        }
                        digerprog = gun + 5 * (s - 1);
                        if (kalan % 2 == 0) {
                            if (program[digerprog][saat][prg].dersAdı == null && program[digerprog][saat + 1][prg].dersAdı == null) {
                                program[digerprog][saat][prg] = n;
                                program[digerprog][saat + 1][prg] = n;
                                kalan -= 2;
                            }
                        } else {
                            if (program[digerprog][saat][prg].dersAdı == null) {
                                program[digerprog][saat][prg] = n;
                                kalan--;
                            }
                        }
                    }
                    // 1+1 ya da sadece 1 saat işlenen derslerin ataması yapılır
                } else if (saatDuzeni == 6 || saatDuzeni == 7) {
                    while (kalan > 0) {
                        gun = (int) (Math.random() * 5);
                        saat = (int) (Math.random() * 9);

                        digerprog = gun + 5 * (s - 1);
                        if (program[digerprog][saat][prg].dersAdı == null) {
                            program[digerprog][saat][prg] = n;
                            kalan--;
                        }
                    }
                }
                i++;
            }
            // Oluşturulan ders programı çözüm havuzuna eklenir
            for (int k = 0; k < 20; k++) {
                for (int l = 0; l < 9; l++) {
                    for (int m = 0; m < 2; m++) {
                        programHavuzu[k][l][m][çözüm] = program[k][l][m];
                    }
                }
            }
            // Yeni oluşturulacak programın eklenmesi için en son oluşturulmuş olan program silinir
            boşProgramOluştur();
            çözüm++;
        }
    }

    // Bir ders programındaki dersin işlendiği sınıf ve dersi veren hocaya ait toplam çakışma miktarını verir.
    public static int çakışma(Dersler prog[][][]) {
        // Çakışmanın olup olmadığını kontrol etmek için sırasıyla derslere ait sınıf bilgisi takip edilir.
        String kontrol = null;
        int çakışma = 0;
        for (int saat = 0; saat < 9; saat++) {
            for (int prg = 0; prg < 2; prg++) {
                for (int gun = 0; gun < 20; gun++) {
                    if (prog[gun][saat][prg].sinifNo != null) {
                        kontrol = prog[gun][saat][prg].sinifNo;
                        for (int diger = gun + 5; diger < 20; diger += 5) {
                            for (int prg2 = 0; prg2 < 2; prg2++) {
                                if ((prog[diger][saat][prg2].sinifNo != null) && (kontrol.equals(prog[diger][saat][prg2].sinifNo))) {
                                    çakışma++;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int saat = 0; saat < 9; saat++) {
            for (int prg = 0; prg < 2; prg++) {
                for (int gun = 0; gun < 20; gun++) {
                    if (prog[gun][saat][prg].hocaID != 0) {
                        for (int diger = gun + 5; diger < 20; diger += 5) {
                            for (int prg2 = 0; prg2 < 2; prg2++) {
                                if ((prog[diger][saat][prg2].hocaID != 0) && (prog[gun][saat][prg].hocaID == prog[diger][saat][prg2].hocaID)) {
                                    çakışma++;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int saat = 0; saat < 9; saat++) {
            for (int prg = 0; prg < 2; prg++) {
                for (int gun = 0; gun < 20; gun++) {
                    if (prog[gun][saat][prg].sinifNo != null) {
                        kontrol = prog[gun][saat][prg].sinifNo;
                        for (int prg2 = 0; prg2 < 2; prg2++) {
                            if ((prog[gun][saat][prg2].sinifNo != null) && (kontrol.equals(prog[gun][saat][prg2].sinifNo))) {
                                if (prg != prg2) {
                                    çakışma++;

                                }
                            }
                        }
                    }
                }
            }
        }
        for (int saat = 0; saat < 9; saat++) {
            for (int prg = 0; prg < 2; prg++) {
                for (int gun = 0; gun < 20; gun++) {
                    if (prog[gun][saat][prg].hocaID != 0) {
                        for (int prg2 = 0; prg2 < 2; prg2++) {
                            if ((prog[gun][saat][prg2].hocaID != 0) && (prog[gun][saat][prg].hocaID == prog[gun][saat][prg2].hocaID)) {
                                if (prg != prg2) {
                                    çakışma++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return çakışma;
    }

    public static void UygunlukDeğeri(Dersler programH[][][][]) {
        int progçakışma;
        double uygunluk;
        double maxUygunluk = 0;
        double uygToplam = 0;

        performanslarıSıfırla(performanslar);
        for (int i = 0; i < havuz; i++) {
            //  boşProgramOluştur();
            for (int k = 0; k < 20; k++) {
                for (int l = 0; l < 9; l++) {
                    for (int m = 0; m < 2; m++) {
                        program[k][l][m] = programH[k][l][m][i];
                    }
                }
            }
            progçakışma = çakışma(program);
            uygunluk = (double) ((40 - progçakışma) * 0.025);
            performanslar[i] = uygunluk;
            uygToplam += uygunluk;
//            System.out.println(i + ". Programın uygunluk değeri : " + uygunluk + "   çakışma sayısı : " + progçakışma);
//            En iyi performansa sahip program bulunur
            if (uygunluk > maxUygunluk) {
                maxUygunluk = uygunluk;
                eniyi = i;
            }
        }
        ortPerformans = uygToplam / havuz;
        System.out.println("Havuzun ortalama performansı : " + ortPerformans);
        System.out.println("En iyi programın performansı : " + maxUygunluk);
        System.out.println("---------------------------------------");
//      Optimal çözümü bulduğunda programı durdurması için true döndürerek yukarıdaki genetik algoritma işlemlerini sonlandırır.
        if (maxUygunluk == 1) {
            optimal = true;
        }
    }

    // Performansı en iyi olan program kopyalanır.
    public static void enİyiProgram() {
        for (int k = 0; k < 20; k++) {
            for (int l = 0; l < 9; l++) {
                for (int m = 0; m < 2; m++) {
                    program[k][l][m] = programHavuzu[k][l][m][eniyi];
                }
            }
        }
    }

    // Havuzdaki tüm çözümlerin uygunluk değerlerini bulur.
    // Belirli sayıda seçilen en iyi çözümleri diğerlerinden ayırmak için yeni havuza aktarır.
    public static void Elitizm() {
        yeniboyut = 0;
        double min = SeçilenMinPerformans(performanslar);
        for (int i = 0; i < havuz; i++) {
            if (performanslar[i] > min && yeniboyut < seçilenProg) {
                for (int m = 0; m < 2; m++) {
                    for (int k = 0; k < 20; k++) {
                        for (int l = 0; l < 9; l++) {
                            seçilenProgramlar[k][l][m][yeniboyut] = programHavuzu[k][l][m][i];
                        }
                    }
                }
                yeniboyut++;
            }
        }
        if (yeniboyut < seçilenProg) {
            for (int i = 0; i < havuz; i++) {
                if (performanslar[i] == min && yeniboyut < seçilenProg) {
                    for (int m = 0; m < 2; m++) {
                        for (int k = 0; k < 20; k++) {
                            for (int l = 0; l < 9; l++) {
                                seçilenProgramlar[k][l][m][yeniboyut] = programHavuzu[k][l][m][i];
                            }
                        }
                    }
                    yeniboyut++;
                }
            }
        }
        boşHavuzOluştur(programHavuzu);

    }

    /* Çözüm havuzundaki en iyi bireylerin seçilmesi için en düşük performans değeri belirlenir
     Örneğin; hevuzdaki çözümlerin en iyi 20 tanesinin seçilmesi için 20. sıradaki uygunluk değerini bulma işlemi yapılır */
    public static double SeçilenMinPerformans(double[] dizi) {

        int L = dizi.length;
        double[] yedekDizi = new double[L];
        for (int i = 0; i < L; i++) {
            yedekDizi[i] = dizi[i];
        }
        for (int j = 1; j < L; j++) {
            double key = yedekDizi[j];
            int i = j - 1;
            while (i >= 0 && yedekDizi[i] < key) {
                yedekDizi[i + 1] = yedekDizi[i];
                i = i - 1;
            }
            yedekDizi[i + 1] = key;
        }
        return yedekDizi[seçilenProg];
    }

    // Yeni nesil bireylerinin performansını tutmak için eski bireylerin performans değerleri silinir.
    public static void performanslarıSıfırla(double[] dizi) {
        for (int i = 0; i < havuz; i++) {
            dizi[i] = 0;
        }
    }

    // Yeni neslin bireylerini havuza aktarmak için önceki nesil silinir.
    public static void boşHavuzOluştur(Dersler Havuz[][][][]) {
        for (int l = 0; l < havuz; l++) {
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 9; j++) {
                    for (int k = 0; k < dersProgSayısı; k++) {
                        Havuz[i][j][k][l] = new Dersler();
                    }
                }
            }
        }
    }

    // Yeni oluşturulan programa geçmek için en son oluşturulan programın bilgileri temizlenir.
    public static void boşProgramOluştur() {
        for (int a = 0; a < 20; a++) {
            for (int b = 0; b < 9; b++) {
                for (int c = 0; c < dersProgSayısı; c++) {
                    program[a][b][c]
                            = new Dersler();
                }
            }
        }
    }

    // Ayni gün içerisindeki sınıfları çakışan derslerin saatleri üzerinde değişiklik yapar
    public static void SaatMutasyonu(Dersler prog[][][]) {
        Dersler ders = new Dersler();
        int değişim = 0;
        // birdsen 9' a kadar hangi saatlerin çakıştığını kontrol eder.
        for (int saat = 0; saat < 9; saat++) {
            for (int gun = 0; gun < 15; gun++) {
                if (değişim < 1) {
                    for (int prg = 0; prg < 2; prg++) {
                        if (değişim < 1 && prog[gun][saat][prg].sinifNo != null) {
                            for (int gun2 = gun + 5; gun2 < 19; gun2 += 5) {
                                for (int prg2 = 0; prg2 < 2; prg2++) {
                                    if (değişim < 1 && prog[gun2][saat][prg2].sinifNo != null) {
                                        if (prog[gun][saat][prg].sinifNo.equals(prog[gun2][saat][prg2].sinifNo)) {
                                            if ((prog[gun][7][prg].sinifNo == null) || (prog[gun][8][prg].sinifNo == null) || (!(prog[gun][8][prg].sinifNo.equals(prog[gun][7][prg].sinifNo)))) {
                                                for (int degisenSaat = 0; degisenSaat < 4; degisenSaat++) {
                                                    ders = prog[gun][degisenSaat][prg];
                                                    prog[gun][degisenSaat][prg] = prog[gun][degisenSaat + 4][prg];
                                                    prog[gun][degisenSaat + 4][prg] = ders;
                                                }
                                                değişim++;
                                            } else if ((prog[gun][4][prg].sinifNo == null) || (prog[gun][5][prg].sinifNo == null) || (!(prog[gun][4][prg].sinifNo.equals(prog[gun][5][prg].sinifNo)))) {
                                                for (int degisenSaat = 0; degisenSaat < 4; degisenSaat++) {
                                                    ders = prog[gun][degisenSaat][prg];
                                                    prog[gun][degisenSaat][prg] = prog[gun][degisenSaat + 5][prg];
                                                    prog[gun][degisenSaat + 5][prg] = ders;
                                                }
                                                değişim++;
                                            } else {
                                            }
                                        } else {
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Ayni gün içerisindeki hoca isimleri çakışan derslerin saatleri üzerinde değişiklik yapar
    public static void SaatMutasyonu2(Dersler prog[][][]) {
        Dersler ders = new Dersler();
        int değişim = 0;
        // 1'den 9' a kadar hangi saatlerin çakıştığını kontrol eder.
        for (int saat = 0; saat < 9; saat++) {
            for (int gun = 0; gun < 15; gun++) {
                if (değişim < 1) {
                    for (int prg = 0; prg < 2; prg++) {
                        if (değişim < 1 && prog[gun][saat][prg].hocaID != 0) {
                            for (int gun2 = gun + 5; gun2 < 19; gun2 += 5) {
                                for (int prg2 = 0; prg2 < 2; prg2++) {
                                    if (değişim < 1 && prog[gun2][saat][prg2].hocaID != 0) {
                                        if (prog[gun][saat][prg].hocaID == prog[gun2][saat][prg2].hocaID) {
                                            if ((prog[gun2][7][prg2].hocaID == 0) || (prog[gun2][8][prg2].hocaID == 0) || (prog[gun2][8][prg2].hocaID != prog[gun2][7][prg2].hocaID)) {
                                                for (int degisenSaat = 0; degisenSaat < 4; degisenSaat++) {
                                                    ders = prog[gun2][degisenSaat][prg2];
                                                    prog[gun2][degisenSaat][prg2] = prog[gun2][degisenSaat + 4][prg2];
                                                    prog[gun2][degisenSaat + 4][prg2] = ders;
                                                }
                                                değişim++;
                                            } else if ((prog[gun2][4][prg2].hocaID == 0) || (prog[gun2][5][prg2].hocaID == 0) || (prog[gun2][4][prg2].hocaID != prog[gun2][5][prg2].hocaID)) {
                                                for (int degisenSaat = 0; degisenSaat < 4; degisenSaat++) {
                                                    ders = prog[gun2][degisenSaat][prg2];
                                                    prog[gun2][degisenSaat][prg2] = prog[gun2][degisenSaat + 5][prg2];
                                                    prog[gun2][degisenSaat + 5][prg2] = ders;
                                                }
                                                değişim++;
                                            } else {
                                            }
                                        } else {
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Sınıfa ait çakışmanın olduğu gün ile başka bir günün dersleri arasında değişiklik yapar
    public static void GünMutasyonu(Dersler prog[][][]) {
        Dersler ders = new Dersler();
        int değişim = 0;
        int gun2;  // Değiştirelecek olan günün indisi

        for (int saat = 0; saat < 9; saat++) {
            for (int gun1 = 0; gun1 < 15; gun1++) {
                gun2 = gun1;
                if (değişim < 1) {
                    for (int prg = 0; prg < 2; prg++) {
                        if (değişim < 1 && prog[gun1][saat][prg].sinifNo != null) {
                            for (int sonraki = gun1 + 5; sonraki < 20; sonraki += 5) {
                                for (int prg2 = 0; prg2 < 2; prg2++) {
                                    if (değişim < 1 && prog[sonraki][saat][prg2].sinifNo != null) {
                                        if ((prog[gun1][saat][prg].sinifNo.equals(prog[sonraki][saat][prg2].sinifNo))) {
                                            while (gun2 == gun1) {
                                                gun2 = (int) (Math.random() * 5);
                                                if (gun1 < 5) {

                                                } else if (gun1 < 10) {
                                                    gun2 += 5;
                                                } else if (gun1 < 15) {
                                                    gun2 += 10;
                                                } else {
                                                    gun2 += 15;
                                                }
                                            }
                                            for (int x = 0; x < 9; x++) {
                                                ders = prog[gun1][x][prg];
                                                prog[gun1][x][prg] = prog[gun2][x][prg];
                                                prog[gun2][x][prg] = ders;

                                            }
                                            değişim++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Hoca ismine ait çakışmanın olduğu gün ile başka bir günün dersleri arasında değişiklik yapar
    public static void GünMutasyonu2(Dersler prog[][][]) {
        Dersler ders = new Dersler();
        int değişim = 0;
        int gun2;  // Değiştirelecek olan günün indisi

        for (int saat = 0; saat < 9; saat++) {
            for (int gun1 = 0; gun1 < 15; gun1++) {
                gun2 = gun1;
                if (değişim < 1) {
                    for (int prg = 0; prg < 2; prg++) {
                        if (değişim < 1 && prog[gun1][saat][prg].hocaID != 0) {
                            for (int sonraki = gun1 + 5; sonraki < 20; sonraki += 5) {
                                for (int prg2 = 0; prg2 < 2; prg2++) {
                                    if (değişim < 1 && prog[sonraki][saat][prg2].hocaID != 0) {
                                        if ((prog[gun1][saat][prg].hocaID == prog[sonraki][saat][prg2].hocaID)) {
                                            while (gun2 == gun1) {
                                                gun2 = (int) (Math.random() * 5);
                                                if (gun1 < 5) {

                                                } else if (gun1 < 10) {
                                                    gun2 += 5;
                                                } else if (gun1 < 15) {
                                                    gun2 += 10;
                                                } else {
                                                    gun2 += 15;
                                                }
                                            }
                                            for (int x = 0; x < 9; x++) {
                                                ders = prog[gun1][x][prg];
                                                prog[gun1][x][prg] = prog[gun2][x][prg];
                                                prog[gun2][x][prg] = ders;

                                            }
                                            değişim++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* En iyi çözümlerin seçildiği havuza iyileştirme metodları uygulayarak yeni bireylerden oluşan bir havuz oluşturur 
     Bir sonraki neslin bireylerini oluşturma işlemi burada yapılır.*/
    public static void Programİyileştirme() {
        boşProgramOluştur();
        int hvz = 0;
        for (int i = 0; i < yeniboyut; i++) {
            for (int m = 0; m < 2; m++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 9; l++) {
                        program[k][l][m] = seçilenProgramlar[k][l][m][i];
                    }
                }
            }
            for (int m = 0; m < 2; m++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 9; l++) {
                        programHavuzu[k][l][m][hvz] = program[k][l][m];
                    }
                }
            }
            hvz++;
            SaatMutasyonu(program);
            for (int m = 0; m < 2; m++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 9; l++) {
                        programHavuzu[k][l][m][hvz] = program[k][l][m];
                    }
                }
            }
            hvz++;
            boşProgramOluştur();
            for (int m = 0; m < 2; m++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 9; l++) {
                        program[k][l][m] = seçilenProgramlar[k][l][m][i];
                    }
                }
            }
            SaatMutasyonu2(program);
            for (int m = 0; m < 2; m++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 9; l++) {
                        programHavuzu[k][l][m][hvz] = program[k][l][m];
                    }
                }
            }
            hvz++;
            boşProgramOluştur();
            for (int m = 0; m < 2; m++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 9; l++) {
                        program[k][l][m] = seçilenProgramlar[k][l][m][i];
                    }
                }
            }
            GünMutasyonu(program);
            for (int m = 0; m < 2; m++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 9; l++) {
                        programHavuzu[k][l][m][hvz] = program[k][l][m];
                    }
                }
            }
            hvz++;
            boşProgramOluştur();
            for (int m = 0; m < 2; m++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 9; l++) {
                        program[k][l][m] = seçilenProgramlar[k][l][m][i];
                    }
                }
            }
            GünMutasyonu2(program);
            for (int m = 0; m < 2; m++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 9; l++) {
                        programHavuzu[k][l][m][hvz] = program[k][l][m];
                    }
                }
            }
            hvz++;
            boşProgramOluştur();
        }
    }

    /* Önce belirli sayıdaki en iyi programları seçer, daha sonra iyileştirme metodlarıyla seçilen programlardan yeni bir havuz oluşturur. 
     Nesil sayısı kadar bu işlemi devam ettirir.*/

    public static void GenetikUygula() {
        System.out.println(" 1. Nesil ");
        UygunlukDeğeri(programHavuzu);
        enİyiProgram();
        // Nesil sayısı kadar elitizm yani seçilim yapılır ve programın performansını iyileştirme metodları uygulanır
        for (int i = 0; i < nesil - 1; i++) {
            Elitizm();
            Programİyileştirme();
            System.out.println((i + 2) + ". Nesil ");
            UygunlukDeğeri(programHavuzu);
            enİyiProgram();
        }
    }

    // Havuzdaki tüm ders programlarını yazdırır.
//  public static void Yazdır(Dersler tumHavuz[][][][]) {
//      for (int i = 0; i < havuz; i++) {
//          for (int l = 0; l < 2; l++) {
//              System.out.println("********************* " + (i + 1) + ". DERS PROGRAMI " + " *********************");
//              for (int j = 0; j < 20; j++) {
//                  for (int k = 0; k < 9; k++) {
//                      System.out.print(tumHavuz[j][k][l][i].dersAdı + "-");
//                  }
//                  System.out.println();
//              }
//          }
//      }
    }
}