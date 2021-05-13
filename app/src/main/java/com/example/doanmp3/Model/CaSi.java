package com.example.doanmp3.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CaSi {

@SerializedName("IdCaSi")
@Expose
private String idCaSi;
@SerializedName("TenCaSi")
@Expose
private String tenCaSi;
@SerializedName("HinhCaSi")
@Expose
private String hinhCaSi;

public String getIdCaSi() {
return idCaSi;
}

public void setIdCaSi(String idCaSi) {
this.idCaSi = idCaSi;
}

public String getTenCaSi() {
return tenCaSi;
}

public void setTenCaSi(String tenCaSi) {
this.tenCaSi = tenCaSi;
}

public String getHinhCaSi() {
return hinhCaSi;
}

public void setHinhCaSi(String hinhCaSi) {
this.hinhCaSi = hinhCaSi;
}



}