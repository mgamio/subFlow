package com.subflow.api;

/**
 * The error body every SubFlow API returns, in the shape of
 * RFC 9457 "Problem Details for HTTP APIs".
 */
public record ApiError(String type, String title, int status, String detail) { }
