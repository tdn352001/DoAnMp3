package com.example.doanmp3.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BaiHat implements Parcelable {

    @SerializedName("IdBaiHat")
    @Expose
    private String idBaiHat;
    @SerializedName("TenBaiHat")
    @Expose
    private String tenBaiHat;
    @SerializedName("HinhBaiHat")
    @Expose
    private String hinhBaiHat;
    @SerializedName("LinkBaiHat")
    @Expose
    private String linkBaiHat;
    @SerializedName("CaSi")
    @Expose
    private List<String> caSi = null;
    @SerializedName("LuotThich")
    @Expose
    private String luotThich;

    public BaiHat() {
        luotThich = "0";
    }

    protected BaiHat(Parcel in) {
        idBaiHat = in.readString();
        tenBaiHat = in.readString();
        hinhBaiHat = in.readString();
        linkBaiHat = in.readString();
        caSi = in.createStringArrayList();
    }

    public static final Creator<BaiHat> CREATOR = new Creator<BaiHat>() {
        @Override
        public BaiHat createFromParcel(Parcel in) {
            return new BaiHat(in);
        }

        @Override
        public BaiHat[] newArray(int size) {
            return new BaiHat[size];
        }
    };

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getTenBaiHat() {
        if (tenBaiHat == null)
            return "Unknown";
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getHinhBaiHat() {
        return hinhBaiHat;
    }

    public void setHinhBaiHat(String hinhBaiHat) {
        this.hinhBaiHat = hinhBaiHat;
    }

    public String getLinkBaiHat() {
        return linkBaiHat;
    }

    public void setLinkBaiHat(String linkBaiHat) {
        this.linkBaiHat = linkBaiHat;
    }

    public List<String> getCaSi() {
        return caSi;
    }

    public String getTenAllCaSi() {
        StringBuilder TenCaSi = new StringBuilder();
        if (caSi != null) {
            if (caSi.size() > 0) {
                for (int i = 0; i < caSi.size(); i++) {
                    if (i != 0)
                        TenCaSi.append(", ");

                    TenCaSi.append(getCaSi().get(i));
                }
                return TenCaSi.toString();
            }
        }
        TenCaSi = new StringBuilder("Unknown");
        return TenCaSi.toString();
    }


    public void setCaSi(List<String> caSi) {
        if (caSi == null) {
            caSi = new ArrayList<>();
            caSi.add("Unknown");
        }
        this.caSi = caSi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idBaiHat);
        dest.writeString(tenBaiHat);
        dest.writeString(hinhBaiHat);
        dest.writeString(linkBaiHat);
        dest.writeStringList(caSi);
    }

    public String getLuotThich() {
        return luotThich;
    }

    public void setLuotThich(String luotThich) {
        this.luotThich = luotThich;
    }

}