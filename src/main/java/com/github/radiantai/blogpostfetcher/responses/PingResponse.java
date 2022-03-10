package com.github.radiantai.blogpostfetcher.responses;

public class PingResponse extends BaseResponse {
    private final boolean success;

    public PingResponse(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }
}
