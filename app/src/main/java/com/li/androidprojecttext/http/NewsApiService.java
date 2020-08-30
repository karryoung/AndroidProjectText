package com.li.androidprojecttext.http;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 假定这是个新系统的对应的API Service
 *
 */
public interface NewsApiService {

    /**
     *
     * @return
     */
    @GET("today")
    Observable<NewsHttpResponse<HotNewsResult>> getNews();




}
