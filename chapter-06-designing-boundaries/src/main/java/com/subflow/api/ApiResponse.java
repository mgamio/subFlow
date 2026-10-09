package com.subflow.api;

/** An HTTP status with either a body or an error. */
public record ApiResponse<T>(int status, T body, ApiError error) {

  public static <T> ApiResponse<T> ok(T body) {
    return new ApiResponse<>(200, body, null);
  }

  public static <T> ApiResponse<T> created(T body) {
    return new ApiResponse<>(201, body, null);
  }

  public static <T> ApiResponse<T> failed(ApiError error) {
    return new ApiResponse<>(error.status(), null, error);
  }

  public boolean isSuccess() {
    return error == null;
  }
}
