package com.github.radiantai.blogpostfetcher.responses;

public class ErrorResponse extends BaseResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
