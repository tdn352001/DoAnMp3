package com.example.doanmp3.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaiHat {

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

public String getIdBaiHat() {
return idBaiHat;
}

public void setIdBaiHat(String idBaiHat) {
this.idBaiHat = idBaiHat;
}

public String getTenBaiHat() {
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

public void setCaSi(List<String> caSi) {
this.caSi = caSi;
}

}