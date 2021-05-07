package com.example.doanmp3.Service;

import com.example.doanmp3.Model.Album;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.Model.QuangCao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {
    @GET("songbanner.php")
    Call<List<QuangCao>> GetDataBanner();

    @GET("randomalbum.php")
    Call<List<Album>> GetRandomAlbum();

    @GET("randomplaylist.php")
    Call<List<Playlist>> GetRandomPlaylist();

    @GET("randomsong.php")
    Call<List<BaiHat>> GetRanDomBaiHat();

    @GET("randomsinger.php")
    Call<List<CaSi>> GetRanDomCaSi();

    @GET("randomcdtl.php")
    Call<List<ChuDeTheLoai>> GetRandomCDTL();

    @GET("getallplaylist.php")
    Call<List<Playlist>> GetAllPlaylist();

    @GET("getallalbum.php")
    Call<List<Album>> GetAllAlbum();

    @GET("getallsong.php")
    Call<List<BaiHat>> GetAllSong();

    @GET("getallsinger.php")
    Call<List<CaSi>> GetAllSinger();


    @GET("getalltheloai.php")
    Call<List<ChuDeTheLoai>> GetAllTheLoai();

    @GET("getallchude.php")
    Call<List<ChuDeTheLoai>> GetAllChuDe();

    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatQuangCao(@Field("IdQuangCao") int IdQuangCao);
}
