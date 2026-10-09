package com.subflow.service;

/** Who is making the request, as established by authentication at the edge. */
public record Caller(long customerId) { }
