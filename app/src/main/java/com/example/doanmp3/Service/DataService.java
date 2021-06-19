package com.example.doanmp3.Service;

import com.example.doanmp3.Model.Album;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.Model.KeyWord;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.Model.QuangCao;
import com.example.doanmp3.Model.User;

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

    @GET("getbaihatquangcao.php")
    Call<List<BaiHat>> GetBaiHatQuangCao();

    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatAlbum(@Field("IdAlbum") String IdAlbum);

    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatPlaylist(@Field("IdPlaylist") String IdPlaylist);

    @FormUrlEncoded
    @POST("searchbaihat.php")
    Call<List<BaiHat>> GetSearchBaiHat(@Field("tukhoa") String tukhoa);

    @FormUrlEncoded
    @POST("searchcasi.php")
    Call<List<CaSi>> GetSearchCaSi(@Field("tukhoa") String tukhoa);

    @FormUrlEncoded
    @POST("searchalbum.php")
    Call<List<Album>> GetSearchAlbum(@Field("tukhoa") String tukhoa);

    @FormUrlEncoded
    @POST("searchplaylist.php")
    Call<List<Playlist>> GetSearchPlaylist(@Field("tukhoa") String tukhoa);

    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatCaSi(@Field("IdCaSi") String IdCaSi);

    @FormUrlEncoded
    @POST("getalbumcasi.php")
    Call<List<Album>> GetAlbumCaSi(@Field("IdCaSi") String IdCaSi);

    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatChuDe(@Field("IdChuDe") String IdChuDe);


    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatTheLoai(@Field("IdTheLoai") String IdPlaylist);


    // User
    @FormUrlEncoded
    @POST("login.php")
    Call<User> GetUser(@Field("Email") String email, @Field("Password") String Password);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> RegisterUser(@Field("email") String email, @Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("forgotpassword.php")
    Call<String> ForgotPassword(@Field("email") String Email);

    @FormUrlEncoded
    @POST("uploadhinhanh.php")
    Call<String> UploadPhoto(@Field("hinhanh") String image, @Field("filename") String tenfile, @Field("IdUser") String Id);

    @FormUrlEncoded
    @POST("changeemail.php")
    Call<String> ChangeEmail(@Field("email") String email, @Field("iduser") String Id);

    @FormUrlEncoded
    @POST("changeusername.php")
    Call<String> ChangeUserName(@Field("username") String username, @Field("iduser") String Id);

    @FormUrlEncoded
    @POST("changepassword.php")
    Call<String> ChangePassword(@Field("password") String password, @Field("iduser") String Id);

    @FormUrlEncoded
    @POST("getuserbaihat.php")
    Call<List<BaiHat>> GetBaiHatYeuThich(@Field("iduser") String IdUser);

    @FormUrlEncoded
    @POST("thichbaihat.php")
    Call<String> YeuThichBaiHat(@Field("idbaihat") String idbaihat, @Field("iduser") String iduser);

    @FormUrlEncoded
    @POST("bothichbaihat.php")
    Call<String> BoThichBaiHat(@Field("idbaihat") String idbaihat, @Field("iduser") String iduser);

    @FormUrlEncoded
    @POST("getuserplaylist.php")
    Call<List<Playlist>> GetUserPlaylist(@Field("iduser") String iduser);

    @FormUrlEncoded
    @POST("taoplaylist.php")
    Call<String> TaoPlaylist(@Field("iduser") String iduser, @Field("tenplaylist") String tenplaylist);

    @FormUrlEncoded
    @POST("getuserbaihatplaylist.php")
    Call<List<BaiHat>> GetUserBaiHatPlaylist(@Field("iduser") String iduser, @Field("idplaylist") String idplaylist);

    @FormUrlEncoded
    @POST("thembaihatplaylist.php")
    Call<String> ThemBaiHatPlaylist(@Field("idplaylist") String idplaylist, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("deletebaihatplaylist.php")
    Call<String> DeleteBaiHatPlaylist(@Field("idplaylist") String idplaylist, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("GetRecentSearch.php")
    Call<List<KeyWord>> GetKeyWordRecent(@Field("iduser") String iduser);

    @FormUrlEncoded
    @POST("search.php")
    Call<String> Search(@Field("iduser") String iduser, @Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("deleterecentsearch.php")
    Call<String> DeleteSearch(@Field("iduser") String iduser);

    @FormUrlEncoded
    @POST("playnhac.php")
    Call<String> PlayNhac(@Field("iduser") String iduser, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("getbaihatrecent.php")
    Call<List<BaiHat>> GetBaiHatRecent(@Field("iduser") String iduser);

    @FormUrlEncoded
    @POST("updateuserplaylist.php")
    Call<String> UpdateUserPlaylist(@Field("idplaylist") String idplaylist);

    @FormUrlEncoded
    @POST
    Call<String> DeleteUserPlaylist(@Field("iduser") String IdUser, @Field("idplaylist") String idplaylist);
}
