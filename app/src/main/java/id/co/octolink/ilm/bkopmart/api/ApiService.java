package id.co.octolink.ilm.bkopmart.api;


import id.co.octolink.ilm.bkopmart.model.BillingResponse;
import id.co.octolink.ilm.bkopmart.model.FavoriteResponse;
import id.co.octolink.ilm.bkopmart.model.GeneralResponse;
import id.co.octolink.ilm.bkopmart.model.LoginResponse;
import id.co.octolink.ilm.bkopmart.model.MarkFavoriteResponse;
import id.co.octolink.ilm.bkopmart.model.PembelianResponse;
import id.co.octolink.ilm.bkopmart.model.ProductResponse;
import id.co.octolink.ilm.bkopmart.model.ProfileResponse;
import id.co.octolink.ilm.bkopmart.model.RegisterResponse;
import id.co.octolink.ilm.bkopmart.model.SliderResponse;
import id.co.octolink.ilm.bkopmart.model.TagihanResponse;
import id.co.octolink.ilm.bkopmart.model.UnMarkFavoriteResponse;
import id.co.octolink.ilm.bkopmart.model.ViewDetilTransaksiResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface ApiService {

    //Register
    @FormUrlEncoded
    @POST("customer/register")
    Call<RegisterResponse> registration(@Field("email") String email,
                                        @Field("password")String password,
                                        @Field("birthdate")String birthdate,
                                        @Field("name")String name,
                                        @Field("gender")String gender);

    //Login
    @FormUrlEncoded
    @POST("customer/login")
    Call<LoginResponse> authentification(@Field("email") String email,
                                         @Field("password")String password);

    // CHANGE PASSWORD
    @FormUrlEncoded
    @POST("customer/changePassword")
    Call<GeneralResponse> changePassword(@Field("email")String email,
                                         @Field("old_password")String old_password,
                                         @Field("new_password")String new_password);

    // UPDATE PROFILE
    @Multipart
    @POST("customer/updateProfile")
    Call<GeneralResponse> updateProfile(@Part("email") RequestBody email,
                                       @Part("birthdate") RequestBody birthdate,
                                       @Part("name") RequestBody name,
                                       @Part("gender") RequestBody gender,
                                       @Part("avatar") RequestBody avatar,
                                       @Part MultipartBody.Part file);

    // VIEW PROFILE
    @FormUrlEncoded
    @POST("customer/viewProfile")
    Call<ProfileResponse> viewProfile(@Field("email")String email);

    // GET ALL SLIDER
    @GET("slider/getAll")
    Call<SliderResponse> getAllSlider();

    // GET ALL PRODUCT
    @GET("product/getAll")
    Call<ProductResponse> getAllProduct();

    // GET ALL PRODUCT by CATEGORY ID
    @GET("product/getAllByCategory/{category_id}")
    Call<GeneralResponse> getAllProductByCategoryId(@Path("category_id")String category_id);

    // GET ALL FAVOURITE PRODUCT by CUSTOMER ID
    @GET("favourite/getAllByCustomerId/{customer_id}")
    Call<FavoriteResponse> getAllFavoriteProductByCustomerId(@Path("customer_id")String customer_id);

    // MARK FAVOURITE
    @GET("favourite/markFavourite/{customer_id}/{item_id}")
    Call<MarkFavoriteResponse> markAsFavorite(@Path("customer_id")String customer_id,
                                              @Path("item_id")String item_id );

    // REMARK FAVOURITE
    @GET("favourite/remarkFavourite/{customer_id}/{item_id}")
    Call<UnMarkFavoriteResponse> unMarkAsFavorite(@Path("customer_id")String customer_id,
                                                  @Path("item_id")String item_id );

    // DO TRANSACTION FOR BILLING
    @FormUrlEncoded
    @POST("transaction/doTransaction")
    Call<BillingResponse> doTransactionBilling(@Field("customer_id")String cid,
                                               @Field("json_transaction")String json_transaction);

    // DO TRANSACTION FOR CHECKOUT
    @FormUrlEncoded
    @POST("transaction/doCheckoutTransaction")
    Call<GeneralResponse> doTransactionCheckout(@Field("customer_id")String customer_id,
                                               @Field("transaction_id")String transaction_id);

    // GET ALL TRANSACTION BILLING
    @GET("transaction/getByCustomerBilling/{customer_id}")
    Call<TagihanResponse> getAllTransactionBilling(@Path("customer_id") String customer_id);

    // GET ALL TRANSACTION PURCHASED
    @GET("transaction/getByCustomerPurchased/{customer_id}")
    Call<PembelianResponse> getAllTransactionPurchased(@Path("customer_id") String customer_id);

    // VIEW TRANSACTION
    @GET("transaction/viewDetailTransaction/{transaction_id}")
    Call<ViewDetilTransaksiResponse> viewTransaction(@Path("transaction_id") String transaction_id);

}
