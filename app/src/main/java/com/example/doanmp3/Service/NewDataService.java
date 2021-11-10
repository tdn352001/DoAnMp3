package com.example.doanmp3.Service;

import com.example.doanmp3.NewModel.Album;
import com.example.doanmp3.NewModel.DetailSinger;
import com.example.doanmp3.NewModel.Genre;
import com.example.doanmp3.NewModel.Playlist;
import com.example.doanmp3.NewModel.ResultSearch;
import com.example.doanmp3.NewModel.Singer;
import com.example.doanmp3.NewModel.Slide;
import com.example.doanmp3.NewModel.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NewDataService {

    @GET("getAllGenreAndTheme.php")
    Call<List<Genre>> getAllGenreAndTheme();

    @FormUrlEncoded
    @POST("search.php")
    Call<List<ResultSearch>> search(@Field("keyWord") String keyWord);

    @GET("getSlides.php")
    Call<List<Slide>> getSlides();

    @GET("getRandomAlbums.php")
    Call<List<Album>> getRandomAlbums();

    @GET("getRandomSingers.php")
    Call<List<Singer>> getRandomSingers();

    @GET("getRandomPlaylists.php")
    Call<List<Playlist>> getRandomPlaylists();

    @FormUrlEncoded
    @POST("getSongsFromAlbumId.php")
    Call<List<Song>> getSongsFromAlbumId(@Field("id") String id);

    @FormUrlEncoded
    @POST("getSongsFromPlaylistId.php")
    Call<List<Song>> getSongsFromPlaylistId(@Field("id") String id);

     @FormUrlEncoded
    @POST("getDetailsSinger.php")
    Call<DetailSinger> getDetailsSinger(@Field("id") String id);


}
