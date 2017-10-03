package com.dp.uheadmaster.webService;

import android.text.Annotation;

import com.dp.uheadmaster.models.response.ErrorResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Dell on 15/08/2017.
 */

public class ErrorUtils {
    public static ErrorResponse parseError(Response<?> response) {
        Converter<ResponseBody, ErrorResponse> converter = ApiClient.getClient()
                        .responseBodyConverter(ErrorResponse.class, (java.lang.annotation.Annotation[]) new Annotation[0]);

        ErrorResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorResponse();
        }

        return error;
    }

}
