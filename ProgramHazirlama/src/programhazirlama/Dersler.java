package programhazirlama;

import java.util.Objects;
import java.util.logging.Logger;

public class Dersler implements Comparable<Dersler> {

    String dersAdı;
    int dersID;
    int hocaID;
    int sinif;
    int saat;
    String sinifNo;
    int bolumNo;
    int saatDuzeni;
    String Hocaisim;

    public Dersler() {
    }

    public Dersler(String dersAdı) {
        this.dersAdı = dersAdı;
    }

    public Dersler(int dersID) {
        this.dersID = dersID;
    }

    public Dersler(String dersAdı, int dersID, int hocaID, int sinif, int saat, String sinifNo, int bolumNo, int saatDuzeni, String Hocaisim) {
        this.dersAdı = dersAdı;
        this.dersID = dersID;
        this.hocaID = hocaID;
        this.sinif = sinif;
        this.saat = saat;
        this.sinifNo = sinifNo;
        this.bolumNo = bolumNo;
        this.saatDuzeni = saatDuzeni;
        this.Hocaisim = Hocaisim;
    }
    private static final Logger LOG = Logger.getLogger(Dersler.class.getName());

    public String getDersAdı() {
        return dersAdı;
    }

    public void setDersAdı(String dersAdı) {
        this.dersAdı = dersAdı;
    }

    public int getDersID() {
        return dersID;
    }

    public void setDersID(int dersID) {
        this.dersID = dersID;
    }

    public int getHocaID() {
        return hocaID;
    }

    public void setHocaID(int hocaID) {
        this.hocaID = hocaID;
    }

    public int getSinif() {
        return sinif;
    }

    public void setSinif(int sinif) {
        this.sinif = sinif;
    }

    public int getSaat() {
        return saat;
    }

    public void setSaat(int saat) {
        this.saat = saat;
    }

    public String getSinifNo() {
        return sinifNo;
    }

    public void setSinifNo(String sinifNo) {
        this.sinifNo = sinifNo;
    }

    public int getBolumNo() {
        return bolumNo;
    }

    public void setBolumNo(int bolumNo) {
        this.bolumNo = bolumNo;
    }

    public int getSaatDuzeni() {
        return saatDuzeni;
    }

    public void setSaatDuzeni(int saatDuzeni) {
        this.saatDuzeni = saatDuzeni;
    }

    public String getHocaisim() {
        return Hocaisim;
    }

    public void setHocaisim(String Hocaisim) {
        this.Hocaisim = Hocaisim;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.dersAdı);
        hash = 59 * hash + this.dersID;
        hash = 59 * hash + this.hocaID;
        hash = 59 * hash + this.sinif;
        hash = 59 * hash + this.saat;
        hash = 59 * hash + Objects.hashCode(this.sinifNo);
        hash = 59 * hash + this.bolumNo;
        hash = 59 * hash + this.saatDuzeni;
        hash = 59 * hash + Objects.hashCode(this.Hocaisim);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dersler other = (Dersler) obj;
        if (!Objects.equals(this.dersAdı, other.dersAdı)) {
            return false;
        }
        if (this.dersID != other.dersID) {
            return false;
        }
        if (this.hocaID != other.hocaID) {
            return false;
        }
        if (this.sinif != other.sinif) {
            return false;
        }
        if (this.saat != other.saat) {
            return false;
        }
        if (!Objects.equals(this.sinifNo, other.sinifNo)) {
            return false;
        }
        if (this.bolumNo != other.bolumNo) {
            return false;
        }
        if (this.saatDuzeni != other.saatDuzeni) {
            return false;
        }
        if (!Objects.equals(this.Hocaisim, other.Hocaisim)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nesne{" + "dersAd\u0131=" + dersAdı + ", dersID=" + dersID + ", hocaID=" + hocaID + ", sinif=" + sinif + ", saat=" + saat + ", sinifNo=" + sinifNo + ", bolumNo=" + bolumNo + ", saatDuzeni=" + saatDuzeni + ", Hocaisim=" + Hocaisim  + '}';
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    public double oncelik() {
        return saat;
    }

//    Ders saati fazla olan derslerin atamasına öncelik verilerek en sona ders saati az olan derslerin ataması bırakılır
    public int compareTo(Dersler diger) {
        if (oncelik() > diger.oncelik()) {
            return 1;
        } else if (oncelik() == diger.oncelik()) {
            return 0;
        } else {
            return -1;
        }
    }
}
